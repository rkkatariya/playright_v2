/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.dao;

import com.revvster.playright.model.LOVType;
import com.revvster.playright.model.Setting;

import com.revvster.playright.model.Email;

import com.revvster.playright.model.RoleEntContext;

import com.revvster.playright.model.Data;


import com.revvster.playright.model.LOVAttribute;


import com.revvster.playright.model.ContextObj;
import com.revvster.playright.model.ListOfValue;
import com.revvster.playright.model.Location;
import com.revvster.playright.model.Company;
import com.revvster.playright.model.DataFilter;
import com.revvster.playright.model.UserEntitlement;
import com.revvster.playright.model.Project;
import com.revvster.playright.model.Role;
import com.revvster.playright.model.User;

import com.revvster.playright.model.Settings;

import com.revvster.playright.access.Action;
import com.revvster.playright.access.Resource;
import com.revvster.playright.access.AuthorizationManager;

import com.revvster.playright.util.DBConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rahul
 */
public class DaoHelper {

    public static final String updateAuditableLast = "last_updated_by = ?,\n"
            + "last_updated_on = now()\n";

    private static final Logger logger = LogManager.getLogger(DaoHelper.class.getName());
    protected Connection conn;

    public static final String listEmailEventsQuery = "select ifnull(ees.id, 0) as subscription_id, ees.company, ees.project, ee.id,\n"
            + "ifnull(ees.active, 0) as active, ee.name, ee.`type` as type, ee.description \n"
            + "from (select eei.* from sb_email_events eei where eei.active = 1) ee\n"
            + "left join sb_email_event_subscription ees \n"
            + "on ee.id = ees.email_event and ees.project = ? \n"
            + "and ees.company = (select p.company from sb_projects p where p.id = ees.project) ";

    public DaoHelper() {
        conn = DBConnectionManager.getConnection();
        logger.debug("getConnection::" + conn.toString());
    }

    public void close() {
        if (conn != null) {
            try {
                conn.close();
                logger.debug("closeConnection::" + conn.toString());
            } catch (SQLException ex) {
                logger.fatal("There was an error closing database connection." + ex);
            }
        }
    }

