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
import com.revvster.playright.access.Action;
import com.revvster.playright.access.AuthorizationManager;
import com.revvster.playright.model.Company;
import com.revvster.playright.model.Location;
import com.revvster.playright.model.Project;
import com.revvster.playright.access.Resource;
import com.revvster.playright.model.RoleEntContext;
import com.revvster.playright.model.User;
import com.revvster.playright.model.UserEntitlement;
import com.revvster.playright.util.SystemConstants;
import com.revvster.playright.model.Setting;
import com.revvster.playright.model.Settings;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
 * @author Rahul
 */
@WebServlet(name = "ContextObjServlet", urlPatterns = {"/context/*", "/context", "/pages/context/*", "/pages/context"})
public class ContextObjServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(ContextObjServlet.class.getName());

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
            ContextObjDao contextDao = new ContextObjDaoImpl();
            response.setContentType("application/json");
            loggedInUser = (User) request.getSession().getAttribute(SystemConstants.LoggedInUser);
            Company company = new Company();
            Project project = new Project();
            List<Company> comps = new ArrayList<>();
            List<Project> projs = new ArrayList<>();
            List<Location> locs = new ArrayList<>();
            List<User> users = new ArrayList<>();
            List<Setting> settings = new ArrayList<>();
            Setting setting = new Setting();
            List<Project> projects = new ArrayList<>();
            RoleEntContext ctx = null;
            String selectedComp = "";
            String selectedProj = "";
            String selectedLoc = "";
            Integer emailEventId = 0;
            Integer companyId = 0;
            Integer projectId = 0;
            Integer locationId = 0;
            Integer customerId = 0;
            Integer selectedSetting = 0;
            Integer selectedCust = 0;
            String resource = "";
            UserEntitlement ue = null;
            boolean authorized = false;
            try {
                switch (action) {
                    case "listCompaniesByContext":      
                        if (loggedInUser.hasEntitlement(new UserEntitlement(Resource.company, Action.list))) {
                            comps = contextDao.listCompaniesByContext(loggedInUser,
                                new UserEntitlement(Resource.company, Action.list));
                        }
                        jsonArray = gson.toJson(comps);
                        jsonArray = "{\n\"data\":\n" + jsonArray + "\n}";
                        break;
                    case "listProjectsByContext":
                        selectedComp = request.getParameter("selectedCompany");
                        resource = request.getParameter("resource");
                        ue = new UserEntitlement(Resource.valueOf(resource),
                                request.getParameter("act") != null ? Action.valueOf(request.getParameter("act")) : Action.list);
                        ctx = AuthorizationManager.getRoleEntContext(loggedInUser.getUserEntitlements(), ue);
                        if (ctx.getContextLevel() > 0) {
                            projs = contextDao.listProjectsByContext(loggedInUser, ue);
                        } else if (selectedComp != null) {
                            projs = contextDao.listProjects(Integer.valueOf(selectedComp), loggedInUser);
                        } else {
                            projs = contextDao.listProjects(0, loggedInUser);
                        }
                        jsonArray = gson.toJson(projs);
                        jsonArray = "{\n\"data\":\n" + jsonArray + "\n}";
                        break;
                    case "listProjectByCustomer":
                        if (request.getParameter("customer") != null) {
                            projects = contextDao.listProjectByCustomer(Integer.valueOf(request.getParameter("customer")));
                            jsonArray = gson.toJson(projects);
                        } else {
                            jsonArray = gson.toJson(getErrorJson("There was an error getting Projects"));
                        }
                        break;
                    case "listProjectForCustSalesrep":
                        selectedCust = Integer.valueOf(request.getParameter("customer"));
                        if (selectedCust != null) {
                            projects = contextDao.listProjectForCustSalesrep(selectedCust);
                            jsonArray = gson.toJson(projects);
                            //jsonArray = "{\n\"data\":\n" + jsonArray + "\n}";
                        } else {
                            jsonArray = gson.toJson(getErrorJson("There was an error getting Projects"));
                        }
                        break;
                    case "listLocationsByContext":
                        locs = contextDao.listLocationsByContext(loggedInUser,
                                new UserEntitlement(Resource.location, Action.list));
                        jsonArray = gson.toJson(locs);
                        jsonArray = "{\n\"data\":\n" + jsonArray + "\n}";
                        break;
                    case "createCompany":
                        UserEntitlement companyAdd = AuthorizationManager.getUserEntitlement(loggedInUser.getUserEntitlements(),
                                new UserEntitlement(Resource.company, Action.add));
                        authorized = AuthorizationManager.authorizeContextObj(companyAdd.getRoleEntContext(), null, null);
                        if (authorized) {
                            company = getCompanyFromRequest(request);
                            companyId = contextDao.createCompany(company);
                            if (companyId > 0) {
                                jsonArray = getSuccessJson("Company created successfuly");
                            } else {
                                jsonArray = getErrorJson("There was a error creating company");
                            }
                        } else {
                            jsonArray = getErrorJson("User not authorized to create company");
                        }
                        break;
                    case "listProjects":
                        selectedComp = request.getParameter("selectedCompany");
                        if (selectedComp != null && !"".equals(selectedComp) && !"0".equals(selectedComp)) {
                            projs = contextDao.listProjects(Integer.valueOf(selectedComp), loggedInUser);
                        }
                        jsonArray = gson.toJson(projs);
                        jsonArray = "{\n\"data\":\n" + jsonArray + "\n}";
                        break;
                    case "createProject":
                        UserEntitlement projectAdd = AuthorizationManager.getUserEntitlement(loggedInUser.getUserEntitlements(),
                                new UserEntitlement(Resource.project, Action.add));
                        authorized = AuthorizationManager.authorizeContextObj(projectAdd.getRoleEntContext(),
                                loggedInUser.getContextScope().get(projectAdd.getRoleEntContext()), loggedInUser.getCompany());
                        if (authorized) {
                            project = getProjectFromRequest(request);
                            Integer compId = 0;
                            projectId = contextDao.createProject(project);
                            if (projectId > 0) {
                                jsonArray = getSuccessJson("Project created successfuly");
                            } else {
                                jsonArray = getErrorJson("There was a error creating project");
                            }
                        } else {
                            jsonArray = getErrorJson("User not authorized to create project");
                        }
                        break;
                    case "getCompaniesStartingWith":
                        String startingWithC = request.getParameter("q");
                        comps = contextDao.getCompaniesStartingWith(startingWithC);
                        jsonArray = "{\n\"items\":\n" + gson.toJson(comps) + "\n}";
                        break;

                    case "getProjectsStartingWith":
                        String startingWithP = request.getParameter("q");
                        selectedComp = request.getParameter("selectedCompany");
                        if (selectedComp != null && !"".equals(selectedComp)) {
                            companyId = Integer.valueOf(selectedComp);
                        }
                        projs = contextDao.getProjectsStartingWith(startingWithP, companyId, loggedInUser);
                        jsonArray = "{\n\"items\":\n" + gson.toJson(projs) + "\n}";
                        break;
                    case "getLocationsStartingWith":
                        String startingWithL = request.getParameter("q");
                        selectedComp = request.getParameter("selectedCompany");
                        if (selectedComp != null && !"".equals(selectedComp)) {
                            companyId = Integer.valueOf(selectedComp);
                        }
                        selectedProj = request.getParameter("selectedProject");
                        if (selectedProj != null && !"".equals(selectedProj)) {
                            projectId = Integer.valueOf(selectedProj);
                        }
                        locs = contextDao.getLocationsStartingWith(startingWithL, projectId, loggedInUser);
                        jsonArray = "{\n\"items\":\n" + gson.toJson(locs) + "\n}";
                        break;
                    case "assignUsersToProject":
                        projectId = Integer.parseInt(request.getParameter("inputProject"));
                        if (request.getParameter("inputCompany") != null) {
                            companyId = Integer.valueOf(request.getParameter("inputCompany"));
                        } else {
                            companyId = loggedInUser.getCompany();
                        }
                        if (request.getParameterValues("inputUsers") != null) {
                            String[] userIdsStr = request.getParameterValues("inputUsers");
                            for (String userIdS : userIdsStr) {
                                User u = new User();
                                u.setId(Integer.valueOf(userIdS));
                                u.setCompany(companyId);
                                u.setLastUpdatedBy(loggedInUser.getId());
                                u.setCreatedBy(loggedInUser.getId());
                                users.add(u);
                            }
                            contextDao.assignUsersToProject(users, projectId, loggedInUser);
                        }
                        jsonArray = getSuccessJson("User/s assigned successfuly");
                        break;
                    case "listSettingsByContext":
                        selectedComp = request.getParameter("selectedCompany");
                        if (request.getParameter("selectedProject") != null
                                && !request.getParameter("selectedProject").equals("0")) {
                            selectedProj = request.getParameter("selectedProject");
                            settings = contextDao.listSettingsByProject(loggedInUser, Integer.valueOf(selectedComp), Integer.valueOf(selectedProj));
                        } else if (request.getParameter("selectedCompany") != null) {
                            settings = contextDao.listSettingsByCompany(loggedInUser, Integer.valueOf(selectedComp));
                        }
                        jsonArray = "{\n\"data\":\n" + gson.toJson(settings) + "\n}";
                        break;
                    case "listEmailSettingsByContext":
                        selectedComp = request.getParameter("selectedCompany");
                        if (request.getParameter("selectedCompany") != null) {
                            settings = contextDao.listEmailSettingsByCompany(loggedInUser, Integer.valueOf(selectedComp));
                        } 
                        jsonArray = "{\n\"data\":\n" + gson.toJson(settings) + "\n}";
                        break;
                    case "createOrUpdateSettingValues":
                        setting = getSettingFromReq(request);
                        if (setting.getId() != null && setting.getId() != 0) {
                            contextDao.updateSettingValues(setting);
                        } else {
                            contextDao.createSettingValues(setting);
                        }
                        jsonArray = getSuccessJson("Setting status updated successfully");
                        break;
                    case "updateProject":
                        UserEntitlement projectUpdate = AuthorizationManager.getUserEntitlement(loggedInUser.getUserEntitlements(),
                                new UserEntitlement(Resource.project, Action.update));
                        authorized = AuthorizationManager.authorizeContextObj(projectUpdate.getRoleEntContext(),
                                loggedInUser.getContextScope().get(projectUpdate.getRoleEntContext()), loggedInUser.getCompany());
                        if (authorized) {
                            project = getProjectFromRequest(request);
                            contextDao.updateProject(project);
                            jsonArray = getSuccessJson("Project created successfuly");
                        } else {
                            jsonArray = getErrorJson("User not authorized to create project");
                        }
                        break;      
                }
            } catch (SQLException | NumberFormatException ex) {
                logger.error(ex);
                String error = "{\"result\":\"error\",\"errorMsg\":\"" + ex.getMessage() + "\"}";
                response.getWriter().write(error);
            } finally {
                contextDao.close();
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

    private Company getCompanyFromRequest(HttpServletRequest request) {
        Company c = new Company();
        if (request.getParameter("inputId") != null) {
            c.setId(Integer.valueOf(request.getParameter("inputId")));
        }
        c.setName(request.getParameter("inputName"));
        c.setDescription(request.getParameter("inputDescription"));
        c.setUrl(request.getParameter("inputUrl"));
        if (request.getParameter("inputActive") != null
                && "on".equals(request.getParameter("inputActive"))) {
            c.setActive(0);
        } else {
            c.setActive(1);
        }
        c.setCreatedBy(loggedInUser.getId());
        c.setLastUpdatedBy(loggedInUser.getId());
        return c;
    }

    private Project getProjectFromRequest(HttpServletRequest request) {
        Project p = new Project();
        if (request.getParameter("inputId") != null) {
            p.setId(Integer.valueOf(request.getParameter("inputId")));
        }
        if (loggedInUser.getCompany() == null || loggedInUser.getCompany() == 0) {
            p.setCompany(Integer.valueOf(request.getParameter("inputCompany")));
        } else {
            p.setCompany(loggedInUser.getCompany());
        }
        p.setName(request.getParameter("inputName"));
        p.setDescription(request.getParameter("inputDescription"));
        p.setUrl(request.getParameter("inputUrl"));
        p.setCoordinates(request.getParameter("inputCoordinates"));
        if (request.getParameter("inputActive") != null
                && "on".equals(request.getParameter("inputActive"))) {
            p.setActive(0);
        } else {
            p.setActive(1);
        }
        p.setCreatedBy(loggedInUser.getId());
        p.setLastUpdatedBy(loggedInUser.getId());
        return p;
    }

    private Setting getSettingFromReq(HttpServletRequest request) {
        Setting s = new Setting();
        if (request.getParameter("inputId") != null) {
            s.setId(Integer.valueOf(request.getParameter("inputId")));
        }
        s.setCompany(Integer.valueOf(request.getParameter("selectedCompany")));
        if (request.getParameter("selectedProject") != null) {
            s.setProject(Integer.valueOf(request.getParameter("selectedProject")));
        }
        s.setParameter(Settings.valueOf(request.getParameter("selectedParameter")));
        s.setValue(request.getParameter("inputValue"));
        s.setActive(1);
        s.setCreatedBy(loggedInUser.getId());
        s.setLastUpdatedBy(loggedInUser.getId());
        return s;
    }
    
}
