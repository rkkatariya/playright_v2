<%-- 
    Document   : users
    Created on : Oct 1, 2015, 10:26:02 AM
    Author     : Rahul
--%>
<%@page import="com.revvster.playright.access.AuthorizationManager"%>
<%@page import="com.revvster.playright.model.RoleEntContext"%>
<%@page import="java.util.List"%>
<%@page import="com.revvster.playright.access.Resource"%>
<%@page import="com.revvster.playright.access.Action"%>
<%@page import="com.revvster.playright.model.UserEntitlement"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@page import="com.revvster.playright.model.User"%>
<%@page import="com.revvster.playright.util.SystemConstants"%>
<%
    User user = new User();
    if (session.getAttribute(SystemConstants.LoggedInUser) != null) {
        user = (User) session.getAttribute(SystemConstants.LoggedInUser);
    }

    RoleEntContext addCtx = null;
    RoleEntContext editCtx = null;
    RoleEntContext deleteCtx = null;

    if (user.getUserEntitlements() != null) {
        List<UserEntitlement> ues = user.getUserEntitlements();
        addCtx = AuthorizationManager.getRoleEntContext(ues,
                new UserEntitlement(Resource.user, Action.add));

        editCtx = AuthorizationManager.getRoleEntContext(ues,
                new UserEntitlement(Resource.user, Action.update));
        deleteCtx = AuthorizationManager.getRoleEntContext(ues,
                new UserEntitlement(Resource.user, Action.delete));
    }
%>

