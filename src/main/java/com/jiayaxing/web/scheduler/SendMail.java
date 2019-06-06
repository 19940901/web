package com.jiayaxing.web.scheduler;


import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Component
public class SendMail {


    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SendMail.class);

    //@Scheduled(fixedRate = 1000 * 20)
    public void sendMail() {
        try {
            String to = "2587877008@qq.com";
            String from = "cc2587877008@outlook.com";

            Properties props = new Properties();
//            props.put("mail.smtp.socketFactory.port", "587");
//            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//            props.put("mail.smtp.socketFactory.fallback", "true");
            props.put("mail.smtp.host", "smtp-mail.outlook.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                            return new javax.mail.PasswordAuthentication("cc2587877008@outlook.com", "7434673cb");
                        }
                    });

//            Session emailSession = Session.getDefaultInstance(props, null);

            String msgBody = "Sending email using JavaMail API...";
            String msgcontent = "<h1>hello</h1>";

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from, "曹斌"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to, "曹斌"));
            msg.setSubject("Welcome To Java Mail API");
            //msg.setText(msgBody);
            msg.setContent(msgcontent, "text/html");
            Transport.send(msg);
            logger.info("Email sent successfully...");
            // logger.error("Email sent successfully...");
        } catch (AddressException e) {
            //logger.error(e.getMessage());
        } catch (MessagingException e) {
            // logger.error(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            // logger.error(e.getMessage());
        }
    }

}
