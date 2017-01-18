<%-- 
    Document   : home
    Created on : Sep 29, 2015, 5:07:59 PM
    Author     : Rahul
--%>

<%@page import="com.revvster.playright.model.Project"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.revvster.playright.model.ContextObj"%>
<%@page import="com.revvster.playright.access.Action"%>
<%@page import="com.revvster.playright.access.Resource"%>
<%@page import="com.revvster.playright.access.AuthorizationManager"%>
<%@page import="com.revvster.playright.model.UserEntitlement"%>
<%@page import="java.util.List"%>
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

    List<ContextObj> projects = new ArrayList<>();
    if (user.getContextScope() != null) {
        if (user.getContextScope().containsKey(RoleEntContext.PROJECT)) {
            projects = user.getContextScope().get(RoleEntContext.PROJECT);

        }
    }
%>
<html lang="en">

    <head>

        <title>Home</title>

        <jsp:include page="header.jsp"/>
        <link rel="stylesheet" href="../adminlte2/plugins/select2/select2.min.css">
        <link rel="stylesheet" href="../adminlte2/plugins/select2/select2-bootstrap.min.css">
        <link rel="stylesheet" href="../adminlte2/bootstrap/css/bootstrap.min.css"> 
        <link rel="stylesheet" href="../adminlte2/plugins/datepicker/css/bootstrap-datepicker3.min.css">
        <style>
            .select2-container--default .select2-selection--single{
                font-size: 1.875em;  
            }
            .select2-container .select2-selection--single .select2-selection__rendered{
                color: #3C8DBC;                
            }
        </style>
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
        <div class="wrapper">

            <!-- Navigation -->
            <jsp:include page="navigation_top.jsp" flush="false" />

            <jsp:include page="navigation_left.jsp" flush="false" />
            <!-- /.Navigation -->

            <!-- Content Wrapper. Contains page content -->
            <div class="content-wrapper">
                <!-- Content Header (Page header) -->

                <!-- Main content -->

            </div><!-- /.content-wrapper -->
        </div><!-- /#wrapper -->

        <jsp:include page="control_sidebar.jsp"/>
        <jsp:include page="footer.jsp"/>
        <script src="../adminlte2/plugins/select2/select2.full.min.js"></script>
        <script src="../adminlte2/plugins/datepicker/js/bootstrap-datepicker.min.js"></script>
        <script src="../adminlte2/plugins/chartjs/Chart.bundle.min.js"></script> 
        <script src="http://maps.googleapis.com/maps/api/js?key=AIzaSyBmWm8nzR95Wd7MPf0mKnUVMid6d5m5qAo"></script>
        <jsp:include page="adminlte_js.jsp"/>
        <script type="text/javascript">
    var selectedCompany = <%=user.getCompany() == null ? 0 : user.getCompany()%>;
    var selectedProject = 0;
    var projectsData;
    var selectProject = $("#selectProject");
    var chart1;
    $(function () {
        $.ajax({
            type: "POST",
            url: "../context?action=listProjectsByContext",
            data: {
                "selectedCompany": selectedCompany,
                "resource": "project",
                "act": "view"
            },
            dataType: 'json'
        })
                .done(function (data, textStatus, jqXHR) {
                    var response = data;
                    if (response.result === "error") {
                        ajaxHandleError(response);
                    } else {
                        projectsData = data.data;
                        loadProjects();
                    }
                })
                .fail(function (jqXHR, textStatus, errorThrown) {
                    var respJson = JSON.parse(jqXHR.responseText);
                    var response = jQuery.parseJSON(respJson);
                    $.alert(response.errorMsg, "Error !!");
                });

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
                    reloadAllElements();
                }
            });
            selectedProject = $("#selectProject").val();
            reloadAllElements();
