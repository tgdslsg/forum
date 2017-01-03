package com.lsg.dao;

import com.lsg.entity.Fav;
import com.lsg.util.DbHelp;
import org.apache.commons.dbutils.handlers.BeanHandler;

/**
 * Created by tgdsl on 2016/12/26.
 */
public class FavDao {
    public Fav findFavByTopicidAndUserid(Integer topicid,Integer userid){
        String sql = "select*from t_fav where topicid=? and userid=?";
        return DbHelp.query(sql,new BeanHandler<Fav>(Fav.class),topicid,userid);
    }
    public void addFav(Fav  fav){
        String sql = "insert into t_fav (userid,topicid)values(?,?)";
        DbHelp.update(sql,fav.getUserid(),fav.getTopicid());
    }
    public void delFav(Integer userid,String  topicid){
        String sql = "delete from t_fav where userid=?and topicid=?";
        DbHelp.update(sql,userid,Integer.valueOf(topicid));
    }


}