<html lang="en">

    <head>

        <title>Users</title>

        <jsp:include page="header.jsp"/>
        <!-- DataTables -->
        <jsp:include page="datatables_css.jsp"/>

        <link rel="stylesheet" href="../adminlte2/plugins/select2/select2.min.css">
        <link rel="stylesheet" href="../adminlte2/plugins/select2/select2-bootstrap.min.css">
        <link rel="stylesheet" href="../DataTables/Buttons-1.1.0/css/buttons.dataTables.min.css">
        <link rel="stylesheet" href="../DataTables/Select-1.1.0/css/select.dataTables.min.css">
        <!--<link rel="stylesheet" href="../DataTables/DataTables-1.10.10/css/jquery.dataTables.min.css">-->
        <!--<link rel="stylesheet" href="../adminlte/plugins/select2/select2-bootstrap.min.css">-->

        <jsp:include page="adminlte_css.jsp"/>

        <style>
            tr.disabled {
                font-weight: bold;
                color: red !important;
                a.link {color: red !important; }
                background-color: lightgrey !important;
            }       
        </style>

    </head>
    <!--
    BODY TAG OPTIONS:
    =================
    Apply one or more of the following classes to get the
    desired effect
    |---------------------------------------------------------|
    | SKINS         | skin-red                               |
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
        <div class="modal fade bs-example-modal-sm" id="modalMoveCustomer" tabindex="-1" role="dialog" aria-labelledby="moveCustomer">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h3 class="modal-title" id="moveCustomers">Move Customers</h4>
                            <div>
                                <h4 id="moveCust"></h4>
                            </div>
                    </div>
                    <div class="modal-body">
                        <form role="form" id="moveCustomer">   

                            <!--                            <div id="getCustForUserc">                            
                                                            <input type="checkbox" id="checkAll">
                                                            <strong>Select all</strong>                                
                                                        </div>-->
                            <div class="col-sm-12">
                                <div class="col-sm-6">
                                    <div class="box-header">
                                        <strong> <div id="custOrSalesRep">List of customers</div></strong>
                                    </div>
                                    <div class="box-body table-responsive " id="hideForSalesrep">
                                        <table id="listCustOrSalesRep" class="table table-bordered table-striped">
                                            <thead>
                                                <tr>
                                                    <th>Name</th>
                                                </tr>
                                            </thead>                                       
                                        </table>

                                    </div>
                                    <div class="box-body table-responsive " id="hideForManager">
                                        <table id="listCustOrSalesRep1" class="table table-bordered table-striped">
                                            <thead>
                                                <tr>
                                                    <th>Name</th>
                                                </tr>
                                            </thead>                                       
                                        </table>
                                    </div>
                                </div>                                
                                <div class="col-sm-6">
                                    <div class="box-header">
                                        <div><strong>List of users</strong></div>
                                    </div>
                                    <div class="box-body table-responsive ">
                                        <table id="getUsers" class="table table-bordered table-striped">
                                            <thead>
                                                <tr>
                                                    <th>Name</th>
                                                </tr>
                                            </thead>                                       
                                        </table>
                                    </div>
                                    <!--                            <div id="dinputSalesrep" class="form-group">
                                                                    <label for="selectedUser">User</label>
                                                                    <select id="selectedUser" name="selectedUser" class="form-control" style="width: 100%;" >
                                                                    </select>                            
                                                                </div>-->
                                </div>
                            </div> 
                        </form>
                    </div>
                    <div class="modal-footer no-border">
                        <button type="button" class="btn btn-primary" id="submitMoveCustomers">Move</button>

                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>                        
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="userEdit.jsp"/> 
        <jsp:include page="userCreate.jsp"/> 
        <div class="wrapper">

            <!-- Navigation -->
            <jsp:include page="navigation_top.jsp" flush="false" />

            <jsp:include page="navigation_left.jsp" flush="false" />
            <!-- /.Navigation -->

            <!-- Content Wrapper. Contains page content -->
            <div class="content-wrapper">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                        Users
                        <!--<small>Optional description</small>-->
                    </h1>
                    <!--                    <ol class="breadcrumb">
                                            <li><a href="#"><i class="fa fa-dashboard"></i> Level</a></li>
                                            <li class="active">Here</li>
                                        </ol>-->
                </section>

                <!-- Main content -->
                <section class="content">

                    <!-- Your Page Content Here -->
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="box box-primary">
                                <div class="box-header">
                                    <!--<h3 class="box-title">Data Table With Full Features</h3>-->
                                    <button type="button" id="btnCreateUser" class="btn btn-primary" data-toggle="modal" data-target="#modalCreateUser">Add</button>
                                    <button type="button" id="btnEditUser" class="btn btn-primary" data-toggle="modal" data-target="#modalEditUser">Edit</button>
                                    <button id="btnDisableUser" type="button" class="btn btn-danger" data-toggle="modal" data-target="#modalMoveCustomer">Disable</button>  

                                </div><!-- /.box-header -->
                                <div class="box-body table-responsive">
                                    <table id="list_users" class="table table-bordered table-striped">
                                        <thead>
                                            <tr class="allSelect">
                                                <th>Id</th>
                                                <th>Login</th>
                                                <th>Name</th>
                                                <th>Last Name</th>
                                                <th>Manager Id</th>
                                                <th>Manager</th>
                                                <th>Last Login</th>
                                                <th>Mobile No</th>
                                                <th>Email Address</th>
                                                <th>Active</th>
                                                <th>Job Title</th>
                                                <th>Department</th>
                                                <th>Alt Phone No</th>
                                                <th>Gender</th>
                                                <th>Company</th>
                                            </tr>
                                        </thead>

                                        <tfoot>
                                            <tr>
                                                <th>Id</th>
                                                <th>Login</th>
                                                <th>Name</th>
                                                <th>Last Name</th>
                                                <th>Manager Id</th>
                                                <th>Manager</th>
                                                <th>Last Login</th>
                                                <th>Mobile No</th>
                                                <th>Email Address</th>
                                                <th>Active</th>
                                                <th>Job Title</th>
                                                <th>Department</th>
                                                <th>Alt Phone No</th>
                                                <th>Gender</th>
                                                <th>Company</th>
                                            </tr>
                                        </tfoot>
                                    </table>
                                </div><!-- /.box-body -->
                            </div><!-- /.box -->
                        </div><!-- /.col -->
                    </div><!-- /.row -->
                </section><!-- /.content -->
            </div><!-- /.content-wrapper -->

            <jsp:include page="control_sidebar.jsp"/>

        </div>
        <!-- /#wrapper -->

        <jsp:include page="footer.jsp"/>
        <!-- DataTables -->
        <jsp:include page="datatables_js.jsp"/>
        <script src="../adminlte2/plugins/select2/select2.full.min.js"></script>        
        <!--<script src="../DataTables/DataTables-1.10.10/js/jquery.dataTables.min.js"></script>-->        
        <script src="../DataTables/Buttons-1.1.0/js/dataTables.buttons.min.js"></script>
        <script src="../DataTables/Select-1.1.0/js/dataTables.select.min.js"></script>
        <script src="../adminlte2/plugins/datatables/extensions/sorting/date-uk.js"></script> 
        <!-- SlimScroll -->
        <script src="../adminlte2/plugins/slimScroll/jquery.slimscroll.min.js"></script>       
       
        <script src="../adminlte2/plugins/fastclick/fastclick.min.js"></script>
        <jsp:include page="adminlte_js.jsp"/>
        <script>
            var table;
            var userId = <%=user.getId() != null ? user.getId().intValue() : 0%>;
            var addCtx = <%=addCtx != null ? addCtx.getContextLevel() : -1%>;
            var editCtx = <%=editCtx != null ? editCtx.getContextLevel() : -1%>;
            var deleteCtx = <%=deleteCtx != null ? deleteCtx.getContextLevel() : -1%>;
            //var selectedUser = $("#selectedUser");
            var selectedCompany = 0;
            var selectedUsr = 0;
            var modalDisableUser = $("#modalDisableUser");
            var action = "";
            var custOrSalesRepData;
            var tableCustSalesRep;
            var tableSalesRep;
            var tableUser;
            $(function () {
                table = $('#list_users').DataTable({
                    "paging": true,
                    "responsive": true,
                    "lengthChange": true,
                    "searching": true,
                    "ordering": true,                   
                    "info": true,
                    "autoWidth": false,                                         
                    "ajax": "../user?action=listUsersByContext",
//                    "ajax": "data/users.json",
                    "columns": [
                        {"data": "id"},
                        {"data": "username"},
                        {"data": "firstName"},
                        {"data": "lastName"},
                        {"data": "manager"},
                        {"data": "managerName"},
                        {"data": "lastLogin", "defaultContent": ""},
                        {"data": "mobileNo", "defaultContent": ""},
                        {"data": "emailAddress"},
                        {"data": "active"},
                        {"data": "jobTitle", "defaultContent": ""},
                        {"data": "department", "defaultContent": ""},
                        {"data": "altPhoneNo", "defaultContent": ""},
                        {"data": "gender", "defaultContent": ""},
                        {"data": "companyName", "defaultContent": ""}
                    ],
                    "columnDefs": [
                        {
                            "visible": false, "searchable": false, "targets": [0]
                        },
                        {
                            // The `data` parameter refers to the data for the cell (defined by the
                            // `data` option, which defaults to the column being worked with, in
                            // this case `data: 0`.
                            "render": function (data, type, columns) {
                                return '<a href="userProfile.jsp?id=' + columns["id"] + '">' + data + ' ' + columns["lastName"] + '</a>';
                            },
                            "targets": [2]
                        },
                        {"visible": false, "targets": [3]},
                        {"visible": false, "targets": [4]},
                        {"render": function (data, type, columns) {
//                                console.log(columns["manager"]);
                                if (columns["manager"] === 0) {
                                    return 'No Manager';
                                } else {
                                    return '<a href="userProfile.jsp?id=' + columns["manager"] + '">' + data + '</a>';
                                }
                            }, "targets": [5]},
                        {
                            type: "date-uk", 
                            "render": function (data, type, columns) {
                                if (data === null || data === '' || typeof data === "undefined") {
                                    return '';
                                } else {
                                   return convertDate(data);
                                    
                                }
                            }, 
                            "targets": [6]
                        },
                        {"visible": false, "targets": [8]},
                        {"visible": false, "targets": [9]},
                        {"visible": false, "targets": [11]},
                        {"visible": false, "targets": [12]},
                        {"visible": false, "targets": [13]}
                    ],
                    "createdRow": function (row, data, index) {
//                        console.log(data["active"]);
                        if (data["active"] === 0) {
                            $(row).addClass('disabled');
                        }
                    }
                });
                
                jQuery.fn.dataTableExt.aTypes.unshift(
                    function ( tData )
                    {
                        var sData = tData.toString();
                       // console.log(sData);
                        if (sData !== null && sData.match(/^(0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[012])\/(19|20|21)\d\d$/))
                        {
                            return 'date-uk';
                        }
                        return null;
                    }
                );                
                // $.fn.table.moment("dddd, MMMM D, h:mm a");
                toggleEditUserBtn();
                toggleDisableUsersBtn();
//Row click
                if (editCtx !== -1 || deleteCtx !== -1) {
                    $('#list_users tbody').on('click', 'tr', function () {
                        var data = table.row(this).data();
                        // console.log(data);
                        if (data["roleName"] === "Sales Representative") {
                            $('#hideForManager').hide();
                            $('#hideForSalesrep').show();
                            if ($(this).hasClass('active')) {
                                $(this).removeClass('active');
                                selectedCompany = 0;
                                selectedUsr = 0;
                                // $('#custOrSalesRep').text('List Of Customers');
                            } else {
                                table.$('tr.active').removeClass('active');
                                $(this).addClass('active');
                                selectedCompany = data["company"];
                                selectedUsr = data["id"];
                                $('#custOrSalesRep').text('List Of Customers');
                                $('#moveCustomers').text('Move Customers');
                            }
                            // console.log(selectedCompany);
//                            $(this).toggleClass('active');
                            //action = "getCustomerForSalesrep";
                            // loadCustOrSalesRep(action);
                            $('button#btnDisableUser').click(function () {
                                loadCustOrSalesRep();
                                loadUsersForCompany();

                            });

                            toggleEditUserBtn();
                            toggleDisableUsersBtn();


                        } else if (data["roleName"] === "Manager") {
                            $('#hideForManager').show();
                            $('#hideForSalesrep').hide();
                            if ($(this).hasClass('active')) {
                                $(this).removeClass('active');
                                selectedCompany = 0;
                                selectedUsr = 0;
                            } else {
                                table.$('tr.active').removeClass('active');
                                $(this).addClass('active');
                                selectedCompany = data["company"];
                                selectedUsr = data["id"];
                                $('#custOrSalesRep').text('List Of Sales Rep');
                                $('#moveCustomers').text('Move Sales Rep');
                            }
                            $('button#btnDisableUser').click(function () {
                                loadSalesRepforManager();
                                loadUsersForCompany();

                            });
                            toggleEditUserBtn();
                            toggleDisableUsersBtn();

                        }
//                        else if (data !== null) {
//                             $('#moveCust').text('Warning!..Before disabling make sure to move all the customers');
//                        } else {
//                              $('#moveCust').text('Continue to disable');
//                        }
                    });
                }
                
                if (addCtx === -1) {
                    $("#btnCreateUser").hide();
                }
                if (editCtx === -1) {
                    $("#btnEditUser").hide();
                }
                if (deleteCtx === -1) {
                    $("#btnDisableUser").hide();
                }

//To call edit
                $('button#btnEditUser').click(function () {
                    var data = table.row('.active').data();
                    //set hidden input
                    $("#editUser :input[id=inputRole]").val(data["role"]);
                    $("#editUser :input[id=roleName]").val(data["roleName"]);
                    $("#editUser :input[id=roleName]").prop("disabled", true);
                    $("#editUser :input[id=inputCompany]").val(data["company"]);
                    //set display input
                    $("#editUser :input[id=companyName]").val(data["companyName"]);
                    $("#editUser :input[id=companyName]").prop("disabled", true);

                    var editInputManager = $("#editUser :input[id=inputManager]");

                    if (data["roleRawName"] === "SalesRep") {
                        editInputManager.append('<option value="' + data["manager"] + '" selected="selected">' + data["managerName"] + '</option>');
                        editInputManager.trigger('change');
                        if (editCtx < 2 || data["manager"] === userId) {
                            editInputManager.prop("disabled", false);
                        } else {
                            editInputManager.prop("disabled", true);
                        }
                    } else {
                        editInputManager.val("");
                        editInputManager.trigger('change');
                        editInputManager.prop("disabled", true);
                    }
                    if (data["id"] === userId) {
                        editInputManager.prop("disabled", true);
                    }

                    $('#editUser :input[id=inputId]').val(data["id"]);
                    $('#editUser :input[id=inputUserName]').val(data["username"]);
                    $('#editUser :input[id=inputFirstName]').val(data["firstName"]);
                    $('#editUser :input[id=inputLastName]').val(data["lastName"]);
                    $('#editUser :input[id=inputJobTitle]').val(data["jobTitle"]);
                    $('#editUser :input[id=inputDepartment]').val(data["department"]);
                    $('#editUser :input:radio[name=inputGender]').filter('[value=' + data["gender"] + ']').prop('checked', true);
                    $('#editUser :input[id=inputMobileNo]').val(data["mobileNo"]);
                    $('#editUser :input[id=inputAltNo]').val(data["altPhoneNo"]);

                });

//To call deactivate
//                $('button#btnDisableUser').click(function () {
//                    alert(table.rows('.active').data().length + ' row(s) selected');
//                });

// Setup - add a text input to each footer cell
                $('#list_users tfoot th').each(function () {
                    var title = $('#list_users thead th').eq($(this).index()).text();
                    $(this).html('<input type="text" placeholder="Search ' + title + '" />');
                });

// Apply the search
                table.columns().every(function () {
                    var that = this;

                    $('input', this.footer()).on('keyup change', function () {
                        if (that.search() !== this.value) {
                            that.search(this.value).draw();
                        }
                    });
                });

            });


            function toggleEditUserBtn() {
                if (table.rows('.active').data().length === 1) {
                    var data = table.row('.active').data();
                    if (editCtx < 4 && editCtx > -1) {
                        $('#btnEditUser').prop('disabled', false);
                    } else if (editCtx === 4 && data["id"] === userId) {
                        $('#btnEditUser').prop('disabled', false);
                    } else {
                        $('#btnEditUser').prop('disabled', true);
                    }
                } else {
                    $('#btnEditUser').prop('disabled', true);
                }
            }
            function toggleDisableUsersBtn() {
                if (table.rows('.active').data().length === 0) {
                    $('#btnDisableUser').prop('disabled', true);
                } else {
                    $('#btnDisableUser').prop('disabled', false);
//                    var data = table.row('.active').data();
//                    if (data["id"] === userId || data["manager"] !== userId) {
//                        $('#btnDisableUser').prop('disabled', true);
//                    } else {
//                        $('#btnDisableUser').prop('disabled', false);
//                    }
                }
            }

            function loadCustOrSalesRep() {
                tableCustSalesRep = $('#listCustOrSalesRep').DataTable({
                    "ajax": '../customer?action=getCustomerForSalesrep&selectedUsr=' + selectedUsr,
                    "columns": [
                        {"data": "name"}
                    ],
                    dom: 'Bf',
                    buttons: [
                        'selectAll',
                        'selectNone',
                        'selectRows'
                    ],
                    select: {
                        style: 'multi'
                    }
                });
                var data = tableCustSalesRep.row(this).data();
                if (data !== null) {
                    $('#moveCust').text('Warning!..Before disabling make sure to move all the customers');
                } else {
                    $('#moveCust').text('Continue to disable');
                }
            }
            function loadSalesRepforManager() {
                tableSalesRep = $('#listCustOrSalesRep1').DataTable({
                    "ajax": '../customer?action=getSalesRepFormanager&selectedUsr=' + selectedUsr,
                    "columns": [
                        {"data": "username"}
                    ],
                    dom: 'Bf',
                    buttons: [
                        'selectAll',
                        'selectNone',
                        'selectRows'
                    ],
                    select: {
                        style: 'multi'
                    }
                });
                var data = tableSalesRep.row(this).data();
                if (data !== null) {
                    $('#moveCust').text('Warning!..Before disabling make sure to move all the customers');
                } else {
                    $('#moveCust').text('Continue to disable');
                }
            }

            function loadUsersForCompany() {
                tableUser = $('#getUsers').DataTable({
                    "ajax": '../user?action=listUsersForComapny&selectedCompany=' + selectedCompany,
                    "columns": [
                        {"items": "firstName"}
                    ],
                    "dom": 'f',
                    "columnDefs": [
                        {
                            // The `data` parameter refers to the data for the cell (defined by the
                            // `data` option, which defaults to the column being worked with, in
                            // this case `data: 0`.
                            "render": function (data, type, columns) {
                                return '<a href="userProfile.jsp?id=' + columns["id"] + '">' + data + ' ' + columns["lastName"] + '</a>';
                            },
                            "targets": [0]
                        }
                    ]
                });
                $('#getUsers tbody').on('click', 'tr', function () {
                    var data = tableUser.row(this).data();
                    // console.log(data);
                    if (data["active"] === 1) {
                        if ($(this).hasClass('active')) {
                            $(this).removeClass('active');

                        } else {
                            tableUser.$('tr.active').removeClass('active');
                            $(this).addClass('active');

                        }
                    }
                });

            }




        </script>    
        <script src="../jquery-validation/js/jquery.validate.min.js"></script>
        <script src="../jquery-validation/js/additional-methods.min.js"></script>

        <script src="userEdit.js"></script>
        <script src="userCreate.js"></script>
    </body>

</html>
