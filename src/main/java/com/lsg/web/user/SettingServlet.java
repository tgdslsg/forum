package com.lsg.web.user;

import com.google.common.collect.Maps;
import com.lsg.dto.JsonResult;
import com.lsg.entity.User;
import com.lsg.exception.ServiceException;
import com.lsg.service.UserService;
import com.lsg.util.Config;
import com.lsg.web.BaseServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import com.qiniu.util.Auth;

/**
 * Created by tgdsl on 2016/12/19.
 */
@WebServlet("/setting")
public class SettingServlet extends BaseServlet {
    Logger logger = LoggerFactory.getLogger(SettingServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //计算七牛云存储的Token
        Auth auth = Auth.create(Config.get("qiniu.ak"), Config.get("qiniu.sk"));
        String token = auth.uploadToken(Config.get("qiniu.bucket"));

        req.setAttribute("token",token);
        forward("User/setting",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        System.out.println(action);
        if ("profile".equals(action)){
            updateProfile(req,resp);
            System.out.println(action);
        }else if ("password".equals(action)){
            updatePassword(req,resp);
            logger.info("选择password，准备修改");
        }else if ("avatar".equals(action)){
            updateAvatar(req,resp);
            logger.info("选择头像");
        }
    }

    /**
     * 修改头像
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void updateAvatar(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException {
        String fileKey = req.getParameter("fileKey");
        User user = getCurrentUser(req);

        UserService userService = new UserService();
        userService.updateAvatar(user,fileKey);

        JsonResult result = new JsonResult();
        result.setState(JsonResult.SUCCESS);
        try {
            renderJSON(result,resp);
        } catch (ServiceException e) {
            JsonResult result1 = new JsonResult(e.getMessage());
            renderJSON(result1,resp);
        }
    }

    //修改密码
    private void updatePassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException{

        String oldPassword = req.getParameter("oldpassword");
        String newPassword = req.getParameter("newpassword");
        logger.info("oldp:"+oldPassword+"newp"+newPassword);
        System.out.println(req);
        User user = getCurrentUser(req);
        logger.info("user{}",user);
        UserService userService = new UserService();
        userService.updatePassword(user,newPassword,oldPassword);
        logger.info("updatePassword执行成功");
        JsonResult result = new JsonResult();
        result.setState(JsonResult.SUCCESS);
        logger.info("返回状态值");
        try {
            renderJSON(result,resp);
        } catch (ServiceException e) {
            JsonResult result1 = new JsonResult(e.getMessage());
            renderJSON(result1,resp);
        }

    }

    //选，改，返回 电子邮件
    private void updateProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException,ServletException{
        String email = req.getParameter("email");
        User user = getCurrentUser(req);
        System.out.println(email);

        UserService userService = new UserService();
        userService.updateEmail(user,email);

        Map<String,Object> result = Maps.newHashMap();
        result.put("state","success");
        renderJSON(result,resp);
        logger.info("return JSON");
    }

}
