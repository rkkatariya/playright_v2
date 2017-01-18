/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.dao;

import com.revvster.playright.model.Company;
import com.revvster.playright.model.Location;
import com.revvster.playright.model.Project;
import com.revvster.playright.model.User;
import com.revvster.playright.model.UserEntitlement;
import com.revvster.playright.model.Setting;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Rahul
 */
public interface ContextObjDao {

    public void close();
    public List<Company> getAllCompanies() throws SQLException;
    public Company getCompany(String name) throws SQLException;
    public Company getCompany(Integer id) throws SQLException;
    public Project getProject(Integer id) throws SQLException;
    public Project getProjectData(Integer id) throws SQLException;
    public void updateCompany(Company company) throws SQLException;
    public void disableCompany(Company company) throws SQLException;
    public List<Project> listProjects(Integer company, User user) throws SQLException;
    public List<User> getUsersOfCompany(Company company) throws SQLException;
    public Integer createCompany(Company company) throws SQLException;
    public List<Company> getCompaniesStartingWith(String name) throws SQLException;    
    public List<Project> getProjectsStartingWith(String name, Integer company, User loggedInUser) throws SQLException;
    public List<Location> getLocationsStartingWith(String name, Integer project, User loggedInUser) throws SQLException;
    public List<Company> listCompaniesByContext(User loggedInUser, UserEntitlement ctxEnt) throws SQLException;
    public List<Project> listProjectsByContext(User loggedInUser, UserEntitlement ctxEnt) throws SQLException;
    public List<Location> listLocationsByContext(User loggedInUser, UserEntitlement ctxEnt) throws SQLException;
    public Integer createProject(Project project) throws SQLException;
    public List<Project> listProjectForCustSalesrep(Integer customer) throws SQLException;
    public void updateProject(Project project) throws SQLException;
    public void assignUsersToProject(List<User> users, Integer projectId, User loggedInUser) throws SQLException;
    public List<Project> getProjectsAssignedToUser(Integer user) throws SQLException;
    public List<Project> listProjectByCustomer(Integer customer) throws SQLException;
    public List<Setting> listSettingsByCompany(User user, Integer company) throws SQLException;
    public List<Setting> listSettingsByProject(User user, Integer company, Integer project) throws SQLException;
    public void updateSettingValues(Setting setting) throws SQLException;    
    public Integer createSettingValues(Setting setting) throws SQLException;
    public List<Setting> listEmailSettingsByProject(User loggedInUser, Integer company, Integer project) throws SQLException;
    public List<Setting> listEmailSettingsByCompany(User loggedInUser, Integer company) throws SQLException;
}
