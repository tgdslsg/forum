package com.lsg.dao;

import com.lsg.entity.Reply;
import com.lsg.entity.User;
import com.lsg.util.Config;
import com.lsg.util.DbHelp;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.handlers.AbstractListHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by tgdsl on 2016/12/22.
 */
public class ReplyDao {
    public void savaReply(Reply reply){
        String sql="insert into t_reply (content,userid,topicid)values(?,?,?)";
        DbHelp.update(sql,reply.getContent(),reply.getUserid(),reply.getTopicid());
    }

    public List<Reply> findListByTopicId(String topicId) {
        String sql = "select tu.id,tu.avatar,tu.username,tr.* from t_reply tr ,t_user tu where tr.userid=tu.id and topicid=?";
        return  DbHelp.query(sql, new AbstractListHandler<Reply>() {
            @Override
            protected Reply handleRow(ResultSet resultSet) throws SQLException {
                Reply reply = new BasicRowProcessor().toBean(resultSet,Reply.class);
                User user = new User();
                user.setAvatar(Config.get("qiniu.domain")+resultSet.getString("avatar"));
                user.setUsername(resultSet.getString("username"));
                user.setId(resultSet.getInt("id"));
                reply.setUser(user);
                return reply;
            }
        },topicId);
    }
}
