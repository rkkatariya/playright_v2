<%-- 
    Document   : company
    Created on : Dec 8, 2015, 7:11:30 PM
    Author     : Rahul
--%>

<%@page import="com.revvster.playright.access.AuthorizationManager"%>
<%@page import="com.revvster.playright.model.RoleEntContext"%>
<%@page import="java.util.List"%>
<%@page import="com.revvster.playright.access.Resource"%>
<%@page import="com.revvster.playright.access.Action"%>
<%@page import="com.revvster.playright.model.UserEntitlement"%>
<%@page import="com.revvster.playright.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.revvster.playright.util.SystemConstants"%>
<!DOCTYPE html>
<%
    User user = new User();
    if (session.getAttribute(SystemConstants.LoggedInUser) != null) {
        user = (User) session.getAttribute(SystemConstants.LoggedInUser);
    }
    boolean createProject = false;
    boolean editProject = false;
    boolean deleteProject = false;
    RoleEntContext addCtx = null;
    RoleEntContext assignUserCtx = null;

    if (user.getUserEntitlements() != null) {
        List<UserEntitlement> ues = user.getUserEntitlements();
        createProject = ues.contains(new UserEntitlement(Resource.project, Action.add));
        editProject = ues.contains(new UserEntitlement(Resource.project, Action.update));
        deleteProject = ues.contains(new UserEntitlement(Resource.project, Action.delete));
        addCtx = AuthorizationManager.getRoleEntContext(ues,
                new UserEntitlement(Resource.project, Action.add));
        assignUserCtx = AuthorizationManager.getRoleEntContext(ues,
                new UserEntitlement(Resource.project, Action.assign_user));
    }

%>

