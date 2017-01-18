/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.model;

/**
 *
 * @author Rahul
 */
public enum Settings {
    EnableLocation(SettingType.system, RoleEntContext.ALL),
    MaxInactiveInterval(SettingType.system, RoleEntContext.ALL),
    EmailEnable(SettingType.email, RoleEntContext.COMPANY),
    EmailHost(SettingType.email, RoleEntContext.COMPANY),
    EmailPort(SettingType.email, RoleEntContext.COMPANY),
    EmailUserName(SettingType.email, RoleEntContext.COMPANY),
    EmailPassword(SettingType.email, RoleEntContext.COMPANY),
    EmailTLSEnable(SettingType.email, RoleEntContext.COMPANY)
    ;
    
    private final RoleEntContext ctx;
    private final SettingType type;

    private Settings(SettingType type, RoleEntContext ctx) {
        this.ctx = ctx;
        this.type = type;
    }
    
    public SettingType getType() {
        return this.type;
    }

    public RoleEntContext getContext() {
        return this.ctx;
    }
    
    
}
