/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revvster.playright.dao.DashboardDao;
import com.revvster.playright.dao.DashboardDaoImpl;
import com.revvster.playright.dao.EmailDao;
import com.revvster.playright.dao.EmailDaoImpl;
import com.revvster.playright.dao.SettingDao;
import com.revvster.playright.dao.SettingDaoImpl;
import com.revvster.playright.model.Company;
import com.revvster.playright.model.Email;
import com.revvster.playright.model.Setting;
import com.revvster.playright.model.Settings;
import com.revvster.playright.model.User;
import com.revvster.playright.util.DBConnectionManager;
import com.revvster.playright.util.EmailUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

/**
 *
 * @author REVV-03
 */
@WebServlet(name = "EmailServlet", urlPatterns = {"/EmailServlet"})
public class EmailServlet extends HttpServlet {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(EmailServlet.class.getName());

    private User loggedInUser;

    protected Connection conn;

    public EmailServlet() {
        conn = DBConnectionManager.getConnection();
        logger.debug("getConnection::" + conn.toString());
    }
    //   private static final Logger logger = LogManager.getLogger(EmailServlet.class.getName());   

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
            throws ServletException, IOException, ParseException, SQLException {
        logger.debug("processRequest::START");
        String action = request.getParameter("action");
        logger.info("processRequest::action::" + action);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonArray = new String();
        if (action != null) {
            DashboardDao dashboardDao = new DashboardDaoImpl();
            EmailDao emailDao = new EmailDaoImpl();
            SettingDao settingDao = new SettingDaoImpl();            
            String to = "";
            String from = "";
            String subject = "";           
            // String body = "";
            Integer id = 0;            
            try {
                switch (action) {
                    case "sendEmail":
                        to = request.getParameter("inputEmailAddress");
                        from = "revvster@gmail.com";
                        subject = request.getParameter("inputSubject");
                        String body = null;
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
                        Date fromDate = dateFormatter.parse(request.getParameter("inputFromDate"));
                        Date toDate = dateFormatter.parse(request.getParameter("inputToDate"));
                        Timestamp inputFrom = new Timestamp(fromDate.getTime());
                        Timestamp inputTo = new Timestamp(toDate.getTime());
                        // String[] inputEdition = {};
                        String inputEdition = "";
                        // String[] inputLanguage = {};
                        String inputLanguage = "";
                        // String[] inputSource = {};
                        String inputSource = "";
                        //  String[] inputNewsPaper = {};
                        String inputNewsPaper = "";
                        if (request.getParameter("inputEdition") != null) {
                            inputEdition = request.getParameter("inputEdition");
                        }
                        if (request.getParameter("inputLanguage") != null) {
                            inputLanguage = request.getParameter("inputLanguage");
                        }
                        if (request.getParameter("inputSource") != null) {
                            inputSource = request.getParameter("inputSource");
                        }
                        if (request.getParameter("inputNewsPaper") != null) {
                            inputNewsPaper = request.getParameter("inputNewsPaper");
                        }
                        Statement stmt = null;
                        ResultSet res = null;
                        String selectDate;
                        selectDate = "select ad.* from analytics_data ad \n"
                                + "where ad.news_date between '" + inputFrom + "' and '" + inputTo + "' \n";
                        String language = inputLanguage != ""
                                ? "and ad.language = '" + inputLanguage + "'\n" : "";
                        String edition = inputEdition != ""
                                ? "and ad.edition = '" + inputEdition + "'\n" : "";
                        String source = inputSource != ""
                                ? "and ad.source = '" + inputSource + "' \n" : "";
                        String newsPaper = inputNewsPaper != ""
                                ? "and ad.news_paper = '" + inputNewsPaper + "'\n" : "";
                        String query = (selectDate + language + edition
                                + source + newsPaper);
                        stmt = conn.createStatement();
                        res = stmt.executeQuery(query);
                        body = "<div style=\"float: left; width: 15%; margin: 0 auto; padding: 20px 0 0 0\">";
                        body += "<img width=\"114\" src=\"cid:img_0_0_1\" alt=\"PlayRight Analytics\" title=\"PlayRight Analytics\">";
                        body += "</div>";
                        body += "<h2 style='color: #2e6c80;' align='center'><strong>PlayRight Media Analytics</strong></h2>";
                        body += "<h3 style='color: #2e6c80;' align='center'>" + request.getParameter("inputFromDate") + "   -   " + request.getParameter("inputToDate") + "</h2>";
                        body += "<table style=\"border-top: 5px solid white; height: 225px;\" border=\"1\" width=\"783\" cellspacing=\"0\" cellpadding=\"2\" align=\"center\">";
                        body += "<thead>";
                        body += "<tr>\n";
                        body += "<td style=\"background-color:    #993366;\"> </td>\n";
                        body += "<td style=\"background-color:    #993366;\"> </td>\n";
                        body += "<td style=\"background-color:    #993366;\"> </td>\n";
                        body += "<td style=\"background-color:    #993366;\"> </td>\n";
                        body += "<td style=\"background-color:    #993366;\"> </td>\n";
                        body += "<td style=\"background-color:    #993366;\"> </td>\n";
                        body += "<td style=\"background-color:    #993366;\"> </td>\n";
                        body += "<td style=\"background-color:    #993366;\"> </td>\n";
                        body += "</tr>";
                        body += "<tr>";
                        body += "<td style=\"background-color:  #ffffff;\"><strong>Date</strong></td>";
                        body += " <td style=\"background-color:  #ffffff;\"><strong>News Paper</strong></td>";
                        body += "<td style=\"background-color:  #ffffff;\"><strong>Headline</strong></td>";
                        body += "<td style=\"background-color:  #ffffff;\"><strong>Edition</strong></td>";
                        body += "<td style=\"background-color:  #ffffff;\"><strong>Source</strong></td>";
                        body += "<td style=\"background-color:  #ffffff;\"><strong>Language</strong></td>";
                        body += "<td style=\"background-color:  #ffffff;\"><strong>Image</strong></td>";
                        body += "<td style=\"background-color:  #ffffff;\"><strong>Page</strong></td>";
                        body += "</tr>";
                        body += "<tr>\n";
                        body += "<td style=\"background-color:    #999999;\"> </td>\n";
                        body += "<td style=\"background-color:    #999999;\"> </td>\n";
                        body += "<td style=\"background-color:    #999999;\"> </td>\n";
                        body += "<td style=\"background-color:    #999999;\"> </td>\n";
                        body += "<td style=\"background-color:    #999999;\"> </td>\n";
                        body += "<td style=\"background-color:    #999999;\"> </td>\n";
                        body += "<td style=\"background-color:    #999999;\"> </td>\n";
                        body += "<td style=\"background-color:    #999999;\"> </td>\n";
                        body += "</tr>";
                        body += "</thead>";
                        body += "<tbody>";
                        while (res.next()) {
                            if (res.getString("news_paper").endsWith(".com")) {
                                body += "<tr>";
                                body += "<td style=\"background-color:  #ffffff;\">" + res.getTimestamp("news_date") + "</td>";
                                body += " <td style=\"background-color:  #ffffff;\">" + res.getString("news_paper") + "</td>";
                                body += "<td style=\"background-color:  #ffffff;\">" + res.getString("headline") + "</td>";
                                body += "<td style=\"background-color:  #ffffff;\"><a href=" + res.getString("edition") + ">" + res.getString("edition") + "</a></td>";
                                body += "<td style=\"background-color:  #ffffff;\">" + res.getString("source") + "</td>";
                                body += "<td style=\"background-color:  #ffffff;\">" + res.getString("language") + "</td>";
                                body += "<td style=\"background-color:  #ffffff;\">" + res.getString("image_exists") + "</td>";
                                body += "<td style=\"background-color:  #ffffff;\">" + res.getInt("page_no") + "</td>";
                                body += "</td>";
                                body += "</tr>";
                            } else {
                                body += "<tr>";
                                body += "<td style=\"background-color:  #ffffff;\">" + res.getTimestamp("news_date") + "</td>";
                                body += " <td style=\"background-color:  #ffffff;\">" + res.getString("news_paper") + "</td>";
                                body += "<td style=\"background-color:  #ffffff;\"><a href=\"" + formURL(request) + "/pages/image.jsp?action=getPhoto&imageId=" + res.getInt("id") + "\">" + res.getString("headline") + "</a></td>";
                                body += "<td style=\"background-color:  #ffffff;\">" + res.getString("edition") + "</td>";
                                body += "<td style=\"background-color:  #ffffff;\">" + res.getString("source") + "</td>";
                                body += "<td style=\"background-color:  #ffffff;\">" + res.getString("language") + "</td>";
                                body += "<td style=\"background-color:  #ffffff;\">" + res.getString("image_exists") + "</td>";
                                body += "<td style=\"background-color:  #ffffff;\">" + res.getInt("page_no") + "</td>";
                                body += "</td>";
                                body += "</tr>";
                            }
                        }
                        body += "</tbody>";
                        body += "</table>";
                        //   body += "<a href=\"revvster.in\"><img src=\"../images/revvster.png\" width=\"50\" height=\"50\"></a>";
                        // body += "<p>dfgdfhfgh</p>";
                        // body += "<p><img src=\"../images/text_logo_white.png\" width=\"50\" height=\"50\"></img></p>";
                        try {
                            Email email = new Email();
                            email.setCompany(1);
                            email.setFrom(from);
                            email.setCreatedBy(loggedInUser.getId());
                            email.setTo(to);
                            email.setSubject(subject);
                            email.setBody(body);
                            email.setRequestedBy(loggedInUser.getId());
                            email.setLastUpdatedBy(loggedInUser.getId());
                            email.setSource("Send Email");
                            MimeBodyPart messageBodyPart = new MimeBodyPart();
                            messageBodyPart.setContent(body, "text/html");
                            Company c = new Company();
                            c.setId(1);
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
                        }
                        break;
                }

            } catch (NumberFormatException ex) {
                logger.error(ex);
                //       jsonArray = getErrorJson(ex.getMessage());
            } finally {
                //   contextObjDao.close();
                dashboardDao.close();
                emailDao.close();
                settingDao.close();
                logger.debug("processRequest::END");
                response.getWriter().write(jsonArray);
            }
        }
    }

