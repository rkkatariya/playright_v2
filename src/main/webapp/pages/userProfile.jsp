<%-- 
    Document   : userProfile
    Created on : Oct 5, 2015, 10:40:09 PM
    Author     : Rahul
--%>

<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.revvster.playright.access.Action"%>
<%@page import="com.revvster.playright.access.Resource"%>
<%@page import="com.revvster.playright.access.AuthorizationManager"%>
<%@page import="com.revvster.playright.model.UserEntitlement"%>
<%@page import="java.util.List"%>
<%@page import="java.util.*"%>
<%@page import="com.revvster.playright.model.RoleEntContext"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@page import="com.revvster.playright.model.User"%>
<%@page import="com.revvster.playright.util.SystemConstants"%>
<%
    User user = new User();
    if (session.getAttribute(SystemConstants.LoggedInUser) != null) {
        user = (User) session.getAttribute(SystemConstants.LoggedInUser);
    }

    RoleEntContext editCtx = null;
    RoleEntContext deleteCtx = null;
    RoleEntContext viewCtx = null;

    if (user.getUserEntitlements() != null) {
        List<UserEntitlement> ues = user.getUserEntitlements();
        editCtx = AuthorizationManager.getRoleEntContext(ues,
                new UserEntitlement(Resource.user, Action.update));
        deleteCtx = AuthorizationManager.getRoleEntContext(ues,
                new UserEntitlement(Resource.user, Action.delete));
        viewCtx = AuthorizationManager.getRoleEntContext(ues,
                new UserEntitlement(Resource.user, Action.view));
    }
