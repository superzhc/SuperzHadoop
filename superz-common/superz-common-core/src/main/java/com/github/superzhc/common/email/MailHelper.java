package com.github.superzhc.common.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author superz
 * @create 2023/1/4 12:24
 **/
public class MailHelper {
    private static final Logger LOG = LoggerFactory.getLogger(MailHelper.class);

    private String email = null;
    private String password = null;
    private Session session = null;
    private MimeMessage message = null;

    private MimeMultipart content = null;

    public MailHelper(Properties properties) {
        this.session = Session.getInstance(properties);
    }

    public MailHelper(Properties properties, String email, String password) {
        this.email = email;
        this.password = password;
        this.session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
    }

    public static MailHelper SMTP_ENT_QQ(String email, String password) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.host", "smtp.exmail.qq.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.from", email);
        return new MailHelper(props, email, password);
    }

    public static MailHelper qq(String email, String password) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.host", "smtp.qq.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.from", email);
        return new MailHelper(props, email, password);
    }

    public static MailHelper _163(String email, String password) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.host", "smtp.163.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.from", email);
        return new MailHelper(props, email, password);
    }

    public MimeMessage getMessage() {
        if (null == message) {
            message = new MimeMessage(this.session);
        }
        return message;
    }

    private MimeMultipart getContent() {
        if (null == content) {
            content = new MimeMultipart("mixed");
        }
        return content;
    }

    public MailHelper subject(String str) {
        return subject(str, "UTF-8");
    }

    public MailHelper subject(String str, String charset) {
        try {
            getMessage().setSubject(str, charset);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MailHelper from(String nickname) {
        return from(this.email, nickname);
    }

    public MailHelper from(String email, String nickname) {
        try {
            String encodeNickname = MimeUtility.encodeText(nickname);
            getMessage().setFrom(new InternetAddress(String.format("%s <%s>", encodeNickname, email)));
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
            getMessage().setReplyTo(InternetAddress.parse(replies.replace(";", ",")));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MailHelper to(String... recipientArr) {
        if (null == recipientArr || recipientArr.length == 0) {
            throw new RuntimeException("At least one recipient");
        }
        return to(String.join(",", recipientArr));
    }

    public MailHelper to(String recipients) {
        try {
            getMessage().setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients.replace(";", ",")));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MailHelper cc(String... recipientArr) {
        if (null == recipientArr || recipientArr.length == 0) {
            throw new RuntimeException("At least one recipient");
        }
        return cc(String.join(",", recipientArr));
    }

    public MailHelper cc(String recipients) {
        try {
            getMessage().setRecipients(Message.RecipientType.CC, InternetAddress.parse(recipients.replace(";", ",")));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MailHelper bcc(String... recipientArr) {
        if (null == recipientArr || recipientArr.length == 0) {
            throw new RuntimeException("At least one recipient");
        }
        return bcc(String.join(",", recipientArr));
    }

    public MailHelper bcc(String recipients) {
        try {
            getMessage().setRecipients(Message.RecipientType.BCC, InternetAddress.parse(recipients.replace(";", ",")));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MailHelper text(String textStr) {
        try {
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText(textStr, "UTF-8");
            getContent().addBodyPart(bodyPart);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MailHelper html(String htmlStr) {
        try {
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(htmlStr, "text/html; charset=utf-8");
            getContent().addBodyPart(bodyPart);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MailHelper attach(File file) {
        return attach(file, null);
    }

    public MailHelper attach(File file, String fileName) {
        try {
            MimeBodyPart attachmentPart = new MimeBodyPart();
            FileDataSource fds = new FileDataSource(file);
            attachmentPart.setDataHandler(new DataHandler(fds));
            attachmentPart.setFileName(null == fileName ? MimeUtility.encodeText(fds.getName()) : MimeUtility.encodeText(fileName));
            getContent().addBodyPart(attachmentPart);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MailHelper attach(URL url, String fileName) {
        try {
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataHandler dataHandler = new DataHandler(url);
            attachmentPart.setDataHandler(dataHandler);
            attachmentPart.setFileName(null == fileName ? MimeUtility.encodeText(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())) : MimeUtility.encodeText(fileName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public void send() {
        try {
            MimeMessage message = getMessage();
            message.setContent(getContent());
            message.setSentDate(new Date());
            Transport.send(message);
            LOG.info("发送邮件成功");
        } catch (Exception e) {
            throw new RuntimeException("发送邮件异常", e);
        }
    }

    public static void main(String[] args) {
        MailHelper mailHelper = MailHelper.qq("xxxx@qq.com", "your password");
        mailHelper.from("易")
                .to("zhengchao0555@163.com")
                .subject("测试邮件")
                .html("<h3>HTML部分</h3>")
                .text("TEXT部分")
                .send();
    }
}
