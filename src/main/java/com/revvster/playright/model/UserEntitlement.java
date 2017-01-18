/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.model;

import com.revvster.playright.access.Action;
import com.revvster.playright.access.Resource;
import java.util.List;

/**
 *
 * @author Rahul
 */
public class UserEntitlement {
    
    private Integer id; //sb_role_ent_mapping
    private Integer entitlement;
    private Resource resource;
    private Action action;
    private RoleEntContext roleEntContext;
    private List<Role> allowedRoles;

    public UserEntitlement() {
    }

    public UserEntitlement(String resource, String action) {
        this.resource = Resource.valueOf(resource);
        this.action = Action.valueOf(action);
    }

    public UserEntitlement(Resource resource, Action action) {
        this.resource = resource;
        this.action = action;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEntitlement() {
        return entitlement;
    }

    public void setEntitlement(Integer entitlement) {
        this.entitlement = entitlement;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public RoleEntContext getRoleEntContext() {
        return roleEntContext;
    }

    public void setRoleEntContext(RoleEntContext roleEntContext) {
        this.roleEntContext = roleEntContext;
    }
    
    public String getEntitlementName() {
        return this.resource + "::" + this.action;
    }

    public List<Role> getAllowedRoles() {
        return allowedRoles;
    }

    public void setAllowedRoles(List<Role> allowedRoles) {
        this.allowedRoles = allowedRoles;
    }

   @Override
    public boolean equals(Object object) {
        boolean sameSame = false;

        if (object != null && object instanceof UserEntitlement) {
            sameSame = this.getEntitlementName().equals(((UserEntitlement) object).getEntitlementName());
        }
        return sameSame;
    }
    
}
