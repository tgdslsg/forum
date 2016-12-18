package com.lsg.dao;

import com.lsg.entity.LoginLog;
import com.lsg.util.DbHelp;

import java.util.List;

/**
 * Created by tgdsl on 2016/12/18.
 */
public class LoginLogDao {

    public void save(LoginLog log){
        String sql = "insert into t_login_log(ip,userid) values(?,?)";
        DbHelp.update(sql,log.getIp(),log.getUserId());
    }
}
