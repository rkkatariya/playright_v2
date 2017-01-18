<%-- 
    Document   : testchart
    Created on : 7 Jun, 2016, 9:28:50 AM
    Author     : rahulkk
--%>
<%@page import="com.revvster.playright.util.SystemConstants"%>
<%@page import="com.revvster.playright.model.User"%>
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
<%
    User user = new User();
    if (session.getAttribute(SystemConstants.LoggedInUser) != null) {
        user = (User) session.getAttribute(SystemConstants.LoggedInUser);
    }
%>

<html lang="en">

    <head>

        <title>Inventory Analytics</title>

        <jsp:include page="header.jsp"/>
        <jsp:include page="adminlte_css.jsp"/>
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

        <div class="wrapper">

            <!-- Navigation -->
            <jsp:include page="navigation_top.jsp" flush="false" />

            <jsp:include page="navigation_left.jsp" flush="false" />
            <!-- /.Navigation -->
            <!-- Content Wrapper. Contains page content -->
            <div class="content-wrapper">
                <!-- Content Header (Page header) -->

                <!-- Main content -->
                <section class="content">

                    <div class="row">

                        <div class="col-md-8 col-sm-8 col-xs-12">
                            <div class="box box-primary">
                                <div class="box-body">
                                    <div class="chart">
                                        <canvas id="myChart" width="400" height="400"></canvas>
                                    </div>
                                </div><!-- /.box-body -->
                            </div><!-- /.box -->
                        </div>                        
                    </div>   
                </section><!-- /.content -->

            </div><!-- /.content-wrapper -->
        </div><!-- /#wrapper -->

        <jsp:include page="control_sidebar.jsp"/>
        <jsp:include page="footer.jsp"/>
        <script src="../adminlte2/plugins/chartjs/Chart.bundle.min.js"></script>
        <jsp:include page="adminlte_js.jsp"/>
        <script>
            var ctx = document.getElementById("myChart");
            var myChart = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"],
                    datasets: [{
                            label: '# of Votes',
                            data: [12, 19, 3, 5, 2, 3],
                            backgroundColor: [
                                'rgba(255, 99, 132, 0.2)',
                                'rgba(54, 162, 235, 0.2)',
                                'rgba(255, 206, 86, 0.2)',
                                'rgba(75, 192, 192, 0.2)',
                                'rgba(153, 102, 255, 0.2)',
                                'rgba(255, 159, 64, 0.2)'
                            ],
                            borderColor: [
                                'rgba(255,99,132,1)',
                                'rgba(54, 162, 235, 1)',
                                'rgba(255, 206, 86, 1)',
                                'rgba(75, 192, 192, 1)',
                                'rgba(153, 102, 255, 1)',
                                'rgba(255, 159, 64, 1)'
                            ],
                            borderWidth: 1
                        }]
                },
                options: {
                    scales: {
                        yAxes: [{
                                ticks: {
                                    beginAtZero: true
                                }
                            }]
                    }
                }
            });
        </script>
    </body>

</html>