    private String getTodaysDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new Date());
    }

    public void sendHTMLMail(String fromDate, String toDate, String recipientEmailAddress, String emailSubject, String imageLink, Map<String, String> inlineImages) throws Exception {
        if (fromDate == null || fromDate.length() == 0) {
            fromDate = getTodaysDate();
        }

        if (toDate == null || toDate.length() == 0) {
            toDate = getTodaysDate();
        }

        //  String htmlText = formHtmlTemplate(fromDate, toDate, imageLink);
//        String htmlText = getTestEmail();
//        System.out.println(htmlText);
        // message info
        //String subject = "HTML Report for " + getTodaysDate();
        try {
            // EmbeddedImageEmailUtil.send(host, port, mailFrom, password, recipientEmailAddress,
            //        emailSubject, htmlText, inlineImages);
            Logger.getLogger(EmailDao.class.getName()).log(Level.INFO, recipientEmailAddress, "HTML Data Email Sent To: ");
        } catch (Exception ex) {
            Logger.getLogger(EmailDao.class.getName()).log(Level.SEVERE, null, "Could not send Email: ");
            Logger.getLogger(EmailDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String formURL(HttpServletRequest request) {
        return request.getScheme().concat("://").concat(request.getServerName()).concat(":").concat(Integer.toString(request.getServerPort())).concat(request.getContextPath());
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
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(EmailServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EmailServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        EmailDao emailDao = new EmailDaoImpl();
        String action = request.getParameter("action");       
        //  String chartsLink = formURL(request).concat("/charts.jsp?fromDate=").concat(fromDate).concat("&toDate=").concat(toDate).concat("&allData=N");
        String imageLink = formURL(request).concat("/image.jsp?action=displayImage&cvgDataId=");

        if ("HTML".equalsIgnoreCase(action)) {
            try {
                // String customerLogo = request.getContextPath().replace("/", "") + ".png";
                // inline images
                Map<String, String> inlineImages = new HashMap<String, String>();
                String img_0_0_1 = getClass().getResource("playright.png").getFile();
                inlineImages.put("img_0_0_1",
                        img_0_0_1);
                String img_0_0_3 = getClass().getResource("revvster.png").getFile();
                inlineImages.put("img_0_0_3",
                        img_0_0_3);
           //     sendHTMLMail(fromDate, toDate, recipientMailAddress, emailSubject, imageLink, inlineImages);
            } catch (Exception ex) {
                //Logger.getLogger(DataController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                emailDao.close();
            }
        }
    }

    private String getErrorJson(String msg) {
        return "{\"result\":\"error\",\"errorMsg\":\"" + msg + "\"}";
    }

    private String getSuccessJson(String msg) {
        return "{\"result\":\"success\",\"successMsg\":\"" + msg + "\"}";
    }
}
