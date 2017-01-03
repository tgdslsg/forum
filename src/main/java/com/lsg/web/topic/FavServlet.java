package com.lsg.web.topic;

import com.lsg.dto.JsonResult;
import com.lsg.entity.User;
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
@WebServlet("/topicFav")
public class FavServlet extends BaseServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action  = req.getParameter("action");
        String topicid = req.getParameter("topicid");
        User user = (User) req.getSession().getAttribute("curr_user");
        TopicService topicService = new TopicService();
        JsonResult jsonResult  = new JsonResult();
        if(StringUtils.isNotEmpty(action)&&StringUtils.isNumeric(topicid)){
            if (action.equals("fav")){
                topicService.favTopic(user,topicid);
                jsonResult.setState(JsonResult.SUCCESS);
            }else if (action.equals("unfav")){
                topicService.unfavTopic(user,topicid);
                jsonResult.setState(JsonResult.SUCCESS);
            }
        }
    }
}
