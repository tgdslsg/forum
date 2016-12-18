package com.lsg.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tgdsl on 2016/12/16.
 */
public class Email {
    private static Logger logger  = LoggerFactory.getLogger(Email.class);
        public static void sendHtmlEmail(String toAdderss,String subject, String context){
            HtmlEmail htmlEmail = new HtmlEmail();
            htmlEmail.setHostName(Config.get("email_smtp"));
 //           htmlEmail.setSmtpPort(Integer.valueOf(Config.get("email.port")));
            htmlEmail.setAuthentication(Config.get("email_username"),Config.get("email_password"));
            htmlEmail.setCharset("UTF-8");
            htmlEmail.setStartTLSEnabled(true);

            try {
                htmlEmail.setFrom(Config.get("email_frommail"));
                htmlEmail.setSubject(subject);
                htmlEmail.setHtmlMsg(context);
                htmlEmail.addTo(toAdderss);
                htmlEmail.send();
            }catch (EmailException ee){
                logger.error("向"+toAdderss+"发行邮件错误");
                throw  new RuntimeException("发送邮件失败："+toAdderss);
            }

        }
}
