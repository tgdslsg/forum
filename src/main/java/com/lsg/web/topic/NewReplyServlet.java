package com.lsg.web.topic;

import com.lsg.entity.User;
import com.lsg.exception.ServiceException;
import com.lsg.service.TopicService;
import com.lsg.util.StringUtils;
import com.lsg.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tgdsl on 2016/12/27.
 */
@WebServlet("/newReply")
public class NewReplyServlet extends BaseServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String topicid = req.getParameter("topicid");
        String content = req.getParameter("content");
        User user = (User)req.getSession().getAttribute("curr_user");
        TopicService topicService = new TopicService();
        if(StringUtils.isNumeric(topicid)){
            try{
                topicService.addTopicReply(topicid,content,user);
            }catch (ServiceException e){
                resp.sendError(404,e.getMessage());
            }
        }else {
            resp.sendError(404);
        }
        resp.sendRedirect("/topicDetail?topicid="+topicid);
    }
}