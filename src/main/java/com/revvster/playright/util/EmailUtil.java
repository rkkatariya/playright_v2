/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.util;

import com.revvster.playright.dao.EmailDao;
import com.revvster.playright.dao.EmailDaoImpl;
import com.revvster.playright.dao.SettingDao;
import com.revvster.playright.dao.SettingDaoImpl;
import com.revvster.playright.model.Company;
import com.revvster.playright.model.Email;
import com.revvster.playright.model.Setting;
import com.revvster.playright.model.Settings;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

/**
 *
 * @author Rahul
 */
public class EmailUtil {

    private static final Logger logger = LogManager.getLogger(EmailUtil.class.getName());

    public static void sendEmail(Email email) throws SQLException, MessagingException, AddressException, IOException {
        EmailDao emailDao = new EmailDaoImpl();
        SettingDao settingDao = new SettingDaoImpl();
        try {
            Company c = new Company();
            c.setId(email.getCompany());
            Setting host = settingDao.getSetting(Settings.EmailHost.toString(), c);
            Setting port = settingDao.getSetting(Settings.EmailPort.toString(), c);
            Setting username = settingDao.getSetting(Settings.EmailUserName.toString(), c);
            Setting password = settingDao.getSetting(Settings.EmailPassword.toString(), c);
            Setting tlsEnable = settingDao.getSetting(Settings.EmailTLSEnable.toString(), c);
            Integer id = emailDao.createEmailLog(email);

            if (id > 0) {
                EmailUtil.send(
                        host.getValue(),
                        port.getValue(),
                        tlsEnable.getValue(),
                        username.getValue(),
                        password.getValue(),
                        email.getTo(),
                        email.getSubject(),
                        email.getBody(),
                        new HashMap<>(),
                        null
                );

                emailDao.updateEmailLogStatus(id, "Sent");

            } else {
                logger.error("There was an error creating email log");
            }
        } finally {
            emailDao.close();
            settingDao.close();
        }
    }

    public static void send(String host, String port, String enableTLS,
            final String userName, final String password, String toAddress,
            String subject, String htmlBody,
            Map<String, String> mapInlineImages,
            List<Document> documents)
            throws AddressException, MessagingException, SQLException, IOException {
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        if (enableTLS != null && "true".equals(enableTLS)) {
            properties.put("mail.smtp.starttls.enable", enableTLS);
        }
        properties.put("mail.user", userName);
        properties.put("mail.password", password);

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);
        String[] toAddresses = toAddress.split(",", 10);
        InternetAddress[] toIAddresses = new InternetAddress[toAddresses.length];
        for (int i = 0; i < toAddresses.length; i++) {
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
                    logger.error("There was an error attaching file to email", ex);
                }
                multipart.addBodyPart(imagePart);
            }
        }

        // add attachments from database blob
//        if (documents != null && documents.size() > 0) {
//            for (Document doc : documents) {
//                //Getting ByteArray From BLOB
//                byte[] bytearray;
//                Blob blob = doc.getDocument();
//                if (blob != null) {
//                    //Attach File for mail
//                    MimeBodyPart attachment = new MimeBodyPart();
//                    ByteArrayDataSource bads = new ByteArrayDataSource(blob.getBinaryStream(), doc.getType());
//                    attachment.setDataHandler(new DataHandler(bads));
//                    attachment.setFileName(doc.getFileName());
//                    multipart.addBodyPart(attachment);
//                }
//            }
//        }

        msg.setContent(multipart);

        Transport.send(msg);
    }

}
