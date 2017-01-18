/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.dao;

import com.revvster.playright.access.Action;
import com.revvster.playright.access.Resource;
import com.revvster.playright.model.ContextObj;
import com.revvster.playright.model.Role;
import com.revvster.playright.model.RoleEntContext;
import com.revvster.playright.model.User;
import com.revvster.playright.model.UserEntitlement;
import com.revvster.playright.util.AuthenticationManager;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rahul
 */
public class UserDaoImpl extends DaoHelper implements UserDao {

    private static final Logger logger = LogManager.getLogger(UserDaoImpl.class.getName());

    private static final String getUserQuery = "select r.name as role_name, r.id as role, r.raw_name as role_raw_name,\n"
            + "concat(concat(m.first_name, ' '), m.last_name) as manager_name, \n"
            + "c.name as company_name, u.*\n"
            + "from sb_users u\n"
            + "left join sb_user_role_mapping urm on u.id = urm.user\n"
            + "left join sb_roles r on urm.role = r.id\n"
            + "left outer join sb_users m on u.manager = m.id\n"
            + "left outer join sb_companies c on u.company = c.id\n"
            + "where urm.active = 1\n"
            + "and r.active = 1\n";

    private static final String usersQueryForCtx = "select distinct upl.project, upl.location, \n"
            + "usr.id as user, usr.* \n "
            + "from sb_users usr \n"
            + "left join sb_user_proj_loc_mapping upl on usr.id = upl.user";

    private static final String fetchColumns = "r.name as role_name, r.id as role, r.raw_name as role_raw_name, \n"
            + "concat(concat(m.first_name, ' '), m.last_name) as manager_name, \n"
            + "c.name as company_name, u.* ";

    private static final String defaultJoins = "left join sb_user_role_mapping urm on u.id = urm.user \n"
            + "left join sb_roles r on urm.role = r.id \n"
            + "left outer join sb_users m on u.manager = m.id \n"
            + "left outer join sb_companies c on u.company = c.id";

    private static final String[] monthName = {"January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"};

    public UserDaoImpl() {
        super();
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void updateLastLogin(Integer userId)
            throws SQLException {
        logger.debug("updateLastLogin::START::" + userId);
        PreparedStatement ps = conn.prepareStatement("update sb_users\n"
                + "set last_login = NOW()\n"
                + "where id = ?");
        ps.setInt(1, userId);
        ps.executeUpdate();
        close(ps);
        logger.debug("updateLastLogin::End");
    }

    @Override
    public List<User> getAllUsers()
            throws SQLException {
        logger.debug("getAllUsers::START");
        List<User> users = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement("select r.name as role_name, r.id as role, r.raw_name as role_raw_name, \n"
                + "concat(concat(m.first_name, ' '), m.last_name) as manager_name, \n"
                + "c.name as company_name, u.* \n"
                + "from sb_users u \n"
                + "left join sb_user_role_mapping urm on u.id = urm.user\n"
                + "left join sb_roles r on urm.role = r.id \n"
                + "left outer join sb_users m on u.manager = m.id \n"
                + "left outer join sb_companies c on u.company = c.id");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User user = getUserObj(rs);
            user.setManagerName(rs.getString("manager_name"));
            users.add(user);
        }
        close(rs);
        close(ps);
        logger.debug("getAllUsers::END");
        return users;
    }

