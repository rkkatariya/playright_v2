<%-- 
    Document   : userCreate
    Created on : Oct 11, 2015, 10:37:45 AM
    Author     : Rahul
--%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.revvster.playright.model.Role"%>
<%@page import="java.util.List"%>
<%@page import="com.revvster.playright.access.Action"%>
<%@page import="com.revvster.playright.access.Resource"%>
<%@page import="com.revvster.playright.model.UserEntitlement"%>
<%@page import="com.revvster.playright.access.AuthorizationManager"%>
<%@page import="com.revvster.playright.model.User"%>
<%@page import="com.revvster.playright.util.SystemConstants"%>
<%
    User user = new User();
    if (session.getAttribute(SystemConstants.LoggedInUser) != null) {
        user = (User) session.getAttribute(SystemConstants.LoggedInUser);
    }
    UserEntitlement ue = AuthorizationManager.getUserEntitlement(user.getUserEntitlements(),
                        new UserEntitlement(Resource.user, Action.add));
    List<Role> allowedRoles = ue.getAllowedRoles() != null ? ue.getAllowedRoles() : new ArrayList<Role>();
%>
<div class="modal fade" id="modalCreateUser" role="dialog" aria-labelledby="createUserProfile">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="createUserProfile">Add User</h4>
            </div>
            <div class="modal-body">
                <form role="form" id="createUser" class="form-horizontal">

                    <div class="box-body">
                        <div class="form-group">
                            <label for="inputUserName" class="col-sm-2 control-label">User Name (Login)</label>
                            <div class="col-sm-4">
                                <input type="email" class="form-control" id="inputUserName" name="inputUserName" placeholder="Email Address">
                            </div>
                            <div class="col-sm-6">
                            </div>
                        </div>
<!--                        <div class="form-group">
                            <label for="inputPassword" class="col-sm-2 control-label">Password</label>
                            <div class="col-sm-4">
                                <input type="password" class="form-control" id="inputPassword" name="inputPassword" placeholder="Enter Password">
                            </div>
                            <div class="col-sm-6">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="inputConfirmPassword" class="col-sm-2 control-label">Confirm Password</label>
                            <div class="col-sm-4">
                                <input type="password" class="form-control" id="inputConfirmPassword" name="inputConfirmPassword" placeholder="Confirm Password">
                            </div>
                            <div class="col-sm-6">
                            </div>
                        </div>-->

                        <div class="form-group">
                            <label for="inputFirstName" class="col-sm-2 control-label">First Name</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" id="inputFirstName" name="inputFirstName" placeholder="Enter First Name">
                            </div>
                            <div class="col-sm-6">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="inputLastName" class="col-sm-2 control-label">Last Name</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" id="inputLastName" name="inputLastName" placeholder="Enter Last Name">
                            </div>
                            <div class="col-sm-6">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="inputJobTitle" class="col-sm-2 control-label">Job Title</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" id="inputJobTitle" name="inputJobTitle" placeholder="Enter Job Title">
                            </div>
                            <div class="col-sm-6">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="inputDepartment" class="col-sm-2 control-label">Department</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" id="inputDepartment" name="inputDepartment" placeholder="Enter Department">
                            </div>
                            <div class="col-sm-6">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="inputMobileNo" class="col-sm-2 control-label">Mobile No.</label>
                            <div class="col-sm-4">
                                <div class="input-group">
                                    <span class="input-group-addon">+91</span>
                                    <input type="text" class="form-control" id="inputMobileNo" name="inputMobileNo" placeholder="Enter Mobile No.">
                                </div>
                            </div>
                            <div class="col-sm-6">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="inputAltNo" class="col-sm-2 control-label">Alt. Phone No.</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" id="inputAltNo" name="inputAltNo" placeholder="Enter Alt. Phone No.">
                            </div>
                            <div class="col-sm-6">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="inputGender" class="col-sm-2 control-label">Gender</label>
                            <div class="col-sm-4 radio">
                                <!--<input type="text" class="form-control" id="inputGender" name="inputGender" placeholder="inputGender">-->
                                <label>
                                    <input type="radio" name="inputGender" value="Male">
                                    Male
                                </label>
                                <label>
                                    <input type="radio" name="inputGender" value="Female">
                                    Female 
                                </label>
                            </div>
                            <div class="col-sm-6">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="inputRole" class="col-sm-2 control-label">Role</label>
                            <div class="col-sm-4">
                                <select class="form-control" id="inputRole" name="inputRole">
                                    <option value="" disabled selected>Select Role</option>
                                <%for (Role r : allowedRoles) {%>   
                                <option value="<%=r.getId()%>" name="<%=r.getRawName()%>"><%=r.getName()%></option>
                                <%}%>
                                </select>                            
                            </div>
                            <div class="col-sm-6">
                            </div>
                        </div>

                                <%if (user.getUserEntitlements() != null 
                                        && user.getUserEntitlements().contains(
                                        new UserEntitlement(Resource.company, Action.add))) {%>
                            <div class="form-group">
                                <label for="inputCompany" class="col-sm-2 control-label">Company</label>
                                <div class="col-sm-4">
                                    <select id="inputCompany" name="inputCompany" class="form-control" style="width: 100%;" >
                                    </select>                                                
                                </div>
                                <div class="col-sm-6">
                                </div>
                            </div>
                        <%} else {%>
                            <input type="hidden" name="inputCompany" value="<%=user.getCompany()%>">
                        <%}%>
                        
                        <div class="form-group">
                            <label for="inputManager" class="col-sm-2 control-label">Manager</label>
                            <div class="col-sm-4">
                                <select id="inputManager" name="inputManager" class="form-control" style="width: 100%;" >
                                </select>                                                
                            </div>
                            <div class="col-sm-6">
                            </div>
                        </div>

                    </div>

                    <div class="row">
                        <div class="col-sm-3">                            
                        </div>
                        <div class="col-sm-3"> 
                            <button type="submit" class="btn btn-primary pull-right" id="submitCreateUser">Submit</button>
                        </div>
                        <div class="col-sm-3">    
                            <button type="reset" class="btn btn-link" id="resetCreateUser">Reset</button>
                        </div>
                        <div class="col-sm-3">                            
                        </div>
                    </div>
                </form>

            </div>
            <div class="modal-footer">

                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>

            </div>
        </div>
    </div>
</div>        