%>
<html lang="en">

    <head>

        <title>User Profile</title>

        <jsp:include page="header.jsp"/>
        <jsp:include page="datatables_css.jsp"/>
        <link rel="stylesheet" href="../adminlte2/plugins/select2/select2.min.css">
        <link rel="stylesheet" href="../adminlte2/bootstrap/css/bootstrap.min.css"> 
        <link rel="stylesheet" href="../adminlte2/plugins/datepicker/css/bootstrap-datepicker3.min.css">
        <jsp:include page="adminlte_css.jsp"/>

    </head>
    <!--
    BODY TAG OPTIONS:
    =================
    Apply one or more of the following classes to get the
    desired effect
    |---------------------------------------------------------|
    | SKINS         | skin-blue                               |
    |               | skin-black                              |
    |               | skin-purple                             |
    |               | skin-yellow                             |
    |               | skin-red                                |
    |               | skin-green                              |
    |---------------------------------------------------------|
    |LAYOUT OPTIONS | fixed                                   |
    |               | layout-boxed                            |
    |               | layout-top-nav                          |
    |               | sidebar-collapse                        |
    |               | sidebar-mini                            |
    |---------------------------------------------------------|
    -->
    <body class="hold-transition skin-red sidebar-mini">
        <div class="modal fade bs-example-modal-sm" id="modalDisableUser" role="dialog" aria-labelledby="disableUser">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="titleUser">Disable User</h4>
                    </div>
                    <div class="modal-body">
                        <div id="disableUsr"></div>
                        <form role="form" id="disableUser">  
                            <input type="hidden" id="inputId" name="inputId"  class="form-control">
                            <input type="hidden" id="inputActive" name="inputActive"  class="form-control">                             
                        </form>                       
                    </div>
                    <div class="modal-footer">                        
                        <button type="button" class="btn btn-primary" id="submitDisableUser">Yes</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade bs-example-modal-sm" id="modalEditNotification" role="dialog" aria-labelledby="editNotificationProfile">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="editNotificationProfile">Edit Notification</h4>
                    </div>
                    <div class="modal-body">
                        <form role="form" id="editNotification">
                            <input type="hidden" id="selectedNotification" name="selectedNotification"  class="form-control">
                            <!-- <input type="hidden" id="selectedUser" name="selectedUser"  class="form-control">-->
                            <!--<div class="form-group">
                                <label for="inputCustomerName">Customer Name</label>
                                <input type="text" class="form-control" id="inputCustomerName" name="inputCustomerName" placeholder="Customer Name" required>
                            </div>-->
                            <div class="form-group">
                                <label for="inputDueOn">Due On</label>
                                <div class="input-group date">
                                    <input type="text" class="form-control" id="inputDueOn" name="inputDueOn" data-provide="datepicker">
                                    <div class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="inputNotes">Followup Notes</label>
                                <input type="text" class="form-control" id="inputNotes" name="inputNotes" placeholder="Followup Notes">
                            </div>
                            <div class="form-group">
                                <label for="inputMode">Mode</label>
                                <select id="inputMode" name="inputMode" class="form-control" style="width: 100%;" >
                                    <option value="call">call</option>
                                    <option value="email">email</option>
                                    <option value="visit">visit</option>
                                </select>                            
                            </div>

                            <div class="form-group">
                                <label for="inputStatus">status</label>
                                <select type="text" id="inputStatus" name="inputStatus" class="form-control" style="width: 100%;" >                                            
                                    <option value="closed">closed</option>  
                                    <option value="pending">pending</option>
                                </select>
                                <!--<input type="text" class="form-control" id="inputStatus" name="inputStatus" placeholder="status">-->
                            </div>

                            <div>
                                <button type="button" class="btn btn-primary" id="btnSubmitEditNotification">Update</button>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                    </div>
                </div>        
            </div>    
        </div> 
        <jsp:include page="userEdit.jsp"/> 
        <div class="modal fade bs-example-modal-sm" id="modalChangePwd" tabindex="-1" role="dialog" aria-labelledby="changePwd">
            <div class="modal-dialog modal-sm">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="changePwd">Change Password</h4>
                    </div>
                    <div class="modal-body">
                        <form role="form" id="formChangePwd">
                            <input type="hidden" id="inputId" name="inputId"  class="form-control">
                            <div class="form-group">
                                <label for="inputPassword">Current Password</label>
                                <input type="password" class="form-control" id="inputPassword" name="inputPassword" placeholder="Current Password" required>
                            </div>
                            <div class="form-group">
                                <label for="inputNewPassword">New Password</label>
                                <input type="password" class="form-control" id="inputNewPassword" name="inputNewPassword" placeholder="New Password" required>
                            </div>
                            <div class="form-group">
                                <label for="inputConfirmPassword">Confirm Password</label>
                                <input type="password" class="form-control" id="inputConfirmPassword" name="inputConfirmPassword" placeholder="Confirm New Password" required>
                            </div>
                            <div>
                                <button type="submit" class="btn btn-primary" id="submitChangePwd">Submit</button>
                            </div>                            
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>                        
                    </div>
                </div>

            </div>
        </div>

        <div class="wrapper">

            <!-- Navigation -->
            <jsp:include page="navigation_top.jsp" flush="false" />

            <jsp:include page="navigation_left.jsp" flush="false" />
            <!-- /.Navigation -->

            <!-- Content Wrapper. Contains page content -->
            <div class="content-wrapper">
                <section class="content-header">
                    <h1>
                        User Profile
                        <!--<small>Optional description</small>-->
                    </h1>
                    <!--                    <ol class="breadcrumb">
                                            <li><a href="#"><i class="fa fa-dashboard"></i> Level</a></li>
                                            <li class="active">Here</li>
                                        </ol>-->
                </section>                
                <!-- Content Header (Page header) -->
                <section class="content">
                    <div class="row">

                        <div class="col-sm-4">

                            <!-- Profile Image -->
                            <div class="box box-primary">                                
                                <div class="box-body box-profile">                                    
                                    <img class="profile-user-img img-responsive img-circle" src="../adminlte2/dist/img/male.png" alt="User profile picture">                                    
                                    <h3 class="profile-username text-center" id="profileName"></h3>
                                    <p class="text-muted text-center" id="profileTitle"></p>

                                    <ul class="list-group list-group-unbordered" style="margin: 0px">
                                        <li class="list-group-item">
                                            <b>Total Sales</b> <a class="pull-right" id="totalsales"></a>
                                        </li>
                                        <li class="list-group-item">
                                            <b>Customers Attended</b> <a class="pull-right" id="customersAttended"></a>
                                        </li>
                                        <li class="list-group-item">
                                            <b>Pending Followup</b> <a class="pull-right" id="pendingFollowup"></a>
                                        </li>
                                    </ul>
                                </div><!-- /.box-body -->
                            </div><!-- /.box -->

                            <!-- About Me Box -->
                            <div class="box box-primary">
                                <div class="box-header with-border">
                                    <h3 class="box-title" id="aboutName"></h3>
                                    <% if (user.getId() == Integer.valueOf(request.getParameter("id"))) { %>
                                    <a href="#" class="text-light-blue pull-right" data-toggle="modal" data-target="#modalChangePwd">Change Password</a>
                                    <% }%>
                                </div><!-- /.box-header -->
                                <div class="box-body">
                                    <ul class="list-group list-group-unbordered" style="margin: 0px">
                                        <li class="list-group-item">
                                            <strong><i class="fa fa-lock margin-r-5"></i> User Name</strong>
                                            <p class="text-muted pull-right"><a href="#" id="aboutUserName"></a></p>
                                        </li>
                                        <li class="list-group-item">
                                            <strong><i class="fa fa-user margin-r-5"></i> Manager</strong>
                                            <p class="text-muted pull-right"><a href="#" id="aboutManager"></a></p>
                                        </li>
                                        <li class="list-group-item">
                                            <strong><i class="fa fa-users margin-r-5"></i> Department</strong>
                                            <p class="text-muted pull-right" id="aboutDepartment">-</p>
                                        </li>
                                        <li class="list-group-item">
                                            <strong><i class="fa fa-user margin-r-5"></i> Job Title</strong>
                                            <p class="text-muted pull-right" id="aboutJobTitle">-</p>
                                        </li>
                                        <li class="list-group-item">
                                            <strong><i class="fa fa-mobile margin-r-5"></i>  Mobile No.</strong>
                                            <p class="text-muted pull-right"><a href="#" id="aboutMobileNo">-</a></p>
                                        </li>
                                        <li class="list-group-item">
                                            <strong><i class="fa fa-phone margin-r-5"></i> Alt. Phone No</strong>
                                            <p class="text-muted pull-right" id="aboutAltPhoneNo">-</p>
                                        </li>
                                        <li class="list-group-item">
                                            <strong><i class="fa fa-bank margin-r-5"></i> Role</strong>
                                            <p class="text-muted pull-right" id="aboutRoleName">-</p>
                                        </li>
                                        <li class="list-group-item">
                                            <strong><i class="fa fa-lock margin-r-5"></i> Status</strong>
                                            <p class="text-muted pull-right" id="aboutStatus">-</p>
                                        </li>
                                    </ul>
                                    <button id="btnEditUser" type="button" class="btn btn-primary" data-toggle="modal" data-target="#modalEditUser">Edit</button>
                                    <button id="btnDisableUser" type="button" class="btn btn-danger" data-toggle="modal" data-target="#modalDisableUser">Disable</button>
                                </div><!-- /.box-body -->
                            </div><!-- /.box -->
                            <!-- Additional Information Box -->
                            <div class="box box-primary">
                                <div class="box-header with-border">
                                    <h3 class="box-title">Additional Information</h3>
                                </div><!-- /.box-header -->
                                <div class="box-body">
                                    <ul class="list-group list-group-unbordered" style="margin: 0px">
                                        <li class="list-group-item">
                                            <strong><i class="fa fa-clock-o margin-r-5"></i> Last Login</strong>
                                            <p class="text-muted pull-right" id="addlLastLogin"></p>
                                        </li>
                                        <li class="list-group-item">
                                            <strong><i class="fa fa-tablet margin-r-5"></i> Last Login Device</strong>
                                            <p class="text-muted pull-right" id="addlLastLoginFrom"></p>
                                        </li>
                                        <li class="list-group-item">
                                            <strong><i class="fa fa-clock-o margin-r-5"></i> Created On</strong>
                                            <p class="text-muted pull-right" id="addlCreatedOn">-</p>
                                        </li>
                                        <li class="list-group-item">
                                            <strong><i class="fa fa-user margin-r-5"></i> Created By</strong>
                                            <p class="text-muted pull-right"><a href="#" id="addlCreatedBy"></a></p>
                                        </li>
                                        <li class="list-group-item">
                                            <strong><i class="fa fa-clock-o margin-r-5"></i> Updated On</strong>
                                            <p class="text-muted pull-right" id="addlLastUpdatedOn">-</p>
                                        </li>
                                        <li class="list-group-item">
                                            <strong><i class="fa fa-user margin-r-5"></i> Updated By</strong>
                                            <p class="text-muted pull-right"><a href="#" id="addlLastUpdatedBy"></a></p>
                                        </li>
                                    </ul>
                                </div><!-- /.box-body -->
                            </div><!-- /.box -->
                        </div><!-- /.col -->
                        <div class="col-sm-8">
                            <div class="nav-tabs-custom">
                                <ul class="nav nav-tabs">


                                    <li class="active"><a href="#analytics" data-toggle="tab">Analytics</a></li>  
                                    <li><a href="#activities" data-toggle="tab">Activity</a></li>
                                    <li><a href="#activitylog" data-toggle="tab">Activity Log</a></li>
                                    <li><a href="#notifications" data-toggle="tab">Notifications</a></li>
                                    <li><a href="#sales" data-toggle="tab">Sales History</a></li>
                                    <li><a href="#tracking" data-toggle="tab">Tracking</a></li>
                                </ul>
                                <div class="tab-content">                                                       

                                    <div class="tab-pane" id="activities">
                                        <!-- The timeline -->
                                        <ul class="timeline timeline-inverse">

                                        </ul>

                                    </div><!-- /.tab-pane -->                                   



                                    <div class="active tab-pane" id="analytics">
                                        <div class="row" id="filters">
                                            <div class="col-md-12 col-sm-12 col-xs-12">

                                                <div class="col-md-4 col-sm-6 col-xs-12">
                                                    <div class="form-group">
                                                        <select id="selectDateRange" name="selectDateRange" class="form-control" style="width: 100%;">
                                                            <option value="last7Days">Last 7 Days</option>
                                                            <option value="lastMonth">Last Month</option>
                                                            <option value="lastQuarter">Last Quarter</option>
                                                            <option value="last6Months">Last 6 Months</option>
                                                            <option value="lastYear">Last Year</option>    
                                                            <!--<option value="dateRange">Select Date Range</option>-->
                                                        </select>                                
                                                    </div>
                                                </div> 
                                                <div class="col-md-4 col-sm-6 col-xs-12" hidden id="fromDate">    
                                                    <div class="form-group">

                                                        <div class="input-group input-daterange">
                                                            <input type="text" class="form-control" id="inputFromDate" name="inputFromDate" data-provide="datepicker">
                                                            <span class="input-group-addon">to</span>
                                                            <input type="text" class="form-control" id="inputToDate" name="inputToDate" data-provide="datepicker">
                                                        </div>
                                                    </div>
                                                </div>    
                                                <div class="col-md-4 col-sm-6 col-xs-12">    
                                                    <button type="button" id="btnApplyFilter" class="btn btn-primary btn-sm">Apply Date Filter</button>
                                                    <button type="button" id="resetFilter" class="btn btn-link"><small>Reset Filters</small></button>
                                                </div>                                    

                                            </div>                        
                                        </div>   


                                        <div class="box box-primary">
                                            <div class="box-header">
                                                <h3 class="box-title">Visits</h3>
                                                <div class="box-tools pull-right">
                                                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                                                    <button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
                                                </div>
                                            </div>
                                            <div class="box-body">
                                                <div class="chart">
                                                    <canvas id="chart1" style="height:310px"></canvas>
                                                </div>
                                            </div><!-- /.box-body -->
                                        </div>

                                        <!-- The timeline -->
                                        <!-- /.box -->                             
                                        <div class="box box-primary">
                                            <div class="box-header with-border">
                                                <h3 class="box-title">Sales</h3>
                                                <div class="box-tools pull-right">
                                                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                                                    <button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
                                                </div>
                                            </div>
                                            <div class="box-body">
                                                <div class="chart">
                                                    <canvas id="chart2" style="height:310px"></canvas>
                                                </div>
                                            </div><!-- /.box-body -->
                                        </div>                                       
                                    </div><!-- /.tab-pane -->  
                                    <div class="tab-pane" id="activitylog">
                                        <div class="box">
                                            <div class="box-header with-border">
                                                <!--<h3 class="box-title">Call Log</h3>-->
                                            </div>
                                            <div class="box-body table-responsive">
                                                <table class="table table-hover" id="list_call" cellspacing="0" width="100%">
                                                    <thead>
                                                        <tr>
                                                            <th>Call</th>
                                                            <th>Date</th>                                                            
                                                            <th>Customer Name</th>
                                                            <th>State</th>
                                                            <!--<th>Attachment</th>-->
                                                            <th>Duration(secs)</th>
                                                        </tr>
                                                    </thead>                                                   
                                                </table>
                                            </div><!-- /.box-body -->
                                        </div>
                                    </div>
                                    <div class="tab-pane" id="notifications">
                                        <!-- The timeline -->
                                        <div class="box">    
                                            <div class="box-header with-border">
                                                <button id="btnEditNotification" type="button" class="btn btn-primary" data-toggle="modal" data-target="#modalEditNotification">Edit</button>
                                            </div>
                                            <div class="box-body table-responsive">
                                                <table class="table table-hover" id="list_notification" cellspacing="0" width="100%">                                                    
                                                    <thead>
                                                        <tr>
                                                            <th>Customer Name</th>                                                           
                                                            <th>Follow Up Date</th>
                                                            <th>Follow Up Notes</th>
                                                            <th>Mode</th>
                                                            <th>status</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                    </tbody>
                                                </table>
                                            </div><!-- /.box-body -->
                                        </div>
                                    </div>
                                    <div class="tab-pane" id="sales">
                                        <div class="box">                                            
                                            <div class="box-body table-responsive">
                                                <table class="table table-hover" id="list_sales" cellspacing="0" width="100%">                                                    
                                                    <thead>
                                                        <tr>
                                                            <th>Customer Name</th>
                                                            <th>Project Name</th>
                                                            <th>Unit Name</th>
                                                            <th>State</th> 
                                                            <th>Date</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                    </tbody>
                                                </table>
                                            </div><!-- /.box-body -->
                                        </div>
                                    </div>
                                    <div class="tab-pane" id="tracking">                                    

                                        <div class="box">                                            
                                            <div class="box-body table-responsive">
                                                <table class="table table-hover" id="list_tracking" cellspacing="0" width="100%">                                                    
                                                    <thead>
                                                        <tr>

                                                            <th>Project Name</th>
                                                            <th>Section Name</th>
                                                            <th>Time spent(Secs)</th> 
                                                            <!--                                                            <th>Date</th>-->
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                    </tbody>
                                                </table>
                                            </div><!-- /.box-body -->
                                        </div>
                                    </div>
                                </div><!-- /.tab-content -->
                            </div><!-- /.nav-tabs-custom -->
                        </div><!-- /.col -->
                    </div>   <!-- /.row -->
                </section><!-- /.content -->
            </div><!-- /.content-wrapper -->

            <jsp:include page="control_sidebar.jsp"/>

        </div>
        <!-- /#wrapper -->

        <jsp:include page="footer.jsp"/>
        <jsp:include page="datatables_js.jsp"/>
        <script src="../adminlte2/plugins/select2/select2.full.min.js"></script> 
        <script src="../adminlte2/plugins/datepicker/js/bootstrap-datepicker.min.js"></script>
        <script src="../adminlte2/plugins/chartjs/Chart.bundle.min.js"></script>       
        <jsp:include page="adminlte_js.jsp"/> 

        <script type="text/javascript">
    var userId = <%=user.getId() != null ? user.getId().intValue() : 0%>;
    var userCompany = <%=user.getCompany() != null ? user.getCompany().intValue() : 0%>;
    var editCtx = <%=editCtx != null ? editCtx.getContextLevel() : -1%>;
    var deleteCtx = <%=deleteCtx != null ? deleteCtx.getContextLevel() : -1%>;
    var viewCtx = <%=viewCtx != null ? viewCtx.getContextLevel() : -1%>;
    var user = <%=request.getParameter("id")%>;
    var selectDateRange = $("#selectDateRange");
    var inputFromDate = $("#inputFromDate");
    var inputToDate = $("#inputToDate");
    var selectedDateRange;
    var selectedNotification = 0;
    var selectedProject = 0;
    var tableSales;
    var tableTrack;
    var tableNotification;
    var selectProject = $("#selectProject");
    var selectedCompany = <%=user.getCompany() == null ? 0 : user.getCompany()%>;
    var btnEdit = $('#btnEditUser');
    var btnDisable = $('#btnDisableUser');
    $(function () {
//        $.ajax({
//            type: "POST",
//            url: "../context?action=listProjectsByContext",
//            data: {
//                "selectedCompany": selectedCompany,
//                "resource": "project",
//                "act": "view"
//            },
//            dataType: 'json'
//        })
//                .done(function (data, textStatus, jqXHR) {
//                    var response = data;
//                    if (response.result === "error") {
//                        ajaxHandleError(response);
//                    } else {
//                        projectsData = data.data;
//                        loadProjects();
//                    }
//                })
//                .fail(function (jqXHR, textStatus, errorThrown) {
//                    var respJson = JSON.parse(jqXHR.responseText);
//                    var response = jQuery.parseJSON(respJson);
//                    $.alert(response.errorMsg, "Error !!");
//                });

        function loadProjects() {
            selectProject.select2({
                data: projectsData,
                placeholder: "Select Project",
                minimumResultsForSearch: "Infinity",
                allowClear: false,
                escapeMarkup: function (markup) {
                    return markup; // let our custom formatter work
                },
                templateResult: function (repo) {
                    if (repo.loading)
                        return repo.text;
                    return repo.name;
                },
                templateSelection: function (repo) {
                    return repo.name || repo.text;
                }
            }).on("change", function (e) {
                if (typeof this.value !== "undefined"
                        && this.value !== '') {
                    selectedProject = this.value;
                    //reloadAllElements();
                }
            });
            selectedProject = $("#selectProject").val();
            //reloadAllElements();        
        }
    });
    function refreshProfile() {
        $.ajax({
            url: "../user",
            data: 'action=userProfile&id=<%=request.getParameter("id")%>',
            type: "GET",
            dataType: "json"
        })
                .done(function (data, textStatus, jqXHR) {
                    $('#profileName').text(data.firstName + ' ' + data.lastName);
                    $('#profileTitle').text(data.jobTitle);
                    $('#aboutName').text('About ' + data.firstName);
                    if (data.username === '') {
                        $('#aboutUserName').removeAttr('href');
                    } else {
                        $('#aboutUserName').text(data.username);
                        $('#aboutUserName').attr('href', 'mailto:' + data.username);
                    }
                    if (data.mobileNo === '') {
                        $('#aboutMobileNo').removeAttr('href');
                    } else {
                        $('#aboutMobileNo').text('+91 ' + data.mobileNo);
                        $('#aboutMobileNo').attr('href', 'tel:+91' + data.mobileNo);
                    }
                    $('#aboutAltPhoneNo').text(data.altPhoneNo);
                    if (data.manager === 0) {
                        $('#aboutManager').text("No Manager");
                        $('#aboutManager').removeAttr('href');
                    } else {
                        $('#aboutManager').text(data.managerName);
                        $('#aboutManager').attr('href', 'userProfile.jsp?id=' + data.manager);
                    }
                    $('#aboutDepartment').text(data.department);
                    $('#aboutJobTitle').text(data.jobTitle);
                    if (data.active === 1) {
                        $('#aboutStatus').text('Active');
                        btnDisable.text('Disable');
                        // $('#disableUser').text('Disable User');                      
                        $('#titleUser').text('Disable User');
                        $('#disableUsr').text('Are you sure do you want to disable this user?');
                        btnDisable.addClass('btn btn-danger pull-right');
                    } else {
                        $('#aboutStatus').text('Disabled');
                        btnDisable.text('Enable');
                        // $('#disableUser').text('Enable User');                       
                        $('#titleUser').text('Enable User');
                        $('#disableUsr').text('Are you sure do you want to enable this user?')
                        btnDisable.addClass('btn btn-success pull-right');
                    }
                    $('#aboutRoleName').text(data.roleName);
                    $('#addlLastLogin').text(data.lastLogin);
                    $('#addlLastLoginFrom').text(data.lastLoginFrom);
                    $('#addlCreatedOn').text(data.createdOn);
                    $('#addlCreatedBy').text(data.createdByName);
                    $('#addlCreatedBy').attr('href', 'userProfile.jsp?id=' + data.createdBy);
                    $('#addlLastUpdatedOn').text(data.lastUpdatedOn);
                    $('#addlLastUpdatedBy').text(data.lastUpdatedByName);
                    $('#addlLastUpdatedBy').attr('href', 'userProfile.jsp?id=' + data.lastUpdatedBy);

                    if (user === 1) {
                        btnEdit.prop('disabled', true);
                        btnDisable.prop('disabled', true);
                    }

                    switch (editCtx) {
                        case 0:
                            btnEdit.show();
                            break;
                        case 1:
                            if (data.company !== userCompany) {
                                btnEdit.hide();
                            }
                            break;
                        case 2:
                            if (data.company !== userCompany) {
                                btnEdit.hide();
                            }
                        case 3:
                            if (data.company !== userCompany) {
                                btnEdit.hide();
                            }
                            break;
                        case 4:
                            if (user !== userId) {
                                btnEdit.hide();
                            }
                            break;
                        default:
                            btnEdit.hide();
                    }

                    switch (deleteCtx) {
                        case 0:
                            if (user !== userId) {
                                btnDisable.show();
                            }
                            break;
                        case 1:
                            if (data.company !== userCompany
                                    || user === userId) {
                                btnDisable.hide();
                            }
                            break;
                        case 2:
                            if (data.company !== userCompany
                                    || user === userId
                                    || data.manager !== userId) {
                                btnDisable.hide();
                            }
                        case 3:
                            if (data.company !== userCompany
                                    || user === userId) {
                                btnDisable.hide();
                            }
                            break;
                        case 4:
                            btnDisable.hide();
                            break;
                        default:
                            btnDisable.hide();
                    }

                    // Following code is needed for editUser Modal

                    //set hidden input
                    $("#editUser :input[id=inputRole]").val(data.role);
                    //set display input
                    $("#editUser :input[id=roleName]").val(data.roleName);
                    $("#editUser :input[id=roleName]").prop("disabled", true);
                    //set hidden input
                    $("#editUser :input[id=inputCompany]").val(data.company);
                    //set display input
                    $("#editUser :input[id=companyName]").val(data.companyName);
                    $("#editUser :input[id=companyName]").prop("disabled", true);

                    var editInputManager = $("#editUser :input[id=inputManager]");

                    if (data.roleRawName === "SalesRep") {
                        editInputManager.append('<option value="' + data.manager + '" selected="selected">' + data.managerName + '</option>');
                        editInputManager.trigger('change');
                        if (editCtx < 2 || data.manager === userId) {
                            editInputManager.prop("disabled", false);
                        } else {
                            editInputManager.prop("disabled", true);
                        }
                    } else {
                        editInputManager.val("");
                        editInputManager.trigger('change');
                        editInputManager.prop("disabled", true);
                    }
                    if (user === userId) {
                        editInputManager.prop("disabled", true);
                    }

                    $('#editUser :input[id=inputId]').val(user);
                    $('#editUser :input[id=inputUserName]').val(data.username);
                    $('#editUser :input[id=inputFirstName]').val(data.firstName);
                    $('#editUser :input[id=inputLastName]').val(data.lastName);
                    $('#editUser :input[id=inputJobTitle]').val(data.jobTitle);
                    $('#editUser :input[id=inputDepartment]').val(data.department);
                    $('#editUser :input:radio[name=inputGender]').filter('[value=' + data.gender + ']').prop('checked', true);
                    $('#editUser :input[id=inputMobileNo]').val(data.mobileNo);
                    $('#editUser :input[id=inputAltNo]').val(data.altPhoneNo);

                    // Above code is needed for editUser Modal
                    $('button#btnDisableUser').click(function () {
                        $("form#disableUser :input[id=inputId]").val(user);
                        $("form#disableUser :input[id=inputActive]").val(data.active);
                    });

                })
                .fail(function (jqXHR, textStatus, errorThrown) {
                    var respJson = JSON.parse(jqXHR.responseText);
                    var response = jQuery.parseJSON(respJson);
                    $.alert(response.errorMsg, "Error !!");
                });

    }
    $("#submitDisableUser").on("click", function () {
        $.ajax({
            type: "POST",
            url: "../user?action=disableUser",
            data: $("form#disableUser").serialize(),
            dataType: 'json'
        })
                .done(function (data, textStatus, jqXHR) {
                    var response = data;
                    if (response.result === "error") {
                        ajaxHandleError(response);
                    } else {
                        ajaxHandleSuccess(response);
                        $("#modalDisableUser").modal('hide');
                        refreshProfile();
                    }
                })
                .fail(function (jqXHR, textStatus, errorThrown) {
                    var respJson = JSON.parse(jqXHR.responseText);
                    var response = jQuery.parseJSON(respJson);
                    $.alert(response.errorMsg, "Error !!");
                });
    });

    function getUsersData() {
        $.ajax({
            url: "../user",
            data: 'action=getUserData&id=' + user,
            type: "GET",
            dataType: "json"
        })
                .done(function (data, textStatus, jqXHR) {
                    $('#totalsales').html(data.totalSales);
                    // console.log(data.totalSales);
                    $('#customersAttended').html(data.customersAttended);
                    $('#pendingFollowup').html(data.pendingFollowup);
                })

                .fail(function (jqXHR, textStatus, errorThrown) {
                    var respJson = JSON.parse(jqXHR.responseText);
                    var response = jQuery.parseJSON(respJson);
                    $.alert(response.errorMsg, "Error !!");
                });
    }
    $(function () {
        if ((editCtx >= 0 && editCtx <= 2) || user === userId) {
            refreshProfile();
            refreshActivity();
            getVisitsData();
            getSalesdata();
            getUsersData();
            loadTracking();
            loadSalesHistory();
            loadCallData();
            loadNotifications();
        } else {
            $('#btnEditNotification').prop('disabled', true);
            $("#btnApplyFilter").prop('disabled', true);
            btnEdit.prop('disabled', true);
            btnDisable.prop('disabled', true);
        }

    });

    function refreshActivity() {
        $.ajax({
            url: "../user",
            data: 'action=userActivity&id=' + user,
            type: "GET",
            dataType: "json"
        })
                .done(function (data, textStatus, jqXHR) {
                    var activities = '';
                    var prevDate = '';
                    for (var i = 0; i < data.length; i++) {
                        var actyear = data[i].activityDate.match(/\d{4}/);
                        var splitdate = data[i].activityDate.split(/\d{4}/);
                        var actdate = splitdate[0] + actyear;

                        if (prevDate === actdate) {

                            activities = activities + getActivityItemLi(data[i].activityType,
                                    splitdate[1], data[i].id, data[i].customerName, data[i].projectName);
                        } else {

                            prevDate = actdate;
                            activities = activities + getActivityDateLi(actdate);
                            activities = activities + getActivityItemLi(data[i].activityType,
                                    splitdate[1], data[i].id, data[i].customerName, data[i].projectName);
                        }
                    }

                    $("#activities ul").append(activities);
                })
                .fail(function (jqXHR, textStatus, errorThrown) {
                    var respJson = JSON.parse(jqXHR.responseText);
                    var response = jQuery.parseJSON(respJson);
                    $.alert(response.errorMsg, "Error !!");
                });
    }

    function getActivityDateLi(date) {
        return '<li class="time-label">' +
                '<span class="bg-blue">' +
                date +
                '</span>' +
                '</li>';
    }

    function getActivityItemLi(type, time, id, name, projectName) {
        var a_class = '';
        var text = '';
        if (type === 'email') {
            a_class = 'fa fa-envelope bg-blue';
            text = projectName + ' :: Emailed ';
        } else if (type === 'visit') {
            a_class = 'fa fa-user bg-aqua';
            text = projectName + ' :: Attended ';
        }
        return '<li>' +
                '<i class="' + a_class + '"></i>' +
                '<div class="timeline-item">' +
                '<span class="time"><i class="fa fa-clock-o"></i> ' + time + '</span>' +
                '<h3 class="timeline-header">' + text +
                '<a href="customerProfile.jsp?id=' + id + '">' + name + '</a>' +
                '</h3>' +
                '</div>' +
                '</li>';
    }



    var lineChartOptions = {
        scales: {
            yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
        }
    };
    var barChartOptions = {
        scales: {
            yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
        }
    };
    var chart1;
    var chart2;

    selectDateRange.select2({minimumResultsForSearch: "Infinity"}).on("change", function (e) {
        if (typeof this.value !== "undefined"
                && this.value !== '') {
            selectedDateRange = this.value;
            if (this.value === 'dateRange') {
                $("#fromDate").show();
                $("#toDate").show();
            } else {
                $("#fromDate").hide();
                $("#toDate").hide();
            }
        }
    });
    inputFromDate.datepicker({
        format: "dd-mm-yyyy",
        endDate: "0d",
        todayHighlight: true,
        autoclose: true
    });

    inputToDate.datepicker({
        format: "dd-mm-yyyy",
        startDate: "-6m",
        endDate: "0d",
        todayHighlight: true,
        autoclose: true
    });
    $('.input-daterange').datepicker({
        todayBtn: "linked"
    });
    $("#btnApplyFilter").on("click", function (event) {
        getVisitsData();
        getSalesdata();

    });

    $("#resetFilter").click(function () {
        selectDateRange.val("last7Days").trigger("change");
//                    inputFromDate.datepicker().clearDate();
//                    inputToDate.datepicker().clearDate();
    });


    function getVisitsData() {
        $.ajax({
            url: "../dashboard?action=getVisitsDataForUser",
            data: {
                selectedUser: user,
                selectedDateRange: selectDateRange.val(),
                inputFromDate: inputFromDate.val(),
                inputToDate: inputToDate.val()
            },
            type: "POST",
            dataType: "json"
        })

                .done(function (data, textStatus, jqXHR) {
//                                chart1Data = data;
                    if (chart1 !== null &&
                            typeof chart1 !== "undefined") {
                        chart1.destroy();
                    }
                    var ctx = $("#chart1");
                    chart1 = new Chart(ctx, {
                        type: 'line',
                        data: data,
                        options: lineChartOptions
                    });
//                    chart1 = new Chart($("#chart1").get(0).getContext("2d")).Line(data, lineChartOptions);
                })
                .fail(function (jqXHR, textStatus, errorThrown) {
                    var respJson = JSON.parse(jqXHR.responseText);
                    var response = jQuery.parseJSON(respJson);
                    $.alert(response.errorMsg, "Error !!");
                });
    }
    function getSalesdata() {
        $.ajax({
            url: "../dashboard?action=getSalesDataForUser",
            data: {
                selectedUser: user,
                selectedDateRange: selectDateRange.val(),
                inputFromDate: inputFromDate.val(),
                inputToDate: inputToDate.val()
            },
            type: "POST",
            dataType: "json"
        })

                .done(function (data, textStatus, jqXHR) {
//                                chart1Data = data;
                    if (chart2 !== null &&
                            typeof chart2 !== "undefined") {
                        chart2.destroy();
                    }
                    var ctx = $("#chart2");
                    chart2 = new Chart(ctx, {
                        type: 'bar',
                        data: data,
                        options: barChartOptions
                    });
//                    chart2 = new Chart($("#chart2").get(0).getContext("2d")).Line(data, lineChartOptions);
                })
                .fail(function (jqXHR, textStatus, errorThrown) {
                    var respJson = JSON.parse(jqXHR.responseText);
                    var response = jQuery.parseJSON(respJson);
                    $.alert(response.errorMsg, "Error !!");
                });
    }

    $("form#editNotification #inputDueOn").datepicker({
        format: "dd-mm-yyyy",
        startDate: "0d",
        todayHighlight: true,
        autoclose: true
    });

    function loadCallData() {
        tableCallData = $('#list_call').DataTable({
            "lengthChange": true,
            "paging": true,
            "responsive": true,
            "ordering": false,
            "info": false,
            "searching": true,
            "ajax": {
                "url": '../user?action=listCallDataForUser&user=' + user,
                "type": "POST",
                "dataType": 'json',
            },
            "columns": [
                {"data": "logFile"},
                {"data": "starttime"},
                {"data": "customerName"},
                {"data": "status"}, //                           
                {"data": "duration"}
            ],
            "columnDefs": [
                {
                    "render": function (data, type, columns) {
                        if (columns["logFile"] !== '' &&
                                typeof columns["logFile"] !== 'undefined') {
                            return '<audio controls><source src="http://mcube.vmc.in/sounds/' +
                                    columns["logFile"] + '" type="audio/wav">' +
                                    '</audio>';
                        }
                    }, "targets": [0]
                },
                {
                    "render": function (data, type, columns) {
                        if (columns["customerName"] === null) {
                            return '';
                        } else {
                            return '<a href="customerProfile.jsp?id=' + columns["customer"] + '">' + columns["customerName"] + '</a>';
                        }
                    }, "targets": [2]
                },
                {
                    "render": function (data, type, columns) {
                        if (columns["status"] === null) {
                            return '';
                        } else {
                            var status = getStatus(columns["status"]);
                            return '<i class="fa ' + status + '"></i>';
                        }

                    }, "targets": [3]
                }
            ]
        });
    }

    function getStatus(status1) {
        var status = "";
        switch (status1) {
            case "ANSWER":
                status = "fa-compress";
                break;
            case "CONNECTING":
                status = "fa-expand";
                break;
            case "CANCEL":
                status = " fa-tty";
                break;

        }
        return status;
    }

    function loadNotifications() {
        tableNotification = $('#list_notification').DataTable({
            "paging": true,
            "responsive": true,
            "ordering": false,
            "info": false,
            "searching": true,
            "lengthChange": true,
            "ajax": {
                "url": '../user?action=listNotificationForUser&id=' + user,
                "type": "POST",
                "dataType": 'json',
            },
            "columns": [
                {"data": "customerName"},
                {"data": "dueOn"},
                {"data": "notes"},
                {"data": "mode"},
                {"data": "statusName"}

            ],
            "columnDefs": [
                {
                    "render": function (data, type, columns) {
                        if (columns["customerName"] === null) {
                            return '';
                        } else {
                            return '<a href="customerProfile.jsp?id=' + columns["customer"] + '">' + columns["customerName"] + '</a>';
                        }
                    }, "targets": [0]
                },
                {
                    "render": function (data, type, columns) {
                        if (data === null || data === '' || typeof data === "undefined") {
                            return '';
                        } else {
                            return convertDate(data);
                        }
                    }, "targets": [1]
                },
                {
                    "render": function (data, type, columns) {
                        if (columns["mode"] === null) {
                            return '';
                        } else {
                            var modeClass = getModeClass(columns["mode"]);
                            return '<i class="fa ' + modeClass + '">' +
                                    '</i>';
                        }
                    }, "targets": [3]
                },
                {
                    "render": function (data, type, columns) {
                        if (columns["status"] === null) {
                            return '';
                        } else {
                            var statusClass = getStatusClass(columns["statusName"]);
                            return '<span class="label ' + statusClass + '">' + columns["statusName"] + '</span>';
                        }
                    }, "targets": [4]
                }
            ]
        });

        toggleEditNotificationBtn();
    }

    $('#list_notification tbody').on('click', 'tr', function () {
        var data = tableNotification.row(this).data();
        if ($(this).hasClass('active')) {
            $(this).removeClass('active');
        } else {
            tableNotification.$('tr.active').removeClass('active');
            $(this).addClass('active');
            if (typeof data !== "undefined") {
                selectedNotification = data["id"];
            } else {
                selectedNotification = 0;
            }
        }
        toggleEditNotificationBtn();

    });
    function getStatusClass(statusName) {
        var statusClass = "label-danger";
        switch (statusName) {
            case "pending":
                statusClass = "label-danger";
                break;
            case "closed":
                statusClass = "label-success";
                break;

        }
        return statusClass;
    }
    function getModeClass(mode) {
        var modeClass = "fa-clock-o";
        switch (mode) {
            case "call":
                modeClass = "fa-phone";
                break;
            case "email":
                modeClass = "fa-envelope";
                break;
            case "visit":
                modeClass = "fa-user";
                break;

        }
        return modeClass;
    }
    //}
    function toggleEditNotificationBtn() {
        if (tableNotification.rows('.active').data().length === 1) {
            $('#btnEditNotification').prop('disabled', false);
        } else {
            $('#btnEditNotification').prop('disabled', true);
        }
    }

    $('button#btnEditNotification').click(function () {
        var data = tableNotification.row('.active').data();
        //  console.log(data["mode"]);
        //  $("#editNotification :input[id=inputCustomerName]").val(data["customerName"]);
        $("#editNotification :input[id=selectedNotification]").val(data["id"]);
        // $("#editNotification :input[id=selectedUser]").val(data["user"]);
//        $("#editNotification :input[id=inputDueOn]").val(data["dueOn"]);
        $("#editNotification :input[id=inputNotes]").val(data["notes"]);
        $("#editNotification :input[id=inputMode]").val(data["mode"]);
        $("#editNotification :input[id=inputStatus]").val(data["statusName"]);
        $("#editNotification :input[id=inputDueOn]").val(convertDate(data["dueOn"]));

    });

    $("#btnSubmitEditNotification").on("click", function () {
        submitEditNotification($("form#editNotification"));
        //console.log($("form#editNotification"));
    });

    function submitEditNotification(form) {
        $.ajax({
            type: "POST",
            url: "../user?action=updateNotification",
            data: form.serialize(),
            dataType: 'json'
        })
                .done(function (data, textStatus, jqXHR) {
                    var response = data;
                    if (response.result === "error") {
                        ajaxHandleError(response);
                    } else {
                        ajaxHandleSuccess(response);
                        $("#modalEditNotification").modal('hide');
                        tableNotification.ajax.reload();
                    }
                })
                .fail(function (jqXHR, textStatus, errorThrown) {
                    alert(data);
                    var respJson = JSON.parse(jqXHR.responseText);
                    var response = jQuery.parseJSON(respJson);
                    $.alert(response.errorMsg, "Error !!");
                });
    }
    //  function reloadTableNotification() {
    //     tableNotification.ajax.reload();
    //  }
    function loadSalesHistory() {
        tableSales = $('#list_sales').DataTable({
            "paging": false,
            "responsive": true,
            "ordering": false,
            "info": false,
            "searching": true,
            "lengthChange": true,
            "ajax": {
                "url": '../inventory?action=listSalesHistoryForUser&id=' + user,
                "type": "POST",
                "dataType": 'json',
            },
            "columns": [
                {"data": "customerName"},
                {"data": "projectName"},
                {"data": "name"},
                {"data": "stateName"},
                {"data": "actionedOn"}
            ],
            "columnDefs": [
                {
                    "render": function (data, type, columns) {
                        if (columns["customerName"] === null) {
                            return '';
                        } else {
                            return '<a href="customerProfile.jsp?id=' + columns["customer"] + '">' + columns["customerName"] + '</a>';
                        }
                    }, "targets": [0]
                },
                {
                    "render": function (data, type, columns) {
                        if (data === null || data === '' || typeof data === "undefined") {
                            return '';
                        } else {
                            return convertDate(data);
                        }
                    }, "targets": [4]
                }
            ]

        });
    }

    function loadTracking() {
        tableTrack = $('#list_tracking').DataTable({
            "paging": false,
            "responsive": true,
            "ordering": false,
            "info": false,
            "searching": false,
            "lengthChange": false,
            "ajax": {
                "url": '../user?action=getAvgTimeSpendForSection&id=' + user,
                "type": "POST",
                "dataType": 'json',
            },
            "columns": [
                //{"data": "customerName"},
                {"data": "projectName"},
                {"data": "sectionName"},
                {"data": "timeSpent"}
//                {"data": "visitedOn"}
            ]
//            "columnDefs": [
//                {
//                    "render": function (data, type, columns) {
//                        if (data === null || data === '' || typeof data === "undefined") {
//                            return '';
//                        } else {
//                            return convertDate(data);
//                        }
//                    }, "targets": [3]
//                }
//            ]
        });
    }

    $(function () {
        var changePwdForm = $('form#formChangePwd');

        function submitChangePwd() {
            $.ajax({
                type: "POST",
                url: "../user?action=changePwd",
                data: $('form#formChangePwd').serialize(),
                dataType: 'json'
            })
                    .done(function (data, textStatus, jqXHR) {
                        var response = jQuery.parseJSON(data);
                        if (response.result === "error") {
                            ajaxHandleError(response);
                        } else {
                            ajaxHandleSuccess(response);
                            $("#modalChangePwd").modal('hide');
                        }
                    })
                    .fail(function (jqXHR, textStatus, errorThrown) {
                        var respJson = JSON.parse(jqXHR.responseText);
                        var response = jQuery.parseJSON(respJson);
                        $.alert(response.errorMsg, "Error !!");
                    });
        }

        var changePwdValidator = changePwdForm.validate({
//            debug: true,
            focusCleanup: true,
            rules: {
                inputPassword: {
                    required: true,
                    remote: {
                        url: '../user?action=validateUserPassword',
                        type: "post",
                        data:
                                {
                                    inputPassword: function ()
                                    {
                                        return $('#formChangePwd :input[id=inputPassword]').val();
                                    }
                                }
                    }
                },
                inputNewPassword: {
                    required: true,
                    maxlength: 20,
//(/^
//(?=.*\d)                //should contain at least one digit
//(?=.*[a-z])             //should contain at least one lower case
//(?=.*[A-Z])             //should contain at least one upper case
//[a-zA-Z0-9]{6,}         //should contain at least 6 from the mentioned characters
//$/)                
                    pattern: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{6,}$/
                },
                inputConfirmPassword: {
                    required: true,
                    equalTo: "#inputNewPassword"
                }
            },
            messages: {
                inputPassword: {
                    remote: "Password is incorrect"
                },
                inputNewPassword: {
                    maxlength: "Cannot be more than 20 characters",
                    pattern: "Must contain at least 6 characters, including 1 UPPERCASE, 1 lowercase and 1 number"
                },
                inputConfirmPassword: {
                    equalTo: "Passwords do not match"
                }
            },
            highlight: function (element) {
                $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
            },
            success: function (element) {
                $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
            },
            submitHandler: function (form) {
                submitChangePwd();
            }
        });
    });
        </script>  

        <script src="../jquery-validation/js/jquery.validate.min.js"></script>
        <script src="../jquery-validation/js/additional-methods.min.js"></script>
        <script src="userEdit.js"></script>
    </body>

</html>

