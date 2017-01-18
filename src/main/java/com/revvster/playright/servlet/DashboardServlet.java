/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revvster.playright.dao.ContextObjDao;
import com.revvster.playright.dao.ContextObjDaoImpl;
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
import com.revvster.playright.model.User;
import com.revvster.playright.ui.charts.LineChartData;
import com.revvster.playright.ui.charts.BarChartData;
import com.revvster.playright.ui.charts.ChartsUtil;
import com.revvster.playright.ui.charts.PieChartData;
import com.revvster.playright.util.DBConnectionManager;
import com.revvster.playright.util.EmailUtil;
import com.revvster.playright.util.SystemConstants;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.rowset.serial.SerialBlob;
import javax.servlet.annotation.MultipartConfig;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author rahulkk
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard/*", "/dashboard", "/pages/dashboard/*", "/pages/dashboard"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1MB
        maxFileSize = 1024 * 1024 * 1, // 1MB
        maxRequestSize = 1024 * 1024 * 2)
public class DashboardServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(DashboardServlet.class.getName());

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
            throws ServletException, IOException, ParseException {
        logger.debug("processRequest::START");
        String action = request.getParameter("action");
        logger.info("processRequest::action::" + action);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonArray = new String();
        if (action != null) {
            ContextObjDao contextObjDao = new ContextObjDaoImpl();
            DashboardDao dashboardDao = new DashboardDaoImpl();
            response.setContentType("application/json");
            loggedInUser = (User) request.getSession().getAttribute(SystemConstants.LoggedInUser);
            Project project = new Project();
            List<Project> projects = new ArrayList<>();
            List<Data> datas = new ArrayList<>();
            Data data = new Data();
            BarChartData bcd = null;
            PieChartData pcd = null;
            EmailDao emailDao = new EmailDaoImpl();
            SettingDao settingDao = new SettingDaoImpl();
            SimpleDateFormat dateFormatr = new SimpleDateFormat("dd-MM-yyyy");
            // Data data = null;
            Integer selectedComp = 0;
            Integer selectedProj = 0;
            Integer selectedLoc = 0;
            Integer userId = 0;
            String selectedDateRange = "";
            Integer selectedQuestion = 0;
            Integer selectedCatalogAttr = 0;
            String inputFromDate = "";
            String inputToDate = "";
            String to = "";
            String from = "";
            String subject = "";
            Integer imageId = 0;
            // String body = "";
            Integer id = 0;
            Timestamp inptFrom = null;
            Timestamp inptTo = null;
            try {
                switch (action) {
                    case "getProject":
                        selectedProj = Integer.valueOf(request.getParameter("selectedProject"));
                        project = contextObjDao.getProject(selectedProj);
                        jsonArray = gson.toJson(project);
                        break;
                    case "getProjectData":
                        selectedProj = Integer.valueOf(request.getParameter("selectedProject"));
                        project.setId(selectedProj);
                        project.setUnitsSold(dashboardDao.getSoldUnitsInProject(selectedProj));
                        //  project.setDescription(contextObjDao.getProject(selectedProj));
                        project.setUnitsAvailable(dashboardDao.getAvailableUnitsInProject(selectedProj));
                        project.setTotalVisits(dashboardDao.getVisitsInProject(selectedProj));
                        project.setSalesRep(dashboardDao.getChampionForProject(selectedProj));
                        jsonArray = gson.toJson(project);
                        break;
                    case "getConversionRateForProject":
                        selectedProj = Integer.valueOf(request.getParameter("selectedProject"));
                        Integer conversationRate = 0;
                        if (selectedProj != null) {
                            Integer x = dashboardDao.getSoldUnitsInProject(selectedProj);
                            Integer c = dashboardDao.getVisitsInProject(selectedProj);
                            if (c != 0) {
                                conversationRate = (x * 100) / c;
                            }
                            jsonArray = gson.toJson(conversationRate);
                        } else {
                            jsonArray = getErrorJson("There was an error getting no of conversation rate");
                        }
                        break;

                    case "getVisitsPerSale":
                        selectedProj = Integer.valueOf(request.getParameter("selectedProject"));
                        Integer visitsPerSale = 0;
                        if (selectedProj != null) {
                            Integer x = dashboardDao.getSoldUnitsForProject(selectedProj);
                            Integer c = dashboardDao.getVisitsInProject(selectedProj);
                            if (x != 0) {
                                visitsPerSale = c / x;
                            } else {
                                visitsPerSale = c;
                            }
                            jsonArray = gson.toJson(visitsPerSale);
                        } else {
                            jsonArray = getErrorJson("There was an error getting visits");
                        }
                        break;
                    case "getTopXSalesRepPerfForProject":
                        selectedProj = Integer.valueOf(request.getParameter("selectedProject"));
                        selectedDateRange = request.getParameter("selectedDateRange");
                        inputFromDate = request.getParameter("inputFromDate");
                        inputToDate = request.getParameter("inputToDate");
                        if (selectedProj != null) {
                            bcd = dashboardDao.getTopXSalesRepPerfForProject(selectedProj,
                                    ChartsUtil.DateRanges.valueOf(selectedDateRange), 5);
                            jsonArray = gson.toJson(bcd);
                        } else {
                            jsonArray = getErrorJson("There was an error getting sales data");
                        }
                        break;
                    case "getSalesVsVisitsDataForProjectInDateRange":
                        selectedProj = Integer.valueOf(request.getParameter("selectedProject"));
                        selectedDateRange = request.getParameter("selectedDateRange");
                        inputFromDate = request.getParameter("inputFromDate");
                        inputToDate = request.getParameter("inputToDate");
                        if (selectedProj != null) {
                            LineChartData acd1 = dashboardDao.getSalesDataForProject(selectedProj,
                                    ChartsUtil.DateRanges.valueOf(selectedDateRange));
                            LineChartData acd2 = dashboardDao.getVisitsDataForProject(selectedProj,
                                    ChartsUtil.DateRanges.valueOf(selectedDateRange));
                            ChartsUtil.mergeLineChartData(acd1, acd2);
                            jsonArray = gson.toJson(acd1);
                        } else {
                            jsonArray = getErrorJson("There was an error getting sales data");
                        }
                        break;
                    case "getSalesDataForProjectInDateRange":
                        selectedProj = Integer.valueOf(request.getParameter("selectedProject"));
                        selectedDateRange = request.getParameter("selectedDateRange");
                        inputFromDate = request.getParameter("inputFromDate");
                        inputToDate = request.getParameter("inputToDate");
                        if (selectedProj != null) {
                            LineChartData acd1 = dashboardDao.getSalesDataForProject(selectedProj,
                                    ChartsUtil.DateRanges.valueOf(selectedDateRange));
                            jsonArray = gson.toJson(acd1);
                        } else {
                            jsonArray = getErrorJson("There was an error getting sales data");
                        }
                        break;
                    case "getVisitsDataForProject":
                        selectedProj = Integer.valueOf(request.getParameter("selectedProject"));
                        selectedDateRange = request.getParameter("selectedDateRange");
                        inputFromDate = request.getParameter("inputFromDate");
                        inputToDate = request.getParameter("inputToDate");
                        if (selectedProj != null) {
                            LineChartData acd = dashboardDao.getVisitsDataForProject(selectedProj,
                                    ChartsUtil.DateRanges.valueOf(selectedDateRange));
                            jsonArray = gson.toJson(acd);
                        } else {
                            jsonArray = getErrorJson("There was an error getting visits data");
                        }
                        break;
                    case "getVisitsDataForUser":
                        userId = Integer.valueOf(request.getParameter("selectedUser"));
                        selectedDateRange = request.getParameter("selectedDateRange");
                        inputFromDate = request.getParameter("inputFromDate");
                        inputToDate = request.getParameter("inputToDate");
                        if (userId != null) {
                            LineChartData acd = dashboardDao.getVisitsDataForUser(userId,
                                    ChartsUtil.DateRanges.valueOf(selectedDateRange));
                            jsonArray = gson.toJson(acd);
                        } else {
                            jsonArray = getErrorJson("There was an error getting visits data");
                        }
                        break;
                    case "getSalesDataForUser":
                        userId = Integer.valueOf(request.getParameter("selectedUser"));
                        selectedDateRange = request.getParameter("selectedDateRange");
                        inputFromDate = request.getParameter("inputFromDate");
                        inputToDate = request.getParameter("inputToDate");
                        if (userId != null) {
                            LineChartData acd = dashboardDao.getSalesDataForUser(userId,
                                    ChartsUtil.DateRanges.valueOf(selectedDateRange));
                            jsonArray = gson.toJson(acd);
                        } else {
                            jsonArray = getErrorJson("There was an error getting sales data");
                        }
                        break;
                    case "getAnswerDistributionByCustomer":
                        selectedQuestion = Integer.valueOf(request.getParameter("selectedQuestion"));
                        if (selectedQuestion != null && selectedQuestion > 0) {
                            bcd = dashboardDao.getAnswerDistributionByCustomer(selectedQuestion);
                            jsonArray = gson.toJson(bcd);
                        } else {
                            jsonArray = getErrorJson("There was an error getting customer choices");
                        }
                        break;
                    case "getInventorySalesByAttribute":
                        selectedCatalogAttr = Integer.valueOf(request.getParameter("selectedAttribute"));
                        if (selectedCatalogAttr != null && selectedCatalogAttr > 0) {
                            BarChartData bcd1 = dashboardDao.getInventoryStatesByAttribute(selectedCatalogAttr, "'available'", "Available");
                            BarChartData bcd2 = dashboardDao.getInventoryStatesByAttribute(selectedCatalogAttr, "'sold'", "Sold");
                            BarChartData bcd3 = dashboardDao.getInventoryStatesByAttribute(selectedCatalogAttr, "'blocked'", "Blocked");
                            ChartsUtil.mergeBarChartData(bcd1, bcd2, bcd3);
                            // ChartsUtil.mergeBarChartData(bcd1, bcd3);
                            jsonArray = gson.toJson(bcd1);
                        } else {
                            jsonArray = getErrorJson("There was an error getting inventory distribution");
                        }
                        break;
                    case "getAwgTimeSpent":
                        selectedProj = Integer.valueOf(request.getParameter("selectedProject"));
                        selectedDateRange = request.getParameter("selectedDateRange");
                        inputFromDate = request.getParameter("inputFromDate");
                        inputToDate = request.getParameter("inputToDate");
                        if (selectedProj != null) {
                            bcd = dashboardDao.getAwgTimeSpent(selectedProj, ChartsUtil.DateRanges.valueOf(selectedDateRange));
                            jsonArray = gson.toJson(bcd);
                        } else {
                            jsonArray = getErrorJson("There was an error getting inventory distribution");
                        }
                        break;
                    case "getHighestSellingForProject":
                        selectedProj = Integer.valueOf(request.getParameter("selectedProject"));
                        if (selectedProj != null) {
                            String highestSelling = dashboardDao.getHighestSellingForProject(selectedProj);
                            jsonArray = gson.toJson(highestSelling);
                        } else {
                            jsonArray = getErrorJson("There was an error getting no of conversation rate");
                        }
                        break;
                    case "listDataByContext":
                        datas = dashboardDao.getDataByContext(loggedInUser);
                        jsonArray = gson.toJson(datas);
                        jsonArray = "{\n\"data\":\n" + jsonArray + "\n}";
                        break;
                    case "getPhoto":
                        imageId = Integer.valueOf(request.getParameter("imageId"));
                        data = dashboardDao.getPhoto(imageId);
                        byte[] imgData = data.getImage().getBytes(1, (int) data.getImage().length());
                        // String value = new String(imgData);
                        response.setHeader("expires", "0");
                        response.setContentType("image/jpg");
                        response.setContentLength(imgData.length);
                        response.getOutputStream().write(imgData);
                        break;
                    case "getCompressedPhoto":
                        imageId = Integer.valueOf(request.getParameter("imageId"));
                        data = dashboardDao.getPhoto(imageId);
                        byte[] imagegData = data.getImage().getBytes(1, (int) data.getImage().length());
                        // PageFormat pageFormat = new PageFormat();
                        response.setHeader("expires", "0");
                        response.setContentType("image/jpg");
                        response.setContentLength(imagegData.length);
                        response.getOutputStream().write(imagegData);
                        break;
                    case "updateAnalyticsData":
                        data = getDataFromRequest(request);
                        if (data != null) {
                            dashboardDao.updateData(data);
                            jsonArray = gson.toJson(getSuccessJson("Data Updated !!"));
                        } else {
                            jsonArray = gson.toJson(getErrorJson("There was an error updatind data"));
                        }
                        break;
                    case "createAnalyticsData":
                        data = getDataFromReq(request);
                        if (data != null) {
                            dashboardDao.createData(data);
                            jsonArray = gson.toJson(getSuccessJson("Data successfully created !!"));
                        } else {
                            jsonArray = gson.toJson(getErrorJson("There was an error creating data"));
                        }
                        break;
                    case "disableData":
                        id = Integer.valueOf(request.getParameter("inputId"));
                        dashboardDao.deleteData(id);
                        jsonArray = gson.toJson("Data deleted successfully");
                        break;
                    case "getLanguageAnalytics":
                        selectedDateRange = request.getParameter("selectedDateRange");
                        inputFromDate = request.getParameter("inputFromDate");
                        inputToDate = request.getParameter("inputToDate");
                        if (inputFromDate != "" || inputToDate != "") {
                            Date inptFromDate = dateFormatr.parse(inputFromDate);
                            Date inptToDate = dateFormatr.parse(inputToDate);
                            inptFrom = new Timestamp(inptFromDate.getTime());
                            inptTo = new Timestamp(inptToDate.getTime());
                            bcd = dashboardDao.getLanguageVsArticles(inptFrom, inptTo);
                        } else {
                            //  if (selectedDateRange != null) {
                            bcd = dashboardDao.getLanguageVsArticles(ChartsUtil.DateRanges.valueOf(selectedDateRange));
                            // }
                        }
                        jsonArray = gson.toJson(bcd);
                        break;
                    case "getEditionAnalytics":
                        selectedDateRange = request.getParameter("selectedDateRange");
                        inputFromDate = request.getParameter("inputFromDate");
                        inputToDate = request.getParameter("inputToDate");
                        if (inputFromDate != "" || inputToDate != "") {
                            Date inptFromDate = dateFormatr.parse(inputFromDate);
                            Date inptToDate = dateFormatr.parse(inputToDate);
                            inptFrom = new Timestamp(inptFromDate.getTime());
                            inptTo = new Timestamp(inptToDate.getTime());
                            pcd = dashboardDao.getEditionVsArticles(inptFrom, inptTo);
                        } else {
                            //  if (selectedDateRange != null) {
                            pcd = dashboardDao.getEditionVsArticles(ChartsUtil.DateRanges.valueOf(selectedDateRange));
                            // }
                        }
                        jsonArray = gson.toJson(pcd);
                        break;
                    case "getJournalistDistribution":
                        selectedDateRange = request.getParameter("selectedDateRange");
                        inputFromDate = request.getParameter("inputFromDate");
                        inputToDate = request.getParameter("inputToDate");
                        if (inputFromDate != "" || inputToDate != "") {
                            Date inptFromDate = dateFormatr.parse(inputFromDate);
                            Date inptToDate = dateFormatr.parse(inputToDate);
                            inptFrom = new Timestamp(inptFromDate.getTime());
                            inptTo = new Timestamp(inptToDate.getTime());
                            pcd = dashboardDao.getJournalistDistribution(inptFrom, inptTo);
                        } else {
                            //  if (selectedDateRange != null) {
                            pcd = dashboardDao.getJournalistDistribution(ChartsUtil.DateRanges.valueOf(selectedDateRange));
                            // }
                        }
                        jsonArray = gson.toJson(pcd);
                        break;
                    case "getTopEnglishPrintDistribution":
                        selectedDateRange = request.getParameter("selectedDateRange");
                        inputFromDate = request.getParameter("inputFromDate");
                        inputToDate = request.getParameter("inputToDate");
                        if (inputFromDate != "" || inputToDate != "") {
                            Date inptFromDate = dateFormatr.parse(inputFromDate);
                            Date inptToDate = dateFormatr.parse(inputToDate);
                            inptFrom = new Timestamp(inptFromDate.getTime());
                            inptTo = new Timestamp(inptToDate.getTime());
                            bcd = dashboardDao.getTopEnglishPrintDistribution(inptFrom, inptTo);
                        } else {
                            //  if (selectedDateRange != null) {
                            bcd = dashboardDao.getTopEnglishPrintDistribution(ChartsUtil.DateRanges.valueOf(selectedDateRange));
                            // }
                        }
                        jsonArray = gson.toJson(bcd);
                        break;
                    case "getTopVernacularPrintDistribution":
                        selectedDateRange = request.getParameter("selectedDateRange");
                        inputFromDate = request.getParameter("inputFromDate");
                        inputToDate = request.getParameter("inputToDate");
                        if (inputFromDate != "" || inputToDate != "") {
                            Date inptFromDate = dateFormatr.parse(inputFromDate);
                            Date inptToDate = dateFormatr.parse(inputToDate);
                            inptFrom = new Timestamp(inptFromDate.getTime());
                            inptTo = new Timestamp(inptToDate.getTime());
                            bcd = dashboardDao.getTopVernacularPrintDistribution(inptFrom, inptTo);
                        } else {
                            //  if (selectedDateRange != null) {
                            bcd = dashboardDao.getTopVernacularPrintDistribution(ChartsUtil.DateRanges.valueOf(selectedDateRange));
                            // }
                        }
                        jsonArray = gson.toJson(bcd);
                        break;
                    case "sendEmail":
                        to = request.getParameter("inputEmailAddress");
                        subject = request.getParameter("inputSubject");
                        String body = null;
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
                        Date fromDate = dateFormatter.parse(request.getParameter("inputFromDate"));
                        Date toDate = dateFormatter.parse(request.getParameter("inputToDate"));
                        inputFromDate = fromDate.toString();
                        inputToDate = toDate.toString();
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

                        datas = dashboardDao.getDataForEmail(inputFromDate, inputToDate,
                                inputLanguage, inputEdition, inputSource, inputNewsPaper);

                        body = getHTMLBody(request, datas);
                        try {

                            Company c = new Company();
                            c.setId(loggedInUser.getCompany());
                            Setting host = settingDao.getSetting(Settings.EmailHost.toString(), c);
                            Setting port = settingDao.getSetting(Settings.EmailPort.toString(), c);
                            Setting usrname = settingDao.getSetting(Settings.EmailUserName.toString(), c);
                            Setting pwd = settingDao.getSetting(Settings.EmailPassword.toString(), c);
                            Setting tlsEnable = settingDao.getSetting(Settings.EmailTLSEnable.toString(), c);

                            Email email = new Email();
                            email.setCompany(loggedInUser.getCompany() == null ? 0 : loggedInUser.getCompany());
                            email.setFrom(usrname.getValue());
                            email.setCreatedBy(loggedInUser.getId());
                            email.setTo(to);
                            email.setSubject(subject);
                            email.setBody(body);
                            email.setRequestedBy(loggedInUser.getId());
                            email.setLastUpdatedBy(loggedInUser.getId());
                            email.setSource("Send Email");
                            MimeBodyPart messageBodyPart = new MimeBodyPart();
                            messageBodyPart.setContent(body, "text/html");
                            Map<String, String> inlineImages = new HashMap<String, String>();
                            String img_0_0_1 = request.getServletContext().getRealPath("/images/playright.png");
                            inlineImages.put("img_0_0_1", img_0_0_1);
                            String img_0_0_2 = request.getServletContext().getRealPath("/images/revvster.jpg");
                            inlineImages.put("img_0_0_2", img_0_0_2);
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
                                        inlineImages,
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
                    case "getData":
                        id = Integer.valueOf(request.getParameter("id"));
                        data = dashboardDao.getData(id);
                        jsonArray = gson.toJson(data);
                        break;

                }
            } catch (SQLException | NumberFormatException ex) {
                logger.error(ex);
                jsonArray = getErrorJson(ex.getMessage());
            } finally {
                contextObjDao.close();
                dashboardDao.close();
                emailDao.close();
                settingDao.close();
                logger.debug("processRequest::END");
                response.getWriter().write(jsonArray);
            }
        }
    }

    private String getHTMLBody(HttpServletRequest request, List<Data> datas) {
        StringBuilder body = new StringBuilder("<p><img width=\"114\" src=\"cid:img_0_0_1\" alt=\"PlayRight Analytics\" title=\"PlayRight Analytics\"/>\n</p>");
        body.append("<h2 style='color: #2e6c80;' align='center'><strong>PlayRight Media Analytics</strong></h2>");
        body.append("<h3 style='color: #2e6c80;' align='center'>" + request.getParameter("inputFromDate") + "   -   " + request.getParameter("inputToDate") + "</h2>");
        body.append("<table style=\"border-top: 5px solid white; height: 225px;\" border=\"1\" width=\"783\" cellspacing=\"0\" cellpadding=\"2\" align=\"center\">");
        body.append("<thead>");
        body.append("<tr>\n");
        body.append("<td style=\"background-color:    #993366;\"> </td>\n");
        body.append("<td style=\"background-color:    #993366;\"> </td>\n");
        body.append("<td style=\"background-color:    #993366;\"> </td>\n");
        body.append("<td style=\"background-color:    #993366;\"> </td>\n");
        body.append("<td style=\"background-color:    #993366;\"> </td>\n");
        body.append("<td style=\"background-color:    #993366;\"> </td>\n");
        body.append("<td style=\"background-color:    #993366;\"> </td>\n");
        body.append("<td style=\"background-color:    #993366;\"> </td>\n");
        body.append("</tr>");
        body.append("<tr>");
        body.append("<td style=\"background-color:  #ffffff;\"><strong>Date</strong></td>");
        body.append(" <td style=\"background-color:  #ffffff;\"><strong>Publication</strong></td>");
        body.append("<td style=\"background-color:  #ffffff;\"><strong>Headline</strong></td>");
        body.append("<td style=\"background-color:  #ffffff;\"><strong>Edition</strong></td>");
        body.append("<td style=\"background-color:  #ffffff;\"><strong>Source</strong></td>");
        body.append("<td style=\"background-color:  #ffffff;\"><strong>Language</strong></td>");
        body.append("<td style=\"background-color:  #ffffff;\"><strong>Image</strong></td>");
        body.append("<td style=\"background-color:  #ffffff;\"><strong>Page</strong></td>");
        body.append("</tr>");
        body.append("<tr>\n");
        body.append("<td style=\"background-color:    #999999;\"> </td>\n");
        body.append("<td style=\"background-color:    #999999;\"> </td>\n");
        body.append("<td style=\"background-color:    #999999;\"> </td>\n");
        body.append("<td style=\"background-color:    #999999;\"> </td>\n");
        body.append("<td style=\"background-color:    #999999;\"> </td>\n");
        body.append("<td style=\"background-color:    #999999;\"> </td>\n");
        body.append("<td style=\"background-color:    #999999;\"> </td>\n");
        body.append("<td style=\"background-color:    #999999;\"> </td>\n");
        body.append("</tr>");
        body.append("</thead>");
        body.append("<tbody>");
        for (Data d : datas) {

            body.append("<tr>");
            body.append("<td style=\"background-color:  #ffffff;\">" + d.getNewsDate() + "</td>");
            if (d.getNewsPaper().endsWith(".com")) {
                body.append(" <td style=\"background-color:  #ffffff;\"><a target=\"_blank\" href=\"" + d.getNewsPaper() + "\">" + d.getNewsPaper() + "</a></td>");
            } else {
                body.append(" <td style=\"background-color:  #ffffff;\">" + d.getNewsPaper() + "</td>");
            }
            if (null == d.getImageFileName() || d.getImageFileName().length() == 0) {
                body.append("<td style=\"background-color:  #ffffff;\">" + d.getHeadline() + "</td>");
            } else {
                body.append("<td style=\"background-color:  #ffffff;\"><a target=\"_blank\" href=\"" + formURL(request) + "/pages/image.jsp?action=getPhoto&imageId=" + d.getId() + "\">" + d.getHeadline() + "</a></td>");
            }
            if (d.getEdition().startsWith("http")) {
                body.append(" <td style=\"background-color:  #ffffff;\"><a target=\"_blank\" href=\"" + d.getEdition() + "\">" + d.getEdition() + "</a></td>");
            } else {
                body.append("<td style=\"background-color:  #ffffff;\">" + d.getEdition() + "</td>");
            }
            body.append("<td style=\"background-color:  #ffffff;\">" + d.getSource() + "</td>");
            body.append("<td style=\"background-color:  #ffffff;\">" + d.getLanguage() + "</td>");
            body.append("<td style=\"background-color:  #ffffff;\">" + d.getImageExists() + "</td>");
            body.append("<td style=\"background-color:  #ffffff;\">" + d.getPageNo() + "</td>");
            body.append("</td>");
            body.append("</tr>");
        }
        body.append("</tbody>");
        body.append("</table>");
        //   body.append("<a href=\"revvster.in\"><img src=\"../images/revvster.png\" width=\"50\" height=\"50\"></a>");
        // body.append("<p>dfgdfhfgh</p>");
        body.append("<p><img width=\"114\" src=\"cid:img_0_0_2\" alt=\"Revvster Technologies Pvt Ltd\" title=\"Revvster\"/></p>");
        return body.toString();
    }

    private String formURL(HttpServletRequest request) {
        return request.getScheme().concat("://").concat(request.getServerName()).concat(":").concat(Integer.toString(request.getServerPort())).concat(request.getContextPath());
    }

    private Data getDataFromReq(HttpServletRequest request) throws SQLException, ParseException {
        Data data = new Data();

        Blob b = null;
        String fileName = "";
        String contentType = "";
        Integer size = 0;
        Part filePart = null;
        if (request.getParameter("inputId") != null) {
            data.setId(Integer.valueOf(request.getParameter("inputId")));
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        if (request.getParameter("inputNewsDate") != null) {
            Date newsDate = dateFormatter.parse(request.getParameter("inputNewsDate"));
            data.setNewsDate(new Timestamp(newsDate.getTime()));
        } else {
            data.setNewsDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        }
        data.setNewsPaper(request.getParameter("inputNewsPaper"));
        data.setLanguage(request.getParameter("inputLanguage"));
        data.setHeadline(request.getParameter("inputHeadline"));
        data.setEdition(request.getParameter("inputEdition"));
        data.setSupplement(request.getParameter("inputSupplement"));
        data.setSource(request.getParameter("inputSource"));
        data.setImageExists(request.getParameter("inputImageExists"));
        data.setPageNo(Integer.valueOf(request.getParameter("inputPageNo")));
        data.setHeight(Integer.valueOf(request.getParameter("inputHeight")));
        data.setWidth(Integer.valueOf(request.getParameter("inputWidth")));
        data.setTotalArticleSize(Integer.valueOf(request.getParameter("inputTotalArticleSize")));
        data.setCirculationFigure(Integer.valueOf(request.getParameter("inputCirculationFigure")));
        data.setQuantitativeAVE(Integer.valueOf(request.getParameter("inputQuantitativeAVE")));
        data.setJournalistFactor(Integer.valueOf(request.getParameter("inputJournalistFactor")));

        try {
            if (request.getPart("inputImage") != null) {
                filePart = request.getPart("inputImage");
                InputStream fileContent = filePart.getInputStream();
                byte[] bytes = IOUtils.toByteArray(fileContent);
                b = new SerialBlob(bytes);
                fileName = filePart.getSubmittedFileName();
                contentType = filePart.getContentType();
                size = (int) filePart.getSize();
            }
        } catch (IOException | ServletException ex) {
            logger.error("There was an error getting image", ex);
        }
        if (b != null && b.length() != 0) {
            data.setImage(b);
            data.setImageFileName(fileName);
            data.setFileSize(size);
            data.setFileType(contentType);
        }
        data.setCreatedBy(loggedInUser.getId());
        data.setLastUpdatedBy(loggedInUser.getId());
        //data.setActive(1);
        return data;
    }

    private Data getDataFromRequest(HttpServletRequest request) throws SQLException, ParseException {
        Data data = new Data();
        Blob b = null;
        String fileName = "";
        String contentType = "";
        Integer size = 0;
        Part filePart = null;
        if (request.getParameter("inputId") != null) {
            data.setId(Integer.valueOf(request.getParameter("inputId")));
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        if (request.getParameter("inputNewsDate") != null) {
            Date newsDate = dateFormatter.parse(request.getParameter("inputNewsDate"));
            data.setNewsDate(new Timestamp(newsDate.getTime()));
        } else {
            data.setNewsDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        }
        data.setNewsPaper(request.getParameter("inputNewsPaper"));
        data.setLanguage(request.getParameter("inputLanguage"));
        data.setHeadline(request.getParameter("inputHeadline"));
        data.setEdition(request.getParameter("inputEdition"));
        data.setSupplement(request.getParameter("inputSupplement"));
        data.setSource(request.getParameter("inputSource"));
        data.setImageExists(request.getParameter("inputImageExists"));
        data.setPageNo(Integer.valueOf(request.getParameter("inputPageNo")));
        data.setHeight(Integer.valueOf(request.getParameter("inputHeight")));
        data.setWidth(Integer.valueOf(request.getParameter("inputWidth")));
        data.setTotalArticleSize(Integer.valueOf(request.getParameter("inputTotalArticleSize")));
        data.setCirculationFigure(Integer.valueOf(request.getParameter("inputCirculationFigure")));
        data.setQuantitativeAVE(Integer.valueOf(request.getParameter("inputQuantitativeAVE")));
        data.setJournalistFactor(Integer.valueOf(request.getParameter("inputJournalistFactor")));
        try {
            if (request.getPart("inputImage") != null) {
                filePart = request.getPart("inputImage");
                InputStream fileContent = filePart.getInputStream();
                byte[] bytes = IOUtils.toByteArray(fileContent);
                b = new SerialBlob(bytes);
                fileName = filePart.getSubmittedFileName();
                contentType = filePart.getContentType();
                size = (int) filePart.getSize();
            }
        } catch (IOException | ServletException ex) {
            logger.error("There was an error getting image", ex);
        }
        if (b != null && b.length() != 0) {
            data.setImage(b);
            data.setImageFileName(fileName);
            data.setFileSize(size);
            data.setFileType(contentType);
        }
        //data.setCreatedBy(Integer.valueOf(request.getParameter("inputCreatedBy")));
        data.setLastUpdatedBy(loggedInUser.getId());
        //data.setActive(1);
        return data;
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
            java.util.logging.Logger.getLogger(DashboardServlet.class.getName()).log(Level.SEVERE, null, ex);
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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(DashboardServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
