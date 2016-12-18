package com.lsg.web.user;

import com.google.common.collect.Maps;
import com.lsg.service.UserService;
import com.lsg.web.BaseServlet;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


/**
 * Created by tgdsl on 2016/12/16.
 */
@WebServlet("/reg")
public class RegServlet extends BaseServlet {
    private static Logger logger = LoggerFactory.getLogger(RegServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forward("reg",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password  = req.getParameter("password");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");

        Map<String,Object>  result = Maps.newHashMap();
        UserService userService = new UserService();
        try {
            userService.saveNewUser(username, password, email, phone);
            result.put("state","success");
            logger.debug("{}邮件发送成功",email);
        }catch (Exception e) {
            logger.error("{}邮件发送失败",email);
            result.put("state","error");
        }
        renderJSON(result,resp);
    }
}
