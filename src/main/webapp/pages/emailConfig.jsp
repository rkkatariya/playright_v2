<%-- 
    Document   : emailConfig
    Created on : 23 Jun, 2016, 2:19:20 PM
    Author     : rahulkk
--%>
<!DOCTYPE html>
<%@page import="com.revvster.playright.model.User"%>
<%@page import="com.revvster.playright.util.SystemConstants"%>
<%@page import="com.revvster.playright.model.Settings"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.revvster.playright.model.RoleEntContext"%>
<%@page import="com.revvster.playright.model.UserEntitlement"%>
<%@page import="com.revvster.playright.access.AuthorizationManager"%>
<%@page import="com.revvster.playright.model.ContextObj"%>
<%@page import="com.revvster.playright.access.Resource"%>
<%@page import="com.revvster.playright.access.Action"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    User user = new User();
    if (session.getAttribute(SystemConstants.LoggedInUser) != null) {
        user = (User) session.getAttribute(SystemConstants.LoggedInUser);
    }

    RoleEntContext addCtx = null;
    RoleEntContext editCtx = null;
    RoleEntContext deleteCtx = null;
    RoleEntContext listCtx = null;
    int listCtxSize = 0;
    int selectedCtxObj = 0;

    if (user.getUserEntitlements() != null) {
        List<UserEntitlement> ues = user.getUserEntitlements();
        addCtx = AuthorizationManager.getRoleEntContext(ues,
                new UserEntitlement(Resource.setting, Action.add));
        editCtx = AuthorizationManager.getRoleEntContext(ues,
                new UserEntitlement(Resource.setting, Action.update));
        deleteCtx = AuthorizationManager.getRoleEntContext(ues,
                new UserEntitlement(Resource.setting, Action.delete));
        listCtx = AuthorizationManager.getRoleEntContext(ues,
                new UserEntitlement(Resource.setting, Action.list));
        List<ContextObj> ctxObjs = user.getContextScope().get(listCtx);
        if (ctxObjs != null) {
            listCtxSize = ctxObjs.size();
            if (listCtxSize == 1) {
                selectedCtxObj = ctxObjs.get(0).getId();
            }
        }
    }
%>

<html lang="en">
    <head>
        <title>Settings</title>
        <jsp:include page="header.jsp"/>
        <jsp:include page="datatables_css.jsp"/>
        <link rel="stylesheet" href="../adminlte2/plugins/select2/select2.min.css">        
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
    <body class="hold-transition skin-red sidebar-mini">
        <div class="modal fade bs-example-modal-sm" id="modalEditSetting" role="dialog" aria-labelledby="editSetting">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">Edit Value</h4>
                    </div>
                    <div class="modal-body">
                        <form role="form" id="editSetting">
                            <input type="hidden" id="inputId" name="inputId"  class="form-control">
                            <input type="hidden" id="selectedCompany" name="selectedCompany"  class="form-control">
                            <input type="hidden" id="selectedParameter" name="selectedParameter"  class="form-control">
                            <div class="form-group">
                                <label for="inputValue">Value</label>
                                <input type="text" class="form-control" id="inputValue" name="inputValue" placeholder="Value" >
                            </div> 
                            <div>
                                <button type="button" class="btn btn-primary" id="submitEditSetting">Submit</button>
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


                <!-- Main content -->
                <section class="content-header">
                    <h1> Email Configuration</h1>
                </section>
                <section class="content">                    
                    <div class="row"> 
                        <div class="col-sm-3">
