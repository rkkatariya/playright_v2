/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.dao;

import com.revvster.playright.access.Action;
import com.revvster.playright.model.Company;
import com.revvster.playright.model.Location;
import com.revvster.playright.model.Project;
import com.revvster.playright.access.Resource;
import com.revvster.playright.model.RoleEntContext;
import com.revvster.playright.model.User;
import com.revvster.playright.model.UserEntitlement;
import com.revvster.playright.access.AuthorizationManager;
import com.revvster.playright.model.ContextObj;
import com.revvster.playright.model.Setting;
import com.revvster.playright.model.SettingType;
import com.revvster.playright.model.Settings;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rahul
 */
public class ContextObjDaoImpl extends DaoHelper implements ContextObjDao {

    private static final Logger logger = LogManager.getLogger(ContextObjDaoImpl.class.getName());

    private static final String companiesQueryForCtx = "select id as company, c.* from sb_companies c";

    private static final String projectsQueryForCtx = "select c.name as company_name, p.id as project, p.* \n"
            + "from sb_projects p\n"
            + "left join sb_companies c on p.company = c.id";

    private static final String locationsQueryForCtx = "select p.name as project_name, p.company, \n"
            + "l.id as location, l.* \n"
            + "from sb_location l\n"
            + "left join sb_projects p on l.project = p.id";

    public ContextObjDaoImpl() {
        super();
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public List<Company> getAllCompanies()
            throws SQLException {
        logger.debug("getAllCompanies::START");
        ArrayList<Company> companies = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement("select c.*\n"
                + "from sb_companies c");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Company c = getCompanyObj(rs);
            setAuditable(rs, c);
            companies.add(c);
        }
        close(rs);
        close(ps);
        logger.debug("getAllCompanies::END");
        return companies;
    }

    @Override
    public Company getCompany(String name)
            throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Company getCompany(Integer id)
            throws SQLException {
        logger.debug("getCompany::ByID::START::" + id);
        PreparedStatement ps = null;
        Company company = new Company();
        ps = conn.prepareStatement("select * from \n"
                + "sb_companies where id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            company = getCompanyObj(rs);
            setAuditable(rs, company);
        }
        close(rs);
        close(ps);
        logger.debug("getCompany::ByID::END");
        return company;
    }

    @Override
    public Project getProject(Integer id)
            throws SQLException {
        logger.debug("getProject::ByID::START::" + id);
        PreparedStatement ps = null;
        Project project = new Project();
        ps = conn.prepareStatement("select c.name as company_name, p.* from sb_projects p \n"
                + "left join sb_companies c on p.company = c.id where p.id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            project = getProjectObj(rs);
            setAuditable(rs, project);
        }
        close(rs);
        close(ps);
        logger.debug("getProject::ByID::END");
        return project;
    }

    @Override
    public Project getProjectData(Integer id)
            throws SQLException {
        logger.debug("getProjectData::ByID::START::" + id);
        PreparedStatement ps = null;
        Project project = new Project();
        ps = conn.prepareStatement("select count(*) as visits, p.* from sb_projects p \n"
                + "left join sb_customer_visits cv on p.id = cv.project "
                + "where p.id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            project = getProjectObj(rs);
            setAuditable(rs, project);
        }
        close(rs);
        close(ps);
        logger.debug("getProjectData::ByID::END");
        return project;
    }

