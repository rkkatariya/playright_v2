/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revvster.playright.dao.UserDao;
import com.revvster.playright.dao.UserDaoImpl;
import com.revvster.playright.access.Action;
import com.revvster.playright.model.ContextObj;
import com.revvster.playright.access.Resource;
import com.revvster.playright.model.RoleEntContext;
import com.revvster.playright.model.User;
import com.revvster.playright.model.UserEntitlement;
import com.revvster.playright.access.AuthorizationManager;
import com.revvster.playright.dao.DashboardDao;
import com.revvster.playright.dao.DashboardDaoImpl;
import com.revvster.playright.dao.EmailDao;
import com.revvster.playright.dao.EmailDaoImpl;
import com.revvster.playright.dao.SettingDao;
import com.revvster.playright.dao.SettingDaoImpl;
import com.revvster.playright.model.Company;
import com.revvster.playright.model.Data;
import com.revvster.playright.model.Email;
import com.revvster.playright.model.Project;
import com.revvster.playright.model.Setting;
import com.revvster.playright.model.Settings;
import com.revvster.playright.util.AuthenticationManager;
import com.revvster.playright.util.EmailUtil;
import com.revvster.playright.util.SystemConstants;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rahul
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/user/*", "/user", "/pages/user/*", "/pages/user"})
public class UserServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(UserServlet.class.getName());
    private User loggedInUser;

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
        loggedInUser = (User) request.getSession().getAttribute(SystemConstants.LoggedInUser);
        String action = request.getParameter("action");
        logger.info("processRequest::action::" + action);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonArray = new String();
        if (action != null) {
            UserDao userDao = new UserDaoImpl();
            DashboardDao dashboardDao = new DashboardDaoImpl();
            response.setContentType("application/json");
            List<User> users = new ArrayList<>();
            User user;
            Integer compId = 0;
            Integer projectId = 0;
            Integer selectedCust = 0;
            Integer userId = 0;
            Integer selectedNotification = 0;
            List<Project> projects = new ArrayList<>();
            String username;
            String password;
            Integer id = 0;
            try {
                switch (action) {
                    case "listUsersByContext":
                        RoleEntContext listCtx = AuthorizationManager.getRoleEntContext(
                                loggedInUser.getUserEntitlements(), new UserEntitlement(Resource.user, Action.list));
                        if (RoleEntContext.ALL.equals(listCtx) || RoleEntContext.SELF.equals(listCtx)) {
                            users = userDao.listUsersByContext(loggedInUser, listCtx);
                        } else {
                            HashMap<RoleEntContext, List<ContextObj>> ctxScopes = loggedInUser.getContextScope();
                            if (ctxScopes != null && ctxScopes.size() > 0) {
                                List<ContextObj> contextScope = ctxScopes.get(listCtx);
                                if (contextScope != null && contextScope.size() > 0) {
                                    users = userDao.listUsersByContext(loggedInUser, listCtx);
                                }
                            }
                        }
                        if (users != null && users.size() > 0) {
                            jsonArray = gson.toJson(users);
                            jsonArray = "{\n\"data\":\n" + jsonArray + "\n}";
                        } else {
                            users = userDao.listUsersByContext(loggedInUser, RoleEntContext.SELF);
//                            response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
//                            jsonArray = gson.toJson(getErrorJson("You have either not been assigned to a project or you do not have the right permissions"));                            
                            jsonArray = gson.toJson(users);
                            jsonArray = "{\n\"data\":\n" + jsonArray + "\n}";
                        }
                        break;
                    case "userProfile":
                        user = userDao.getUserByContext(loggedInUser,
                                new UserEntitlement(Resource.user, Action.view),
                                Integer.valueOf(request.getParameter("id")));
                        if (user != null) {
                            jsonArray = gson.toJson(user);
                        } else {
//                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            jsonArray = gson.toJson(getErrorJson("You are not Authorized to view this user profile"));
                        }
                        break;
                    case "updateUser":
                        user = getUserFromReq(request);
                        User userInCtx = userDao.getUserByContext(loggedInUser,
                                new UserEntitlement(Resource.user, Action.update),
                                user.getId());
                        if (userInCtx != null) {
                            userDao.updateUser(user);
                            jsonArray = gson.toJson(getSuccessJson("User Updated !!"));
                        } else {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            jsonArray = gson.toJson(getErrorJson("You are not Authorized to edit this user"));
                        }
                        break;
                    case "createUser":
                        String pasword = AuthenticationManager.generatePassword();
                        user = getUserFromReq(request);
                        user.setPassword(pasword);
                        if (AuthorizationManager.canAddUser(loggedInUser, user)) {
                            user.setCreatedBy(loggedInUser.getId());
                            id = userDao.createUser(user);
                            user = userDao.getUser(id);
                            if (user != null) {
                                String to = user.getEmailAddress();
                                String from = "revvster@gmail.com";
                                String subject = "Welcome to PlayRight!";
                                String body = "<p>Dear " + user.getName() + ",</p>\n"
                                        + "<p>Welcome to PlayRight!<br /> Your account has been set up and your login details are below.</p>\n"
                                        + "<p>Username: <strong><a href=\"mailto:" + user.getUsername() + "\">" + user.getUsername() + "</a></strong><br /> Password: <strong>" + pasword + "</strong></p>\n"
                                        + "<p>You can use these credentials to login to the PlayRight application. <br />You can also login to your dashboard with the same credentials by clicking on the link below:</p>\n"
                                        + "<p><a href=\"http://www.playrightanalytics.com\">http://www.playrightanalytics.com</a></p>\n"
                                        + "<p>For any further queries, please feel free to email us.</p>\n"
                                        + "<p>Happy Selling!!</p>\n"
                                        + "<p>Team Revvster</p>\n"
                                        + "<p><strong></p>\n"
                                        + "<p>&nbsp;</p>\n"
                                        + "<p>Registered company:</p>\n"
                                        + "<p>ReVVster Technologies Pvt. Ltd<br /> Web:&nbsp;<a href=\"http://www.revvster.in/\">www.revvster.in</a></p>\n"
                                        + "<p>&nbsp;</p>";

                                EmailDao emailDao = new EmailDaoImpl();
                                SettingDao settingDao = new SettingDaoImpl();
                                //  Integer id = 0;
                                try {
                                    Email email = new Email();
                                    email.setCompany(user.getCompany());
                                    email.setFrom(from);
                                    email.setCreatedBy(user.getId());
                                    email.setTo(to);
                                    email.setSubject(subject);
                                    email.setBody(body);
                                    email.setRequestedBy(user.getId());
                                    email.setLastUpdatedBy(user.getId());
                                    email.setSource("New User");
                                    Company c = new Company();
                                    c.setId(user.getCompany());
                                    Setting host = settingDao.getSetting(Settings.EmailHost.toString(), c);
                                    Setting port = settingDao.getSetting(Settings.EmailPort.toString(), c);
                                    Setting usrname = settingDao.getSetting(Settings.EmailUserName.toString(), c);
                                    Setting pwd = settingDao.getSetting(Settings.EmailPassword.toString(), c);
                                    Setting tlsEnable = settingDao.getSetting(Settings.EmailTLSEnable.toString(), c);

                                    id = emailDao.createEmailLog(email);

                                    if (id > 0) {
                                        EmailUtil.send(
                                                host.getValue(),
                                                port.getValue(),
                                                tlsEnable.getValue(),
                                                usrname.getValue(),
                                                pwd.getValue(),
                                                email.getTo(),
                                                email.getSubject(),
                                                email.getBody(),
                                                new HashMap<String, String>(),
                                                null
                                        );
                                        emailDao.updateEmailLogStatus(id, "Sent");
                                        // userDao.updateUserPassword(user.getUsername(), pasword, 1);
                                        jsonArray = gson.toJson(getSuccessJson("Password has been reset !!"));
                                    } else {
                                        logger.error("There was an error logging Email Request");
                                        jsonArray = gson.toJson(getErrorJson("There was an error logging Email Request"));
                                    }
                                } catch (MessagingException | IOException ex) {
                                    logger.error("There was an error sending Email", ex);
                                    try {
                                        emailDao.updateEmailLogStatus(id, "Failed");
                                    } catch (SQLException e) {
                                        logger.error("There was an error updating email log status", e);
                                    }
                                    jsonArray = gson.toJson(getErrorJson("There was an error sending email !!"));
                                } catch (SQLException ex) {
                                    logger.error("There was an error reading Email Request", ex);
                                    jsonArray = gson.toJson(getErrorJson("There was an error sending email !!"));
                                } finally {
                                    emailDao.close();
                                    settingDao.close();
                                }
                            } else {
                                jsonArray = gson.toJson(getErrorJson("Email Address you have entered does not exist !!"));
                            }
                            if (id == null || id == 0) {
                                throw new RuntimeException("There was an error creating user " + user.getUsername());
                            }

                        } else {
                            jsonArray = gson.toJson(getErrorJson("You are not Authorized to create user"));
                        }
                        break;
                    case "disableUser":
                        user = getUsrFromReq(request);
                        userDao.deleteUser(user);
                        jsonArray = gson.toJson("User Updated successfully");
                        break;
                    case "getManagersStartingWith":
                        String startingWith = request.getParameter("q");
                        if (null == request.getParameter("inputCompany")) {
                            compId = (null == loggedInUser.getCompany() ? 0 : loggedInUser.getCompany());
                        } else {
                            compId = Integer.valueOf(request.getParameter("inputCompany"));
                        }
                        List<User> mgrs = userDao.getManagersStartingWith(startingWith, compId);
                        jsonArray = "{\n\"items\":\n" + gson.toJson(mgrs) + "\n}";
                        break;
                    case "validateUserName":
                        username = request.getParameter("inputUserName");
                        user = userDao.getUser(username);
                        if (user == null) {
                            jsonArray = "true";
                        } else {
                            jsonArray = "false";
                        }
                        break;
                    case "validateUserPassword":
                        password = request.getParameter("inputPassword");
                        if (userDao.validateUserPassword(loggedInUser.getUsername(), password)) {
                            jsonArray = "true";
                        } else {
                            jsonArray = "false";
                        }
                        break;
                    case "validateMobileNo":
                        String mobileNo = request.getParameter("inputMobileNo");
                        if (request.getParameter("inputId") != null) {
                            userId = Integer.parseInt(request.getParameter("inputId"));
                        }
                        user = userDao.getUserFromMobileNo(mobileNo);
                        if (user == null) {
                            jsonArray = "true";
                        } else if (null != userId && user.getId().intValue() == userId.intValue()) {
                            jsonArray = "true";
                        } else {
                            jsonArray = "false";
                        }
                        break;
                    case "listUsersAssignedToProject":
                        projectId = Integer.valueOf(request.getParameter("selectedProject"));
                        if (projectId != null && !"".equals(projectId) && !"0".equals(projectId)) {
                            users = userDao.listUsersAssignedToProject(projectId);
                        }
                        jsonArray = gson.toJson(users);
                        jsonArray = "{\n\"data\":\n" + jsonArray + "\n}";
                        break;
                    case "getUsersToAssignToProject":
                        projectId = Integer.valueOf(request.getParameter("selectedProject"));
                        users = userDao.getAssignableUsersForProject(projectId);
                        jsonArray = "{\n\"items\":\n" + gson.toJson(users) + "\n}";
                        break;
                    case "listUsersForComapny":
                        compId = Integer.valueOf(request.getParameter("selectedCompany"));
                        users = userDao.getUsersOfCompany(compId);
                        jsonArray = "{\n\"items\":\n" + gson.toJson(users) + "\n}";
                        break;
                    case "getSalesRepsForProject":
                        projectId = Integer.valueOf(request.getParameter("selectedProject"));
                        users = userDao.getUsersForRoleByProject("Sales Representative", projectId);
                        jsonArray = "{\n\"items\":\n" + gson.toJson(users) + "\n}";
                        break;
                    case "changePwd":
                        if (request.getParameter("inputNewPassword") != null
                                && userDao.updateUserPassword(loggedInUser.getUsername(), request.getParameter("inputNewPassword"), 0)) {
                            jsonArray = gson.toJson(getSuccessJson("Password updated !!"));
                        } else {
                            jsonArray = gson.toJson(getErrorJson("There was an error changing the password"));
                        }
                        break;
                    case "forgotPwd":
                        String to = request.getParameter("inputUserName");
                        user = userDao.getUser(to);
                        if (user != null) {
                            String pass = AuthenticationManager.generatePassword();
                            String from = "someemail@test.com";
                            String subject = "Password has been reset";
                            String body = "<p>Dear " + user.getName() + ",</p>\n"
                                    + "<p style=\"text-align: left;\">Use the below password to login and reset your password.<br /> Temporary Password: <em><strong>" + pass + "</strong></em></p>\n"
                                    + "<p>Thanks,</p>\n"
                                    + "<p>Team Revvster</p>";

                            userDao.updateUserPassword(to, pass, 1);

                            EmailDao emailDao = new EmailDaoImpl();
                            SettingDao settingDao = new SettingDaoImpl();
                            try {
                                Email email = new Email();
                                email.setCompany(user.getCompany());
                                email.setFrom(from);
                                email.setCreatedBy(user.getId());
                                email.setTo(user.getEmailAddress());
                                email.setSubject(subject);
                                email.setBody(body);
                                email.setRequestedBy(user.getId());
                                email.setLastUpdatedBy(user.getId());
                                email.setSource("Reset Password");
                                Company c = new Company();
                                c.setId(user.getCompany());
                                Setting host = settingDao.getSetting(Settings.EmailHost.toString(), c);
                                Setting port = settingDao.getSetting(Settings.EmailPort.toString(), c);
                                Setting usrname = settingDao.getSetting(Settings.EmailUserName.toString(), c);
                                Setting pwd = settingDao.getSetting(Settings.EmailPassword.toString(), c);
                                Setting tlsEnable = settingDao.getSetting(Settings.EmailTLSEnable.toString(), c);

                                id = emailDao.createEmailLog(email);

                                if (id > 0) {
                                    EmailUtil.send(
                                            host.getValue(),
                                            port.getValue(),
                                            tlsEnable.getValue(),
                                            usrname.getValue(),
                                            pwd.getValue(),
                                            email.getTo(),
                                            email.getSubject(),
                                            email.getBody(),
                                            new HashMap<String, String>(),
                                            null
                                    );
                                    emailDao.updateEmailLogStatus(id, "Sent");

                                    jsonArray = gson.toJson(getSuccessJson("Password has been reset !!"));
                                } else {
                                    logger.error("There was an error logging Email Request");
                                    jsonArray = gson.toJson(getErrorJson("There was an error logging Email Request"));
                                }
                            } catch (MessagingException | IOException ex) {
                                logger.error("There was an error sending Email", ex);
                                try {
                                    emailDao.updateEmailLogStatus(id, "Failed");
                                } catch (SQLException e) {
                                    logger.error("There was an error updating email log status", e);
                                }
                                jsonArray = gson.toJson(getErrorJson("There was an error sending email !!"));
                            } catch (SQLException ex) {
                                logger.error("There was an error reading Email Request", ex);
                                jsonArray = gson.toJson(getErrorJson("There was an error sending email !!"));
                            } finally {
                                emailDao.close();
                                settingDao.close();
                            }
                        } else {
                            jsonArray = gson.toJson(getErrorJson("Email Address you have entered does not exist !!"));
                        }
                        break;
                    case "sendChartEmail":
                        String body = null;
                        to = request.getParameter("inputEmailAddress");
                        String content = request.getParameter("inputContent");
                        if (null != to && to.length() > 0) {
                            String from = "playrightreports@gmail.com";
                            String subject = request.getParameter("inputSubject");
                            String chartsLink = formURL(request).concat("/pages/emailAnalytics.jsp");
                            body = getHTMLBody(chartsLink, content);
                            EmailDao emailDao = new EmailDaoImpl();
                            SettingDao settingDao = new SettingDaoImpl();
                            try {
                                Email email = new Email();
                                email.setCompany(loggedInUser.getCompany() == null ? 0 : loggedInUser.getCompany());
                                email.setFrom(from);
                                email.setCreatedBy(loggedInUser.getId());
                                email.setTo(to);
                                email.setSubject(subject);
                                email.setBody(body);
                                email.setRequestedBy(loggedInUser.getId());
                                email.setLastUpdatedBy(loggedInUser.getId());
                                email.setSource("Send Email");
                                // email.setCc(content);
                                MimeBodyPart messageBodyPart = new MimeBodyPart();
                                messageBodyPart.setContent(body, "text/html");
                                Company c = new Company();
                                c.setId(loggedInUser.getCompany());
                                Setting host = settingDao.getSetting(Settings.EmailHost.toString(), c);
                                Setting port = settingDao.getSetting(Settings.EmailPort.toString(), c);
                                Setting usrname = settingDao.getSetting(Settings.EmailUserName.toString(), c);
                                Setting pwd = settingDao.getSetting(Settings.EmailPassword.toString(), c);
                                Setting tlsEnable = settingDao.getSetting(Settings.EmailTLSEnable.toString(), c);

                                id = emailDao.createEmailLog(email);

                                if (id > 0) {
                                    EmailUtil.send(
                                            host.getValue(),
                                            port.getValue(),
                                            tlsEnable.getValue(),
                                            usrname.getValue(),
                                            pwd.getValue(),
                                            email.getTo(),
                                            email.getSubject(),
                                            email.getBody(),
                                            null,
                                            null
                                    );
                                    emailDao.updateEmailLogStatus(id, "Sent");
                                    // userDao.updateUserPassword(user.getUsername(), pasword, 1);
                                    jsonArray = gson.toJson(getSuccessJson("Email has been sent successfully !!"));

                                } else {
                                    logger.error("There was an error logging Email Request");
                                    jsonArray = gson.toJson(getErrorJson("There was an error logging Email Request"));
                                }
                            } catch (MessagingException | IOException ex) {
                                logger.error("There was an error sending Email", ex);
                                try {
                                    emailDao.updateEmailLogStatus(id, "Failed");
                                } catch (SQLException e) {
                                    logger.error("There was an error updating email log status", e);
                                }
                                jsonArray = gson.toJson(getErrorJson("There was an error sending email !!"));
                            } catch (SQLException ex) {
                                logger.error("There was an error reading Email Request", ex);
                                jsonArray = gson.toJson(getErrorJson("There was an error sending email !!"));
                            } finally {
                                emailDao.close();
                                settingDao.close();
                                //conn.close();
                            }
                        }
                        break;

                }
            } catch (NumberFormatException | SQLException | NoSuchAlgorithmException | UnsupportedEncodingException ex) {
                logger.error(ex);
                jsonArray = gson.toJson(getErrorJson(ex.toString()));
            } finally {
                userDao.close();
                dashboardDao.close();
                logger.debug("processRequest::END");
                response.getWriter().write(jsonArray);
            }
        }
    }

    private User getUsrFromReq(HttpServletRequest request) {
        User user = new User();
        if (request.getParameter("inputId") != null) {
            user.setId(Integer.valueOf(request.getParameter("inputId")));
        }
        if (Integer.valueOf(request.getParameter("inputActive")) == 1) {
            user.setActive(0);
        } else {
            user.setActive(1);
        }
        return user;
    }

    private String formURL(HttpServletRequest request) {
        return request.getScheme().concat("://").concat(request.getServerName()).concat(":").concat(Integer.toString(request.getServerPort())).concat(request.getContextPath());
    }

    private String getHTMLBody(String link, String content) {
        StringBuilder sb = new StringBuilder("<html><body> \n");
        sb.append("<div>" + content.replaceAll("\n", "<br/>") + "</div>");
        sb.append("<p> Click <a href=\"" + link + "\">here</a> to view the charts.</p>");
        sb.append("<div><br/></div>");
        sb.append("<div><br/></div>");
        sb.append("<p><strong>Disclaimer:</strong>All features,functionalities,graphs,analytics etc might not work on ios/apple devices.");
        sb.append("</body></html>");
        return sb.toString();
    }

    private User getUserFromReq(HttpServletRequest request) {
        User user = new User();
        if (request.getParameter("inputId") != null) {
            user.setId(Integer.valueOf(request.getParameter("inputId")));
        }
        user.setUsername(request.getParameter("inputUserName"));
        user.setEmailAddress(request.getParameter("inputUserName"));
        user.setFirstName(request.getParameter("inputFirstName"));
        user.setLastName(request.getParameter("inputLastName"));
        user.setJobTitle(request.getParameter("inputJobTitle"));
        user.setDepartment(request.getParameter("inputDepartment"));
        user.setGender(request.getParameter("inputGender"));
        user.setMobileNo(request.getParameter("inputMobileNo"));
        user.setAltPhoneNo(request.getParameter("inputAltNo"));
        // user.setPassword(password);
        if (null != request.getParameter("inputManager")
                && !"".equals(request.getParameter("inputManager"))) {
            user.setManager(Integer.valueOf(request.getParameter("inputManager")));
        }
        user.setRole(Integer.valueOf(request.getParameter("inputRole")));
        if (null != request.getParameter("inputCompany")) {
            user.setCompany(Integer.valueOf(request.getParameter("inputCompany")));
        } else {
            user.setCompany(loggedInUser.getCompany());
        }
//        if (null != request.getParameter("inputCompany")) {
//            user.setCompany(Integer.valueOf(request.getParameter("inputCompany")));
//        } else {
//            user.setCompany(loggedInUser.getCompany());
//        }
        user.setLastUpdatedBy(loggedInUser.getId());
        logger.debug("processRequest::userFromReq::" + user);
        return user;
    }

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

    private String getErrorJson(String msg) {
        return "{\"result\":\"error\",\"errorMsg\":\"" + msg + "\"}";
    }

    private String getSuccessJson(String msg) {
        return "{\"result\":\"success\",\"successMsg\":\"" + msg + "\"}";
    }

}
