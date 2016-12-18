package com.lsg.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by tgdsl on 2016/12/16.
 */
public class Config {
    private static Properties properties = new Properties();
    static{
        try{
            properties.load(Config.class.getClassLoader().getResourceAsStream("Config.properties"));
        }catch (IOException ioe){
            throw new RuntimeException("配置文件读取异常"+ioe);
        }
    }
    public static String get(String key){
        return properties.getProperty(key);
    }
}
