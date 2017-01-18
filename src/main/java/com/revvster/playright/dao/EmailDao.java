/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.dao;

import com.revvster.playright.model.Email;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Rahul
 */
public interface EmailDao {
    
    public void close();
    public Integer createEmailLog(Email email) throws SQLException;
    public void updateEmailLogStatus(Integer email, String status) throws SQLException;
    public List<Email> listMailInfoForCustomer(Integer to_id)  throws SQLException;
     
}
