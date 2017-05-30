<%-- 
    Document   : analytics
    Created on : Sep 26, 2016, 11:29:31 AM
    Author     : REVV-03--%>

<%@page import="com.revvster.playright.model.ContextObj"%>
<%@page import="com.revvster.playright.model.Settings"%>
<%@page import="java.util.HashMap"%>
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

    RoleEntContext listCtx = null;
    int listCtxSize = 0;
    int selectedCtxObj = 0;

    if (user.getUserEntitlements() != null) {
        List<UserEntitlement> ues = user.getUserEntitlements();
        listCtx = AuthorizationManager.getRoleEntContext(ues,
                new UserEntitlement(Resource.analytics, Action.list));
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

        <title>Analytics</title>

        <jsp:include page="header.jsp"/>
        <jsp:include page="datatables_css.jsp"/>
        <link rel="stylesheet" href="../adminlte2/plugins/select2/select2.min.css">
        <link rel="stylesheet" href="../adminlte2/plugins/select2/select2-bootstrap.min.css">
        <link rel="stylesheet" href="../adminlte2/plugins/datepicker/css/bootstrap-datepicker3.min.css">
        <style>
            .select2-container--default .select2-selection--single{
                font-size: 1.50em;  
            }
            .select2-container .select2-selection--single .select2-selection__rendered{
                color: #3C8DBC;                
            }
        </style>
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
        <div class="modal fade bs-example-modal-sm" id="modalSendEmail" role="dialog" aria-labelledby="sendEmail">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="titleUser">Send Email</h4>
                    </div>
                    <div class="modal-body">
                        <form role="form" id="sendEmail">    
                            <input type="hidden" id="emailBody" name="emailBody"  class="form-control">
                            <div class="form-group">
                                <label for="inputEmailAddress">Recipient Email Address</label>
                                <input type="text" class="form-control" id="inputEmailAddress" name="inputEmailAddress" placeholder="Email">
                            </div>
                            <div class="form-group">
                                <label for="inputSubject">Email Subject</label>
                                <input type="text" class="form-control" id="inputSubject" name="inputSubject" placeholder="Subject">
                            </div>  
                            <div class="form-group">
                                <label for="inputContent">Email Content</label>                              

                                <textarea class="form-control" rows="3" id="inputContent" name="inputContent" placeholder="Content"></textarea>
                            </div>

                            <div>
                                <button type="button" class="btn btn-primary" id="btnSubmitSendEmail">Submit</button>
                            </div>
                        </form>
                    </div>


                    <div class="modal-footer">                   

                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
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
                <section class="content">      
                    <div class="row" id="filters">
                        <div class="col-sm-12">
                            <div class="box">
                                <div class="box-header with-border">

                                    <div class="col-md-3 col-sm-6 col-xs-12">
                                        <div class="form-group">
                                            <select id="selectDateRange" name="selectDateRange" class="form-control" style="width: 100%;">
                                                <!--                                                <option value="null"></option>-->
                                                <option value="last7Days">Last 7 Days</option>
                                                <option value="lastMonth">Last Month</option>
                                                <option value="lastQuarter">Last Quarter</option>
                                                <option value="last6Months">Last 6 Months</option>
                                                <option value="lastYear">Last Year</option>    
                                                <option value="dateRange">Select Date Range</option>
                                            </select>                                
                                        </div>
                                    </div> 
                                    <div class="col-md-2 col-sm-6 col-xs-12" id="fromDate" hidden>    
                                        <div class="form-group">
                                            <div  class="input-group date">
                                                <input type="text" class="form-control" id="inputFromDate" name="inputFromDate" data-provide="datepicker" placeholder="From Date">
                                                <div class="input-group-addon">
                                                    <span class="glyphicon glyphicon-calendar"></span>
                                                </div>
                                            </div>                                                                                        
                                        </div>                                        
                                    </div>                                     
                                    <div class="col-md-2 col-sm-6 col-xs-12" id="toDate" hidden>    
                                        <div class="form-group">
                                            <div class="input-group date">
                                                <input type="text" class="form-control" id="inputToDate" name="inputToDate" data-provide="datepicker" placeholder="To Date">
                                                <div class="input-group-addon">
                                                    <span class="glyphicon glyphicon-calendar"></span>
                                                </div>
                                            </div>

                                        </div>

                                    </div>  
                                    <!--                                    <div class="col-md-2 col-sm-6 col-xs-12">    
                                                                            <div class="input-group input-daterange">
                                                                                <input type="text" class="form-control" id="inputFromDate" name="inputFromDate" data-provide="datepicker">
                                                                                <span class="input-group-addon">to</span>
                                                                                <input type="text" class="form-control" id="inputToDate" name="inputToDate" data-provide="datepicker">
                                                                            </div>
                                                                        </div>-->
                                    <div class="col-md-2 col-sm-6 col-xs-12">    
                                        <button type="button" id="btnApplyFilter" class="btn btn-primary btn-sm">Apply Date Filter</button>
                                        <button type="button" id="resetFilter" class="btn btn-link"><small>Reset Filters</small></small></button>
                                    </div>   
                                    <div class="col-md-2 col-sm-6 col-xs-12">
                                        <button type="button" id="btnSendEmail" class="btn btn-primary" data-toggle="modal" data-target="#modalSendEmail">Email Charts</button>
                                    </div>
                                </div>
                            </div><!-- /.box -->
                        </div>                        
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <!--                            <div class="box box-primary">
                                                            <div class="box-body">-->                           
                            <div class="row">
                                <div class="col-md-6 col-sm-12 col-xs-12">
                                    <div class="box box-primary">
                                        <div class="box-header with-border">
                                            <h3 class="box-title">Language Distribution of Coverage</h3>
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
                                    </div><!-- /.box -->
                                </div>

                                <div class="col-md-6 col-sm-12 col-xs-12">
                                    <div class="box box-danger">
                                        <div class="box-header with-border">
                                            <h3 class="box-title">National Distribution of Coverage</h3>

                                            <div class="box-tools pull-right">
                                                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                                                </button>
                                                <button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
                                            </div>
                                        </div>
                                        <div class="box-body">
                                            <canvas id="pieChart" style="height:250px"></canvas>
                                        </div>
                                        <!-- /.box-body -->
                                    </div>                                  
                                </div>

                            </div>                                    
                            <!--                                </div>
                                                        </div> /.box -->
                        </div>                        
                    </div>   
                    <div class="row">
                        <div class="col-md-6 col-sm-12 col-xs-12">
                            <div class="box box-danger" id="topPerformingBox">
                                <div class="box-header with-border">
                                    <h3 class="box-title">Journalist Distribution</h3>
                                    <div class="box-tools pull-right">
                                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                                        <button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
                                    </div>
                                </div>
                                <div class="box-body">
                                    <div class="chart">
                                        <canvas id="pieChart1" style="height:310px"></canvas>
                                    </div>
                                </div>
                            </div>      
                        </div> 

                        <div class="col-md-6 col-sm-12 col-xs-12">
                            <div class="box box-primary" id="topPerformingBox">
                                <div class="box-header with-border">
                                    <h3 class="box-title">Top English Print Distribution</h3>
                                    <div class="box-tools pull-right">
                                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                                        <button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
                                    </div>
                                </div>
                                <div class="box-body">
                                    <div class="chart">
                                        <canvas id="chart4" style="height:310px"></canvas>
                                    </div>
                                </div>
                            </div>      
                        </div>  
                    </div>
                    <div class="row">                      

                        <div class="col-md-6 col-sm-12 col-xs-12">
                            <div class="box box-primary" id="topPerformingBox">
                                <div class="box-header with-border">
                                    <h3 class="box-title">Top Vernacular Print Distribution</h3>
                                    <div class="box-tools pull-right">
                                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                                        <button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
                                    </div>
                                </div>
                                <div class="box-body">
                                    <div class="chart">
                                        <canvas id="chart5" style="height:310px"></canvas>
                                    </div>
                                </div>
                            </div>      
                        </div> 
                    </div>
                </section><!-- /.content -->

            </div><!-- /.content-wrapper -->
        </div><!-- /#wrapper -->

        <jsp:include page="control_sidebar.jsp"/>
        <jsp:include page="footer.jsp"/>
        <script src="../adminlte2/plugins/select2/select2.full.min.js"></script>
        <script src="../adminlte2/plugins/datepicker/js/bootstrap-datepicker.min.js"></script>
        <script src="../adminlte2/plugins/chartjs/Chart.bundle.min.js"></script>   
        <!--        <script src="../adminlte2/plugins/chartjs/Chart.min.js"></script>   -->
        <jsp:include page="adminlte_js.jsp"/>
        <script type="text/javascript">
            var chart1;
            var chart2;
            var chart4;
            var chart5;
            var pieChart;
            var pieChart1;
            var selectDateRange = $("#selectDateRange");
            var inputFromDate = $("#inputFromDate");
            var inputToDate = $("#inputToDate");
            var selectedDateRange;
            var barChartOptions = {
                scales: {
                    yAxes: [{
                            ticks: {
                                beginAtZero: true
                            }
                        }]
                }
            };
            var options = {
                //Boolean - Whether we should show a stroke on each segment
                segmentShowStroke: true,
                //String - The colour of each segment stroke
                segmentStrokeColor: "#fff",
                //Number - The width of each segment stroke
                segmentStrokeWidth: 2,
                //Number - The percentage of the chart that we cut out of the middle
                percentageInnerCutout: 50, // This is 0 for Pie charts
                //Number - Amount of animation steps
                animationSteps: 100,
                //String - Animation easing effect
                animationEasing: "easeOutBounce",
                //Boolean - Whether we animate the rotation of the Doughnut
                animateRotate: true,
                //Boolean - Whether we animate scaling the Doughnut from the centre
                animateScale: false,
                //Boolean - whether to make the chart responsive to window resizing
                responsive: true,
                // Boolean - whether to maintain the starting aspect ratio or not when responsive, if set to false, will take up entire container
                maintainAspectRatio: true
                        //String - A legend template

            };

            $(function () {

                function loadTopEnglishPrintDistribution() {

                    $.ajax({
                        url: "../dashboard?action=getTopEnglishPrintDistribution",
                        data: {
                            selectedDateRange: selectDateRange.val(),
                            inputFromDate: inputFromDate.val(),
                            inputToDate: inputToDate.val()
                        },
                        type: "POST",
                        dataType: "json"
                    })
                            .done(function (data, textStatus, jqXHR) {
                                if (chart4 !== null &&
                                        typeof chart4 !== "undefined") {
                                    chart4.destroy();
                                }
                                var ctx = $("#chart4");
                                chart4 = new Chart(ctx, {
                                    type: 'bar',
                                    data: data,
                                    options: barChartOptions
                                });
//                                chart2 = new Chart($("#chart2").get(0).getContext("2d")).Bar(data, barChartOptions);
                            })
                            .fail(function (jqXHR, textStatus, errorThrown) {
                                var respJson = JSON.parse(jqXHR.responseText);
                                var response = jQuery.parseJSON(respJson);
                                $.alert(response.errorMsg, "Error !!");
                            });
                }
                function loadTopVernacularPrintDistribution() {
                    $.ajax({
                        url: "../dashboard?action=getTopVernacularPrintDistribution",
                        data: {
                            selectedDateRange: selectDateRange.val(),
                            inputFromDate: inputFromDate.val(),
                            inputToDate: inputToDate.val()
                        },
                        type: "POST",
                        dataType: "json"
                    })
                            .done(function (data, textStatus, jqXHR) {
                                if (chart5 !== null &&
                                        typeof chart5 !== "undefined") {
                                    chart5.destroy();
                                }
                                var ctx = $("#chart5");
                                chart5 = new Chart(ctx, {
                                    type: 'bar',
                                    data: data,
                                    options: barChartOptions
                                });
//                                chart2 = new Chart($("#chart2").get(0).getContext("2d")).Bar(data, barChartOptions);
                            })
                            .fail(function (jqXHR, textStatus, errorThrown) {
                                var respJson = JSON.parse(jqXHR.responseText);
                                var response = jQuery.parseJSON(respJson);
                                $.alert(response.errorMsg, "Error !!");
                            });
                }
                function loadEditionAnalytics() {
                    $.ajax({
                        url: "../dashboard?action=getEditionAnalytics",
                        data: {
                            selectedDateRange: selectDateRange.val(),
                            inputFromDate: inputFromDate.val(),
                            inputToDate: inputToDate.val()
                        },
                        type: "POST",
                        dataType: "json"
                    })
                            .done(function (data, textStatus, jqXHR) {
                                if (pieChart !== null &&
                                        typeof pieChart !== "undefined") {
                                    pieChart.destroy();
                                }
                                var ctx = $("#pieChart");
                                pieChart = new Chart(ctx, {
                                    type: 'doughnut',
                                    data: data,
                                    options: options
                                });
//                                chart2 = new Chart($("#chart2").get(0).getContext("2d")).Bar(data, barChartOptions);
                            })
                            .fail(function (jqXHR, textStatus, errorThrown) {
                                var respJson = JSON.parse(jqXHR.responseText);
                                var response = jQuery.parseJSON(respJson);
                                $.alert(response.errorMsg, "Error !!");
                            });
                }
                function loadJournalistDistribution() {
                    $.ajax({
                        url: "../dashboard?action=getJournalistDistribution",
                        data: {
                            selectedDateRange: selectDateRange.val(),
                            inputFromDate: inputFromDate.val(),
                            inputToDate: inputToDate.val()
                        },
                        type: "POST",
                        dataType: "json"
                    })
                            .done(function (data, textStatus, jqXHR) {
                                if (pieChart1 !== null &&
                                        typeof pieChart1 !== "undefined") {
                                    pieChart1.destroy();
                                }
                                var ctx = $("#pieChart1");
                                pieChart1 = new Chart(ctx, {
                                    type: 'pie',
                                    backgroundColor: "#F5DEB3",
                                    data: data,
                                    options: options

                                });
//                                chart2 = new Chart($("#chart2").get(0).getContext("2d")).Bar(data, barChartOptions);
                            })
                            .fail(function (jqXHR, textStatus, errorThrown) {
                                var respJson = JSON.parse(jqXHR.responseText);
                                var response = jQuery.parseJSON(respJson);
                                $.alert(response.errorMsg, "Error !!");
                            });
                }

                function loadLanguageAnalytics() {
                    $.ajax({
                        url: "../dashboard?action=getLanguageAnalytics",
                        data: {
                            selectedDateRange: selectDateRange.val(),
                            inputFromDate: inputFromDate.val(),
                            inputToDate: inputToDate.val()
                        },
                        type: "POST",
                        dataType: "json"
                    })
                            .done(function (data, textStatus, jqXHR) {
                                if (chart1 !== null &&
                                        typeof chart1 !== "undefined") {
                                    chart1.destroy();
                                }
                                var ctx = $("#chart1");
                                chart1 = new Chart(ctx, {
                                    type: 'bar',
                                    data: data,
                                    options: barChartOptions
                                });
//                                chart2 = new Chart($("#chart2").get(0).getContext("2d")).Bar(data, barChartOptions);
                            })
                            .fail(function (jqXHR, textStatus, errorThrown) {
                                var respJson = JSON.parse(jqXHR.responseText);
                                var response = jQuery.parseJSON(respJson);
                                $.alert(response.errorMsg, "Error !!");
                            });
                }
                $("#btnApplyFilter").on("click", function (event) {
                    reloadAllElements();

                });
                $("#resetFilter").click(function () {
                    selectDateRange.val("last7Days").trigger("change");
                    inputFromDate.val("").trigger("change");
                    inputToDate.val("").trigger("change");
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
                selectDateRange.select2({minimumResultsForSearch: "Infinity"}).on("change", function (e) {
                    //  console.log(this.value);
                    if (typeof this.value !== "undefined"
                            && this.value !== '') {
                        if (this.value === 'dateRange') {
                            $("#fromDate").show();
                            $("#toDate").show();
                        } else {
                            $("#fromDate").hide();
                            $("#toDate").hide();
                            inputFromDate.val("").trigger("change");
                            inputToDate.val("").trigger("change");
                        }
                        selectedDateRange = this.value;

                    }
                });

                function reloadAllElements() {
                    loadLanguageAnalytics();
                    loadTopVernacularPrintDistribution();
                    loadJournalistDistribution();
                    loadEditionAnalytics();
                    loadTopEnglishPrintDistribution();
                }
                reloadAllElements();

                $("#btnSubmitSendEmail").on("click", function () {
                    $.ajax({
                        type: "POST",
                        url: "../user?action=sendChartEmail",
                        data: $("form#sendEmail").serialize(),
                        dataType: 'json'
                    })
                            .done(function (data, textStatus, jqXHR) {
                                var response = data;
                                if (response.result === "error") {
                                    ajaxHandleError(response);
                                } else {
                                    ajaxHandleSuccess(response);
                                    $("#modalSendEmail").modal('hide');
                                    reloadAllElements();
                                }
                            })
                            .fail(function (jqXHR, textStatus, errorThrown) {
                                alert(data);
                                var respJson = JSON.parse(jqXHR.responseText);
                                var response = jQuery.parseJSON(respJson);
                                $.alert(response.errorMsg, "Error !!");
                            });
                });
            });

        </script> 
    </body>

</html>