//                    if (projectsData.length === 1) {
//                        $("#filters").hide();
//                    }
        }

        function loadMap() {
            var coordinates = [0, 0];
            for (i = 0; i < projectsData.length; i++) {
                if (projectsData[i].id === Number(selectedProject)) {
                    if (projectsData[i].coordinates !== null &&
                            typeof projectsData[i].coordinates !== "undefined") {
                        coordinates = projectsData[i].coordinates.split(',');
                    } else {
                        coordinates = [0, 0];
                    }
                    break;
                }
            }
            var projLatLng = new google.maps.LatLng(coordinates[0], coordinates[1]);
            var mapProp = {
                center: projLatLng,
                zoom: 15,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };
            var map = new google.maps.Map($("#projMap").get(0), mapProp);

            var marker = new google.maps.Marker({
                position: projLatLng,
                map: map
            });
            marker.setMap(map);


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

        function getSalesData() {
            $.ajax({
                url: "../dashboard?action=getSalesDataForProjectInDateRange",
                data: {
                    selectedProject: selectedProject,
                    selectedDateRange: "last6Months",
                    inputFromDate: "",
                    inputToDate: ""
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
//                        chart1 = new Chart($("#chart1").get(0).getContext("2d")).Line(data, lineChartOptions);
                    })
                    .fail(function (jqXHR, textStatus, errorThrown) {
                        var respJson = JSON.parse(jqXHR.responseText);
                        var response = jQuery.parseJSON(respJson);
                        $.alert(response.errorMsg, "Error !!");
                    });
        }

        function reloadAllElements() {
            loadMap();
            getSalesData();
            getDescription();
            getConversionRate();
            getVisitsPerSale();
            getProjectdata();
            getHighestSelling();
        }

        function getProjectdata() {
            $.ajax({
                url: "../dashboard?action=getProjectData",
                data: 'selectedProject=' + selectedProject,
                type: "POST",
                dataType: "json"
            })
                    .done(function (data, textStatus, jqXHR) {
                        $('#projectdata').html(data);
                        $('#availableunits').html(data.unitsAvailable);
                        $('#totalVisits').html(data.totalVisits);
                        $('#unitsSold').html(data.unitsSold);
                        $('#champion').html(data.salesRep);
                        // console.log(data.unitsAvailable);

                    })
                    .fail(function (jqXHR, textStatus, errorThrown) {
                        var respJson = JSON.parse(jqXHR.responseText);
                        var response = jQuery.parseJSON(respJson);
                        $.alert(response.errorMsg, "Error !!");
                    });
        }


        function getVisitsPerSale() {
            $.ajax({
                url: "../dashboard?action=getVisitsPerSale",
                data: 'selectedProject=' + selectedProject,
                type: "POST",
                dataType: "json"
            })
                    .done(function (data, textStatus, jqXHR) {
                        $('#visitsPerSale').html(data);
                    })
                    .fail(function (jqXHR, textStatus, errorThrown) {
                        var respJson = JSON.parse(jqXHR.responseText);
                        var response = jQuery.parseJSON(respJson);
                        $.alert(response.errorMsg, "Error !!");
                    });

        }
        function getDescription() {
            $.ajax({
                url: "../dashboard?action=getProject",
                data: 'selectedProject=' + selectedProject,
                type: "POST",
                dataType: "json"
            })
                    .done(function (data, textStatus, jqXHR) {
                        $('#projectDesc').html(data.description);
                    })
                    .fail(function (jqXHR, textStatus, errorThrown) {
                        var respJson = JSON.parse(jqXHR.responseText);
                        var response = jQuery.parseJSON(respJson);
                        $.alert(response.errorMsg, "Error !!");
                    });

        }

        function getConversionRate() {
            $.ajax({
                url: "../dashboard?action=getConversionRateForProject",
                data: 'selectedProject=' + selectedProject,
                type: "POST",
                dataType: "json"
            })
                    .done(function (data, textStatus, jqXHR) {
                        $('#conversionRate').html(data + ' %');
                    })
                    .fail(function (jqXHR, textStatus, errorThrown) {
                        var respJson = JSON.parse(jqXHR.responseText);
                        var response = jQuery.parseJSON(respJson);
                        $.alert(response.errorMsg, "Error !!");
                    });
        }
        function getHighestSelling() {
            $.ajax({
                url: "../dashboard?action=getHighestSellingForProject",
                data: 'selectedProject=' + selectedProject,
                type: "POST",
                dataType: "json"
            })
                    .done(function (data, textStatus, jqXHR) {
                        $('#highestSelling').html(data);
                    })
                    .fail(function (jqXHR, textStatus, errorThrown) {
                        var respJson = JSON.parse(jqXHR.responseText);
                        var response = jQuery.parseJSON(respJson);
                        $.alert(response.errorMsg, "Error !!");
                    });
        }
    });
        </script> 

    </body>
</html>