    @Override
    public Integer createSettingValues(Setting setting) throws SQLException {
        logger.debug("createSettingValues::START::" + setting);
        PreparedStatement ps = conn.prepareStatement("INSERT INTO sb_settings \n"
                + "(company, project, parameter, value, \n"
                + "active, created_by, last_updated_by) \n"
                + "VALUES (?, ?, ?, ?, \n"
                + "?, ?, ?)");
        if (setting.getCompany() != null) {
            ps.setInt(1, setting.getCompany());
        } else {
            ps.setNull(1, Types.INTEGER);
        }
        if (setting.getProject() != null) {
            ps.setInt(2, setting.getProject());
        } else {
            ps.setNull(2, Types.INTEGER);
        }
        ps.setString(3, setting.getParameter().toString());
        ps.setString(4, setting.getValue());
        ps.setInt(5, setting.getActive());
        ps.setInt(6, setting.getCreatedBy());
        ps.setInt(7, setting.getLastUpdatedBy());
        ps.executeUpdate();
        Integer id = getLastInsertId();
        setting.setId(id);
        // createDummyLocation(project);
        close(ps);
        logger.debug("createSettingValues::END");
        return id;
    }

    @Override
    public List<Setting> listSettingsByCompany(User loggedInUser, Integer company)
            throws SQLException {
        logger.debug("listSettingsByCompany::START");
        RoleEntContext listCtx = AuthorizationManager.getRoleEntContext(loggedInUser.getUserEntitlements(),
                new UserEntitlement(Resource.setting, Action.list));
        String ctxObjStr = loggedInUser.getContextScopeStr().get(listCtx);
        String ctxFilter = "";
        if (listCtx.equals(RoleEntContext.ALL)) {
            if (company != null && company != 0) {
                ctxFilter = "(s.company = ?".replace("?", company.toString()) + ")";
            } else {
                ctxFilter = "(s.company is null)";
            }
        } else {
            ctxFilter = "(s." + getCtxFilter(listCtx).replace("?", company.toString()) + ")";
        }
        ArrayList<Setting> settings = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement("select s.* from sb_settings s \n"
                + "where " + ctxFilter);
//        ps.setInt(1, company);
//        if (!listCtx.equals(RoleEntContext.ALL)) {
//            ps.setString(2, ctxObjStr);
//        }
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Setting s = getSettingObj(rs);
            setSettingAuditable(rs, s);
            settings.add(s);
        }
        close(rs);
        close(ps);
        logger.debug("listSettingsByCompany::END");
        return settings;
    }

    @Override
    public List<Setting> listEmailSettingsByCompany(User loggedInUser, Integer company)
            throws SQLException {
        logger.debug("listEmailSettingsByCompany::START");
        String query = "select es.*, s.* from (\n"
                + getEmailSettingTmpTable()
                + ") es\n"
                + "left join sb_settings s on es.param = s.parameter "
                + "and ifnull(s.company, ?) = ?";
        ArrayList<Setting> settings = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement(query);
        ps.setInt(1, company);
        ps.setInt(2, company);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Setting s = getSettingObj(rs);
            setSettingAuditable(rs, s);
            settings.add(s);
        }
        close(rs);
        close(ps);
        logger.debug("listEmailSettingsByCompany::END");
        return settings;
    }

    private String getEmailSettingTmpTable() {
        StringBuilder emailSettings = new StringBuilder("SELECT * FROM (");
        for (Settings s : Settings.values()) {
            if (s.getType().equals(SettingType.email)) {
                emailSettings.append("SELECT '").append(s.toString()).append("' param \n");
                emailSettings.append("UNION \n");
            }
        }
        int lastIdxUnion = emailSettings.lastIndexOf("UNION");
        return emailSettings.replace(lastIdxUnion, lastIdxUnion + 5, "").append(") tmp").toString();
    }

    @Override
    public List<Setting> listSettingsByProject(User loggedInUser, Integer company, Integer project)
            throws SQLException {
        logger.debug("listSettingsByProject::START");
        RoleEntContext listCtx = AuthorizationManager.getRoleEntContext(loggedInUser.getUserEntitlements(),
                new UserEntitlement(Resource.setting, Action.list));
        String ctxObjStr = loggedInUser.getContextScopeStr().get(listCtx);
        String ctxFilter = "";
        if (listCtx.equals(RoleEntContext.ALL)) {
            ctxFilter = "and (s.location is null)";
        } else {
            ctxFilter = "and (s.location is null or s." + getCtxFilter(listCtx) + ")";
        }
        ArrayList<Setting> settings = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement("select s.* from sb_settings s \n"
                + "where s.company = ? \n"
                + "and s.project = ?");
        ps.setInt(1, company);
        ps.setInt(2, project);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Setting s = getSettingObj(rs);
            setSettingAuditable(rs, s);
            settings.add(s);
        }
        close(rs);
        close(ps);
        logger.debug("listSettingsByProject::END");
        return settings;
    }

    @Override
    public List<Setting> listEmailSettingsByProject(User loggedInUser, Integer company, Integer project) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateSettingValues(Setting setting)
            throws SQLException {
        logger.debug("updateSettingValues::START::" + setting);
        PreparedStatement ps = conn.prepareStatement("update sb_settings\n"
                + "set value = ?, \n"
                + updateAuditableLast
                + "where id = ?");
        ps.setString(1, setting.getValue());
        ps.setInt(2, setting.getLastUpdatedBy());
        ps.setInt(3, setting.getId());
        ps.executeUpdate();
        close(ps);
        logger.debug("updateSettingValues::End");
    }

    @Override
    public void updateCompany(Company company)
            throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void disableCompany(Company company)
            throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Project> listProjects(Integer company, User user)
            throws SQLException {
        logger.debug("listProjects::START::" + company);
        String query = "";
        boolean getAll = false;
//        Integer id = 0;
        if (RoleEntContext.ALL.equals(AuthorizationManager.getRoleEntContext(
                user.getUserEntitlements(), new UserEntitlement(Resource.project, Action.list)))) {
            getAll = true;
            if (null == company || company == 0) {
                query = "select c.name as company_name, p.* from sb_projects p \n"
                        + "left join sb_companies c on p.company = c.id";
                logger.debug("listProjects::listingAllProjects");
            } else {
                query = "select c.name as company_name, p.* from sb_projects p\n"
                        + "left join sb_companies c on p.company = c.id \n"
                        + "where p.company = ?";
                logger.debug("listProjects::listingProjectsOfSelectedCompany");
            }
        } else {
            getAll = false;
//            query = "select c.name as company_name, p.* from \n"
//                    + "sb_projects p,\n"
//                    + "sb_users u,\n"
//                    + "sb_companies c,\n"
//                    + "sb_user_proj_loc_mapping upm\n"
//                    + "where p.id = upm.project\n"
//                    + "and upm.user = u.id\n"
//                    + "and c.id = p.company\n"
//                    + "and u.id = ?";
//            id = user.getId();
            logger.debug("listProjects::listingProjectsOfLoggedInUser");
        }

        List<Project> projs = new ArrayList<>();
        if (getAll) {
            PreparedStatement ps;
            ps = conn.prepareStatement(query);
            if (company != null && company > 0) {
                ps.setInt(1, company);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                projs.add(getProjectObj(rs));
            }
            close(rs);
            close(ps);
        } else {
            projs = listProjectsByContext(user, new UserEntitlement(Resource.project, Action.list));
        }
        logger.debug("listProjects::END");
        return projs;
    }

    @Override
    public List<User> getUsersOfCompany(Company company)
            throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer createCompany(Company company)
            throws SQLException {
        logger.debug("createCompany::START::" + company.getName());
        PreparedStatement ps = conn.prepareStatement("INSERT INTO sb_companies \n"
                + "(name, description, url, \n"
                + "created_by, last_updated_by) \n"
                + "VALUES (?, ?, ?, \n"
                + "?, ?)");
        ps.setString(1, company.getName());
        ps.setString(2, company.getDescription());
        ps.setString(3, company.getUrl());
        ps.setInt(4, company.getCreatedBy());
        ps.setInt(5, company.getLastUpdatedBy());
        ps.executeUpdate();
        Integer id = getLastInsertId();
        close(ps);
        logger.debug("createCompany::END");
        return id;
    }

    @Override
    public List<Company> getCompaniesStartingWith(String name)
            throws SQLException {
        logger.debug("getCompStartingWith::START::" + name);
        List<Company> companies = new ArrayList<>();

        PreparedStatement ps;
        ps = conn.prepareStatement(companiesQueryForCtx + "\n"
                + "where instr(lower(c.name), ?) > 0 \n"
                + "and c.active = 1");
        ps.setString(1, name.toLowerCase());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Company company = getCompanyObj(rs);
            companies.add(company);
        }
        close(rs);
        close(ps);
        logger.debug("getCompStartingWith::END");
        return companies;
    }

    @Override
    public List<Project> getProjectsStartingWith(String name, Integer company, User loggedInUser)
            throws SQLException {
        logger.debug("getProjStartingWith::START::" + name);
        List<Project> projects = new ArrayList<>();
        RoleEntContext listCtx = AuthorizationManager.getRoleEntContext(loggedInUser.getUserEntitlements(),
                new UserEntitlement(Resource.user, Action.list));
        String ctxObjStr = loggedInUser.getContextScopeStr().get(listCtx);
        PreparedStatement ps;
        ps = conn.prepareStatement(getQueryWithCtxFilter(projectsQueryForCtx, listCtx).replace("?", ctxObjStr) + "\n"
                + "and instr(lower(p.name), ?) > 0 \n"
                + "and p.active = 1 \n"
                + "and p.company = ? ");
        ps.setString(1, name.toLowerCase());
        ps.setInt(2, company);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Project project = getProjectObj(rs);
            projects.add(project);
        }
        close(rs);
        close(ps);
        logger.debug("getProjStartingWith::END");
        return projects;
    }

    @Override
    public List<Location> getLocationsStartingWith(String name, Integer project, User loggedInUser)
            throws SQLException {
        logger.debug("getLocStartingWith::START::" + name);
        List<Location> locations = new ArrayList<>();
        RoleEntContext listCtx = AuthorizationManager.getRoleEntContext(loggedInUser.getUserEntitlements(),
                new UserEntitlement(Resource.user, Action.list));
        String ctxObjStr = loggedInUser.getContextScopeStr().get(listCtx);
        PreparedStatement ps;
        ps = conn.prepareStatement(getQueryWithCtxFilter(locationsQueryForCtx, listCtx).replace("?", ctxObjStr) + "\n"
                + "and instr(lower(l.name), ?) > 0 \n"
                + "and l.active = 1\n"
                + "and l.project = ? ");
        ps.setString(1, name.toLowerCase());
        ps.setInt(2, project);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Location location = getLocationObj(rs);
            locations.add(location);
        }
        close(rs);
        close(ps);
        logger.debug("getLocStartingWith::END");
        return locations;
    }

    @Override
    public List<Company> listCompaniesByContext(User loggedInUser, UserEntitlement ctxEnt)
            throws SQLException {
        logger.debug("listCompaniesByContext::START");
        RoleEntContext ctx = getRoleEntContext(loggedInUser.getUserEntitlements(), ctxEnt);
        String ctxObjStr = loggedInUser.getContextScopeStr().get(ctx);
        String query = getQueryWithCtxFilter(companiesQueryForCtx, ctx).replace("?", ctxObjStr);
        List<Company> companies = getCompanies(query);
        logger.debug("listCompaniesByContext::END");
        return companies;
    }

    public List<Company> getCompanies(String query)
            throws SQLException {
        List<Company> companies = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Company c = getCompanyObj(rs);
            setAuditable(rs, c);
            companies.add(c);
        }
        close(rs);
        close(ps);
        logger.debug("getCompanies::END");
        return companies;
    }

    @Override
    public List<Project> listProjectsByContext(User loggedInUser, UserEntitlement ctxEnt)
            throws SQLException {
        logger.debug("listProjectsByContext::START");
        RoleEntContext ctx = getRoleEntContext(loggedInUser.getUserEntitlements(), ctxEnt);
        String ctxObjStr = loggedInUser.getContextScopeStr().get(ctx);
        String query = getQueryWithCtxFilter(projectsQueryForCtx, ctx).replace("?", ctxObjStr);
        List<Project> projects = getProjects(query);
        logger.debug("listProjectByContext::END");
        return projects;
    }

    @Override
    public List<Project> listProjectForCustSalesrep(Integer customer)
            throws SQLException {
        logger.debug("listProjectForCustSalesrep::START::" + customer);
        List<Project> projects = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement(projectsQueryForCtx + "\n"
                + "where p.id in (select csr.project \n"
                + "from sb_customer_sales_rep csr \n"
                + "where csr.customer = ?)");
        ps.setInt(1, customer);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Project p = getProjectObj(rs);
            projects.add(p);
        }
        close(rs);
        close(ps);
        logger.debug("listProjectForCustSalesrep::END");
        return projects;
    }

    @Override
    public List<Project> listProjectByCustomer(Integer customer)
            throws SQLException {
        logger.debug("listProjectByCustomer::START::" + customer);
        List<Project> projects = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement(projectsQueryForCtx + "\n"
                + "where p.id in (select cv.project \n"
                + "from sb_customer_visits cv \n"
                + "where cv.customer = ?)");

        ps.setInt(1, customer);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Project p = getProjectObj(rs);
            projects.add(p);
        }
        close(rs);
        close(ps);
        logger.debug("listProjectByCustomer::END");
        return projects;
    }

    public List<Project> getProjects(String query)
            throws SQLException {
        List<Project> projects = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Project c = getProjectObj(rs);
            setAuditable(rs, c);
            projects.add(c);
        }
        close(rs);
        close(ps);
        logger.debug("getCompanies::END");
        return projects;
    }

    @Override
    public List<Location> listLocationsByContext(User loggedInUser, UserEntitlement ctxEnt)
            throws SQLException {
        logger.debug("listLocationsByContext::START");
        RoleEntContext ctx = getRoleEntContext(loggedInUser.getUserEntitlements(), ctxEnt);
        String ctxObjStr = loggedInUser.getContextScopeStr().get(ctx);
        String query = getQueryWithCtxFilter(locationsQueryForCtx, ctx).replace("?", ctxObjStr);
        List<Location> locations = getLocations(query);
        logger.debug("listCompaniesByContext::END");
        return locations;
    }

    public List<Location> getLocations(String query)
            throws SQLException {
        List<Location> locations = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Location c = getLocationObj(rs);
            setAuditable(rs, c);
            locations.add(c);
        }
        close(rs);
        close(ps);
        logger.debug("getCompanies::END");
        return locations;
    }

    @Override
    public Integer createProject(Project project)
            throws SQLException {
        logger.debug("createProject::START::" + project.getName());
        PreparedStatement ps = conn.prepareStatement("INSERT INTO sb_projects \n"
                + "(name, description, url, coordinates, \n"
                + "company, created_by, last_updated_by) \n"
                + "VALUES (?, ?, ?, ?, \n"
                + "?, ?, ?)");
        ps.setString(1, project.getName());
        ps.setString(2, project.getDescription());
        ps.setString(3, project.getUrl());
        ps.setString(4, project.getCoordinates());
        ps.setInt(5, project.getCompany());
        ps.setInt(6, project.getCreatedBy());
        ps.setInt(7, project.getLastUpdatedBy());
        ps.executeUpdate();
        Integer id = getLastInsertId();
        project.setId(id);
        createDummyLocation(project);
        close(ps);
        logger.debug("createProject::END");
        return id;
    }

    private void createDummyLocation(Project project) throws SQLException {
        logger.debug("createDummyLocation::START::" + project.getName());
        PreparedStatement ps = conn.prepareStatement("INSERT INTO sb_location \n"
                + "(id, project, name, \n"
                + "created_by, last_updated_by) \n"
                + "VALUES (?, ?, ?, \n"
                + "?, ?)");
        ps.setInt(1, project.getId());
        ps.setInt(2, project.getId());
        ps.setString(3, project.getName());
        ps.setInt(4, project.getCreatedBy());
        ps.setInt(5, project.getLastUpdatedBy());
        ps.executeUpdate();
        close(ps);
        logger.debug("createDummyLocation::END");
    }

    @Override
    public void assignUsersToProject(List<User> users, Integer projectId, User loggedInUser) throws SQLException {
        RoleEntContext projectAssignUser = AuthorizationManager.getRoleEntContext(loggedInUser.getUserEntitlements(),
                new UserEntitlement(Resource.project, Action.assign_user));
        List<ContextObj> ctxObjs = loggedInUser.getContextScope().get(projectAssignUser);
        Integer ctxObjId = 0;
        for (User u : users) {
            List<Project> assignedProjects = getProjectsAssignedToUser(u.getId());
            boolean existingProject = isUserAssignedToProject(projectId, assignedProjects);
            if (!existingProject) {
                ctxObjId = RoleEntContext.COMPANY.equals(projectAssignUser) ? u.getCompany() : projectId;
                if (AuthorizationManager.authorizeContextObj(projectAssignUser, ctxObjs, ctxObjId)) {
                    assignUserToProject(u, projectId);
                } else {
                    throw new RuntimeException("You are not authorized to Assign 1 or more users to this project");
                }
            }
        }
    }

    @Override
    public List<Project> getProjectsAssignedToUser(Integer user) throws SQLException {
        logger.debug("getProjectAssignedToUser::START::" + user);
        List<Project> projs = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement("select c.name as company_name, p.* from sb_projects p\n"
                + "left join sb_companies c on p.company = c.id\n"
                + "left join sb_user_proj_loc_mapping upm on p.id = upm.project\n"
                + "where upm.active = 1\n"
                + "and p.active = 1\n"
                + "and upm.user = ?");
        ps.setInt(1, user);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            projs.add(getProjectObj(rs));
        }
        close(rs);
        close(ps);
        logger.debug("getProjectAssignedToUser::END");
        return projs;
    }

    private boolean isUserAssignedToProject(Integer project, List<Project> assignedProjects) {
        for (Project p : assignedProjects) {
            if (p.getId().intValue() == project.intValue()) {
                return true;
            }
        }
        return false;
    }

    private void assignUserToProject(User user, Integer project) throws SQLException {
        logger.debug("assignUserToProject::START::" + project);
        PreparedStatement ps = conn.prepareStatement("INSERT INTO sb_user_proj_loc_mapping \n"
                + "(project, location, user, \n"
                + "created_by, last_updated_by) \n"
                + "VALUES (?, ?, ?, \n"
                + "?, ?)");
        ps.setInt(1, project);
        ps.setInt(2, project);
        ps.setInt(3, user.getId());
        ps.setInt(4, user.getCreatedBy());
        ps.setInt(5, user.getLastUpdatedBy());
        ps.executeUpdate();
        close(ps);
        logger.debug("assignUserToProject::END");
    }

    @Override
    public void updateProject(Project project) throws SQLException {
        logger.debug("updateProject::START::" + project.getName());
        PreparedStatement ps = conn.prepareStatement("update sb_projects\n"
                + "set name = ?, \n"
                + "description = ?, \n"
                + "url = ?, \n"
                + "coordinates = ?, \n"
                + "active = ?, \n"
                + updateAuditableLast
                + "where id = ?");
        ps.setString(1, project.getName());
        ps.setString(2, project.getDescription());
        ps.setString(3, project.getUrl());
        ps.setString(4, project.getCoordinates());
        ps.setInt(5, project.getActive());
        ps.setInt(6, project.getLastUpdatedBy());
        ps.setInt(7, project.getId());
        ps.executeUpdate();
        close(ps);
        logger.debug("updateProject::End");
    }

}
