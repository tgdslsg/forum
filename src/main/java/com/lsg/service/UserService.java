package com.lsg.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lsg.dao.LoginLogDao;
import com.lsg.dao.NotifyDao;
import com.lsg.dao.UserDao;
import com.lsg.entity.LoginLog;
import com.lsg.entity.Notify;
import com.lsg.entity.User;
import com.lsg.exception.ServiceException;
import com.lsg.util.Config;

import com.lsg.util.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import com.lsg.util.Email;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.lsg.entity.User.USERSTATE_ACTIVE;

public class UserService {


    private NotifyDao notifyDao = new NotifyDao();
    private UserDao userDao = new UserDao();
    private LoginLogDao loginLogDao = new LoginLogDao();
    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    //发送找回密码邮件的Token缓存
    private static Cache<String,String> passwordCache = CacheBuilder.newBuilder()
            .expireAfterWrite(30,TimeUnit.MINUTES)
            .build();
    //限制操作频率的缓存
    private static Cache<String,String> activeCache = CacheBuilder.newBuilder()
            .expireAfterWrite(60,TimeUnit.SECONDS)
            .build();

    //发送激活邮件的TOKEN缓存
    private static Cache<String,String> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(6, TimeUnit.HOURS)
            .build();

    public void activeUser(String token){
        String userName = cache.getIfPresent(token);//token->username
        if (userName==null){
            throw  new ServiceException("token无效或已过期");
        }else {
            User user = userDao.findByUserName(userName);
            if (user==null){
                throw new ServiceException("无法找到对应的账号");
            }else {
                user.setState(USERSTATE_ACTIVE);
                userDao.update(user);
            }
        }
    }



    /**
     * 校验用户名是否被占用
     * @param username
     * @return
     */
    public boolean validateUserName(String username) {
        //注意保留用户名(名人或其他的名字)
        String name = Config.get("no.signup.usernames");
        List<String> nameList = Arrays.asList(name.split(","));
        if(nameList.contains(username)) {
            return false;
        }
        System.out.println(username);
        return  userDao.findByUserName(username) == null;
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    /**
     * 新用户注册
     * @param username
     * @param password
     * @param email
     * @param phone
     */
    public void saveNewUser(String username, String password, String email, String phone) {
        User user = new User();
        user.setUsername(username);
        user.setAvatar(User.DEFAULT_AVATAR_NAME);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(DigestUtils.md5Hex(Config.get("user.password.salt") + password));
        user.setState(User.USERSTATE_UNACTIVE);

        userDao.save(user);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //给用户发送激活邮件
                String uuid = UUID.randomUUID().toString();
                String url = "http://localhost/user/active?_="+uuid;
                //放入缓存等待6个小时
                cache.put(uuid,username);

                String html ="<h3>Dear "+username+":</h3>请点击<a href='"+url+"'>该链接</a>去激活你的账号. <br> ";

                Email.sendHtmlEmail(email,"用户激活邮件",html);
            }
        });
        thread.start();
    }

    public User login(String username, String password, String ip) {
        User user = userDao.findByUserName(username);
        if (user != null && DigestUtils.md5Hex( Config.get("user.password.salt")+ password).equals(user.getPassword())) {
            if (user.getState().equals(User.USERSTATE_ACTIVE)) {
                //记录登录日志
                LoginLog log = new LoginLog();
                log.setIp(ip);
                log.setUserId(user.getId());
                loginLogDao.save(log);
                logger.info("{}登录系统；ip：{}", username, ip);
                return user;
            } else if (User.USERSTATE_UNACTIVE.equals(user.getState())) {
                throw new ServiceException("该账号未激活");
            } else {
                throw new ServiceException("该账号已被禁用");
            }
        } else {
            throw new ServiceException("账号或密码错误");
        }
    }
    /**
     * 用户找回密码
     * @param sessionId 客户端的sessionID,限制客户端的操作频率
     * @param type 找回密码方式 email | phone
     * @param value 电子邮件地址 | 手机号码
     */
    public void foundPassword(String sessionId, String type, String value) {
        if(activeCache.getIfPresent(sessionId) == null) {
            if("phone".equals(type)) {
                //TODO 根据手机号码找回密码
            } else if("email".equals(type)) {
                User user = userDao.findByEmail(value);
                if(user != null) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String uuid = UUID.randomUUID().toString();
                            String url = "http://localhost/foundpassword/newpassword?token=" + uuid;

                            passwordCache.put(uuid,user.getUsername());
                            String html = user.getUsername()+"<br>请点击该<a href='"+url+"'>链接</a>进行找回密码操作，链接在30分钟内有效";
                            Email.sendHtmlEmail(value,"密码找回邮件",html);
                        }
                    });
                    thread.start();
                }
            }

            activeCache.put(sessionId,"xxx");
        } else {
            throw new ServiceException("操作频率过快");
        }
    }
    /**
     * 根据找回密码的链接获取找回密码的用户
     * @param token
     * @return
     */
    public User foundPasswordGetUserByToken(String token) {
        String username = passwordCache.getIfPresent(token);
        if(StringUtils.isEmpty(username)) {
            throw new ServiceException("token过期或错误");
        } else {
            User user = userDao.findByUserName(username);
            if(user == null) {
                throw new ServiceException("未找到对应账号");
            } else {
                return user;
            }
        }

    }

    /**
     * 重置用户的密码
     * @param id 用户ID
     * @param token 找回密码的TOken
     * @param password 新密码
     */
    public void resetPassword(String id, String token, String password) {
        if(passwordCache.getIfPresent(token) == null) {
            throw new ServiceException("token过期或错误");
        } else {
            User user = userDao.findById(Integer.valueOf(id));
            user.setPassword(DigestUtils.md5Hex(Config.get("user.password.salt")+password));
            userDao.update(user);
            logger.info("{} 重置了密码",user.getUsername());
        }
    }
    /**
     * 修改用户邮箱
     * user，email
     */
    public void updateEmail(User user,String email){
        user.setEmail(email);
        userDao.update(user);
        logger.debug("修改{}的邮箱",user);
    }
    /**
     *该秘密
     */
    public void updatePassword(User user,String newPassword,String oldPassword){
        String salt = Config.get("user.password.salt");
        if(DigestUtils.md5Hex( Config.get("user.password.salt")+ oldPassword).equals(user.getPassword())) {

            newPassword = DigestUtils.md5Hex(salt + newPassword);
            user.setPassword(newPassword);
            userDao.update(user);
            logger.debug("{}修改密码成功",user);
        } else {

            throw new ServiceException("原始密码错误");
        }
    }


    //改头像
    public void updateAvatar(User user, String fileKey) {
        user.setAvatar(fileKey);
        userDao.update(user);
        System.out.println("updateAvatar修改头像成功");
    }

    public List<Notify> findNotifyListByUser(User user) {
        logger.info("执行findNotifyListByUser");
        System.out.println(user);
        return notifyDao.findByUserid(user.getId());
    }
    public void updateNotifyStateByIds(String ids) {
        String idArray[] = ids.split(",");
        for (int i= 0 ;i <idArray.length;i++ ){
            Notify notify = notifyDao.findById(idArray[i]);
            notify.setState(1);
            notify.setReadtime(new Timestamp(DateTime.now().getMillis()));
            notifyDao.update(notify);
        }

    }
}
