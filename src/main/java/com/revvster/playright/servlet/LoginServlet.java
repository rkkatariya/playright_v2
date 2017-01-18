/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.servlet;

import com.revvster.playright.dao.SettingDao;
import com.revvster.playright.dao.SettingDaoImpl;
import com.revvster.playright.dao.UserDao;
import com.revvster.playright.dao.UserDaoImpl;
import com.revvster.playright.model.Settings;
import com.revvster.playright.model.User;
import com.revvster.playright.access.AuthorizationManager;
import com.revvster.playright.util.SystemConstants;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rahul
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login", "/pages/login"})
public class LoginServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(LoginServlet.class.getName());
    private static final String authenticationFailed = "User name and/or password entered is incorrect.";
    private static final String authorizationFailed = "Cannot login because user name is not associated to company. Contact Admin.";

    @Override
    public void init() throws ServletException {
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("processRequest::START");
        response.setContentType("text/html;charset=UTF-8");
        //get request parameters for userID and password
        String username = request.getParameter("loginEmail");
        String pwd = request.getParameter("loginPassword");
        //logging example
        logger.info("processRequest::User::" + username);
        logger.debug("processRequest::password::" + pwd);

        UserDao userDao = new UserDaoImpl();
        SettingDao settingDao = new SettingDaoImpl();
        try {
            boolean authenticate = userDao.login(username, pwd);
            boolean authorize = true;
            if (authenticate) {

                logger.info("processRequest::authenticated::" + username + "::true");

                if (userDao.getUserRoles(username).isEmpty()) {
                    throw new RuntimeException("User not assigned to any role");
                }

                User user = userDao.getUserForSession(username);

                //Login only if user user is assigned to company and has a role assgined 
                //or has login_without_company::allow entitlement
                authorize = AuthorizationManager.canUserLogin(user);
                if (authorize) {
                    logger.info("processRequest::login::" + username + "::successful");

                    userDao.updateLastLogin(user.getId());

                    HttpSession session = request.getSession(true);
                    session.setAttribute(SystemConstants.LoggedInUser, user);
                    HashMap<Settings, String> settings = settingDao.getSystemSettings();
                    session.setAttribute(SystemConstants.SystemSettings, settings);
                    //setting session to expiry in 30 mins
                    int maxInactiveIntMin = Integer.valueOf(settings.get(Settings.MaxInactiveInterval));
                    session.setMaxInactiveInterval(maxInactiveIntMin * 60);

                    //setting cookie to expiry in 30 mins
//            Cookie loginCookie = new Cookie(SystemConstants.LoggedInUserName,user);
//            loginCookie.setMaxAge(30*60);
//            response.addCookie(loginCookie);
                    if (user.getResetPassword().equals("1")) {
                        response.sendRedirect("pages/resetPassword.jsp?id=" + user.getId());
                    } else {
                        response.sendRedirect("pages/data.jsp?id=" + user.getId());
                    } 
                }
            }
            if (!authorize || !authenticate) {
                //Set this up correctly to show on login page
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
                PrintWriter out = response.getWriter();
                if (!authorize) {
//                    out.println("<font color=red>" + authorizationFailed + "</font>");
                    request.setAttribute("errorMsg", authorizationFailed);
                    logger.info("processRequest::login::" + username + "::failed::AuthorizationFailed");
                } else {
//                    out.println("<font color=red>" + authenticationFailed + "</font>");
                    logger.info("processRequest::login::" + username + "::failed::AuthenticationFailed");
                    request.setAttribute("errorMsg", authenticationFailed);
                }
                rd.include(request, response);
            }
        } catch (SQLException | NoSuchAlgorithmException ex) {
            logger.error(ex);
        } finally {
            userDao.close();
            settingDao.close();
            logger.debug("processRequest::END");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
