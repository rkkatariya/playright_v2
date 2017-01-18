/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.dao;

import com.revvster.playright.model.Email;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rahul
 */
public class EmailDaoImpl extends DaoHelper implements EmailDao {

    private static final Logger logger = LogManager.getLogger(EmailDaoImpl.class.getName());

    public EmailDaoImpl() {
        super();
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public Integer createEmailLog(Email email)
            throws SQLException {
        logger.debug("createEmailLog::START::Status::" + email.getStatus());
        PreparedStatement ps = conn.prepareStatement("INSERT INTO sb_email_log \n"
                + "(company, project, location, \n"
                + "requested_by, from_id, to_id, \n"
                + "source, status, `from`, \n"
                + "`to`, cc, bcc, \n"
                + "template, subject, body, \n"
                + "attachments, created_by, last_updated_by) \n"
                + "VALUES (?, ?, ?, \n"
                + "?, ?, ?, \n"
                + "?, ?, ?, \n"
                + "?, ?, ?, \n"
                + "?, ?, ?, \n"
                + "?, ?, ?) ");
        ps.setInt(1, email.getCompany());
        if (email.getProject() == null || email.getProject() == 0) {
            ps.setNull(2, Types.INTEGER);
        } else {
            ps.setInt(2, email.getProject());
        }
        ps.setNull(3, Types.INTEGER);
        ps.setInt(4, email.getRequestedBy());
        if (email.getFromId() == null || email.getFromId() == 0) {
            ps.setNull(5, Types.INTEGER);
        } else {
            ps.setInt(5, email.getFromId());
        }
        if (email.getToId() == null || email.getToId() == 0) {
            ps.setNull(6, Types.INTEGER);
        } else {
            ps.setInt(6, email.getToId());
        }
        ps.setString(7, email.getSource());
        ps.setString(8, email.getStatus() == null ? "New" : email.getStatus());
        ps.setString(9, email.getFrom());
        ps.setString(10, email.getTo());
        ps.setString(11, email.getCc() == null ? "" : email.getCc());
        ps.setString(12, email.getBcc() == null ? "" : email.getBcc());
        if (email.getTemplate() != null) {
            ps.setInt(13, email.getTemplate());
        } else {
            ps.setNull(13, Types.INTEGER);
        }
        ps.setString(14, email.getSubject());
        ps.setString(15, email.getBody());
        ps.setInt(16, (email.getAttachments() != null
                && email.getAttachments().size() > 0) ? 1 : 0);
        ps.setInt(17, email.getCreatedBy());
        ps.setInt(18, email.getLastUpdatedBy());
        ps.executeUpdate();
        Integer id = getLastInsertId();
        if (id > 0 && email.getAttachments() != null
                && email.getAttachments().size() > 0) {
            addAttachmentLogs(id, email.getAttachments());
        }
        close(ps);
        logger.debug("createEmailLog::END");
        return id;

    }

    @Override
    public void updateEmailLogStatus(Integer email, String status)
            throws SQLException {
        logger.debug("updateEmailLogStatus::START::LogId::" + email);
        PreparedStatement ps = conn.prepareStatement("update sb_email_log\n"
                + "set status = ?,\n"
                + "last_updated_on = now()\n"
                + "where id = ?");
        ps.setString(1, status);
        ps.setInt(2, email);
        ps.executeUpdate();
        close(ps);
        logger.debug("updateEmailLogStatus::End");
    }

    @Override
    public List<Email> listMailInfoForCustomer(Integer customer)
            throws SQLException {
        logger.debug("listMailInfoForCustomer::START::" + customer);
        ArrayList<Email> emails = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement("select el.*\n"
                + "from sb_email_log el \n"
                + "where el.status = 'sent'\n"
                + "and el.to_id =?");
        ps.setInt(1, customer);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Email email = getEmailObj(rs);
            emails.add(email);
        }
        close(rs);
        close(ps);
        logger.debug("listMailInfoForCustomer::END");
        return emails;
    }

    private void addAttachmentLog(Integer logId, Integer attachment)
            throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO sb_email_log_attachment \n"
                + "(email_log_id, document_id) \n"
                + "VALUES (?, ?)");
        ps.setInt(1, logId);
        ps.setInt(2, attachment);
        ps.executeUpdate();
        close(ps);
    }

    private void addAttachmentLogs(Integer id, List<Integer> attachments)
            throws SQLException {
        for (Integer attachment : attachments) {
            addAttachmentLog(id, attachment);
        }
    }

}
