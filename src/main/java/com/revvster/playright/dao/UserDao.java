/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.dao;

import com.revvster.playright.model.ContextObj;
import com.revvster.playright.model.Role;
import com.revvster.playright.model.RoleEntContext;
import com.revvster.playright.model.User;
import com.revvster.playright.model.UserEntitlement;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Rahul
 */
public interface UserDao {
    
    public void close();
    public List<User> getAllUsers() throws SQLException;
    public List<User> getAllReportees() throws SQLException;
    public List<User> getUsersOfCompany(Integer companyId) throws SQLException;
    public User getUser(Integer id) throws SQLException;
    public User getUser(String username) throws SQLException;
    public void updateUser(User user) throws SQLException;
    public void deleteUser(User user) throws SQLException;
    public Integer createUser(User user) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException;
    public void deleteUsers(List<User> users, Integer loggedInUserId) throws SQLException;
    public List<User> getManagersStartingWith(String name, Integer companyId) throws SQLException;
    public boolean login(String username, String password) throws SQLException, NoSuchAlgorithmException;
    public String basicAuthApi(String authCredentials) throws SQLException, NoSuchAlgorithmException;
    public void updateLastLogin(Integer userId) throws SQLException;
    public List<Role> getUserRoles(Integer userId) throws SQLException;
    public List<Role> getUserRoles(String username) throws SQLException;
    public Role getRole(String roleName) throws SQLException;
    public User getUserFromMobileNo(String mobileNo) throws SQLException;
    public void addUserToRole(User user, Integer roleId) throws SQLException;
    public User getUserForSession(String username) throws SQLException;
    public List<UserEntitlement> getUserEntitlements(Integer userId) throws SQLException;
    public List<Role> getAllowedRolesForUserEnt(Integer userEntId) throws SQLException;
    public List<ContextObj> getContextScope(RoleEntContext ctx, Integer userId) throws SQLException;
    public List<User> listUsersByContext(User loggedInUser, RoleEntContext ctx) throws SQLException;
    public User getUserByContext(User loggedInUser, UserEntitlement ctxEnt, Integer userId) throws SQLException;
    public List<User> listUsersAssignedToProject(Integer project) throws SQLException;
    public List<User> getAssignableUsersForProject(Integer project) throws SQLException;    
    public List<User> getUsersForRoleByProject(String role, Integer project) throws SQLException;
    public List<User> getSalesRepForManager(Integer manager) throws SQLException;
    public Integer getTotalSale(Integer user) throws SQLException;
    public Integer getPendingFollowup(Integer user) throws SQLException;
    public Integer getCustomersAttendedForUser(Integer user) throws SQLException;
    public boolean validateUserPassword(String username, String password) throws SQLException, NoSuchAlgorithmException;
    public boolean updateUserPassword(String username, String password, Integer resetpwd) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException;
    
    //public Integer addCustomerSalesRep(CustomerSalesRepMapping custSalesRep) throws SQLException;
   
}
