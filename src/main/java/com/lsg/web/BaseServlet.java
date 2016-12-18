package com.lsg.web;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Created by tgdsl on 2016/12/15.
 */
public class BaseServlet extends HttpServlet {
    public void forward(String path, HttpServletRequest req, HttpServletResponse resp)throws IOException,ServletException{
        req.getRequestDispatcher("/WEB-INF/views/User/"+path+".jsp").forward(req,resp);
    }
    public void renderText(String str,HttpServletResponse response)throws ServletException,IOException{
       response.setContentType("text/plain;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print(str);
        writer.flush();
        writer.close();
    }
    public void renderJSON(Object object,HttpServletResponse response)throws ServletException,IOException{
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print(new Gson().toJson(object));
        writer.flush();
        writer.close();
    }
}
