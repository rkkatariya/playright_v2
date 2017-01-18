/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.util;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rahul
 */
public class DBConnectionManager {
    
    private static final Logger logger = LogManager.getLogger(DBConnectionManager.class.getName());
    
    private static Connection connection = null;   
        
    public static Connection getConnection() {
            try {
                Context initContext = new InitialContext();
                //this is needed for tomcat
                Context envContext = (Context) initContext.lookup("java:comp/env");
                DataSource ds = (DataSource) envContext.lookup("playrightDS");
                
                //this is for glass fish
                //DataSource ds = (DataSource) initContext.lookup("jdbc/playRightAnalyticsDS");

                connection = ds.getConnection();
            } catch (NamingException | SQLException ex) {
                logger.fatal("getConnection::Unable to get database connection");
                logger.fatal(ex);
            }
        return connection;
    }
}
