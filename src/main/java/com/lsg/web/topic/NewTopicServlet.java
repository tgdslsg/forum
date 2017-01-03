package com.lsg.web.topic;

import com.lsg.dto.JsonResult;
import com.lsg.entity.Node;
import com.lsg.entity.Topic;
import com.lsg.entity.User;
import com.lsg.exception.ServiceException;
import com.lsg.service.TopicService;
import com.lsg.util.Config;
import com.lsg.web.BaseServlet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/newTopic")
public class NewTopicServlet extends BaseServlet {
    TopicService topicService = new TopicService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取token传到界面
        Auth auth = Auth.create(Config.get("qiniu.ak"),Config.get("qiniu.sk"));//七牛用的Auth
        StringMap stringMap = new StringMap();
        stringMap.put("returnBody","{ \"success\": true,\"file_path\": \""+Config.get("qiniu.domain")+"${key}\"}");
        String token = auth.uploadToken(Config.get("qiniu.bucket"),null,3600,stringMap);
        System.out.println("token:"+token);
        //获取nodelist，传到界面
        List<Node> nodeList = new TopicService().findAllNode();
        req.setAttribute("nodeList",nodeList);
        req.setAttribute("token",token);
       // req.getRequestDispatcher("/WEB-INF/views/"+"topic/newtopic"+".jsp").forward(req,resp);

       forward("topic/newTopic",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String nodeid = req.getParameter("nodeid");
        User user = (User) req.getSession().getAttribute("curr_user");
        Topic topic = null;
        JsonResult jsonResult = null;
        try {
            topic = topicService.addNewTopic(title,content, Integer.valueOf(nodeid), user.getId());
            jsonResult  =  new JsonResult(topic);
        } catch (ServiceException ex) {
            jsonResult = new JsonResult(ex.getMessage());
        }
        renderJSON(jsonResult,resp);
    }
}
