package com.lsg.web;

import com.lsg.entity.Node;
import com.lsg.entity.Topic;
import com.lsg.service.TopicService;
import com.lsg.util.Page;
import com.lsg.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by tgdsl on 2016/12/15.
 */
@WebServlet("/home")
public class HomeServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nodeid = req.getParameter("nodeid");
        String p = req.getParameter("p");
        Integer pageNo = StringUtils.isNumeric(p)?Integer.valueOf(p):1;
        if (!StringUtils.isEmpty(nodeid) && !StringUtils.isNumeric(nodeid)){
            forward("index",req,resp);
            return;
        }
        TopicService topicService = new TopicService();
        List<Node> nodeList = topicService.findAllNode();

        Page<Topic> page = topicService.findAllTopics(nodeid,pageNo);

        req.setAttribute("page",page);
        req.setAttribute("nodeList",nodeList);
        forward("User/index",req,resp);
    }
}
