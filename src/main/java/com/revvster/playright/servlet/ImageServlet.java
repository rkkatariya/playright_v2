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
import com.revvster.playright.model.Data;
import com.revvster.playright.util.DBConnectionManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author REVV-03
 */
@WebServlet(name = "ImageServlet", urlPatterns = {"/image/*", "/image", "/pages/image/*", "/pages/image"})
public class ImageServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(ImageServlet.class.getName());

    protected Connection conn;

    public ImageServlet() {
        conn = DBConnectionManager.getConnection();
        logger.debug("getConnection::" + conn.toString());
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
        String action = request.getParameter("action");
        logger.info("processRequest::action::" + action);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonArray = new String();
        if (action != null) {
            Integer imageId = 0;
            Integer id = 0;
            DashboardDao dashboardDao = new DashboardDaoImpl();
            List<Data> datas = new ArrayList<>();
            Data data = new Data();
            try {
                switch (action) {
                    case "getPhoto":
                        imageId = Integer.valueOf(request.getParameter("imageId"));
                        data = dashboardDao.getPhoto(imageId);
                        byte[] imgData = data.getImage().getBytes(1, (int) data.getImage().length());
                        // String value = new String(imgData);
                        response.setHeader("expires", "0");
                        response.setContentType(data.getFileType());
                        response.setContentLength(imgData.length);
                        response.getOutputStream().write(imgData);
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
