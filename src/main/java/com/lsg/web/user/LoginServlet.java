package com.lsg.web.user;

import com.google.common.collect.Maps;
import com.lsg.entity.User;
import com.lsg.exception.ServiceException;
import com.lsg.service.UserService;
import com.lsg.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * Created by tgdsl on 2016/12/16.
 */
@WebServlet("/login")
public class LoginServlet extends BaseServlet {



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().removeAttribute("curr_user");
        forward("User/login",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        //获取客户端的IP地址
        String ip = req.getRemoteAddr();//服务端的远程就是客户端

        Map<String,Object> result = Maps.newHashMap();
        UserService userService = new UserService();
        try{
            User user = userService.login(username,password,ip);
            //将这个登陆成功的用户放入session中
            HttpSession session = req.getSession();
            session.setAttribute("curr_user",user);
            result.put("state","success");
        }catch (ServiceException ex) {
            result.put("state","error");
            result.put("message",ex.getMessage());
        }
        renderJSON(result,resp);
    }
}
