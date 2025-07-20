
package com.yb.versionchecker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Notifier {
    private static final Logger logger = AppLogger.getLogger();
    private static final String RECIPIENTS_JSON_PATH = "src/main/resources/recipients.json";
    public static void sendNotification(String emailBody, String notificationGroup) throws Exception {
        String username = System.getenv("EMAIL_USERNAME");
        String password = System.getenv("EMAIL_PASSWORD");
        logger.log(Level.FINE,"Send mail called with:"+emailBody+"->"+notificationGroup);
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username,password);
                }
            });

        List<String> recipients = (notificationGroup.equalsIgnoreCase("debug")) ? getDebugRecipients() : getUpdateRecipients() ;

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("ybtest003@gmail.com"));
        for (String recipient : recipients) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        }
        message.setSubject("OS Version Update Notification");
        message.setText(emailBody);

        Transport.send(message);
        logger.log(Level.FINE,"Send mail ended with:"+emailBody+"->"+recipients.toString());
    }

    public static List<String> getDebugRecipients() {
        return getRecipientsByKey("debug");
    }

    public static List<String> getUpdateRecipients() {
        Set<String> recipients = new HashSet<>();
        recipients.addAll(getRecipientsByKey("updates_only"));
        recipients.addAll(getRecipientsByKey("debug"));
        return new ArrayList<>(recipients);
    }

    private static List<String> getRecipientsByKey(String key) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File(RECIPIENTS_JSON_PATH));
            JsonNode arrayNode = root.get(key);
            List<String> result = new ArrayList<>();

            if (arrayNode != null && arrayNode.isArray()) {
                for (JsonNode email : arrayNode) {
                    result.add(email.asText());
                }
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE,"Exception in Notifier class",e);
            return new ArrayList<>();
        }
    }
}
