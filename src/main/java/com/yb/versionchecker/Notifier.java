
package com.yb.versionchecker;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.mail.*;
import javax.mail.internet.*;
import java.nio.file.*;
import java.util.*;

public class Notifier {

    public static void sendNotification(VersionInfo newVersion, VersionInfo oldVersion) throws Exception {
        String username = System.getenv("EMAIL_USERNAME");
        String password = System.getenv("EMAIL_PASSWORD");
        ObjectMapper mapper = new ObjectMapper();
        List<String> recipients;
        recipients = mapper.readValue(
                Paths.get("src/main/resources/recipients.json").toFile(),
                List.class
        );

        StringBuilder body = new StringBuilder();
        if(!newVersion.isDifferent(oldVersion)){
            body.append("There are no new updates at this time.");
        }else {
            body.append("OS Version Update Detected:\n\n");
            if (!newVersion.ios.equals(oldVersion.ios)) {
                body.append("iOS: ").append(oldVersion.ios).append(" ➡ ").append(newVersion.ios).append("\n");
            }
            if (!newVersion.ipados.equals(oldVersion.ipados)) {
                body.append("iPadOS: ").append(oldVersion.ipados).append(" ➡ ").append(newVersion.ipados).append("\n");
            }
            if (!newVersion.android.equals(oldVersion.android)) {
                body.append("Android: ").append(oldVersion.android).append(" ➡ ").append(newVersion.android).append("\n");
            }
        }

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

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("ybtest003@gmail.com"));
        for (String recipient : recipients) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        }
        message.setSubject("OS Version Update Notification");
        message.setText(body.toString());

        Transport.send(message);
    }
}
