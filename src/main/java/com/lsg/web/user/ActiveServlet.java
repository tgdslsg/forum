package com.lsg.web.user;

import com.lsg.exception.ServiceException;
import com.lsg.service.UserService;
import com.lsg.util.StringUtils;
import com.lsg.web.BaseServlet;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by tgdsl on 2016/12/17.
 */
@WebServlet("/user/active")
public class ActiveServlet extends BaseServlet {
    private static Logger logger = LoggerFactory.getLogger(ActiveServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取token值
        String token = req.getParameter("_");
        logger.debug(token);
        //根据token判断是否为空
        if (StringUtils.isEmpty(token)){
            resp.sendError(404);
        }else{
            UserService userService = new UserService();
            try {
                userService.activeUser(token);
                forward("active_success",req,resp);
            } catch (ServiceException ex) {
                req.setAttribute("message",ex.getMessage());
                forward("active_error",req,resp);
            }
        }


    }
}
