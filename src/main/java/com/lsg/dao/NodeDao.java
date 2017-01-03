package com.lsg.dao;

import com.lsg.entity.Node;
import com.lsg.util.DbHelp;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.List;

/**
 * Created by tgdsl on 2016/12/21.
 */
public class NodeDao {
    public List<Node> findAllNodes(){
        String sql = "select * from t_node ";
        return  DbHelp.query(sql,new BeanListHandler<>(Node.class));
    }
    public Node findNodeById(Integer nodeid){
        String sql = "select * from t_node where id=?";
        return  DbHelp.query(sql,new BeanHandler<Node>(Node.class),nodeid);
    }
    public void update(Node node){
        String sql = "update t_node set topicnum=?,nodename=? where id=?";
        DbHelp.update(sql,node.getTopicnum(),node.getNodename(),node.getId());
    }

    public Node findNodeByName(String nodeName) {
        String sql = "select * from t_node where nodename = ?";
        return DbHelp.query(sql,new BeanHandler<>(Node.class),nodeName);
    }

    public void del(String id) {
        String sql = "delete from t_node where id = ?";
        DbHelp.update(sql,id);
    }
}
