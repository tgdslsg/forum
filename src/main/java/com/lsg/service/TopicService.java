package com.lsg.service;

import com.google.common.collect.Maps;
import com.lsg.dao.*;
import com.lsg.entity.*;
import com.lsg.exception.ServiceException;
import com.lsg.util.Config;
import com.lsg.util.Page;
import com.lsg.util.StringUtils;
import org.joda.time.DateTime;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;



/**
 * Created by tgdsl on 2016/12/22.
 */
public class TopicService {
    TopicDao topicDao = new TopicDao();
    UserDao userDao = new UserDao();
    NodeDao nodeDao = new NodeDao();
    ReplyDao replyDao = new ReplyDao();
    FavDao favDao  = new FavDao();
    NotifyDao notifyDao = new NotifyDao();
    public List<Node> findAllNode(){
        List<Node> nodeList = nodeDao.findAllNodes();
        return nodeList;
    }

    public Topic addNewTopic(String title, String content, Integer nodeid,Integer userId){
        //封装topic对象
        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setNodeid(nodeid);
        topic.setUserid(userId);
        //暂时设置最后回复时间为当前时间
        topic.setLastreplytime(new Timestamp(new DateTime().getMillis()));
        Integer topicId = topicDao.save(topic);
        topic.setId(topicId);

        //更新node表的topicnum
        Node node = nodeDao.findNodeById(nodeid);
        if (node != null){
            node.setTopicnum(node.getTopicnum() + 1);
            nodeDao.update(node);
        } else{
            throw new ServiceException("节点不存在");
        }
        return topic ;
    }

    public Topic findTopicById(String topicId) {
        if (StringUtils.isNumeric(topicId)){
            Topic topic = topicDao.findTopicById(topicId);
            if (topic != null ){
                //通过topic对象的userid、nodeid 获取user和node对象,并set到tipic对象中;
                User user = userDao.findById(topic.getUserid());
                Node node = nodeDao.findNodeById(topic.getNodeid());
                user.setAvatar(Config.get("qiniu.domain")+user.getAvatar());
                topic.setUser(user);
                topic.setNode(node);

                //更新topic表中的clicknum字段
                topic.setClicknum(topic.getClicknum() + 1/2);
                topicDao.update(topic);

                return topic;
            }else{
                throw new ServiceException("该帖不存在或已被删除");
            }
        }else{
            throw  new ServiceException("参数错误");
        }
    }

    public void addTopicReply(String topicId, String content, User user) {
        //新增回复到t_reply表
        Reply reply = new Reply();
        reply.setContent(content);
        reply.setUserid(user.getId());
        reply.setTopicid(Integer.valueOf(topicId));
        replyDao.savaReply(reply);

        //更新t_topic表中的replynum 和 lastreplytime字段
        Topic topic = topicDao.findTopicById(topicId);
        if (topic != null){
            topic.setReplynum(topic.getReplynum() + 1);
            topic.setLastreplytime(new Timestamp(DateTime.now().getMillis()));
            topicDao.update(topic);
        }else{
            throw new ServiceException("回复的主题不存在或被删除");
        }
        //新增回复通知
        if (!user.getId().equals(topic.getUserid())){
            Notify notify = new Notify();
            notify.setUserid(topic.getUserid());
            notify.setContent("您的主题 <a href=\"/topicDetail?topicid="+topic.getId()+"\">["+ topic.getTitle()+"] </a> 有了新的回复,请查看.");
            notify.setState(Notify.getNotifyStateUnread());
            notifyDao.save(notify);
        }

    }

    public List<Reply> findReplyListByTopicId(String topicId) {
        return replyDao.findListByTopicId(topicId);
    }

    public void updateTopic(Topic topic) {
        topicDao.update(topic);
    }

    public Fav findFavByTopicidAndUserid(String topicid, User user) {
        return favDao.findFavByTopicidAndUserid(Integer.valueOf(topicid),user.getId());
    }
    public void favTopic(User user,String topicid){
        Fav  fav = new Fav();
        fav.setTopicid(Integer.valueOf(topicid));
        fav.setUserid(user.getId());
        favDao.addFav(fav);
        //topic表收藏+1
        Topic topic = topicDao.findTopicById(topicid);
        topic.setFavnum(topic.getFavnum()+1);
        topicDao.update(topic);
    }
    public void unfavTopic(User user,String topicid){
        favDao.delFav(user.getId(),topicid);
        //topic收藏字段降一
        Topic topic = topicDao.findTopicById(topicid);
        topic.setFavnum(topic.getFavnum()-1);
        topicDao.update(topic);
    }
    public Page<Topic> findAllTopics(String nodeid,Integer pageNo){
        HashMap<String,Object> map  = Maps.newHashMap();
        int count = 0;
        if (StringUtils.isEmpty(nodeid)){
            count = topicDao.count();
        }else{
            count = topicDao.count();
        }
        /*Node node = nodeDao.findNodeById(Integer.valueOf(nodeid));
        count = node.getTopicnum();*/
        Page<Topic> topicPage = new Page<>(count,pageNo);
        map.put("nodeid",nodeid);
        map.put("start",topicPage.getStart());
        map.put("pageSize",topicPage.getPageSize());

        List<Topic> topicList = topicDao.findAll(map);
        topicPage.setItems(topicList);
        return topicPage;
    }

    public void updateTopicById(String title, String content, String nodeid, String topicId) {
        Topic topic = topicDao.findTopicById(topicId);
        Integer lastNodeId = topic.getNodeid();
        if( topic.isEdit() ) {
            //更新topic
            topic.setTitle(title);
            topic.setContent(content);
            topic.setNodeid(Integer.valueOf(nodeid));
            topicDao.update(topic);
            updatNode(lastNodeId,nodeid);
        }else{
            throw new ServiceException("该帖已经不可编辑");
        }
    }

    private void updatNode(Integer lastNodeId,String nodeid) {
        if (lastNodeId != Integer.valueOf(nodeid)) {
            //更新node表，使得原來的node的topicnum -1
            Node lastNode = nodeDao.findNodeById(lastNodeId);
            lastNode.setTopicnum(lastNode.getTopicnum() - 1);
            nodeDao.update(lastNode);
            //更新node表，使得新的node的topicnum + 1
            Node newNode = nodeDao.findNodeById(Integer.valueOf(nodeid));
            newNode.setTopicnum(newNode.getTopicnum() + 1);
            nodeDao.update(newNode);
        }
    }

}

