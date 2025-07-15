
package com.example.versionchecker;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.mail.*;
import javax.mail.internet.*;
import java.nio.file.*;
import java.util.*;

public class Notifier {

    public static void sendNotification(VersionInfo newVersion) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<String> recipients = mapper.readValue(
                Paths.get("src/main/resources/recipients.json").toFile(),
                List.class
        );

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.example.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("your-email@example.com", "your-password");
                }
            });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("your-email@example.com"));
        for (String recipient : recipients) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        }
        message.setSubject("OS Version Update Notification");
        message.setText("New OS versions: iOS: " + newVersion.ios + "iPadOS: " + newVersion.ipados + " Android: " + newVersion.android);

        Transport.send(message);
    }
}
