package com.lsg.web;

import com.google.gson.Gson;
import com.lsg.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Created by tgdsl on 2016/12/15.
 */
public class BaseServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(BaseServlet.class);
    public void forward(String path, HttpServletRequest req, HttpServletResponse resp)throws IOException,ServletException{
        req.getRequestDispatcher("/WEB-INF/views/"+path+".jsp").forward(req,resp);
    }
    public void renderText(String str,HttpServletResponse response)throws ServletException,IOException{
       response.setContentType("text/plain;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print(str);
        writer.flush();
        writer.close();
    }
    public void renderJSON(Object object,HttpServletResponse response)throws ServletException,IOException{
        response.setContentType("application/json;charset=UTF-8");//声明
        PrintWriter writer = response.getWriter();
        writer.print(new Gson().toJson(object));
        writer.flush();
        writer.close();
    }
    public User getCurrentUser(HttpServletRequest req){
        HttpSession session= req.getSession();
        System.out.println(session);
        if(session.getAttribute("curr_user")==null){//curr_user登陆时放入session中的
            System.out.println("jasckjsc");
            return null;

        }else {
            User user = (User)session.getAttribute("curr_user");
            System.out.println("wjjjjj"+user);
            return user;

        }
    }
}
