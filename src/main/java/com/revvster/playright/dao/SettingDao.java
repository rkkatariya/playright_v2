/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.dao;

import com.revvster.playright.model.Company;
import com.revvster.playright.model.Location;
import com.revvster.playright.model.Project;
import com.revvster.playright.model.Setting;
import com.revvster.playright.model.Settings;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Rahul
 */
public interface SettingDao {
    
    public void close();
    public List<Setting> getSettings(Settings parameter) throws SQLException;
    public List<Setting> listSettings() throws SQLException;
    public HashMap<Settings,String> getSystemSettings() throws SQLException;
    public List<Setting> getCompanySettings(Integer company) throws SQLException;
    public List<Setting> getProjectSettings(Integer project) throws SQLException;
    public List<Setting> getLocationSettings(Integer location) throws SQLException;
    public Setting getSystemSetting(Settings parameter) throws SQLException;
    public Setting getSetting(String parameter, Company c) throws SQLException;
    public Setting getSetting(String parameter, Project p) throws SQLException;
    public Setting getSetting(String parameter, Location l) throws SQLException;
    
}
