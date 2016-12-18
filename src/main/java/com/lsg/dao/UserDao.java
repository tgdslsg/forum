package com.lsg.dao;

import com.lsg.entity.User;
import com.lsg.util.DbHelp;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import java.util.List;

public class UserDao {


    public void save(User user) {
        String sql = "INSERT INTO t_user(username, password, Email, phone, state, avatar) VALUES (?,?,?,?,?,?)";
        DbHelp.update(sql,user.getUsername(),user.getPassword(),user.getEmail(),user.getPhone(),user.getState(),user.getAvatar());
    }

    public User findByUserName(String  username) {
        String sql = "select * from t_user where username = ?";
        return DbHelp.query(sql, new BeanHandler<>(User.class), username);
    }

    public User findByEmail(String email) {
        String sql = "select * from t_user where Email = ?";
        return DbHelp.query(sql,new BeanHandler<>(User.class),email);
    }

    public List<User> findAll() {
        String sql = "select * from t_user";
        return DbHelp.query(sql,new BeanListHandler<>(User.class));
    }

    public void del(int id) {
        String sql = "delete from t_user where id = ?";
        DbHelp.update(sql,id);
    }

    public void update(User user) {
        String sql = "update t_user set password=?,Email=?,phone=?,state=?,avatar=? where id = ?";
        DbHelp.update(sql,user.getPassword(),user.getEmail(),user.getPhone(),user.getState(),user.getAvatar(),user.getId());
    }
}
