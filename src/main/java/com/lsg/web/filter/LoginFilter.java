package com.lsg.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tgdsl on 2016/12/21.
 */

public class LoginFilter extends AbstractFilter {

    private  List<String> urlList = null;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String  validateUrl = filterConfig.getInitParameter("validateUrl");//去web.xml取到validate的值
        urlList = Arrays.asList(validateUrl.split(","));//将所有必须登陆后才能访问的的网页名称用，分开放到urlLiat中间

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        //获取当前用户要访问的url
        String url = req.getRequestURI();
        if (urlList!=null&&urlList.contains(url)){
            if (req.getSession().getAttribute("curr_user")==null){
                resp.sendRedirect("/login?redirect="+url);
            }else {
                filterChain.doFilter(req,resp);
            }
        }else {
            filterChain.doFilter(req, resp);
        }
    }
}