    /**
     * Closes the current resultset
     *
     * @param ps Statement
     */
    public void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignore) {
            }
        }
    }

    /**
     * Closes the current statement
     *
     * @param ps Statement
     */
    public void close(Statement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException ignore) {
            }
        }
    }

    public String getFullName(Integer id)
            throws SQLException {
        logger.debug("getFullName::ByID::START::" + id);
        PreparedStatement ps = null;
        String fn = "";
        ps = conn.prepareStatement("select \n"
                + "concat(concat(first_name, ' '), last_name) as full_name\n"
                + "from sb_users\n"
                + "where id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            fn = rs.getString("full_name");
        }
        close(rs);
        close(ps);
        logger.debug("getFullName::ByID::END");
        return fn;
    }

    public Integer getLastInsertId()
            throws SQLException {
        logger.debug("getLastInsertId::START");
        PreparedStatement ps = null;
        Integer id = 0;
        ps = conn.prepareStatement("select LAST_INSERT_ID() as id");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            id = rs.getInt("id");
        }
        logger.debug("getLastInsertId::ID::" + id);
        close(rs);
        close(ps);
        logger.debug("getLastInsertId::END");
        return id;
    }

    public RoleEntContext getRoleEntContext(List<UserEntitlement> userEntitlements,
            UserEntitlement userEntitlement) {
        return AuthorizationManager.getRoleEntContext(userEntitlements, userEntitlement);
    }

    public Role getRoleObj(ResultSet rs)
            throws SQLException {
        Role role = new Role();
        role.setId(rs.getInt("id"));
        role.setRawName(rs.getString("raw_name"));
        role.setName(rs.getString("name"));
        role.setDescription(rs.getString("description"));
        return role;
    }

    public DataFilter getDataFilterObj(ResultSet rs)
            throws SQLException {
        DataFilter datafilter = new DataFilter();
        datafilter.setCustomer(rs.getInt("customer"));
        datafilter.setProject(rs.getInt("project"));
        datafilter.setSalesRep(rs.getInt("sales_rep"));
        datafilter.setEmailAddress(rs.getString("email_address"));
        datafilter.setMobileNo(rs.getString("mobile_no"));
        datafilter.setCustomerName(rs.getString("customer_name"));
        datafilter.setSalesRepName(getFullName(rs.getInt("sales_rep")));
        datafilter.setVisitedOn(rs.getTimestamp("visited_on"));
        return datafilter;
    }


    public ListOfValue getLOVFromName(String name, LOVType type) throws SQLException {
        logger.debug("getLOV::START");
        PreparedStatement ps = null;
        ListOfValue lov = new ListOfValue();
        ps = conn.prepareStatement("select * from sb_lov where name = ? and active = 1 and type = ?");
        ps.setString(1, name);
        ps.setString(2, type.toString());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lov = getListOfValueObj(rs);
        }
        close(rs);
        close(ps);
        logger.debug("getLOV::END");
        return lov;
    }

    public ListOfValue getLOVFromId(Integer lovId) throws SQLException {
        logger.debug("getLOV::START");
        PreparedStatement ps = null;
        ListOfValue lov = new ListOfValue();
        ps = conn.prepareStatement("select * from sb_lov where id = ?");
        ps.setInt(1, lovId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lov = getListOfValueObj(rs);
        }
        close(rs);
        close(ps);
        logger.debug("getLOV::END");
        return lov;
    }

    public ListOfValue getLOVFromValue(String value, LOVType type) throws SQLException {
        logger.debug("getLOV::START");
        PreparedStatement ps = null;
        ListOfValue lov = new ListOfValue();
        ps = conn.prepareStatement("select * from sb_lov where value = ? and active = 1 and type = ?");
        ps.setString(1, value);
        ps.setString(2, type.toString());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lov = getListOfValueObj(rs);
        }
        close(rs);
        close(ps);
        logger.debug("getLOV::END");
        return lov;
    }

    public User getUserObj(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmailAddress(rs.getString("email_address"));
        user.setManager(rs.getInt("manager"));
        user.setMobileNo(rs.getString("mobile_no"));
        user.setLastLogin(rs.getTimestamp("last_login"));
        user.setActive(rs.getInt("active"));
        user.setJobTitle(rs.getString("job_title"));
        user.setDepartment(rs.getString("department"));
        user.setAltPhoneNo(rs.getString("alt_phone_no"));
        user.setGender(rs.getString("gender"));
        user.setCompanyName(rs.getString("company_name"));
        user.setCompany(rs.getInt("company"));
        user.setRoleName(rs.getString("role_name"));
        user.setRoleRawName(rs.getString("role_raw_name"));
        user.setRole(rs.getInt("role"));
        user.setResetPassword(rs.getString("reset_password"));
        return user;
    }
    
    public Data getDataObj(ResultSet rs) throws SQLException {
        Data data = new Data();
        data.setId(rs.getInt("id"));
        data.setNewsDate(rs.getTimestamp("news_date"));
        data.setNewsPaper(rs.getString("news_paper"));
        data.setEdition(rs.getString("edition"));
        data.setHeadline(rs.getString("headline"));
        data.setHeight(rs.getInt("height"));
        data.setWidth(rs.getInt("width"));
        data.setLanguage(rs.getString("language"));
        data.setPageNo(rs.getInt("page_no"));
        data.setQuantitativeAVE(rs.getInt("quantitative_AVE"));
        data.setSource(rs.getString("source"));
        data.setSupplement(rs.getString("supplement"));
        data.setTotalArticleSize(rs.getInt("total_article_size"));
        data.setCirculationFigure(rs.getInt("circulation_figure"));
        data.setImageFileName(rs.getString("image_filename"));
        data.setImageExists(rs.getString("image_exists"));
        data.setJournalistFactor(rs.getInt("journalist_factor")); 
        data.setCreatedBy(rs.getInt("created_by"));
        data.setLastUpdatedBy(rs.getInt("last_updated_by"));
        data.setFileType(rs.getString("file_type"));
        data.setFileSize(rs.getInt("file_size"));
        data.setActive(rs.getInt("active"));
        data.setLastUpdatedOn(rs.getTimestamp("last_updated_on"));
        return data;
    }

    //Dont get this for list of users
    public void setAuditable(ResultSet rs, User user)
            throws SQLException {
        user.setCreatedOn(rs.getTimestamp("created_on"));
        user.setCreatedBy(rs.getInt("created_by"));
        user.setCreatedByName(getFullName(rs.getInt("created_by")));
        user.setLastUpdatedBy(rs.getInt("last_updated_by"));
        user.setLastUpdatedOn(rs.getTimestamp("last_updated_on"));
        user.setLastUpdatedByName(getFullName(rs.getInt("last_updated_by")));
    }

    public UserEntitlement getUserEntitlementObj(ResultSet rs)
            throws SQLException {
        UserEntitlement ue = new UserEntitlement();
        ue.setId(rs.getInt("id"));
        ue.setEntitlement(rs.getInt("entitlement"));
        ue.setResource(Resource.valueOf(rs.getString("resource")));
        ue.setAction(Action.valueOf(rs.getString("action")));
        ue.setRoleEntContext(RoleEntContext.valueOf(rs.getString("context")));
        return ue;
    }

    public Company getCompanyObj(ResultSet rs)
            throws SQLException {
        Company c = new Company();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setDescription(rs.getString("description"));
        c.setUrl(rs.getString("url"));
        c.setBusinessOwner(rs.getInt("business_owner"));
        c.setActive(rs.getInt("active"));
        return c;
    }

    public void setAuditable(ResultSet rs, Company c)
            throws SQLException {
        c.setCreatedOn(rs.getTimestamp("created_on"));
        c.setCreatedBy(rs.getInt("created_by"));
        c.setCreatedByName(getFullName(rs.getInt("created_by")));
        c.setLastUpdatedBy(rs.getInt("last_updated_by"));
        c.setLastUpdatedOn(rs.getTimestamp("last_updated_on"));
        c.setLastUpdatedByName(getFullName(rs.getInt("last_updated_by")));
    }

    public Project getProjectObj(ResultSet rs)
            throws SQLException {
        Project p = new Project();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setCoordinates(rs.getString("coordinates"));
        p.setUrl(rs.getString("url"));
        p.setBusinessOwner(rs.getInt("business_owner"));
        p.setActive(rs.getInt("active"));
        p.setCompany(rs.getInt("company"));
        p.setCompanyName(rs.getString("company_name"));
        return p;
    }

    public void setAuditable(ResultSet rs, Project p)
            throws SQLException {
        p.setCreatedOn(rs.getTimestamp("created_on"));
        p.setCreatedBy(rs.getInt("created_by"));
        p.setCreatedByName(getFullName(rs.getInt("created_by")));
        p.setLastUpdatedBy(rs.getInt("last_updated_by"));
        p.setLastUpdatedOn(rs.getTimestamp("last_updated_on"));
        p.setLastUpdatedByName(getFullName(rs.getInt("last_updated_by")));
    }

    public Location getLocationObj(ResultSet rs)
            throws SQLException {
        Location l = new Location();
        l.setId(rs.getInt("id"));
        l.setName(rs.getString("name"));
        l.setDescription(rs.getString("description"));
        l.setUrl(rs.getString("url"));
        l.setBusinessOwner(rs.getInt("business_owner"));
        l.setActive(rs.getInt("active"));
        l.setProject(rs.getInt("project"));
        l.setProjectName(rs.getString("project_name"));
        return l;
    }

    public void setAuditable(ResultSet rs, Location l)
            throws SQLException {
        l.setCreatedOn(rs.getTimestamp("created_on"));
        l.setCreatedBy(rs.getInt("created_by"));
        l.setCreatedByName(getFullName(rs.getInt("created_by")));
        l.setLastUpdatedBy(rs.getInt("last_updated_by"));
        l.setLastUpdatedOn(rs.getTimestamp("last_updated_on"));
        l.setLastUpdatedByName(getFullName(rs.getInt("last_updated_by")));
    }

    public Setting getSettingObj(ResultSet rs)
            throws SQLException {
        Setting s = new Setting();
        s.setId(rs.getInt("id"));
        s.setName(rs.getString("name"));
        if (rs.getString("parameter") == null) {
            s.setParameter(Settings.valueOf(rs.getString("param")));
        } else {
            s.setParameter(Settings.valueOf(rs.getString("parameter")));
        }
        s.setValue(rs.getString("value"));
        s.setDescription(rs.getString("description"));
        s.setCompany(rs.getInt("company"));
        s.setProject(rs.getInt("project"));
        s.setLocation(rs.getInt("location"));
        return s;
    }

    public void setSettingAuditable(ResultSet rs, Setting s)
            throws SQLException {
        s.setCreatedOn(rs.getTimestamp("created_on"));
        s.setCreatedBy(rs.getInt("created_by"));
        s.setCreatedByName(getFullName(rs.getInt("created_by")));
        s.setLastUpdatedBy(rs.getInt("last_updated_by"));
        s.setLastUpdatedOn(rs.getTimestamp("last_updated_on"));
        s.setLastUpdatedByName(getFullName(rs.getInt("last_updated_by")));
    }


    public User getUserCtxObj(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmailAddress(rs.getString("email_address"));
        user.setManager(rs.getInt("manager"));
        user.setMobileNo(rs.getString("mobile_no"));
        user.setLastLogin(rs.getTimestamp("last_login"));
        user.setActive(rs.getInt("active"));
        user.setCompany(rs.getInt("company"));
        return user;
    }

    public ContextObj getContextObj(RoleEntContext ctx, ResultSet rs)
            throws SQLException {
        ContextObj ctxObj = null;
        switch (ctx) {
            case ALL:
                ctxObj = null;
                break;
            case COMPANY:
                ctxObj = getCompanyObj(rs);
                break;
            case PROJECT:
                ctxObj = getProjectObj(rs);
                break;
            case LOCATION:
                ctxObj = getLocationObj(rs);
                break;
            case SELF:
                ctxObj = getUserCtxObj(rs);
                break;
            default:
                ctxObj = null;
        }
        return ctxObj;
    }

    public String getCtxFilter(RoleEntContext ctx) {
        String filter = "";
        switch (ctx) {
            case ALL:
                filter = "1 = 1";
                break;
            case COMPANY:
                filter = "company in (?)";
                break;
            case PROJECT:
                filter = "project in (?)";
                break;
            case LOCATION:
                filter = "location in (?)";
                break;
            case SELF:
                filter = "user in (?)";
                break;
            default:
                filter = "id in (?)";
        }
        logger.debug("getCtxFilter::" + ctx + "::" + filter);
        return filter;
    }

    public String getQueryWithCtxFilter(String query, RoleEntContext ctx) {
        return query + " having " + getCtxFilter(ctx);
    }

    public Integer lovCountByTypeUpdatedAfter(LOVType type, Timestamp ludate)
            throws SQLException {
        logger.debug("lovCountByTypeUpdatedAfter::START");
        Integer updatedRows = 0;
        PreparedStatement ps;
        ps = conn.prepareStatement("select count(*) as updated_rows from sb_lov \n "
                + "where active = 1 and type = ? \n"
                + "and last_updated_on > ? ");
        ps.setString(1, type.toString());
        ps.setTimestamp(2, ludate);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            updatedRows = rs.getInt("updated_rows");
        }
        close(rs);
        close(ps);
        return updatedRows;
    }

    public List<ListOfValue> getListOfValues(LOVType type)
            throws SQLException {
        logger.debug("getListOfValues::START");
        PreparedStatement ps = null;
        List<ListOfValue> lovs = new ArrayList<>();
        String query = "select * from sb_lov where active = 1 and type = ?";
        ps = conn.prepareStatement(query);
        ps.setString(1, type.toString());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ListOfValue lov = getListOfValueObj(rs);
            lov.setAttributes(getLOVAttributes(lov.getId()));
            lovs.add(lov);
        }
        close(rs);
        close(ps);
        logger.debug("getListOfValues::END");
        return lovs;
    }

    public List<ListOfValue> getListOfValuesByContext(LOVType type, ContextObj ctxObj, Integer ctxId)
            throws SQLException {
        logger.debug("getListOfValues::START");
        PreparedStatement ps = null;
        List<ListOfValue> lovs = new ArrayList<>();
        String query = "select * from sb_lov lov";
        String ctxFilter = "1 = ?";
        if (ctxObj != null) {
            if (ctxObj instanceof Company) {
                ctxFilter = "where lov.company = ? \n";
            } else if (ctxObj instanceof Project) {
                ctxFilter = ", sb_projects p\n"
                        + "where ((lov.project = p.id)\n"
                        + "or (lov.company = p.company and lov.project is null))\n"
                        + "and p.id = ? \n";
            } else if (ctxObj instanceof Location) {
                ctxFilter = ",sb_projects p, \n"
                        + "sb_location l\n"
                        + "where l.project = p.id\n"
                        + "and ((lov.location = l.id)\n"
                        + "or (lov.project = p.id and lov.location is null)\n"
                        + "or (lov.company = p.company and lov.project is null and lov.location is null))\n"
                        + "and l.id = ? \n";
            }
        } else {
            ctxFilter = "where lov.company is null \n"
                    + "and lov.project is null \n"
                    + "and lov.location is null \n"
                    + "and 0 = ? \n";
        }
        String lovFilter = "and lov.type = ?";
        ps = conn.prepareStatement(query + ctxFilter + lovFilter);
        ps.setInt(1, ctxId);
        ps.setString(2, type.toString());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ListOfValue lov = getListOfValueObj(rs);
            lov.setAttributes(getLOVAttributes(lov.getId()));
            lovs.add(lov);
        }
        close(rs);
        close(ps);
        logger.debug("getListOfValues::END");
        return lovs;
    }

    public ListOfValue getListOfValueObj(ResultSet rs)
            throws SQLException {
        ListOfValue lov = new ListOfValue();
        lov.setId(rs.getInt("id"));
        lov.setCompany(rs.getInt("company"));
        lov.setProject(rs.getInt("project"));
        lov.setLocation(rs.getInt("location"));
        lov.setType(LOVType.valueOf(rs.getString("type")));
        lov.setName(rs.getString("name"));
        lov.setValue(rs.getString("value"));
        lov.setActive(rs.getInt("active"));
        return lov;
    }

    public List<LOVAttribute> getLOVAttributes(Integer lovId)
            throws SQLException {
        logger.debug("getLOVAttributes::START");
        PreparedStatement ps = null;
        List<LOVAttribute> lovAttrs = new ArrayList<>();
        String query = "select * from sb_lov_attr where active = 1 and lov = ?";
        ps = conn.prepareStatement(query);
        ps.setInt(1, lovId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lovAttrs.add(getLOVAttributesObj(rs));
        }
        close(rs);
        close(ps);
        logger.debug("getLOVAttributes::END");
        return lovAttrs;
    }

    public LOVAttribute getLOVAttributesObj(ResultSet rs)
            throws SQLException {
        LOVAttribute attr = new LOVAttribute();
        attr.setId(rs.getInt("id"));
        attr.setName(rs.getString("name"));
        attr.setValue(rs.getString("value"));
        attr.setActive(rs.getInt("active"));
        return attr;
    }

    public Email getEmailObj(ResultSet rs)
            throws SQLException {
        Email e = new Email();
        e.setCompany(rs.getInt("company"));
        e.setProject(rs.getInt("project"));
        e.setLocation(rs.getInt("location"));
        e.setRequestedBy(rs.getInt("requested_by"));
        e.setFromId(rs.getInt("from_id"));
        e.setFromName(getFullName(rs.getInt("from_id")));
        e.setToId(rs.getInt("to_id"));
        e.setTemplate(rs.getInt("template"));
        e.setFrom(rs.getString("from"));
        e.setTo(rs.getString("to"));
        e.setCc(rs.getString("cc"));
        e.setBcc(rs.getString("bcc"));
        e.setSubject(rs.getString("subject"));
        e.setBody(rs.getString("body"));
        e.setStatus(rs.getString("status"));
        e.setSource(rs.getString("source"));
        e.setSentOn(rs.getTimestamp("sent_on"));
        return e;
    }

    public String getInClause(String[] strArray) {
        StringBuilder inClause = new StringBuilder();
        int i = 0;
        int size = strArray.length;
        while (i < size) {
            inClause.append(strArray[i]);
            i++;
            if (i < size) {
                inClause.append("', '");
            }
        }
        if (size == 0) {
            return "";
        } else {
            return "'" + inClause.toString() + "'";
        }
    }

}
