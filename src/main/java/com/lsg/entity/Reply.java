package com.lsg.entity;

import java.security.Timestamp;

/**
 * Created by tgdsl on 2016/12/22.
 */
public class Reply {
    private Integer replayid;
    private Integer userid;
    private String content;
    private Integer topicid;
    private Timestamp creattime;

    private  User user;
    private Node node;

    public Integer getReplayid() {
        return replayid;
    }

    public void setReplayid(Integer replayid) {
        this.replayid = replayid;
    }

    public User getUser() {
        return user;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Integer getId() {
        return replayid;
    }

    public void setId(Integer id) {
        this.replayid = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTopicid() {
        return topicid;
    }

    public void setTopicid(Integer topicid) {
        this.topicid = topicid;
    }

    public Timestamp getCreattime() {
        return creattime;
    }

    public void setCreattime(Timestamp creattime) {
        this.creattime = creattime;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