<html lang="en">

    <head>

        <title>Projects</title>

        <jsp:include page="header.jsp"/>
        <jsp:include page="datatables_css.jsp"/>
        <link rel="stylesheet" href="../adminlte2/plugins/select2/select2.min.css">
        <link rel="stylesheet" href="../adminlte2/bootstrap/css/bootstrap.min.css"> 
        <link rel="stylesheet" href="../adminlte2/plugins/datepicker/css/bootstrap-datepicker3.min.css">
        <jsp:include page="adminlte_css.jsp"/>
        <style>
            tr.disabled {
                /*font-weight: bold;*/
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
        <div class="modal fade bs-example-modal-sm" id="modalEditProject" role="dialog" aria-labelledby="editProject">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="editProject">Edit Project</h4>
                    </div>
                    <div class="modal-body">
                        <form role="form" id="editProject">
                            <input type="hidden" id="inputId" name="inputId"  class="form-control">
                            <input type="hidden" id="inputCompany" name="inputCompany"  class="form-control">
                            <div class="form-group">
                                <label for="inputCompany">Company</label>
                                <input type="text" class="form-control" id="inputCompanyName" name="inputCompany" disabled>
                            </div>
                            <div class="form-group">
                                <label for="inputName">Project Name</label>
                                <input type="text" class="form-control" id="inputName" name="inputName" placeholder="Project Name" required>
                            </div>
                            <div class="form-group">
                                <label for="inputDescription">Description</label>
                                <textarea rows="2" class="form-control" id="inputDescription" name="inputDescription" placeholder="Description"></textarea>

                            </div>
                            <div class="form-group">
                                <label for="inputUrl">Project Website</label>                                
                                <input type="text" class="form-control" id="inputUrl" name="inputUrl" placeholder="Project Website"> 
                            </div>
                            <div class="form-group">
                                <label for="inputCoordinates">Coordinates</label>
                                <input type="text" class="form-control" id="inputCoordinates" name="inputCoordinates" placeholder="Geo Coordinates for Map" required>
                            </div>
                            <div class="form-group">
                                <label for="inputActive">Disable  
                                    <input type="checkbox" class="checkbox" id="inputActive" name="inputActive">
                                </label>
                            </div>
                            <div>
                                <button type="submit" class="btn btn-primary" id="submitEditProject">Submit</button>
                            </div>
                        </form>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>        
        <div class="modal fade bs-example-modal-sm" id="modalCreateProject" role="dialog" aria-labelledby="createCompany">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">Add Project</h4>
                    </div>
                    <div class="modal-body">
                        <form role="form" id="createProject">
                            <%if (addCtx == RoleEntContext.ALL) {%>
                            <div class="form-group">
                                <label for="inputCompany" class="control-label">Company</label>
                                <select id="inputCompany" name="inputCompany" class="form-control" style="width: 100%;" >
                                </select>                                                
                            </div>
                            <%} else {%>
                            <input type="hidden" name="inputCompany" value="<%=user.getCompany()%>">
                            <%}%>

                            <div class="form-group">
                                <label for="inputName">Project Name</label>
                                <input type="text" class="form-control" id="inputName" name="inputName" placeholder="Enter Project Name" required>
                            </div>
                            <div class="form-group">
                                <label for="inputDescription">Description</label>
                                <textarea rows="2" class="form-control" id="inputDescription" name="inputDescription" placeholder="Enter Description"></textarea>
                            </div>
                            <div class="form-group">
                                <label for="inputUrl">Project Website</label>
                                <input type="text" class="form-control" id="inputUrl" name="inputUrl" placeholder="Enter Project Website">
                            </div>
                            <div class="form-group">
                                <label for="inputCoordinates">Coordinates</label>
                                <input type="text" class="form-control" id="inputCoordinates" name="inputCoordinates" placeholder="Geo Coordinates for Map" required>
                            </div>
                            <div class="form-group">
                                <label for="inputActive">Disable  
                                    <input type="checkbox" class="checkbox" id="inputActive" name="inputActive">
                                </label>
                            </div>
                            <div>
                                <button type="submit" class="btn btn-primary" id="submitCreateProject">Submit</button>
                                <button type="reset" class="btn btn-link" id="resetCreateProject">Reset</button>
                            </div>
                        </form>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>        
        <div class="modal fade" id="modalEditAssignedUsers" tabindex="-1" role="dialog" aria-labelledby="editAssignedUsers">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="editAssignedUsersTitle"></h4>
                    </div>
                    <div class="modal-body">
                        <form role="form" id="editAssignedUsers">
                            <input type="hidden" id="inputCompany" name="inputCompany"  class="form-control">
                            <input type="hidden" id="inputProject" name="inputProject"  class="form-control">
                            <div class="form-group">
                                <label for="inputUsers">Select Users to Add</label>
                                <select id="inputUsers" name="inputUsers" multiple="multiple" class="form-control" style="width: 100%;" >
                                </select>
                            </div> 
                            <div>
                                <button type="submit" class="btn btn-primary" id="submitEditAssignedUsers">Submit</button>
                                <button type="reset" class="btn btn-link" id="resetEditAssignedUsers">Reset</button>
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
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                        Manage Projects                        
                    </h1>
                    <!--                    <ol class="breadcrumb">
                                            <li><a href="#"><i class="fa fa-dashboard"></i> Level</a></li>
                                            <li class="active">Here</li>
                                        </ol>-->
                </section>

                <!-- Main content -->
                <section class="content">
                    <div class="row">
                        <div class="col-sm-6">
                            <div class="box box-primary">
                                <div class="box-header">
                                    <button type="button" id="btnCreateProject" class="btn btn-primary" data-toggle="modal" data-target="#modalCreateProject">Add</button>
                                    <button type="button" id="btnEditProject" class="btn btn-primary" data-toggle="modal" data-target="#modalEditProject">Edit</button>
                                </div><!-- /.box-header -->
                                <div class="box-body table-responsive">
                                    <table id="list_projects" class="table table-bordered table-striped">
                                        <thead>
                                            <tr>
                                                <th>Id</th>
                                                <th>Name</th>
                                                <th>URL</th>
                                                <th>Status</th>
                                            </tr>
                                        </thead>
                                    </table>
                                </div><!-- /.box-body -->
                            </div><!-- /.box -->
                        </div><!-- /.col -->
                        <div class="col-sm-6">
                            <div class="box box-primary">
                                <div class="box-header">
                                    <button type="button" id="btnEditAssignedUsers" class="btn btn-primary" data-toggle="modal" data-target="#modalEditAssignedUsers">Assign Users</button>
                                </div><!-- /.box-header -->
                                <div class="box-body table-responsive">
                                    <table id="list_assigned_users" class="table table-bordered table-striped">
                                        <thead>
                                            <tr>
                                                <th>Id</th>
                                                <th>Name</th>
                                                <th>Last Name</th>
                                                <th>Manager Id</th>
                                                <th>Manager</th>
                                                <th>Mobile No</th>
                                                <th>Status</th>
                                            </tr>
                                        </thead>
                                    </table>
                                </div><!-- /.box-body -->
                            </div><!-- /.box -->
                        </div><!-- /.col -->                        
                    </div><!-- /.row -->

                    <div class="row">
                        <div class="col-sm-12">
                            <div class="box">
                                <div class="box-header with-border">                                    
                                    <div class="col-md-3 col-sm-6 col-xs-12">
                                        <div class="form-group">
                                            <select id="selectDateRange" name="selectDateRange" class="form-control" style="width: 100%;">
                                                <option value="last7Days">Last 7 Days</option>
                                                <option value="lastMonth">Last Month</option>
                                                <option value="lastQuarter">Last Quarter</option>
                                                <option value="last6Months">Last 6 Months</option>
                                                <option value="lastYear">Last Year</option>    
<!--                                                <option value="dateRange">Select Date Range</option>-->
                                            </select>                                
                                        </div>
                                    </div> 
                                    <div class="col-md-3 col-sm-6 col-xs-12" hidden="true" id="fromDate">    
                                        <div class="form-group">
                                            <div class="input-group input-daterange">
                                                <input type="text" class="form-control" id="inputFromDate" name="inputFromDate" data-provide="datepicker">
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="form-control" id="inputToDate" name="inputToDate" data-provide="datepicker">
                                            </div>
                                        </div>
                                    </div>    
                                    <div class="col-md-3 col-sm-6 col-xs-12">    
                                        <button type="button" id="btnApplyFilter" class="btn btn-primary btn-sm">Apply Date Filter</button>
                                        <button type="button" id="resetFilter" class="btn btn-link"><small>Reset Filters</small></small></button>
                                    </div>                                    
                                </div>
                            </div><!-- /.box -->
                            <div class="box box-primary">
                                <div class="box-header with-border">
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
        <script src="../jquery-validation/js/jquery.validate.min.js"></script>
        <script src="../jquery-validation/js/additional-methods.min.js"></script>

        <jsp:include page="adminlte_js.jsp"/>

        <script>
            var tableProjects;
            var tableAssignedUsers;
            var addProject = <%=createProject%>;
            var editProject = <%=editProject%>;
            var deleteProject = <%=deleteProject%>;
            var assignUserCtx = <%=assignUserCtx != null ? assignUserCtx.getContextLevel() : -1%>;
            var selectedProject = 0;
            var users;
            var chart1;
            var selectDateRange = $("#selectDateRange");
            var inputFromDate = $("#inputFromDate");
            var inputToDate = $("#inputToDate");
            var selectedDateRange;
            if (addProject === false) {
                $("#btnCreateProject").hide();
            }
            if (editProject === false) {
                $("#btnEditProject").hide();
            }
            if (deleteProject === false) {
                $('#editProject input[id=inputActive]').prop('disabled', true);
            }
            if (assignUserCtx === -1) {
                $("#btnEditAssignedUsers").hide();
            }
            $(function () {
                tableProjects = $('#list_projects').DataTable({
                    "paging": true,
                    "responsive": true,
                    "lengthMenu": [5, 10, 25],
                    "lengthChange": true,
                    "pageLength": 5,
                    "searching": true,
                    "ordering": true,
                    "info": true,
                    "autoWidth": false,
                    "ajax": "../context?action=listProjectsByContext&resource=project",
                    "columns": [
                        {"data": "id"},
                        {"data": "name"},
                        {"data": "url", "defaultContent": ""},
                        {"data": "active"}
                    ],
                    "columnDefs": [
                        {
                            "visible": true, "searchable": false, "targets": [0]
                        },
//                        {
//                            "render": function (data, type, columns) {
//                                if (columns["url"] === null) {
//                                    return '';
//                                } else {
//                                    return '<a href="'+columns["url"]+'">' + data + '</a>';
//                                }
//                            }, "targets": [2]
//                        },
                        {
                            "render": function (data, type, columns) {
                                if (columns["active"] === 0) {
                                    return 'Disabled';
                                } else {
                                    return 'Active';
                                }
                            }, "targets": [3]
                        }
                    ],
                    "createdRow": function (row, data, index) {
                        if (data["active"] === 0) {
                            $(row).addClass('disabled');
                        }
                    }

                });
                toggleEditProjectBtn();
                toggleEditAssignUserBtn();
//Row click          
                // if (tableProjects.rows().data().length >1){
                //$('#list_projects tbody').on('click','tr', function(){
                //  rowSelectAction();
                //}); 
                // }
                //  else {
                $('#list_projects tbody').on('click', 'tr', function () {
                    rowSelectAction(this);
                });
                // }
                function rowSelectAction(row) {
                    var data = tableProjects.row(row).data();
                    if ($(row).hasClass('active')) {
                        $(row).removeClass('active');
                        selectedProject = 0;
                    } else {
                        tableProjects.$('tr.active').removeClass('active');
                        $(row).addClass('active');
                        selectedProject = data["id"];
                    }
                    $('#editProject input[id=inputId]').val(data["id"]);
                    $('#editProject input[id=inputCompany]').val(data["company"]);
                    $('#editProject input[id=inputCompanyName]').val(data["companyName"]);
                    $('#editProject input[id=inputName]').val(data["name"]);
                    $('#editProject input[id=inputDescription]').val(data["description"]);
                    $('#editProject input[id=inputUrl]').val(data["url"]);
                    $('#editProject input[id=inputCoordinates]').val(data["coordinates"]);
                    if (data["active"] === 1) {
                        $('#editProject input[id=inputActive]').prop('checked', false);
                    } else {
                        $('#editProject input[id=inputActive]').prop('checked', true);
                    }
                    toggleEditProjectBtn();
                    reloadTableAssignedUsers();
                    getVisitsData();
                }

//To call assign users
                $('button#btnEditAssignedUsers').click(function () {
                    var data = tableProjects.row('.active').data();
                    $("#editAssignedUsersTitle").html("Assign users to " + data["name"]);
                    //set hidden input
                    $("#editAssignedUsers :input[id=inputCompany]").val(data["company"]);
                    $("#editAssignedUsers :input[id=inputProject]").val(selectedProject);
                    $.ajax({
                        type: "POST",
                        url: "../user?action=getUsersToAssignToProject&selectedProject=" + selectedProject,
                        dataType: 'json'
                    })
                            .done(function (data, textStatus, jqXHR) {
                                var response = data;
                                if (response.result === "error") {
                                    ajaxHandleError(response);
                                } else {
                                    users = data.items;
                                    loadUsers($("#editAssignedUsers :input[id=inputUsers]"));
                                }
                            })
                            .fail(function (jqXHR, textStatus, errorThrown) {
                                var respJson = JSON.parse(jqXHR.responseText);
                                var response = jQuery.parseJSON(respJson);
                                $.alert(response.errorMsg, "Error !!");
                            });
                });
                function loadUsers(inputUsers) {
                    inputUsers.select2({
                        data: users,
                        placeholder: "Select users to assign",
                        minimumResultsForSearch: "Infinity",
                        disabled: false,
                        allowClear: true,
                        escapeMarkup: function (markup) {
                            return markup; // let our custom formatter work
                        },
                        templateResult: function (repo) {
                            if (repo.loading)
                                return repo.text;
                            if (repo.firstName === null || repo.firstName === '' || typeof repo.firstName === "undefined")
                                return repo.firstName;
                            return repo.firstName + ' ' + repo.lastName;
                        },
                        templateSelection: function (repo) {
                            if (repo.firstName === null || repo.firstName === '' || typeof repo.firstName === "undefined")
                                return repo.firstName;
                            return repo.firstName + ' ' + repo.lastName || repo.text;
                        }
                    });
                }

                tableAssignedUsers = $('#list_assigned_users').DataTable({
                    "paging": true,
                    "responsive": true,
                    "lengthMenu": [5, 10, 25],
                    "lengthChange": true,
                    "pageLength": 5,
                    "searching": true,
                    "ordering": true,
                    "info": true,
                    "autoWidth": false,
                    "ajax": {
                        "url": "../user?action=listUsersAssignedToProject",
                        "type": "POST",
                        "data": function (d) {
                            d.selectedProject = selectedProject;
                        }
                    },
                    "columns": [
                        {"data": "id"},
                        {"data": "firstName"},
                        {"data": "lastName"},
                        {"data": "manager"},
                        {"data": "managerName"},
                        {"data": "mobileNo"},
                        {"data": "active"}
                    ],
                    "columnDefs": [
                        {
                            "visible": false, "searchable": false, "targets": [0]
                        },
                        {
                            "render": function (data, type, columns) {
                                return '<a href="userProfile.jsp?id=' + columns["id"] + '">' + data + ' ' + columns["lastName"] + '</a>';
                            },
                            "targets": [1]
                        },
                        {
                            "visible": false, "searchable": false, "targets": [2]
                        },
                        {
                            "visible": false, "searchable": false, "targets": [3]
                        },
                        {
                            "render": function (data, type, columns) {
                                if (columns["manager"] === 0) {
                                    return 'No Manager';
                                } else {
                                    return '<a href="userProfile.jsp?id=' + columns["manager"] + '">' + data + '</a>';
                                }
                            }, "targets": [4]
                        },
                        {
                            "visible": false, "searchable": false, "targets": [6]
                        }
                    ],
                    "createdRow": function (row, data, index) {
                        if (data["active"] === 0) {
                            $(row).addClass('disabled');
                        }
                    }
                });
            });
            function reloadTableProjects() {
                tableProjects.ajax.reload(null, false);
                selectedProject = 0;
                reloadTableAssignedUsers();
            }

            function toggleEditProjectBtn() {
                if (tableProjects.rows('.active').data().length === 1) {
                    $('#btnEditProject').prop('disabled', false);
                } else {
                    $('#btnEditProject').prop('disabled', true);
                }
            }
            function toggleEditAssignUserBtn() {
                if (tableProjects.rows('.active').data().length === 1) {
                    var data = tableProjects.row('.active').data();
                    if (data["active"] === 1) {
                        $('button#btnEditAssignedUsers').prop('disabled', false);
                    } else {
                        $('button#btnEditAssignedUsers').prop('disabled', true);
                    }
                } else {
                    $('button#btnEditAssignedUsers').prop('disabled', true);
                }
            }

            function reloadTableAssignedUsers() {
                tableAssignedUsers.ajax.reload(null, false);
                tableAssignedUsers.$('tr.active').removeClass('active');
                toggleEditAssignUserBtn();
            }

            var editProjectForm = $("form#editProject");
            var editAssignedUsersForm = $("form#editAssignedUsers");
            function submitEditProject() {
                console.log(editProjectForm);
                $.ajax({
                    type: "POST",
                    url: "../context?action=updateProject",
                    data: editProjectForm.serialize(),
                    dataType: 'json'
                })
                        .done(function (data, textStatus, jqXHR) {
                            var response = data;
                            if (response.result === "error") {
                                ajaxHandleError(response);
                            } else {
                                ajaxHandleSuccess(response);
                                $("#modalEditProject").modal('hide');
                                reloadTableProjects();
                            }
                        })
                        .fail(function (jqXHR, textStatus, errorThrown) {
                            var respJson = JSON.parse(jqXHR.responseText);
                            var response = jQuery.parseJSON(respJson);
                            $.alert(response.errorMsg, "Error !!");
                        });
            }

            function submitEditAssignedUsers() {
                $.ajax({
                    type: "POST",
                    url: "../context?action=assignUsersToProject",
                    data: editAssignedUsersForm.serialize(),
                    dataType: 'json'
                })
                        .done(function (data, textStatus, jqXHR) {
                            var response = data;
                            if (response.result === "error") {
                                ajaxHandleError(response);
                            } else {
                                ajaxHandleSuccess(response);
                                $("#modalEditAssignedUsers").modal('hide');
                                reloadTableAssignedUsers();
                            }
                        })
                        .fail(function (jqXHR, textStatus, errorThrown) {
                            var respJson = JSON.parse(jqXHR.responseText);
                            var response = jQuery.parseJSON(respJson);
                            $.alert(response.errorMsg, "Error !!");
                        });
            }

            jQuery.validator.addMethod("simple_url", function (val, elem) {
                // if no url, don't do anything
                if (val.length > 0)
                {
                    if (/^(http:\/\/www\.|https:\/\/www\.|http:\/\/|https:\/\/|www\.)[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/.test(val)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            });
            editProjectForm.validate({
                focusCleanup: true,
                debug: false,
                rules: {
                    inputName: {
                        required: true
                    },
                    inputUrl: {
                        required: false,
                        simple_url: true
                    }
                },
                messages: {
                    inputUrl: {
                        simple_url: "Enter a valid Url"
                    }
                },
                highlight: function (element) {
                    $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
                },
                success: function (element) {
                    $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
                },
                errorPlacement: function (error, element) {
                    element.parent().closest('div').last().append(error);
                },
                submitHandler: function (form) {
                    submitEditProject();
                }
            });
            editAssignedUsersForm.validate({
                focusCleanup: true,
                debug: false,
                rules: {
                    inputUsers: {
                        required: true
                    }
                },
                messages: {
                    inputUsers: {
                        required: "Select atleast 1 user"
                    }
                },
                highlight: function (element) {
                    $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
                },
                success: function (element) {
                    $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
                },
                errorPlacement: function (error, element) {
                    element.parent().closest('div').last().append(error);
                },
                submitHandler: function (form) {
                    submitEditAssignedUsers();
                }
            });
            $("#resetEditAssignedUsers").click(function () {
//                editAssignedUsersForm.validate().resetForm();
                $('form#editAssignedUsers .form-group').removeClass('has-error has-feedback has-success');
                $("#editAssignedUsers :input[id=inputUsers]").select2("val", "");
            });
            var lineChartOptions = {
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero:true
                        }
                    }]
                }
            };
            
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
            });
            $("#resetFilter").click(function () {
                selectDateRange.val("last7Days").trigger("change");
//                    inputFromDate.datepicker().clearDate();
//                    inputToDate.datepicker().clearDate();
            });
            $(function () {
                getVisitsData();
            });
            function getVisitsData() {
                $.ajax({
                    url: "../dashboard?action=getVisitsDataForProject",
                    data: {
                        selectedProject: selectedProject,
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
                            chart1 = new Chart(ctx , {
                                type: 'line',
                                data: data,
                                options: lineChartOptions
                            });
//                            chart1 = new Chart($("#chart1").get(0).getContext("2d")).Line(data, lineChartOptions);
                        })

                .fail(function (jqXHR, textStatus, errorThrown) {
                    var respJson = JSON.parse(jqXHR.responseText);
                    var response = jQuery.parseJSON(respJson);
                    $.alert(response.errorMsg, "Error !!");
                });
            }

        </script>  

        <script>
            var cInputCompany = $("#createProject :input[id=inputCompany]");
            var cInputName = $('#createProject :input[id=inputName]');
            var cInputDesc = $('#createProject :input[id=inputDescription]');
            var cInputUrl = $('#createProject :input[id=inputUrl]');
            var cInputActive = $('#createProject :input[id=inputActive]');
            $(function () {
                cInputCompany.select2({
                    placeholder: "Select a company",
                    allowClear: true,
                    ajax: {
                        url: "../context",
                        dataType: 'json',
                        delay: 250,
                        data: function (params) {
                            return {
                                q: params.term,
                                action: 'getCompaniesStartingWith'
                            };
                        },
                        processResults: function (data, params) {
                            return {
                                results: data.items
                            };
                        },
                        cache: true
                    },
                    escapeMarkup: function (markup) {
                        return markup; // let our custom formatter work
                    },
                    minimumInputLength: 1,
                    templateResult: function (repo) {
                        if (repo.loading)
                            return repo.text;
                        return repo.name;
                    },
                    templateSelection: function (repo) {
                        return repo.name || repo.text;
                    }
                }).on("change", function (e) {
                    $(this).valid(); //jquery validation script validate on change;
                });
                enableProjectFields();
                cInputCompany.change(function () {
                    enableProjectFields();
                });
            });
            function enableProjectFields() {
                if (cInputCompany.find('option:selected').val() !== ''
                        && typeof cInputCompany.find('option:selected').val() !== "undefined") {
                    cInputName.prop("disabled", false);
                    cInputDesc.prop("disabled", false);
                    cInputUrl.prop("disabled", false);
                    cInputActive.prop("disabled", false);
                } else {
                    cInputName.prop("disabled", true);
                    cInputDesc.prop("disabled", true);
                    cInputUrl.prop("disabled", true);
                    cInputActive.prop("disabled", true);
                }
            }

            var createProjectForm = $('form#createProject');
            function submitCreateProject() {
                $.ajax({
                    type: "POST",
                    url: "../context?action=createProject",
                    data: createProjectForm.serialize(),
                    dataType: 'json'
                })
                        .done(function (data, textStatus, jqXHR) {
                            var response = data;
                            if (response.result === "error") {
                                ajaxHandleError(response);
                            } else {
                                ajaxHandleSuccess(response);
                                $("#modalCreateProject").modal('hide');
                                reloadTableProjects();
                            }
                        })
                        .fail(function (jqXHR, textStatus, errorThrown) {
                            var respJson = JSON.parse(jqXHR.responseText);
                            var response = jQuery.parseJSON(respJson);
                            $.alert(response.errorMsg, "Error !!");
                        });
            }

            var createProjectValidator = createProjectForm.validate({
//                focusCleanup: true,
//                debug: true,
                rules: {
                    inputCompany: {
                        required: true
                    },
                    inputName: {
                        required: true
                    },
                    inputUrl: {
                        required: false,
                        simple_url: true
                    }
                },
                messages: {
                    inputUrl: {
                        simple_url: "Enter a valid Url"
                    }
                },
                highlight: function (element) {
                    $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
                },
                success: function (element) {
                    $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
                },
                errorPlacement: function (error, element) {
                    element.parent().closest('div').last().append(error);
                },
                submitHandler: function (form) {
                    submitCreateProject();
                }
            });
            $("#resetCreateProject").click(function () {
                createProjectForm.validate().resetForm();
                $('form#createProject .form-group').removeClass('has-error has-feedback has-success');
                cInputCompany.select2("val", "");
                cInputCompany.empty();
                enableProjectFields();
            });

        </script>
    </body>

</html>
