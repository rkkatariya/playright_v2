/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.access;

import com.revvster.playright.model.ContextObj;
import com.revvster.playright.model.Role;
import com.revvster.playright.model.RoleEntContext;
import com.revvster.playright.model.User;
import com.revvster.playright.model.UserEntitlement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rahul
 */
public class AuthorizationManager {

    private static final Logger logger = LogManager.getLogger(AuthorizationManager.class.getName());

    public static RoleEntContext getRoleEntContext(List<UserEntitlement> userEntitlements,
            UserEntitlement userEntitlement) {
        return getUserEntitlement(userEntitlements, userEntitlement).getRoleEntContext();
    }

    public static List<Role> getAllowedRoles(List<UserEntitlement> userEntitlements,
            UserEntitlement userEntitlement) {
        return getUserEntitlement(userEntitlements, userEntitlement).getAllowedRoles();
    }

    public static List<Action> getActionsOnResource(List<UserEntitlement> userEntitlements,
            Resource resource) {
        List<Action> actions = new ArrayList<>();
        for (UserEntitlement ue : getUserEntsWithResource(userEntitlements, resource)) {
            actions.add(ue.getAction());
        }
        return actions;
    }

    public static List<UserEntitlement> getUserEntsWithResource(List<UserEntitlement> userEntitlements,
            Resource resource) {
        List<UserEntitlement> userEntitlementsR = new ArrayList<>();
        if (userEntitlements != null) {
            for (UserEntitlement ue : userEntitlements) {
                if (ue.getResource() == resource) {
                    userEntitlementsR.add(ue);
                }
            }
        }
        return userEntitlementsR;
    }

    public static UserEntitlement getUserEntitlement(List<UserEntitlement> userEntitlements,
            UserEntitlement userEntitlement) {
        UserEntitlement returnUe = new UserEntitlement();
        if (userEntitlements != null) {
            for (UserEntitlement ue : userEntitlements) {
                if (ue.equals(userEntitlement)) {
                    returnUe = ue;
                    break;
                }
            }
        }
        return returnUe;
    }

    // has user::delete and ( contxt = ALL or (ctx = COMPANY and authorized company) 
    //or (ctx = PROJECT/LOCATION and authorized manager))    
    public static boolean canDisableUser(User loggedInUser, User user2Delete) {
        RoleEntContext ctx = getRoleEntContext(loggedInUser.getUserEntitlements(),
                new UserEntitlement(Resource.user, Action.delete));
        if (ctx != null && (RoleEntContext.ALL.equals(ctx)
                || (ctx.getContextLevel() == RoleEntContext.COMPANY.getContextLevel()
                && loggedInUser.getCompany() == user2Delete.getCompany())
                || (ctx.getContextLevel() < RoleEntContext.SELF.getContextLevel()
                && user2Delete.getManager() == loggedInUser.getManager()))) {
            return true;
        } else {
            return false;
        }
    }

    // has user::add and ( contxt = ALL or (ctx = COMPANY/PROJECT/LOCATION and authorized company))
    public static boolean canAddUser(User loggedInUser, User user2Add) {
        RoleEntContext ctx = getRoleEntContext(loggedInUser.getUserEntitlements(),
                new UserEntitlement(Resource.user, Action.add));
        if (ctx != null && (RoleEntContext.ALL.equals(ctx)
                || (ctx.getContextLevel() < RoleEntContext.SELF.getContextLevel()
                && loggedInUser.getCompany() == user2Add.getCompany()))) {
            return true;
        } else {
            return false;
        }
    }

    //return true if user is assigned to company and has some entitlements 
    //or has login without company entitlement
    public static boolean canUserLogin(User user) {
        return ((user.getCompany() != null && user.getCompany() != 0
                && !user.getUserEntitlements().isEmpty())
                || user.getUserEntitlements().contains(
                        new UserEntitlement(Resource.user,
                                Action.login_without_company)));
    }

    public static boolean authorizeContextObj(RoleEntContext ctx,
                                List<ContextObj> ctxObjs, Integer ctxId) {
        boolean authorized = false;
        switch (ctx) {
            case ALL:
                authorized = true;
                break;
            case COMPANY:
                authorized = containsCtxObj(ctxObjs, ctxId);
                break;
            case PROJECT:
                authorized = containsCtxObj(ctxObjs, ctxId);
                break;
            case LOCATION:
                authorized = containsCtxObj(ctxObjs, ctxId);
                break;
            case SELF:
                authorized = containsCtxObj(ctxObjs, ctxId);
                break;
            default:
                authorized = false;
        }

        return authorized;
    }

    private static boolean containsCtxObj(List<ContextObj> ctxObjs, Integer ctxId) {
        boolean authorized = false;
        for (ContextObj ctxObj : ctxObjs) {
            if (ctxObj.getId() != null
                    && ctxObj.getId().intValue() == ctxId.intValue()) {
                authorized = true;
                break;
            }
        }
        return authorized;
    }

}
