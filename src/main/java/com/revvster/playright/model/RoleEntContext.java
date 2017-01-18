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
public enum RoleEntContext {
    ALL     (0),
    COMPANY (1),
    PROJECT (2),
    LOCATION(3),
    SELF    (4);
    
    private final int contextLevel;
    
    RoleEntContext (int contextCode) {
        this.contextLevel = contextCode;
    }
    
    public int getContextLevel() {
        return this.contextLevel;
    }
}
