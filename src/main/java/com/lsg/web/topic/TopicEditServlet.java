package com.lsg.web.topic;

import com.lsg.dao.TopicDao;
import com.lsg.dto.JsonResult;
import com.lsg.entity.Node;
import com.lsg.entity.Topic;
import com.lsg.exception.ServiceException;
import com.lsg.service.TopicService;
import com.lsg.util.StringUtils;
import com.lsg.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by jimi_jin on 2016-12-23.
 */
@WebServlet("/topicEdit")
public class TopicEditServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String topicId = req.getParameter("topicId");
        TopicService service = new TopicService();
        Topic topic = service.findTopicById(topicId);

        List<Node> nodeList = service.findAllNode();
        req.setAttribute("topic",topic);
        req.setAttribute("nodeList",nodeList);
        forward("topic/topicEdit",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String nodeid = req.getParameter("nodeid");
        String topicId = req.getParameter("topicId");
        JsonResult result = null;
        if(StringUtils.isNumeric(topicId)){
            TopicService service  = new TopicService();
            try {
                service.updateTopicById(title, content, nodeid, topicId);
                result = new JsonResult();
                result.setState(JsonResult.SUCCESS);
                result.setData(topicId);
            }catch (ServiceException e){
                result = new JsonResult(e.getMessage());
            }
        }else{
            result = new JsonResult("参数异常");
        }
        renderJSON(result,resp);
    }
}
