<%-- 
    Document   : image
    Created on : Sep 28, 2016, 10:30:31 AM
    Author     : REVV-03
--%>
<%@page import="com.revvster.playright.model.Data"%>
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
<html lang="en">

    <head>

        <title>Data</title>

        <jsp:include page="header.jsp"/>
        <jsp:include page="datatables_css.jsp"/>
        <link rel="stylesheet" href="../adminlte2/plugins/select2/select2.min.css">
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

        <div class="wrapper" style="background-color: lightgrey">

            <!-- Navigation -->



            <!-- /.Navigation -->

            <!-- Content Wrapper. Contains page content -->
            <div>
                <!-- Content Header (Page header) -->

                <!-- Main content -->
                <section class="content">
                    <div class="row">                       
                        <h2 class="box-title" align='center'>Playright Media Analysis</h3> 
                            <div id="getData">
                            </div>

                    </div>                   
                    <div class="row">
                        <div class="col-sm-3"></div>
                        <div class="col-sm-6">
                            <a>
                                <!-- The user image in the navbar-->

                                <img class="col-sm-12" src="../image?action=getPhoto&imageId=<%=request.getParameter("imageId")%>" alt="Image"/>                    
                                <!-- hidden-xs hides the username on small devices so only the image appears. -->

                            </a>
                        </div> 
                        <div class="col-sm-3"></div>
                    </div>  
                    <div class="row">
                        <h2 align='center'> <a href="printImage.jsp?id=<%=request.getParameter("imageId")%>" target="_blank" > Click here to view printable version. </a></h2>
                    </div>
                </section>
                <jsp:include page="control_sidebar.jsp"/>
            </div>   
            <!-- /.content -->

        </div><!-- /.content-wrapper -->


        <jsp:include page="control_sidebar.jsp"/>
        <jsp:include page="footer.jsp"/>
        <jsp:include page="datatables_js.jsp"/>
        <script src="../adminlte2/plugins/select2/select2.full.min.js"></script>
        <script src="../adminlte2/plugins/datepicker/js/bootstrap-datepicker.min.js"></script>            
        <jsp:include page="adminlte_js.jsp"/>
<!--        <script type="text/javascript">
            var id = 
            $(function () {
                $.ajax({
                    url: "../dashboard?action=getData",
                    data: 'id=' + id,
                    type: "POST",
                    dataType: "json"
                })
                        .done(function (data, textStatus, jqXHR) {
                            //$('#getData').html(data.newsPaper);
                            $("#getData").append(getData(data));

                        })
                        .fail(function (jqXHR, textStatus, errorThrown) {
                            var respJson = JSON.parse(jqXHR.responseText);
                            var response = jQuery.parseJSON(respJson);
                            $.alert(response.errorMsg, "Error !!");
                        });

                function getData(data) {
                    return '<h4 class="box-title" align="center">' +
                            data.newsDate +
                            ',' +
                            data.newsPaper +
                            ',' +
                            'Page No.' +
                            data.pageNo +
                            '</h4>' +
                            '<h3 class="box-title" align="center">' +
                            '<strong>' +
                            data.headline +
                            '</strong>' +
                            '</h3>';
                }

            });

        </script> -->

    </body>
</html>


