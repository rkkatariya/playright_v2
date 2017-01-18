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
public enum LOVType {

    QUESTION_TYPE(true, RoleEntContext.ALL),
    SECTION_TYPE(false, RoleEntContext.ALL),
    CATALOG_STATE(false, RoleEntContext.ALL),
    CATALOG_ITEM_ATTR(true, RoleEntContext.ALL),
    CATALOG_OPTION_ATTR(true, RoleEntContext.ALL),
    CATALOG_ITEM_COST_CAT(true, RoleEntContext.PROJECT),
    CATALOG_OPTION_COST_CAT(true, RoleEntContext.PROJECT),
    CATALOG_GROUP(true, RoleEntContext.PROJECT),
    NOTIFICATION_STATUS(false, RoleEntContext.ALL);

    private final boolean editable;
    private final RoleEntContext ctx;

    private LOVType(boolean editable, RoleEntContext ctx) {
        this.editable = editable;
        this.ctx = ctx;
    }

    public boolean isEditable() {
        return this.editable;
    }

    public RoleEntContext getContext() {
        return this.ctx;
    }
}
