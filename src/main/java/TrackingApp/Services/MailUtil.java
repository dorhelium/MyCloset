package TrackingApp.Services;

import org.apache.log4j.Logger;

import javax.mail.*;
import java.util.Properties;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {

    static String fromEmail = "shop.my.closet.v1@gmail.com";
    static String fromEmailPassword = "shopmyclosetv1";
    private final static Logger LOGGER = Logger.getLogger(MailUtil.class.getName());

    public static void sendMail (String recepient, String subject, String messageContent){
        LOGGER.info("Start to send mail to"+ recepient);

        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, fromEmailPassword);
            }
        });

        try {
            Message message = prepareMessage(session, recepient, subject, messageContent);
            Transport.send(message);
            LOGGER.info("Mail was sent seccessfully to"+ recepient);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static Message prepareMessage(Session session, String recepient, String subject, String messageContent) throws Exception{
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
        message.setSubject(subject);
        message.setText(messageContent);
        return message;
    }

}
