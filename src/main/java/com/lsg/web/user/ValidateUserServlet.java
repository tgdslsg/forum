package com.lsg.web.user;

import com.lsg.service.UserService;
import com.lsg.util.StringUtils;
import com.lsg.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tgdsl on 2016/12/16.
 */
@WebServlet("/validate/user")
public class ValidateUserServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        System.out.println(username+"iiiii");
       username = StringUtils.isoToUtf8(username);
        UserService userService = new UserService();
        Boolean result = userService.validateUserName(username);
        if(result) {
            renderText("true",resp);
        } else {
            renderText("false",resp);
        }
    }
}