    @Override
    public Integer getTotalSale(Integer user)
            throws SQLException {
        logger.debug("getTotalSale::START");
        Integer totalSale = 0;
        PreparedStatement ps;
        ps = conn.prepareStatement("SELECT count(*) as totalSale FROM sb_catalog_items ci\n"
                + "left join sb_lov l on ci.state = l.id\n"
                + " where sales_rep = ?\n"
                + "and l.value = 'sold'");
        ps.setInt(1, user);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            totalSale = rs.getInt("totalSale");
        }
        close(rs);
        close(ps);
        logger.debug("getTotalSale::END");
        return totalSale;
    }

    @Override
    public Integer getPendingFollowup(Integer user)
            throws SQLException {
        logger.debug("getPendingFollowup::START");
        Integer followUp = 0;
        PreparedStatement ps;
        ps = conn.prepareStatement("select count(*) as pending from sb_notifications n \n"
                + "left join sb_lov l on n.status = l.id\n"
                + "where l.value = 'pending'\n"
                + "and n.user = ?");
        ps.setInt(1, user);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            followUp = rs.getInt("pending");
        }
        close(rs);
        close(ps);
        logger.debug("getPendingFollowup::END");
        return followUp;
    }

    @Override
    public Integer getCustomersAttendedForUser(Integer user) throws SQLException {
        logger.debug("getCustomersAttendedForUser::START");
        Integer customers = 0;
        PreparedStatement ps;
        ps = conn.prepareStatement("SELECT count(*) as cust_attended FROM sb_customer_sales_rep "
                + "where sales_rep = ?");
        ps.setInt(1, user);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            customers = rs.getInt("cust_attended");
        }
        close(rs);
        close(ps);
        logger.debug("getCustomersAttendedForUser::END");
        return customers;
    }

    @Override
    public List<User> getUsersOfCompany(Integer companyId)
            throws SQLException {
        logger.debug("getUsersOfCompany::START::CompanyId::" + companyId);
        List<User> users = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement("select r.name as role_name, r.id as role, r.raw_name as role_raw_name, \n"
                + "concat(concat(m.first_name, ' '), m.last_name) as manager_name, \n"
                + "c.name as company_name, u.* \n"
                + "from sb_users u \n"
                + "left join sb_user_role_mapping urm on u.id = urm.user\n"
                + "left join sb_roles r on urm.role = r.id \n"
                + "left outer join sb_users m on u.manager = m.id \n"
                + "left outer join sb_companies c on u.company = c.id\n"
                + "where u.company = ?");
        ps.setInt(1, companyId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User user = getUserObj(rs);
            user.setManagerName(rs.getString("manager_name"));
            users.add(user);
        }
        close(rs);
        close(ps);
        logger.debug("getUsersOfCompany::END");
        return users;
    }

    @Override
    public List<User> getAllReportees()
            throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User getUser(Integer id)
            throws SQLException {
        logger.debug("getUser::ByID::START::" + id);
        PreparedStatement ps = null;
        User user = new User();
        ps = conn.prepareStatement(getUserQuery + "and u.id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            user = getUserObj(rs);
            user.setManagerName(rs.getString("manager_name"));
            user.setLastLoginFrom(rs.getString("last_login_from"));
            setAuditable(rs, user);
        }
        close(rs);
        close(ps);
        logger.debug("getUser::ByID::END");
        return user;
    }

    @Override
    public User getUser(String username)
            throws SQLException {
        logger.debug("getUser::ByUSERNAME::START::" + username);
        PreparedStatement ps = null;
        User user = null;
        ps = conn.prepareStatement(getUserQuery + "and u.username = ?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            user = getUserObj(rs);
            user.setManagerName(rs.getString("manager_name"));
            user.setLastLoginFrom(rs.getString("last_login_from"));
            setAuditable(rs, user);
        }
        close(rs);
        close(ps);
        logger.debug("getUser::ByUSERNAME::END");
        return user;
    }

    @Override
    public User getUserFromMobileNo(String mobileNo)
            throws SQLException {
        logger.debug("getUser::ByMOBILENO::START::" + mobileNo);
        PreparedStatement ps = null;
        User user = null;
        ps = conn.prepareStatement(getUserQuery + "and u.mobile_no = ?");
        ps.setString(1, mobileNo);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            user = getUserObj(rs);
            user.setManagerName(rs.getString("manager_name"));
            user.setLastLoginFrom(rs.getString("last_login_from"));
            setAuditable(rs, user);
        }
        close(rs);
        close(ps);
        logger.debug("getUser::ByMOBILENO::END");
        return user;
    }

    @Override
    public void updateUser(User user)
            throws SQLException {
        logger.debug("updateUser::START::" + user.getUsername());
        PreparedStatement ps = conn.prepareStatement("update sb_users\n"
                + "set first_name = ?,\n"
                + "last_name = ?,\n"
                + "job_title = ?,\n"
                + "department = ?,\n"
                + "gender = ?,\n"
                + "mobile_no = ?,\n"
                + "manager = ?,\n"
                + "alt_phone_no = ?,\n"
                + updateAuditableLast
                + "where id = ?");
        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getLastName());
        ps.setString(3, user.getJobTitle());
        ps.setString(4, user.getDepartment());
        ps.setString(5, user.getGender());
        ps.setString(6, user.getMobileNo());
        if (user.getManager() != null && user.getManager() > 0) {
            ps.setInt(7, user.getManager());
        } else {
            ps.setNull(7, Types.INTEGER);
        }
        ps.setString(8, user.getAltPhoneNo());
        ps.setInt(9, user.getLastUpdatedBy());
        ps.setInt(10, user.getId());
        ps.executeUpdate();
        close(ps);
        logger.debug("updateUser::End");
    }

    @Override
    public void deleteUser(User user)
            throws SQLException {
        logger.debug("deleteUser::START::");
        PreparedStatement ps = conn.prepareStatement("update sb_users \n"
                + "set active = ? \n"
                + "where id = ? ");
        ps.setInt(1, user.getActive());
        //ps.setInt(2, user.getLastUpdatedBy());
        ps.setInt(2, user.getId());
        ps.executeUpdate();
        close(ps);
        logger.debug("deleteUser::END");
    }

    @Override
    public Integer createUser(User user)
            throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
        logger.debug("createUser::START::" + user.getUsername());
        PreparedStatement ps = conn.prepareStatement("INSERT INTO sb_users \n"
                + "(username, first_name, last_name, \n"
                + "email_address, mobile_no, alt_phone_no, \n"
                + "password, manager, \n"
                + "gender, created_by, last_updated_by, \n"
                + "job_title, department, company) \n"
                + "VALUES (?, ?, ?, \n"
                + "?, ?, ?, \n"
                + "?, ?, \n"
                + "?, ?, ?, \n"
                + "?, ?, ?)");
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getFirstName());
        ps.setString(3, user.getLastName());
        ps.setString(4, user.getEmailAddress());
        ps.setString(5, user.getMobileNo());
        ps.setString(6, user.getAltPhoneNo());
        ps.setString(7, user.getPassword());
        if (user.getManager() == null) {
            ps.setNull(8, Types.INTEGER);
        } else {
            ps.setInt(8, user.getManager());
        }
        ps.setString(9, user.getGender());
        ps.setInt(10, user.getCreatedBy());
        ps.setInt(11, user.getLastUpdatedBy());
        ps.setString(12, user.getJobTitle());
        ps.setString(13, user.getDepartment());
        if (user.getCompany() == null) {
            ps.setNull(14, Types.INTEGER);
        } else {
            ps.setInt(14, user.getCompany());
        }
        ps.executeUpdate();
        Integer id = getLastInsertId();
        if (id > 0) {
            user.setId(id);
            AuthenticationManager auth = new AuthenticationManager();
            auth.updateCredentials(conn, user.getUsername(), user.getPassword(), 1);
            addUserToRole(user, user.getRole());
        }
        close(ps);
        logger.debug("createUser::END");
        return id;
    }

    @Override
    public boolean updateUserPassword(String username, String password, Integer resetPwd)
            throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
        logger.debug("updateUserPassword::START::");
        AuthenticationManager auth = new AuthenticationManager();
        boolean updated = auth.updateCredentials(conn, username, password, resetPwd);
        logger.debug("updateUserPassword::END::");
        return updated;
    }

    @Override
    public void deleteUsers(List<User> users, Integer loggedInUserId)
            throws SQLException {
        logger.debug("deleteUsers::START::SIZE=" + users.size());
        PreparedStatement ps = conn.prepareStatement("update sb_users \n"
                + "set active = 0 \n"
                + updateAuditableLast
                + "where id in (?)");
        ps.setInt(1, loggedInUserId);
        ps.setString(2, getUserIdsAsString(users));
        ps.executeUpdate();
        close(ps);
        logger.debug("deleteUsers::END");
    }

    private String getUserIdsAsString(List<User> users) {
        StringBuilder userIds = new StringBuilder();
        int i = 0;
        int size = users.size();
        while (i < size) {
            userIds.append(users.get(i).getId());
            i++;
            if (i < size) {
                userIds.append(", ");
            }
        }
        return userIds.toString();
    }

    @Override
    public List<User> getManagersStartingWith(String name, Integer companyId)
            throws SQLException {
        logger.debug("getMgrsStartingWith::START::" + name);
        List<User> users = new ArrayList<>();
        PreparedStatement ps;
        String companyFilter = companyId > 0 ? "?" : "u.company";
        ps = conn.prepareStatement("select u.id, \n"
                + "concat(concat(u.first_name, ' '), u.last_name) as manager_name\n"
                + "from sb_users u\n"
                + "left join sb_user_role_mapping urm on u.id = urm.user\n"
                + "left join sb_roles r on urm.role = r.id\n"
                + "where r.raw_name = 'Manager'\n"
                + "and instr(lower(concat(concat(u.first_name, ' '), u.last_name)), ?) > 0 \n"
                + "and u.company = " + companyFilter);
        ps.setString(1, name.toLowerCase());
        if (companyId > 0) {
            ps.setInt(2, companyId);
        }
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setManagerName(rs.getString("manager_name"));
            users.add(user);
        }
        close(rs);
        close(ps);
        logger.debug("getMgrsStartingWith::END");
        return users;

    }

    @Override
    public List<User> getSalesRepForManager(Integer manager)
            throws SQLException {
        logger.debug("getSalesRepForManager::START");
        ArrayList<User> users = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement(getUserQuery
                + "and r.name = 'Sales Representative'\n"
                + "and u.manager = ?");
        ps.setInt(1, manager);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User user = getUserObj(rs);
            // setCustomerAuditable(rs, user);
            users.add(user);
        }
        close(rs);
        close(ps);
        logger.debug("getSalesRepForManager::END");
        return users;
    }

    @Override
    public boolean login(String username, String password)
            throws SQLException, NoSuchAlgorithmException {
        logger.debug("login::START::" + username);
        boolean authenticated = false;
        AuthenticationManager auth = new AuthenticationManager();
        authenticated = auth.authenticate(conn, username, password);
        logger.debug("login::authenticated::" + authenticated);
        logger.debug("login::END");
        return authenticated;
    }

    @Override
    public String basicAuthApi(String authCredentials)
            throws SQLException, NoSuchAlgorithmException {
        logger.debug("basicAuthApi::START");
        AuthenticationManager auth = new AuthenticationManager();
        String authenticated = auth.authenticateApi(conn, authCredentials);
        logger.info("basicAuthApi::Authenticated::" + authenticated);
        logger.debug("basicAuthApi::END");
        return authenticated;
    }

    @Override
    public List<Role> getUserRoles(Integer userId)
            throws SQLException {
        logger.debug("getRoleFromUser::START::" + userId);
        PreparedStatement ps = null;
        ps = conn.prepareStatement("select r.*\n"
                + "from sb_users u\n"
                + "left join sb_user_role_mapping urm on u.id = urm.user\n"
                + "left join sb_roles r on urm.role = r.id\n"
                + "where urm.active = 1\n"
                + "and r.active = 1\n"
                + "and u.id = ?");
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        List<Role> roles = new ArrayList<>();
        while (rs.next()) {
            roles.add(getRoleObj(rs));
        }
        close(rs);
        close(ps);
        logger.debug("getRoleFromUser::END");
        return roles;
    }

    @Override
    public List<Role> getUserRoles(String username)
            throws SQLException {
        logger.debug("getRoleFromUser::START::" + username);
        List<Role> roles = new ArrayList<>();
        PreparedStatement ps = null;
        ps = conn.prepareStatement("select r.*\n"
                + "from sb_users u\n"
                + "left join sb_user_role_mapping urm on u.id = urm.user\n"
                + "left join sb_roles r on urm.role = r.id\n"
                + "where urm.active = 1\n"
                + "and r.active = 1\n"
                + "and u.username = ?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            roles.add(getRoleObj(rs));
        }
        close(rs);
        close(ps);
        logger.debug("getRoleFromUser::END");
        return roles;
    }

    @Override
    public Role getRole(String roleName)
            throws SQLException {
        logger.debug("getRole::START::" + roleName);
        Role role = new Role();
        PreparedStatement ps = null;
        ps = conn.prepareStatement("select r.*\n"
                + "from sb_roles r\n"
                + "where r.active = 1\n"
                + "and r.name = ?");
        ps.setString(1, roleName);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            role.setId(rs.getInt("id"));
            role.setName(rs.getString("name"));
        }
        close(rs);
        close(ps);
        logger.debug("getRole::END");
        return role;
    }

    @Override
    public void addUserToRole(User user, Integer roleId)
            throws SQLException {
        logger.debug("addUserToRole::START::User::" + user.getUsername());
        PreparedStatement ps = conn.prepareStatement("INSERT INTO \n"
                + "sb_user_role_mapping (user, role, created_by, last_updated_by) \n"
                + "values (?, ? , ?, ?)");
        ps.setInt(1, user.getId());
        ps.setInt(2, roleId);
        ps.setInt(3, user.getCreatedBy());
        ps.setInt(4, user.getLastUpdatedBy());
        ps.executeUpdate();
        close(ps);
        logger.debug("addUserToRole::END");
    }

    @Override
    public User getUserForSession(String username)
            throws SQLException {
        logger.debug("getUserForSession::ByUSERNAME::START::" + username);
        User user = getUser(username);
        user.setUserEntitlements(getUserEntitlements(user.getId()));
        user.setContextScope(getUserContextsScope(user.getId()));
        user.setContextScopeStr(getUserContextsScopeStr(user.getContextScope()));
        logger.debug("getUserForSession::ByUSERNAME::END");
        return user;
    }

    @Override
    public List<UserEntitlement> getUserEntitlements(Integer userId)
            throws SQLException {
        logger.debug("getUserEntitlements::ByUserId::START::" + userId);
        PreparedStatement ps = null;
        List<UserEntitlement> ues = new ArrayList<>();
        ps = conn.prepareStatement("select distinct rem.id, rem.role, rem.context, rem.entitlement, \n"
                + "rem.entitlement_name, ent.resource, ent.action \n"
                + "from sb_user_role_mapping urm\n"
                + "left join sb_role_ent_mapping rem on urm.role = rem.role\n"
                + "left join sb_entitlements ent on rem.entitlement = ent.id\n"
                + "where urm.user = ? \n"
                + "and rem.active = 1 \n"
                + "order by entitlement");
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        UserEntitlement previousUe = null;
        while (rs.next()) {
            UserEntitlement ue = getUserEntitlementObj(rs);
            //exlude 1st iter && add if current and previous are different
            if (previousUe != null && ue.getEntitlement() != previousUe.getEntitlement()) {
                previousUe.setAllowedRoles(getAllowedRolesForUserEnt(previousUe.getId()));
                ues.add(previousUe);
            }
            //first iter or current and previous are different
            if (previousUe == null || ue.getEntitlement() != previousUe.getEntitlement()) {
                previousUe = ue;
            } else //if current and previous are same
            //compare context levels, and update previous with current
                if (previousUe.getRoleEntContext().getContextLevel()
                        > ue.getRoleEntContext().getContextLevel()) {
                    previousUe = ue;
                }
            }
        //add last iter
        previousUe.setAllowedRoles(getAllowedRolesForUserEnt(previousUe.getId()));
        ues.add(previousUe);
        close(rs);
        close(ps);
        logger.debug("getUserEntitlements::ByUserId::END");
        return ues;
    }

    @Override
    public List<Role> getAllowedRolesForUserEnt(Integer userEntId)
            throws SQLException {
        logger.debug("getAllowedRolesForUserEnt::START::UserEntId::" + userEntId);
        List<Role> roles = new ArrayList<>();
        PreparedStatement ps = null;
        ps = conn.prepareStatement("SELECT r.* \n"
                + "FROM sb_roles_context rc\n"
                + "left join sb_roles r on rc.allowed_role = r.id\n"
                + "where rc.role_ent = ? \n"
                + "order by r.name");
        ps.setInt(1, userEntId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            roles.add(getRoleObj(rs));
        }
        close(rs);
        close(ps);
        logger.debug("getAllowedRolesForUserEnt::END");
        return roles;
    }

    private HashMap<RoleEntContext, List<ContextObj>> getUserContextsScope(Integer userId)
            throws SQLException {
        logger.debug("getUserContextsScope::START::UserId::" + userId);
        HashMap<RoleEntContext, List<ContextObj>> ctxScope = new HashMap<>();
        PreparedStatement ps = null;
        ps = conn.prepareStatement("select distinct rem.context from sb_role_ent_mapping rem\n"
                + "left join sb_user_role_mapping urm on rem.role = urm.role\n"
                + "where urm.user = ?");
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ctxScope.put(RoleEntContext.valueOf(rs.getString("context")),
                    getContextScope(RoleEntContext.valueOf(rs.getString("context")), userId));
        }
        close(rs);
        close(ps);
        logger.debug("getUserContextsScope::END");
        return ctxScope;
    }

    @Override
    public List<ContextObj> getContextScope(RoleEntContext ctx, Integer userId)
            throws SQLException {
        logger.debug("getContextScope::START::Context::" + ctx);
        List<ContextObj> ctxObjs = new ArrayList<>();
        String ctxQuery = null;
        switch (ctx) {
            case ALL:
                ctxQuery = null;
                break;
            case COMPANY:
                ctxQuery = "select c.* from sb_companies c\n"
                        + "left join sb_users u on c.id = u.company\n"
                        + "where u.id = ?";
                break;
            case PROJECT:
                ctxQuery = "select c.name as company_name, p.* from sb_projects p\n"
                        + "left join sb_user_proj_loc_mapping upl on p.id = upl.project\n"
                        + "left join sb_companies c on p.company = c.id\n"
                        + "where upl.user = ?";
                break;
            case LOCATION:
                ctxQuery = "select p.name as project_name, l.* from sb_location l\n"
                        + "left join sb_user_proj_loc_mapping upl on l.id = upl.location\n"
                        + "left join sb_projects p on l.project = p.id\n"
                        + "where upl.user = ?";
                break;
            case SELF:
                ctxQuery = "select u.* from sb_users u \n"
                        + "where u.id = ?";
                break;
            default:
                ctxQuery = null;
        }
        if (ctxQuery != null) {
            PreparedStatement ps = null;
            ps = conn.prepareStatement(ctxQuery);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ctxObjs.add(getContextObj(ctx, rs));
            }
            close(rs);
            close(ps);
        }
        logger.debug("getContextScope::END");
        return ctxObjs;
    }

    private HashMap<RoleEntContext, String> getUserContextsScopeStr(HashMap<RoleEntContext, List<ContextObj>> ctxScope) {
        logger.debug("getUserContextsScopeStr::START");
        HashMap<RoleEntContext, String> ctxScopeStr = new HashMap<>();
        for (RoleEntContext ctx : RoleEntContext.values()) {
            ctxScopeStr.put(ctx, getCtxObjIdAsString(ctxScope.get(ctx)));
        }
        logger.debug("getUserContextsScopeStr::END");
        return ctxScopeStr;
    }

    private String getCtxObjIdAsString(List<ContextObj> ctxObjs) {
        StringBuilder idStr = new StringBuilder();
        if (ctxObjs != null) {
            for (ContextObj ctxObj : ctxObjs) {
                idStr.append(ctxObj.getId()).append(",");
            }
            if (idStr.length() > 1) {
                idStr.delete(idStr.length() - 1, idStr.length());
            }
        }
        return idStr.toString();
    }

    @Override
    public List<User> listUsersByContext(User loggedInUser, RoleEntContext ctx)
            throws SQLException {
        logger.debug("listUsersByContext::START");
        String ctxObjStr = "";
        if (RoleEntContext.SELF.equals(ctx)) {
            ctxObjStr = loggedInUser.getId().toString();
        } else {
            ctxObjStr = loggedInUser.getContextScopeStr().get(ctx);
        }
        String query = "select " + fetchColumns + " from ( \n"
                + getQueryWithCtxFilter(usersQueryForCtx, ctx).replace("?", ctxObjStr)
                + ") u \n"
                + defaultJoins;
        List<User> users = getUsers(query);
        logger.debug("listUsersByContext::END");
        return users;
    }

    private List<User> getUsers(String usersQueryWithCtxFilter)
            throws SQLException {
        List<User> users = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement(usersQueryWithCtxFilter);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User user = getUserObj(rs);
            user.setManagerName(rs.getString("manager_name"));
            setAuditable(rs, user);
            users.add(user);
        }
        close(rs);
        close(ps);
        return users;
    }

    @Override
    public User getUserByContext(User loggedInUser, UserEntitlement ctxEnt, Integer userId)
            throws SQLException {
        logger.debug("getUserByContext::START");
        RoleEntContext ctx = getRoleEntContext(loggedInUser.getUserEntitlements(), ctxEnt);
        String ctxObjStr = "";
        if (RoleEntContext.SELF.equals(ctx)) {
            ctxObjStr = loggedInUser.getId().toString();
        } else {
            ctxObjStr = loggedInUser.getContextScopeStr().get(ctx);
        }
        String query = "select " + fetchColumns + " from ( \n"
                + getQueryWithCtxFilter(usersQueryForCtx, ctx).replace("?", ctxObjStr)
                + " and id = " + userId
                + ") u \n"
                + defaultJoins;
        List<User> users = getUsers(query);
        User user = users.size() > 0 ? users.get(0) : null;
        logger.debug("getUserByContext::END");
        return user;
    }

    @Override
    public List<User> listUsersAssignedToProject(Integer project)
            throws SQLException {
        logger.debug("listUsersByProject::START");
        String query = "select " + fetchColumns + " from ( \n"
                + getQueryWithCtxFilter(usersQueryForCtx, RoleEntContext.PROJECT)
                + ") u \n"
                + defaultJoins;
        List<User> users = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement(query);
        ps.setInt(1, project);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User user = getUserObj(rs);
            user.setManagerName(rs.getString("manager_name"));
            users.add(user);
        }
        close(rs);
        close(ps);
        logger.debug("listUsersByProject::END");
        return users;
    }

    @Override
    public List<User> getAssignableUsersForProject(Integer project) throws SQLException {
        logger.debug("getAssignableUsersForProject::START");
        String query = "select u.id, u.first_name, u.last_name from sb_users u\n"
                + "where company = (select p.company from sb_projects p where p.id = ?) \n"
                + "and not exists (select 1 from sb_role_ent_mapping rem\n"
                + "left join sb_entitlements ent on rem.entitlement = ent.id\n"
                + "left join sb_user_role_mapping urm on rem.role = urm.role\n"
                + "where ent.resource = ? \n"
                + "and ent.action = ? \n"
                + "and urm.user = u.id)\n"
                + "and not exists (select 1 from sb_user_proj_loc_mapping uplm\n"
                + "where uplm.project = ? \n"
                + "and uplm.user = u.id)\n"
                + "and u.active = 1";
        List<User> users = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement(query);
        ps.setInt(1, project);
        ps.setString(2, Resource.configuration.toString());
        ps.setString(3, Action.update.toString());
        ps.setInt(4, project);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            users.add(user);
        }
        close(rs);
        close(ps);
        logger.debug("getAssignableUsersForProject::END");
        return users;
    }

    @Override
    public List<User> getUsersForRoleByProject(String role, Integer project) throws SQLException {
        logger.debug("getUsersForRoleByProject::START");
        String query = "select u.id, u.first_name, u.last_name from sb_users u\n"
                + "left join sb_user_role_mapping urm on u.id = urm.user\n"
                + "left join sb_user_proj_loc_mapping upl on u.id = upl.user\n"
                + "left join sb_roles r on urm.role = r.id\n"
                + "where u.active = 1\n"
                + "and r.name = ? \n"
                + "and upl.project = ? ";
        List<User> users = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement(query);
        ps.setString(1, role);
        ps.setInt(2, project);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            users.add(user);
        }
        close(rs);
        close(ps);
        logger.debug("getUsersForRoleByProject::END");
        return users;
    }

    @Override
    public boolean validateUserPassword(String username, String password)
            throws SQLException, NoSuchAlgorithmException {
        AuthenticationManager auth = new AuthenticationManager();
        boolean authenticated = auth.authenticate(conn, username, password);
        return authenticated;
    }

}
