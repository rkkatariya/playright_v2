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
public class SettingDaoImpl extends DaoHelper implements SettingDao {
    
    private static final Logger logger = LogManager.getLogger(SettingDaoImpl.class.getName());
    
    private static final String selectQuery = "select * from sb_settings\n";

    @Override
    public void close() {
        super.close();
    }
    
    @Override
    public List<Setting> getSettings(Settings parameter) throws SQLException {
        logger.debug("getSettings::START::param::"+parameter);
        List<Setting> settings = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement(selectQuery 
                + "where active = 1 \n"
                + "and parameter = ?");
        ps.setString(1, parameter.toString());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Setting s = getSettingObj(rs);
            settings.add(s);
        }
        close(rs);
        close(ps);
        logger.debug("getSettings::END");
        return settings;
    }

    @Override
    public List<Setting> listSettings() throws SQLException {
        logger.debug("listSettings::START");
        List<Setting> settings = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement(selectQuery);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Setting s = getSettingObj(rs);
            settings.add(s);
        }
        close(rs);
        close(ps);
        logger.debug("listSettings::END");
        return settings;
    }

    @Override
    public HashMap<Settings, String> getSystemSettings() throws SQLException {
        logger.debug("getSystemSettings::START");
        HashMap<Settings, String> settings = new HashMap<>();
        PreparedStatement ps;
        ps = conn.prepareStatement(selectQuery 
                + "where active = 1 \n"
                + "and company is null \n"
                + "and project is null \n"
                + "and location is null \n");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Setting s = getSettingObj(rs);
            settings.put(s.getParameter(), s.getValue());
        }
        close(rs);
        close(ps);
        logger.debug("getSystemSettings::END");
        return settings;
    }

    @Override
    public List<Setting> getCompanySettings(Integer company) throws SQLException {
        logger.debug("getCompanySettings::START::company::"+company);
        List<Setting> settings = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement(selectQuery 
                + "where active = 1 \n"
                + "and company = ?");
        ps.setInt(1, company);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Setting s = getSettingObj(rs);
            settings.add(s);
        }
        close(rs);
        close(ps);
        logger.debug("getCompanySettings::END");
        return settings;
    }

    @Override
    public List<Setting> getProjectSettings(Integer project) throws SQLException {
        logger.debug("getProjectSettings::START::project::"+project);
        List<Setting> settings = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement(selectQuery 
                + "where active = 1 \n"
                + "and project = ?");
        ps.setInt(1, project);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Setting s = getSettingObj(rs);
            settings.add(s);
        }
        close(rs);
        close(ps);
        logger.debug("getProjectSettings::END");
        return settings;
    }

    @Override
    public List<Setting> getLocationSettings(Integer location) throws SQLException {
        logger.debug("getLocationSettings::START::location::"+location);
        List<Setting> settings = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement(selectQuery 
                + "where active = 1 \n"
                + "and location = ?");
        ps.setInt(1, location);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Setting s = getSettingObj(rs);
            settings.add(s);
        }
        close(rs);
        close(ps);
        logger.debug("getLocationSettings::END");
        return settings;
    }

    @Override
    public Setting getSystemSetting(Settings parameter) throws SQLException {
        logger.debug("getSystemSetting::START::parameter::"+parameter);
        PreparedStatement ps;
        ps = conn.prepareStatement(selectQuery
                + "where active = 1 \n"
                + "and company is null \n"
                + "and project is null \n"
                + "and location is null \n"
                + "and parameter = ?");
        ps.setString(1, parameter.toString());
        ResultSet rs = ps.executeQuery();
        Setting s = null;
        while (rs.next()) {
            s = getSettingObj(rs);
        }
        close(rs);
        close(ps);
        logger.debug("getSystemSetting::END");
        return s;
    }

    @Override
    public Setting getSetting(String parameter, Company c) throws SQLException {
        logger.debug("getSetting::START::parameter::"+parameter+"::company::"+c.getId());
        PreparedStatement ps;
        ps = conn.prepareStatement(selectQuery
                + "where active = 1 \n"
                + "and company = ? \n"
                + "and parameter = ?");
        ps.setInt(1, c.getId());
        ps.setString(2, parameter);
        ResultSet rs = ps.executeQuery();
        Setting s = null;
        while (rs.next()) {
            s = getSettingObj(rs);
        }
        close(rs);
        close(ps);
        logger.debug("getSetting::END");
        return s;
    }

    @Override
    public Setting getSetting(String parameter, Project p) throws SQLException {
        logger.debug("getSetting::START::parameter::"+parameter+"::project::"+p.getId());
        PreparedStatement ps;
        ps = conn.prepareStatement(selectQuery
                + "where active = 1 \n"
                + "and project = ? \n"
                + "and parameter = ?");
        ps.setInt(1, p.getId());
        ps.setString(2, parameter);
        ResultSet rs = ps.executeQuery();
        Setting s = null;
        while (rs.next()) {
            s = getSettingObj(rs);
        }
        close(rs);
        close(ps);
        logger.debug("getSetting::END");
        return s;
    }

    @Override
    public Setting getSetting(String parameter, Location l) throws SQLException {
        logger.debug("getSetting::START::parameter::"+parameter+"::location::"+l.getId());
        PreparedStatement ps;
        ps = conn.prepareStatement(selectQuery
                + "where active = 1 \n"
                + "and location = ? \n"
                + "and parameter = ?");
        ps.setInt(1, l.getId());
        ps.setString(2, parameter);
        ResultSet rs = ps.executeQuery();
        Setting s = null;
        while (rs.next()) {
            s = getSettingObj(rs);
        }
        close(rs);
        close(ps);
        logger.debug("getSetting::END");
        return s;
    }
    
    
    
}
