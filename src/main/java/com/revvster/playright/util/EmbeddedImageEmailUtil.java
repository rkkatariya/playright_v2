/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.util;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author REVV-03
 */
public class EmbeddedImageEmailUtil {

    /**
     * Sends an HTML e-mail with inline images.
     *
     * @param host SMTP host
     * @param port SMTP port
     * @param userName e-mail address of the sender's account
     * @param password password of the sender's account
     * @param toAddress e-mail address of the recipient
     * @param subject e-mail subject
     * @param htmlBody e-mail content with HTML tags
     * @param mapInlineImages key: Content-ID value: path of the image file
     * @throws AddressException
     * @throws MessagingException
     */
    public static void send(String host, String port,
            final String userName, final String password, String toAddress,
            String subject, String htmlBody,
            Map<String, String> mapInlineImages)
            throws AddressException, MessagingException, IOException {
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);
        String[] toAddresses = toAddress.split(",", 10);
        InternetAddress[] toIAddresses = new InternetAddress[toAddresses.length];
        for(int i=0; i < toAddresses.length; i++) {
            toIAddresses[i] = new InternetAddress(toAddresses[i]);
        } 
        msg.setFrom(new InternetAddress(userName));
        msg.setRecipients(Message.RecipientType.TO, toIAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(htmlBody, "text/html");

        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // adds inline image attachments
        if (mapInlineImages != null && mapInlineImages.size() > 0) {
            Set<String> setImageID = mapInlineImages.keySet();

            for (String contentId : setImageID) {
                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.setHeader("Content-ID", "<" + contentId + ">");
                imagePart.setDisposition(MimeBodyPart.INLINE);

                String imageFilePath = mapInlineImages.get(contentId);
                try {
                    imagePart.attachFile(imageFilePath);
                } catch (IOException ex) {
                    Logger.getLogger(EmbeddedImageEmailUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
                multipart.addBodyPart(imagePart);
            }
        }

        msg.setContent(multipart);

        Transport.send(msg);
    }
}
