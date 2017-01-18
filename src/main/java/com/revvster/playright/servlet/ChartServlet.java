/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
// */
package com.revvster.playright.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revvster.playright.dao.DashboardDao;
import com.revvster.playright.dao.DashboardDaoImpl;
import com.revvster.playright.model.User;
import com.revvster.playright.ui.charts.LineChartData;
import com.revvster.playright.ui.charts.PieChartData;
import com.revvster.playright.util.SystemConstants;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author rahulkk
 */
@WebServlet(name = "ChartServlet", urlPatterns = {"/chart/*", "/chart", "/pages/chart/*", "/pages/chart"})
public class ChartServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(ChartServlet.class.getName());

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
        String action = request.getParameter("action");
        logger.info("processRequest::action::" + action);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonArray = new String();
        if (action != null) {
            DashboardDao dashboardDao = new DashboardDaoImpl();
            response.setContentType("application/json");
            loggedInUser = (User) request.getSession().getAttribute(SystemConstants.LoggedInUser);
            Integer selectedComp = 0;
            Integer selectedProj = 0;
            Integer selectedLoc = 0;
            Integer userId = 0;
            try {
                switch (action) {
                    case "getConversionRateBySalesRep":
                        selectedProj = Integer.valueOf(request.getParameter("id"));
                        
                        if (selectedProj != null && selectedProj > 0) {
                            LineChartData acd = dashboardDao.getConversionRateBySalesRep(selectedProj);
                            jsonArray = gson.toJson(acd);
                        } else {
                            jsonArray = getErrorJson("There was an error getting no of conversation rate");
                        }
                        break;
                      case "getSalesQuotientBySalesRep":
                        selectedProj = Integer.valueOf(request.getParameter("id"));
                        
                        if (selectedProj != null && selectedProj > 0) {
                            List<PieChartData> pcd = dashboardDao.getSalesQuotientBySalesRep(selectedProj);
                            jsonArray = gson.toJson(pcd);
                        } else {
                            jsonArray = getErrorJson("There was an error getting no of Sales Quotient");
                        }
                        break;
                      
//                    case "customerProfile":
////                        customer = customerDao.getCustomer(Integer.valueOf(request.getParameter("id")));
////                        if (customer != null) {
////                            jsonArray = "{\n\"data\":\n" + gson.toJson(customer) + "\n}";
////                        } else {
////                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
////                            jsonArray = gson.toJson(getErrorJson("You are not Authorized to view this user"));
////                        }
//                        break;
//                    case "updateUser":
////                        userDao.updateUser(getUserFromReq(request));
//                        jsonArray = gson.toJson("");
//                        break;
//                    case "createUser":
////                        user = getUserFromReq(request);
////                        user.setCreatedBy((Integer)request.getSession().getAttribute(SystemConstants.LoggedInUserId));
////                        user.setLastUpdatedBy((Integer)request.getSession().getAttribute(SystemConstants.LoggedInUserId));
////                        userDao.createUser(user);
//                        jsonArray = gson.toJson("");
//                        break;
//                    case "disableUsers":
////                        userDao.deleteUsers();
//                        jsonArray = gson.toJson("");
//                        break;
//                    case "getCompaniesStartingWith":
//                        String startingWith = request.getParameter("q");
////                        List<Company> comps = projDao.getCompaniesStartingWith(startingWith);
////                        jsonArray = "{\n\"items\":\n" + gson.toJson(comps) + "\n}";
//                        break;
                }
            } catch (SQLException | NumberFormatException ex) {
                logger.error(ex);
                jsonArray = getErrorJson(ex.getMessage());
            } finally {
                dashboardDao.close();
                logger.debug("processRequest::END");
                response.getWriter().write(jsonArray);
            }
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

    private String getErrorJson(String msg) {
        return "{\"result\":\"error\",\"errorMsg\":\"" + msg + "\"}";
    }

    private String getSuccessJson(String msg) {
        return "{\"result\":\"success\",\"successMsg\":\"" + msg + "\"}";
    }
}