<!--                            <div id="selectCompanyBox" class="box box-primary">
                                <div class="box-header with-border">
                                    <h3 class="box-title" id="company">Select Company</h3>
                                </div> /.box-header 
                                <div class="box-body">
                                    <div>
                                        <select id="selectCompany" name="selectCompany" class="form-control" style="width: 100%;" >
                                        </select>
                                    </div>
                                </div>
                            </div>-->
                            <div class="box box-primary" id="selectCompanyBox">  
                                <div class="box-header with-border">
                                    <h3 class="box-title">Select Company</h3>
                                </div>
                                <div class="box-body table-responsive">
                                    <table id="list_companies" class="table table-bordered table-striped">
                                        <thead>
                                            <tr>
                                                <th>Company Name</th>
                                            </tr>
                                        </thead>
                                    </table>
                                </div><!-- /.box-body -->
                            </div><!-- /.box -->
                        </div>
                        <div class="col-sm-4">
                            <div class="box box-primary">
                                <div class="box-header">
                                    <button type="button" id="btnEditSetting" class="btn btn-primary" data-toggle="modal" data-target="#modalEditSetting">Edit</button>
                                </div>
                                <div class="box-body table-responsive">                                        
                                    <table class="table table-hover table-bordered" id="list_settings">
                                        <thead>
                                            <tr>
                                                <th>Parameter</th>
                                                <th>Value</th>
                                            </tr>
                                        </thead>
                                    </table>
                                </div><!-- /.box-body -->
                            </div><!-- /.box -->
                        </div>
                    </div>

                </section>
            </div>
            <jsp:include page="control_sidebar.jsp"/>
        </div>

        <jsp:include page="footer.jsp"/>
        <jsp:include page="datatables_js.jsp"/>
        <script src="../adminlte2/plugins/select2/select2.full.min.js"></script>        
        <jsp:include page="adminlte_js.jsp"/>
        <script src="../jquery-tabletoJSON/jquery.tabletojson.min.js"></script>
        <script type="text/javascript">
            var tableSetting;
            var selectCompany = $("#selectCompany");
            var selectedCompany = 0;
            var selectedParameter = 0;
            var selectedSetting = 0;
            var tableCompanies;
            var listCtx = <%=listCtx != null ? listCtx.getContextLevel() : -1%>;
            var addCtx = <%=addCtx != null ? addCtx.getContextLevel() : -1%>;
            var editCtx = <%=editCtx != null ? editCtx.getContextLevel() : -1%>;
            var deleteCtx = <%=deleteCtx != null ? deleteCtx.getContextLevel() : -1%>;
            var selectedCtxObj = <%=selectedCtxObj%>;
            $(function () {
                if (listCtx > 0) {
                    $("#selectCompanyBox").hide();
                    selectedCompany = <%=user.getCompany() != null ? user.getCompany() : 0%>;
                }
                if (editCtx === -1) {
                    $("#btnEditSetting").hide();
                }

                tableCompanies = $('#list_companies').DataTable({
                    "paging": false,
                    "responsive": true,
                    "processing": true,
                    "lengthChange": false,
                    "searching": false,
                    "ordering": true,
                    "info": true,
                    "autoWidth": false,
                    "ajax": {
                        "url": "../context?action=listCompaniesByContext",
                        "type": "POST"
                    },
                    "columns": [
                        {"data": "name"}
                    ]

                });

                $('#list_companies tbody').on('click', 'tr', function () {
                    var data = tableCompanies.row(this).data();
                    if ($(this).hasClass('active')) {
                        $(this).removeClass('active');
                        selectedCompany = 0;
                    } else {
                        tableCompanies.$('tr.active').removeClass('active');
                        $(this).addClass('active');
                        selectedCompany = data["id"];
                    }
                    reloadTableSetting();
                    toggleEditSettingBtn();
                });

                tableSetting = $('#list_settings').DataTable({
                    "pageLength": 5,
                    "ordering": false,
                    "paging": false,
                    "responsive": false,
                    "searching": false,
                    "info": false,
                    "lengthChange": false,
                    "ajax": {
                        "url": "../context?action=listEmailSettingsByContext",
                        "type": "POST",
                        "data": function (d) {
                            d.selectedCompany = selectedCompany;
                        }
                    },
                    "columns": [
                        {"data": "parameter"},
                        {"data": "value"}
                    ],
                    "columnDefs": [
                        {
                            "render": function (data, type, columns) {
                                if (typeof data === "undefined" || data === null) {
                                    return '';
                                } else {
                                    return data;
                                }
                            }, "targets": [1]
                        }
                    ]
                });
                $('#list_settings tbody').on('click', 'tr', function () {
                    var data = tableSetting.row(this).data();
                    if ($(this).hasClass('active')) {
                        $(this).removeClass('active');
                    } else {
                        tableSetting.$('tr.active').removeClass('active');
                        $(this).addClass('active');
                        if (typeof data !== "undefined") {
                            selectedSetting = data["id"];
                        } else {
                            selectedSetting = 0;
                        }
                    }
                    toggleEditSettingBtn();
                });

                function toggleEditSettingBtn() {
                    if (selectedCompany > 0 
                            && tableSetting.rows('.active').data().length === 1) {
                        $('#btnEditSetting').prop('disabled', false);
                    } else {
                        $('#btnEditSetting').prop('disabled', true);

                    }
                }
                toggleEditSettingBtn();
                $('button#btnEditSetting').click(function () {
                    var data = tableSetting.row('.active').data();
                    $("#editSetting :input[id=inputId]").val(selectedSetting);
                    $("#editSetting :input[id=inputValue]").val(data["value"]);
                    $("#editSetting :input[id=selectedCompany]").val(selectedCompany);
                    $("#editSetting :input[id=selectedParameter]").val(data["parameter"]);
                });

                $("#submitEditSetting").on("click", function () {
                    $.ajax({
                        type: "POST",
                        url: "../context?action=createOrUpdateSettingValues",
                        data: $("form#editSetting").serialize(),
                        dataType: 'json'
                    })
                            .done(function (data, textStatus, jqXHR) {
                                var response = data;
                                if (response.result === "error") {
                                    ajaxHandleError(response);
                                } else {
                                    ajaxHandleSuccess(response);
                                    $("#modalEditSetting").modal('hide');
                                    reloadTableSetting();
                                }
                            })
                            .fail(function (jqXHR, textStatus, errorThrown) {
                                var respJson = JSON.parse(jqXHR.responseText);
                                var response = jQuery.parseJSON(respJson);
                                $.alert(response.errorMsg, "Error !!");
                            });
                });

                function reloadTableSetting() {
                    tableSetting.ajax.reload();
                }

            });

        </script>
        <script src="../jquery-validation/js/jquery.validate.min.js"></script>
        <script src="../jquery-validation/js/additional-methods.min.js"></script>
    </body>
</html>

