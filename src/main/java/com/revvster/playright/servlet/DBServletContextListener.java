/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.servlet;

import java.util.Enumeration;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author rahulkk
 */
@WebListener
public class DBServletContextListener implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger(DBServletContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Servlet Context Initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            com.mysql.jdbc.AbandonedConnectionCleanupThread.shutdown();
        } catch (Throwable t) {
            logger.warn("Error shutting down adandoned connections", t);
        }
        // This manually deregisters JDBC driver, which prevents Tomcat from complaining about memory leaks
        Enumeration<java.sql.Driver> drivers = java.sql.DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            java.sql.Driver driver = drivers.nextElement();
            try {
                java.sql.DriverManager.deregisterDriver(driver);
            } catch (Throwable t) {
                logger.warn("Error deregistering driver", t);
            }
        }
        try {
            Thread.sleep(2000L);
        } catch (Exception e) {
            logger.warn("Error !!", e);
        }
        logger.info("Servlet Context Destroyed");
    }

}
