package com.lsg.web.user;

import com.google.common.collect.Maps;
import com.lsg.exception.ServiceException;
import com.lsg.service.UserService;
import com.lsg.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/foundpassword")
public class FoundPasswordServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forward("foundpassword",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        String value = req.getParameter("value");

        //获取当前客户端的sessionID
        String sessionId = req.getSession().getId();

        Map<String,Object> result = Maps.newHashMap();

        UserService userService = new UserService();
        try {
            userService.foundPassword(sessionId, type, value);

            result.put("state","success");
        } catch (ServiceException ex) {
            result.put("state","error");
            result.put("message",ex.getMessage());
        }

        renderJSON(result,resp);


    }
}
