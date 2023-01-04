package com.github.superzhc.common.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @author superz
 * @create 2023/1/4 12:24
 **/
public class MailHelper {
    private static final Logger LOG = LoggerFactory.getLogger(MailHelper.class);

    private Session session;
    private MimeMessage mimeMessage;

    public MailHelper(Properties properties) {
        this.session = Session.getInstance(properties);
        this.mimeMessage = new MimeMessage(this.session);
    }

//    public MailHelper(Properties properties, String username, String password) {
//        this.session = Session.getInstance(properties, new Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(username, password);
//            }
//        });
//    }

    public MailHelper subject(String str) {
        return subject(str, "UTF-8");
    }

    public MailHelper subject(String str, String charset) {
        try {
            mimeMessage.setSubject(str, charset);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MailHelper from(String username, String nickname) {
        try {
            String encodeNickname = MimeUtility.encodeText(nickname);
            mimeMessage.setFrom(new InternetAddress(String.format("%s<%s>", encodeNickname, username)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MailHelper replyTo(String... replyArr) {
        if (null == replyArr || replyArr.length == 0) {
            throw new RuntimeException("At least one reply");
        }

        return replyTo(String.join(",", replyArr));
    }

    public MailHelper replyTo(String replies) {
        try {
            mimeMessage.setReplyTo(InternetAddress.parse(replies.replace(";", ",")));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

//    public MailHelper
}
