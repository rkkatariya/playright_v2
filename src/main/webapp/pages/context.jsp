<%-- 
    Document   : home
    Created on : Sep 29, 2015, 5:07:59 PM
    Author     : Rahul
--%>

<%@page import="com.revvster.playright.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.revvster.playright.util.SystemConstants"%>
<!DOCTYPE html>
<html lang="en">

    <head>

        <title>Companies & Projects</title>

        <jsp:include page="header.jsp"/>
        <jsp:include page="datatables_css.jsp"/>
        <link rel="stylesheet" href="../adminlte2/plugins/select2/select2.min.css">
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
        <div class="modal fade bs-example-modal-sm" id="modalEditCompany" role="dialog" aria-labelledby="editCompany">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="editCompany">Edit Company</h4>
                    </div>
                    <div class="modal-body">
                        <form role="form" id="editCompany">
                            <input type="hidden" id="inputId" name="inputId"  class="form-control">
                            <div class="form-group">
                                <label for="inputName">Company Name</label>
                                <input type="text" class="form-control" id="inputName" name="inputName" placeholder="Company Name" required>
                            </div>
                            <div class="form-group">
                                <label for="inputDescription">Description</label>
                                <input type="text" class="form-control" id="inputDescription" name="inputDescription" placeholder="Description">
                            </div>
                            <div class="form-group">
                                <label for="inputUrl">Company Website</label>
                                <input type="url" class="form-control" id="inputUrl" name="inputUrl" placeholder="Company Website">
                            </div>

                            <div class="form-group">
                                <label for="inputActive">Disable  
                                    <input type="checkbox" class="checkbox" id="inputActive" name="inputActive">
                                </label>
                            </div>
                        </form>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary" id="submitEditCompany">Submit</button>
                    </div>
                </div>
            </div>
        </div>   

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
                            <div class="form-group">
                                <label for="inputName">Project Name</label>
                                <input type="text" class="form-control" id="inputName" name="inputName" placeholder="Project Name" required>
                            </div>
                            <div class="form-group">
                                <label for="inputDescription">Description</label>
                                <input type="text" class="form-control" id="inputDescription" name="inputDescription" placeholder="Description">
                            </div>
                            <div class="form-group">
                                <label for="inputUrl">Project Website</label>
                                <input type="url" class="form-control" id="inputUrl" name="inputUrl" placeholder="Project Website">
                            </div>

                            <div class="form-group">
                                <label for="inputActive">Disable  
                                    <input type="checkbox" class="checkbox" id="inputActive" name="inputActive">
                                </label>
                            </div>
                        </form>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary" id="submitEditProject">Submit</button>
                    </div>
                </div>
            </div>
        </div>        

        <div class="modal fade bs-example-modal-sm" id="modalCreateCompany" role="dialog" aria-labelledby="createCompany">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="createCompany">Add Company</h4>
                    </div>
                    <div class="modal-body">
                        <form role="form" id="createCompany">
                            <div class="form-group">
                                <label for="inputName">Company Name</label>
                                <input type="text" class="form-control" id="inputName" name="inputName" placeholder="Enter Company Name" required>
                            </div>
                            <div class="form-group">
                                <label for="inputDescription">Description</label>
                                <input type="text" class="form-control" id="inputDescription" name="inputDescription" placeholder="Enter Description">
                            </div>
                            <div class="form-group">
                                <label for="inputUrl">Company Website</label>
                                <input type="url" class="form-control" id="inputUrl" name="inputUrl" placeholder="Enter Company Website">
                            </div>

                            <div class="form-group">
                                <label for="inputActive">Disable  
                                    <input type="checkbox" class="checkbox" id="inputActive" name="inputActive">
                                </label>
                            </div>
                        </form>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary" id="submitCreateCompany">Submit</button>
                    </div>
                </div>
            </div>
        </div>        

        <div class="wrapper">

            <!-- Navigation -->
            <jsp:include page="navigation_top.jsp" flush="false" />

            <jsp:include page="navigation_left.jsp" flush="false" />
            <!-- /.Navigation -->
            <%
                User user = null;
                if (session.getAttribute(SystemConstants.LoggedInUser) != null) {
                    user = (User) session.getAttribute(SystemConstants.LoggedInUser);
                }
            %>
            <!-- Content Wrapper. Contains page content -->
            <div class="content-wrapper">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                        "Manage companies and their projects"
                        <small>Optional description</small>
                    </h1>
                    <!--                    <ol class="breadcrumb">
                                            <li><a href="#"><i class="fa fa-dashboard"></i> Level</a></li>
                                            <li class="active">Here</li>
                                        </ol>-->
                </section>

                <!-- Main content -->
                <section class="content">
                    <div class="row">

                        <div class="col-sm-4">

                            <!-- Company -->
                            <div class="box box-primary" id="box-company">
                                <div class="box-header with-border">
                                    <h3 class="box-title" id="company">Company</h3>
                                    <a href="#" class="text-light-blue pull-right" data-toggle="modal" data-target="#modalCreateCompany">Add Company</a>
                                </div><!-- /.box-header -->
                                <div class="box-body">
                                    <ul class="list-group list-group-unbordered" style="margin: 0px">
                                        <li class="list-group-item">
                                            <select id="selectCompany" name="selectCompany" class="form-control" style="width: 100%;" >
                                            </select>
                                        </li>
                                    </ul>
                                    <button id="btnEditCompany" type="button" class="btn btn-primary" data-toggle="modal" data-target="#modalEditCompany">Edit</button>
                                </div><!-- /.box-body -->
                            </div><!-- /.box -->
                            <!-- Projects -->
                            <div class="box box-primary">
                                <div class="box-header with-border">
                                    <h3 class="box-title">Projects</h3>
                                </div><!-- /.box-header -->
                                <div class="box-body">
                                    <div class="table-responsive">
                                        <table id="list_projects" class="table table-bordered table-striped">
                                            <thead>
                                                <tr>
                                                    <th>Id</th>
                                                    <th>Name</th>
                                                    <th>Active</th>
                                                </tr>
                                            </thead>
                                            <tfoot>
                                                <tr>
                                                    <th>Id</th>
                                                    <th>Name</th>
                                                    <th>Active</th>
                                                </tr>
                                            </tfoot>
                                        </table>
                                    </div>
                                    <button id="btnEditProject" type="button" class="btn btn-primary" data-toggle="modal" data-target="#modalEditProject">Edit</button>
                                </div><!-- /.box-body -->
                            </div><!-- /.box -->
                        </div><!-- /.col -->
                        <div class="col-sm-8">
                            <div class="nav-tabs-custom">
                                <ul class="nav nav-tabs">
                                    <li class="active"><a href="#analytics" data-toggle="tab">Analytics</a></li>
                                    <li><a href="#reports" data-toggle="tab">Reports</a></li>
                                    <li><a href="#settings" data-toggle="tab">Settings</a></li>
                                </ul>
                                <div class="tab-content">
                                    <div class="active tab-pane" id="analytics">
                                        <div class="box box-danger">
                                            <div class="box-header with-border">
                                                <h3 class="box-title">Donut Chart</h3>
                                                <div class="box-tools pull-right">
                                                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                                                    <button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
                                                </div>
                                            </div>
                                            <div class="box-body">
                                                <canvas id="pieChart" style="height:250px"></canvas>
                                            </div><!-- /.box-body -->
                                        </div><!-- /.box -->

                                        <!-- BAR CHART -->
                                        <div class="box box-success">
                                            <div class="box-header with-border">
                                                <h3 class="box-title">Bar Chart</h3>
                                                <div class="box-tools pull-right">
                                                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                                                    <button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
                                                </div>
                                            </div>
                                            <div class="box-body">
                                                <div class="chart">
                                                    <canvas id="barChart" style="height:230px"></canvas>
                                                </div>
                                            </div><!-- /.box-body -->
                                        </div><!-- /.box -->

                                    </div><!-- /.tab-pane -->
                                    <div class="tab-pane" id="reports">
                                        <!-- The timeline -->
                                        <ul class="timeline timeline-inverse">
                                            <!-- timeline time label -->
                                            <li class="time-label">
                                                <span class="bg-red">
                                                    10 Feb. 2014
                                                </span>
                                            </li>
                                            <!-- /.timeline-label -->
                                            <!-- timeline item -->
                                            <li>
                                                <i class="fa fa-envelope bg-blue"></i>
                                                <div class="timeline-item">
                                                    <span class="time"><i class="fa fa-clock-o"></i> 12:05</span>
                                                    <h3 class="timeline-header"><a href="#">Support Team</a> sent you an email</h3>
                                                    <div class="timeline-body">
                                                        Etsy doostang zoodles disqus groupon greplin oooj voxy zoodles,
                                                        weebly ning heekya handango imeem plugg dopplr jibjab, movity
                                                        jajah plickers sifteo edmodo ifttt zimbra. Babblely odeo kaboodle
                                                        quora plaxo ideeli hulu weebly balihoo...
                                                    </div>
                                                    <div class="timeline-footer">
                                                        <a class="btn btn-primary btn-xs">Read more</a>
                                                        <a class="btn btn-danger btn-xs">Delete</a>
                                                    </div>
                                                </div>
                                            </li>
                                            <!-- END timeline item -->
                                            <!-- timeline item -->
                                            <li>
                                                <i class="fa fa-user bg-aqua"></i>
                                                <div class="timeline-item">
                                                    <span class="time"><i class="fa fa-clock-o"></i> 5 mins ago</span>
                                                    <h3 class="timeline-header no-border"><a href="#">Sarah Young</a> accepted your friend request</h3>
                                                </div>
                                            </li>
                                            <!-- END timeline item -->
                                            <!-- timeline item -->
                                            <li>
                                                <i class="fa fa-comments bg-yellow"></i>
                                                <div class="timeline-item">
                                                    <span class="time"><i class="fa fa-clock-o"></i> 27 mins ago</span>
                                                    <h3 class="timeline-header"><a href="#">Jay White</a> commented on your post</h3>
                                                    <div class="timeline-body">
                                                        Take me to your leader!
                                                        Switzerland is small and neutral!
                                                        We are more like Germany, ambitious and misunderstood!
                                                    </div>
                                                    <div class="timeline-footer">
                                                        <a class="btn btn-warning btn-flat btn-xs">View comment</a>
                                                    </div>
                                                </div>
                                            </li>
                                            <!-- END timeline item -->
                                            <!-- timeline time label -->
                                            <li class="time-label">
                                                <span class="bg-green">
                                                    3 Jan. 2014
                                                </span>
                                            </li>
                                            <!-- /.timeline-label -->
                                            <!-- timeline item -->
                                            <li>
                                                <i class="fa fa-camera bg-purple"></i>
                                                <div class="timeline-item">
                                                    <span class="time"><i class="fa fa-clock-o"></i> 2 days ago</span>
                                                    <h3 class="timeline-header"><a href="#">Mina Lee</a> uploaded new photos</h3>
                                                    <div class="timeline-body">
                                                        <img src="http://placehold.it/150x100" alt="..." class="margin">
                                                        <img src="http://placehold.it/150x100" alt="..." class="margin">
                                                        <img src="http://placehold.it/150x100" alt="..." class="margin">
                                                        <img src="http://placehold.it/150x100" alt="..." class="margin">
                                                    </div>
                                                </div>
                                            </li>
                                            <!-- END timeline item -->
                                            <li>
                                                <i class="fa fa-clock-o bg-gray"></i>
                                            </li>
                                        </ul>
                                    </div><!-- /.tab-pane -->

                                    <div class="tab-pane" id="settings">
                                        <form class="form-horizontal">
                                            <div class="form-group">
                                                <label for="inputName" class="col-sm-2 control-label">Name</label>
                                                <div class="col-sm-10">
                                                    <input type="email" class="form-control" id="inputName" placeholder="Name">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="inputEmail" class="col-sm-2 control-label">Email</label>
                                                <div class="col-sm-10">
                                                    <input type="email" class="form-control" id="inputEmail" placeholder="Email">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="inputName" class="col-sm-2 control-label">Name</label>
                                                <div class="col-sm-10">
                                                    <input type="text" class="form-control" id="inputName" placeholder="Name">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="inputExperience" class="col-sm-2 control-label">Experience</label>
                                                <div class="col-sm-10">
                                                    <textarea class="form-control" id="inputExperience" placeholder="Experience"></textarea>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="inputSkills" class="col-sm-2 control-label">Skills</label>
                                                <div class="col-sm-10">
                                                    <input type="text" class="form-control" id="inputSkills" placeholder="Skills">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="col-sm-offset-2 col-sm-10">
                                                    <div class="checkbox">
                                                        <label>
                                                            <input type="checkbox"> I agree to the <a href="#">terms and conditions</a>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="col-sm-offset-2 col-sm-10">
                                                    <button type="submit" class="btn btn-danger">Submit</button>
                                                </div>
                                            </div>
                                        </form>
                                    </div><!-- /.tab-pane -->
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
        <jsp:include page="adminlte_js.jsp"/>


        <script>
            var selectedCompany =<%=user.getCompany()%>;
            if (selectedCompany !== 0) {
                $("#box-company").hide();
            }
            $(function () {
                $("#selectCompany").select2({
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
                });
                $("button#submitCreateCompany").click(function () {
                    $.ajax({
                        type: "POST",
                        url: "../context?action=createCompany",
                        data: $('form#createCompany').serialize(),
                        success: function (msg) {
                            $("#modalCreateCompany").modal('hide');
                        },
                        error: function () {
                            alert("failure");
                        }
                    });
                });
                $("button#btnEditCompany").click(function () {
                    var data = $('#selectCompany').select2("data")[0];
                    $('#editCompany input[id=inputName]').val(data.name);
                    $('#editCompany input[id=inputDescription]').val(data.description);
                    $('#editCompany input[id=inputUrl]').val(data.url);
                    $('#editCompany input[id=inputId]').val(data.id);
                    if (data.active === 1) {
                        $('#editCompany input[id=inputActive]').prop('checked', false);
                    } else {
                        $('#editCompany input[id=inputActive]').prop('checked', true);
                    }

                });

                $("#selectCompany").on("change", function (e) {
                    selectedCompany = this.value;
                    reloadTableProjects();
                });
            });

        </script>
        <script>
            var tableProjects;
            $(document).ready(function () {
                tableProjects = $('#list_projects').DataTable({
                    "paging": true,
                    "lengthChange": true,
                    "searching": false,
                    "ordering": true,
                    "info": false,
                    "autoWidth": false,
                    "ajax": "../context?action=listProjects&selectedCompany=" + selectedCompany,
                    "columns": [
                        {"data": "id"},
                        {"data": "name"},
                        {"data": "active"}
                    ],
                    "columnDefs": [
                        {
                            "visible": false, "searchable": false, "targets": [0]
                        },
                        {
                            "visible": false, "searchable": false, "targets": [2]
                        }
                    ],
                    "createdRow": function (row, data, index) {
                        if (data["active"] === 0) {
                            $(row).addClass('disabled');
                        }
                    }
                });
                toggleEditProjectBtn();
//Row click

                $('#list_projects tbody').on('click', 'tr', function () {
                    var data = tableProjects.row(this).data();
                    if (data["active"] === 1) {
                        if ($(this).hasClass('active')) {
                            $(this).removeClass('active');
                        }
                        else {
                            tableProjects.$('tr.active').removeClass('active');
                            $(this).addClass('active');
                        }
                        $('#editProject input[id=inputId]').val(data["id"]);
                        $('#editProject input[id=inputName]').val(data["name"]);
                        $('#editProject input[id=inputDescription]').val(data["description"]);
                        $('#editProject input[id=inputUrl]').val(data["url"]);
                        if (data["active"] === 1) {
                            $('#editProject input[id=inputActive]').prop('checked', false);
                        } else {
                            $('#editProject input[id=inputActive]').prop('checked', true);
                        }
                        toggleEditProjectBtn();
                    }
                });

// Setup - add a text input to each footer cell
                $('#list_projects tfoot th').each(function () {
                    var title = $('#list_projects thead th').eq($(this).index()).text();
                    $(this).html('<input type="text" placeholder="Search ' + title + '" />');
                });

// Apply the search
//This is not working TBD
                tableProjects.columns().every(function () {
                    var that = this;

                    $('input', this.footer()).on('keyup change', function () {
                        if (that.search() !== this.value) {
                            that.search(this.value).draw();
                        }
                    });
                });
            });

            function reloadTableProjects() {
                tableProjects.ajax.url("../context?action=listProjects&selectedCompany=" + selectedCompany).load(); // user paging is not reset on reload
            }
            function toggleEditProjectBtn() {
                if (tableProjects.rows('.active').data().length === 1) {
                    $('#btnEditProject').prop('disabled', false);
                } else {
                    $('#btnEditProject').prop('disabled', true);
                }
            }
        </script>    

    </body>

</html>
