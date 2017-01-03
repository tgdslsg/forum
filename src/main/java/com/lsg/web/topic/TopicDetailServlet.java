package com.lsg.web.topic;

import com.lsg.entity.Fav;
import com.lsg.entity.Reply;
import com.lsg.entity.Topic;
import com.lsg.entity.User;
import com.lsg.service.TopicService;
import com.lsg.util.StringUtils;
import com.lsg.web.BaseServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by tgdsl on 2016/12/26.
 */
@WebServlet("/topicDetail")
public class TopicDetailServlet extends BaseServlet {
    Logger logger = LoggerFactory.getLogger(TopicDetailServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("进入TopicDetailServlet");
        String topicid = req.getParameter("topicid");
        TopicService topicService = new TopicService();
        try{  //更新topic表中的clicknum字段
            Topic topic  = topicService.findTopicById(topicid);
            topic.setClicknum(topic.getClicknum()+1);
            topicService.updateTopic(topic);
            logger.info("更新topic表中的clicknum字段");
            //获取该topicid对应的回复列表
            List<Reply>  replyList = topicService.findReplyListByTopicId(topicid);
            req.setAttribute("replyList",replyList);
            req.setAttribute("topic",topic);
            logger.info("获取该topicid对应的回复列表");
            User user  = (User) req.getSession().getAttribute("curr_user");
            //panduanyonghushifou 收藏
            if (user!=null&& StringUtils.isNumeric(topicid)){
                Fav fav = topicService.findFavByTopicidAndUserid(topicid,user);
                req.setAttribute("fav",fav);
            }
            logger.info("准备跳转至topic/topicDetail");
            forward("topic/topicDetail",req,resp);
        }catch (Exception exception){
            System.out.println(exception);
            resp.sendError(404404);
        }
    }
}

