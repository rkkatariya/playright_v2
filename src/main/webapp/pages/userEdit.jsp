<%-- 
    Document   : editUser
    Created on : Oct 11, 2015, 10:25:51 AM
    Author     : Rahul
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="com.revvster.playright.model.Role"%>
<%@page import="java.util.List"%>
<%@page import="com.revvster.playright.access.Resource"%>
<%@page import="com.revvster.playright.access.Action"%>
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
                        new UserEntitlement(Resource.user, Action.update));
    List<Role> allowedRoles = ue.getAllowedRoles() != null ? ue.getAllowedRoles() : new ArrayList<Role>();
%>

<div class="modal fade bs-example-modal-sm" id="modalEditUser" role="dialog" aria-labelledby="editUserProfile">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="editUserProfile">Edit User Profile</h4>
            </div>
            <div class="modal-body">
                <form role="form" id="editUser" class="form-horizontal">
                    <div class="box-body">
                        <input type="hidden" id="inputId" name="inputId"  class="form-control">
                        <input type="hidden" id="inputUserName" name="inputUserName"  class="form-control">
                        <input type="hidden" id="inputRole" name="inputRole"  class="form-control">
                        <input type="hidden" id="inputCompany" name="inputCompany"  class="form-control">

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
                            <label for="roleName" class="col-sm-2 control-label">Role</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" id="roleName" name="roleName">
                            </div>
                            <div class="col-sm-6">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="companyName" class="col-sm-2 control-label">Company</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" id="companyName" name="companyName" 
                                       placeholder="<%=SystemConstants.DefaultCompanyName%>">
                            </div>
                            <div class="col-sm-6">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="inputManager" class="col-sm-2 control-label">Manager</label>
                            <div class="col-sm-4">
                                <select id="inputManager" name="inputManager" class="form-control" style="width: 100%;" >
                                </select>                                                
                            </div>
                            <div class="col-sm-6">
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-sm-3">                            
                            </div>
                            <div class="col-sm-3"> 
                                <button type="submit" class="btn btn-primary pull-right" id="submitEditUser">Submit</button>
                            </div>
                            <div class="col-sm-3">    
                            </div>
                            <div class="col-sm-3">                            
                            </div>
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