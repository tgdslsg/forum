package com.lsg.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;


/**
 * Created by tgdsl on 2016/12/16.
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    private static Logger logger = LoggerFactory.getLogger(StringUtils.class);

    public static String isoToUtf8(String str){
       try {
           return new String(str.getBytes("iso8859-1"), "UTF-8");
       }catch (UnsupportedEncodingException e){
           logger.error("字符串转换异常"+str);
           throw new RuntimeException("字符串"+str+"转换异常");
       }
    }

}
