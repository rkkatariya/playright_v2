/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.model;

import com.revvster.playright.util.SystemConstants;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Rahul
 */
public class User extends Auditable implements ContextObj {

    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String mobileNo;
    private String altPhoneNo;
    private String resetPassword;
    private String password;
    private Integer manager;
    private String managerName;
    private Timestamp lastLogin;
    private String lastLoginFrom;
    private String jobTitle;
    private String department;
    private Integer role;
    private String roleName;
    private String roleRawName;
    private String gender;
    private Integer company;
    private String companyName;
    private List<UserEntitlement> userEntitlements;
    private HashMap<RoleEntContext, List<ContextObj>> contextScope;
    private HashMap<RoleEntContext, String> contextScopeStr;

    public User() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return getFullName();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAltPhoneNo() {
        return altPhoneNo;
    }

    public void setAltPhoneNo(String altPhoneNo) {
        this.altPhoneNo = altPhoneNo;
    }

    public String getResetPassword() {
        return resetPassword;
    }

    public void setResetPassword(String resetPassword) {
        this.resetPassword = resetPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getManager() {
        return manager;
    }

    public void setManager(Integer manager) {
        this.manager = manager;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLastLoginFrom() {
        return lastLoginFrom;
    }

    public void setLastLoginFrom(String lastLoginFrom) {
        this.lastLoginFrom = lastLoginFrom;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getCompany() {
        return company;
    }

    public void setCompany(Integer company) {
        this.company = company;
    }

    public String getCompanyName() {
        if (null == this.companyName || "".equals(this.companyName)) {
            return SystemConstants.DefaultCompanyName;
        } else {
            return companyName;
        }
    }

    public void setCompanyName(String companyName) {
        if ("".equals(companyName)) {
            this.companyName = SystemConstants.DefaultCompanyName;
        } else {
            this.companyName = companyName;
        }
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getRoleRawName() {
        return roleRawName;
    }

    public void setRoleRawName(String roleRawName) {
        this.roleRawName = roleRawName;
    }

    public List<UserEntitlement> getUserEntitlements() {
        return userEntitlements;
    }

    public void setUserEntitlements(List<UserEntitlement> userEntitlements) {
        this.userEntitlements = userEntitlements;
    }

    public HashMap<RoleEntContext, List<ContextObj>> getContextScope() {
        return contextScope;
    }

    public void setContextScope(HashMap<RoleEntContext, List<ContextObj>> contextScope) {
        this.contextScope = contextScope;
    }

    public HashMap<RoleEntContext, String> getContextScopeStr() {
        return contextScopeStr;
    }

    public void setContextScopeStr(HashMap<RoleEntContext, String> contextScopeStr) {
        this.contextScopeStr = contextScopeStr;
    }
    
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
    
    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + '}';
    }

    public boolean hasEntitlement(UserEntitlement ue) {
        boolean hasEnt = false;
        for (UserEntitlement uent : getUserEntitlements()) {
            if (ue.equals(uent)) {
                hasEnt = true;
                break;
            }
        }
        return hasEnt;
    }
}
