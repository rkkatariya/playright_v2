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

%>
<html lang="en">
    <head>     
        <title>Data</title>

        <jsp:include page="header.jsp"/>
        <jsp:include page="datatables_css.jsp"/>

        <link rel="stylesheet" href="https://cdn.datatables.net/1.10.13/css/dataTables.bootstrap.min.css">
        <link rel="stylesheet" href="https://cdn.datatables.net/responsive/2.1.1/css/responsive.bootstrap.min.css">
        <link rel="stylesheet" href="https://cdn.datatables.net/buttons/1.2.4/css/buttons.dataTables.min.css">
        <link rel="stylesheet" href="../adminlte2/plugins/select2/select2.min.css">
        <link rel="stylesheet" href="../adminlte2/bootstrap/css/bootstrap.min.css"> 
        <link rel="stylesheet" href="../adminlte2/plugins/datepicker/css/bootstrap-datepicker3.min.css">
        <link rel="stylesheet" href="../DataTables/Buttons-1.1.0/css/buttons.dataTables.min.css"> 
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

        <div class="modal fade" id="modalCreateData" role="dialog" aria-labelledby="createData">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="createData">Add Data</h4>
                    </div>
                    <div class="modal-body">
                        <form role="form" id="createData">                           

                            <div class="box-body">
                                <div class="col-sm-5">                                    
                                    <div class="form-group">
                                        <label for="inputCustomer">Customer</label>
                                        <select id="inputCustomer" name="inputCustomer" class="form-control" style="width: 100%;" >
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="inputNewsDate">News Date</label> 
                                        <button type="button" class="btn btn-xs btn-warning" id="prevDayBtn">  <i class="fa fa-calendar-minus-o" data-toggle="tooltip" title="Previous Day"></i></button>
                                        <button type="button" class="btn btn-xs btn-primary" id="todayBtn">  <i class="fa fa-calendar-check-o" data-toggle="tooltip" title="Today"></i></button>
                                        <div class="input-group date" id="newsDateDiv">
                                            <input type="text" class="form-control" id="inputNewsDate" name="inputNewsDate">
                                            <span class="input-group-addon">
                                                <i class="glyphicon glyphicon-calendar"></i>
                                            </span>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="inputNewsPaper">News Paper</label>
                                        <input type="text" class="form-control" id="inputNewsPaper" name="inputNewsPaper" placeholder="News Paper">
                                    </div>
                                    <div class="form-group">
                                        <label for="inputLanguage">Language</label>
                                        <select id="inputLanguage" name="inputLanguage" class="form-control" style="width: 100%;" >
                                        </select>                            
                                    </div>
                                    <div class="form-group">
                                        <label for="inputHeadline">Headline</label>
                                        <input type="text" class="form-control" id="inputHeadline" name="inputHeadline" placeholder="Headline">
                                    </div>
                                    <div class="form-group">
                                        <label for="inputEdition">Edition</label>
                                        <input type="text" class="form-control" id="inputEdition" name="inputEdition" placeholder="Edition">
                                    </div>
                                    <div class="form-group">
                                        <label for="inputSupplement">Supplement</label>
                                        <input type="text" class="form-control" id="inputSupplement" name="inputSupplement" placeholder="Supplement">
                                    </div>
                                    <div class="form-group">
                                        <label for="inputSource">Source</label>
                                        <input type="text" class="form-control" id="inputSource" name="inputSource" placeholder="Source">
                                    </div>

                                    <div class="form-group">
                                        <label for="inputImageExists" class="control-label">Image Exists</label>
                                        <div class="radio">                                       
                                            <label>
                                                <input type="radio" name="inputImageExists" value="Y">
                                                Yes
                                            </label>
                                            <label>
                                                <input type="radio" name="inputImageExists" value="N">
                                                No
                                            </label>
                                        </div>                                   
                                    </div>


                                </div>
                                <div class="col-sm-2">                                    
                                </div>                                                                
                                <div class="col-sm-5">
                                    <div class="form-group">
                                        <label for="inputPageNo">Page No</label>
                                        <input type="number" class="form-control" id="inputPageNo" name="inputPageNo" placeholder="Page No">
                                    </div>
                                    <div class="form-group">
                                        <label for="inputHeight">Height</label>
                                        <input type="number" class="form-control" id="inputHeight" name="inputHeight" placeholder="Height">
                                    </div>

                                    <div class="form-group">
                                        <label for="inputWidth">Width</label>
                                        <input type="number" class="form-control" id="inputWidth" name="inputWidth" placeholder="Width">
                                    </div>

                                    <div class="form-group">
                                        <label for="inputTotalArticleSize">Total Article Size</label>
                                        <input type="number" class="form-control" id="inputTotalArticleSize" name="inputTotalArticleSize" placeholder="Total Article Size">
                                    </div>

                                    <div class="form-group">
                                        <label for="inputCirculationFigure">Circulation Figure</label>
                                        <input type="number" class="form-control" id="inputCirculationFigure" name="inputCirculationFigure" placeholder="Circulation Figure">
                                    </div>                                   

                                    <div class="form-group">
                                        <label for="inputJournalistFactor">Journalist Factor</label>
                                        <select id="inputJournalistFactor" name="inputJournalistFactor" class="form-control" style="width: 100%;" >
                                            <option value="1">Editor</option>
                                            <option value="2">Reporter</option>
                                            <option value="3">Special Correspondent</option>
                                            <option value="4">Bureau</option>
                                            <option value="5">PTI Copy</option>                                       
                                        </select>                            
                                    </div>
                                    <div class="form-group">
                                        <label for="inputImage">Image</label>
                                        <input type="file" name="inputImage" id="inputImage" accept="image/*">
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-3">                            
                                </div>
                                <div class="col-sm-3"> 
                                    <button type="submit" class="btn btn-primary pull-right" id="submitCreateData">Submit</button>
                                </div>
                                <div class="col-sm-3">    
                                    <button type="reset" class="btn btn-link" id="resetCreateData">Reset</button>
                                </div>
                                <div class="col-sm-3">                            
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

        <div class="modal fade" id="modalEditData" role="dialog" aria-labelledby="editData">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="editData">Edit Data</h4>
                    </div>
                    <div class="modal-body">
                        <form role="form" id="editData" class="form-horizontal">
                            <div class="box-body">
                                <div class="col-sm-5">
                                    <input type="hidden" id="inputId" name="inputId"  class="form-control">
                                    <input type="hidden" id="inputFileSize" name="inputFileSize"  class="form-control">
                                    <input type="hidden" id="inputFileType" name="inputFileType"  class="form-control">
                                    <div class="form-group">
                                        <label for="inputCustomer">Customer</label>
                                        <select id="inputCustomer" name="inputCustomer" class="form-control" style="width: 100%;" >
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="inputNewsDate">News Date</label>
                                        <div class="input-group date" id="newsDateDiv">
                                            <input type="text" class="form-control" id="inputNewsDate" name="inputNewsDate">
                                            <span class="input-group-addon">
                                                <i class="glyphicon glyphicon-calendar"></i>
                                            </span>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="inputNewsPaper">News Paper</label>
                                        <input type="text" class="form-control" id="inputNewsPaper" name="inputNewsPaper" placeholder="News Paper">
                                    </div>                                    
                                    <div class="form-group">
                                        <label for="inputLanguage">Language</label>
                                        <select id="inputLanguage" name="inputLanguage" class="form-control" style="width: 100%;" >
                                        </select>                            
                                    </div>
                                    <div class="form-group">
                                        <label for="inputHeadline">Headline</label>
                                        <input type="text" class="form-control" id="inputHeadline" name="inputHeadline" placeholder="Headline">
                                    </div>
                                    <div class="form-group">
                                        <label for="inputEdition">Edition</label>
                                        <input type="text" class="form-control" id="inputEdition" name="inputEdition" placeholder="Edition">
                                    </div>
                                    <div class="form-group">
                                        <label for="inputSupplement">Supplement</label>
                                        <input type="text" class="form-control" id="inputSupplement" name="inputSupplement" placeholder="Supplement">
                                    </div>
                                    <div class="form-group">
                                        <label for="inputSource">Source</label>
                                        <input type="text" class="form-control" id="inputSource" name="inputSource" placeholder="Source">
                                    </div>                              

                                    <div class="form-group">
                                        <label for="inputImageExists" class="control-label">Image Exists</label>
                                        <div class="radio">                                       
                                            <label>
                                                <input type="radio" name="inputImageExists" value="Y">
                                                Yes
                                            </label>
                                            <label>
                                                <input type="radio" name="inputImageExists" value="N">
                                                No 
                                            </label>
                                        </div>                                   
                                    </div>

                                </div>
                                <div class="col-sm-2">                                    
                                </div>                                                                
                                <div class="col-sm-5">
                                    <div class="form-group">
                                        <label for="inputPageNo">Page No</label>
                                        <input type="text" class="form-control" id="inputPageNo" name="inputPageNo" placeholder="Page No">
                                    </div>

                                    <div class="form-group">
                                        <label for="inputHeight">Height</label>
                                        <input type="text" class="form-control" id="inputHeight" name="inputHeight" placeholder="Height">
                                    </div>

                                    <div class="form-group">
                                        <label for="inputWidth">Width</label>
                                        <input type="text" class="form-control" id="inputWidth" name="inputWidth" placeholder="Width">
                                    </div>

                                    <div class="form-group">
                                        <label for="inputTotalArticleSize">Total Article Size</label>
                                        <input type="text" class="form-control" id="inputTotalArticleSize" name="inputTotalArticleSize" placeholder="Total Article Size">
                                    </div>

                                    <div class="form-group">
                                        <label for="inputCirculationFigure">Circulation Figure</label>
                                        <input type="text" class="form-control" id="inputCirculationFigure" name="inputCirculationFigure" placeholder="Circulation Figure">
                                    </div>                                   

                                    <div class="form-group">
                                        <label for="inputJournalistFactor">Journalist Factor</label>
                                        <select id="inputJournalistFactor" name="inputJournalistFactor" class="form-control" style="width: 100%;" >
                                            <option value="1">Editor</option>
                                            <option value="2">Reporter</option>
                                            <option value="3">Special Correspondent</option>
                                            <option value="4">Bureau</option>
                                            <option value="5">PTI Copy</option>                                       
                                        </select>                            
                                    </div>
                                    <div class="form-group">
                                        <label for="inputImage">Image</label>
                                        <input type="file" name="inputImage" id="inputImage" accept="image/*">
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-3">                            
                                </div>
                                <div class="col-sm-3"> 
                                    <button type="submit" class="btn btn-primary pull-right" id="submitEditData">Submit</button>
                                </div>
                                <div class="col-sm-3">                            
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
        <div class="modal fade bs-example-modal-sm" id="modalDisableData" role="dialog" aria-labelledby="disableData">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="titleUser">Disable Data</h4>
                    </div>
                    <div class="modal-body">
                        <div id="disableData">Are you sure do you want to delete this data?</div>
                        <form role="form" id="disableData">  
                            <input type="hidden" id="inputId" name="inputId"  class="form-control">
                        </form>                       
                    </div>
                    <div class="modal-footer">                        
                        <button type="button" class="btn btn-primary" id="submitDisableData">Yes</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                    </div>
                </div>
            </div>            
        </div>
        <div class="modal fade" id="modalSendEmail" role="dialog" aria-labelledby="sendEmail">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="titleUser">Send Email</h4>
                    </div>
                    <div class="modal-body">
                        <form role="form" id="sendEmail" class="form-horizontal"> 
                            <div class="box-body">                                
                                <div class="col-sm-5">
                                    <input type="hidden" id="emailBody" name="emailBody"  class="form-control">
                                    <div class="form-group">
                                        <label><h4>Filters</h4></label>
                                    </div>                                
                                    <div class="form-group">
                                        <label>Select dates</label>
                                        <div class="input-group input-daterange">                                    
                                            <input type="text" class="form-control" id="inputFromDate" name="inputFromDate" data-provide="datepicker">
                                            <span class="input-group-addon">to</span>
                                            <input type="text" class="form-control" id="inputToDate" name="inputToDate" data-provide="datepicker">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="inputCustomer">Customer</label>
                                        <select id="inputCustomer" name="inputCustomer" class="form-control" style="width: 100%;" >
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="selectLanguage">Language</label>
                                        <select id="selectLanguage" name="selectLanguage" class="form-control" style="width: 100%;" >
                                        </select>                            
                                    </div>
                                    <div class="form-group">
                                        <label for="inputEdition">Edition</label>
                                        <input type="text" class="form-control" id="inputEdition" name="inputEdition" placeholder="Edition">
                                    </div>                                    
                                    <div class="form-group">
                                        <label for="inputSource">Source</label>
                                        <input type="text" class="form-control" id="inputSource" name="inputSource" placeholder="Source">
                                    </div>
                                    <div class="form-group">
                                        <label for="inputNewsPaper">News Paper</label>
                                        <input type="text" class="form-control" id="inputNewsPaper" name="inputNewsPaper" placeholder="News Paper">
                                    </div>                                    
                                    <div class="form-group">
                                        <label for="inputImageExists" class="control-label">Image Exists</label>
                                        <div class="radio">                                       
                                            <label>
                                                <input type="radio" name="inputImageExists" value="Y">
                                                With Image
                                            </label>
                                            <label>
                                                <input type="radio" name="inputImageExists" value="N">
                                                Without Image
                                            </label>
                                        </div>                                   
                                    </div>
                                </div>
                                <div class="col-sm-2">                                    
                                </div>
                                <div class="col-sm-5">
                                    <div class="form-group">
                                        <label><h4>Email</h4></label>
                                    </div>
                                    <div class="form-group">
                                        <label for="inputEmailAddress">Recipient Email Address</label>
                                        <input type="text" class="form-control" id="inputEmailAddress" name="inputEmailAddress" placeholder="Email">
                                    </div>
                                    <div class="form-group">
                                        <label for="inputSubject">Subject</label>
                                        <input type="text" class="form-control" id="inputSubject" name="inputSubject" placeholder="Subject">
                                    </div>                                                    
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-3">                            
                                </div>
                                <div class="col-sm-3"> 
                                    <button type="submit" class="btn btn-primary pull-right" id="btnSubmitSendEmail">Submit</button>
                                </div>

                                <div class="col-sm-3">                            
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

                        <!--<small>Optional description</small>-->
                    </h1>
                </section>
                <!-- Main content -->
                <section class="content">  

                    <div class="row">
                        <div class="col-sm-12">
                            <div class="box box-primary ">
                                <div class="box-header with-border">
                                    <button type="button" id="btnCreateData" class="btn btn-primary" data-toggle="modal" data-target="#modalCreateData">Add</button>
                                    <button type="button" id="btnEditData" class="btn btn-primary" data-toggle="modal" data-target="#modalEditData">Edit</button>
                                    <button id="btnDisableData" type="button" class="btn btn-danger" data-toggle="modal" data-target="#modalDisableData">Delete</button>  
                                    <%if (null != user.getCompany() && user.getCompany() > 0) {%>
                                    <button type="button" id="btnSendEmail" class="btn btn-primary" data-toggle="modal" data-target="#modalSendEmail">Email Data</button>
                                    <%}%>
                                    <div class="col-sm-3">
                                        <select id="selectCustomer" name="selectCustomer" class="form-control" style="width: 100%;" >
                                        </select>
                                    </div> 
                                </div>
                                <div class="box-body">
                                    <table id="list_data" class="table table-striped table-bordered dt-responsive nowrap" cellspacing="0" width="100%">
                                        <thead>
                                            <tr>                                               
                                                <th>News Date</th>
                                                <th>News Paper</th>
                                                <th>Language</th>
                                                <th>Headline</th>
                                                <th>Edition</th>
                                                <th>Supplement</th>
                                                <th>Source</th>   
                                                <th>Image Exists</th>
                                                <th>Page No</th>
                                                <th>Height</th>
                                                <th>Width</th>
                                                <th>Total Article Size</th>
                                                <th>Circulation Figure</th>
                                                <th>Customer</th>
                                                <th>Journalist Factor</th>  
                                                <th>Last updated</th>  
                                                <th>Image</th>
                                            </tr>
                                        </thead>
                                        <tbody></tbody>                                            
                                        <tfoot>
                                            <tr>                                               
                                                <th>News Date</th>
                                                <th>News Paper</th>
                                                <th>Language</th>
                                                <th>Headline</th>
                                                <th>Edition</th>
                                                <th>Supplement</th>
                                                <th>Source</th>   
                                                <th>Image Exists</th>
                                                <th>Page No</th>
                                                <th>Height</th>
                                                <th>Width</th>
                                                <th>Total Article Size</th>
                                                <th>Circulation Figure</th>
                                                <th>Customer</th>
                                                <th>Journalist Factor</th>  
                                                <th>Last updated</th>  
                                                <th>Image</th>
                                            </tr>
                                        </tfoot>
                                    </table>
                                </div>
                            </div>
                        </div>                                   

                    </div>  
                </section>
            </div>   
            <!-- /.content -->

        </div><!-- /.content-wrapper -->
        <!-- /#wrapper -->

        <jsp:include page="control_sidebar.jsp"/>
        <jsp:include page="footer.jsp"/>
        <script src="https://cdn.datatables.net/1.10.13/js/jquery.dataTables.min.js"></script>
        <script src="https://cdn.datatables.net/1.10.13/js/dataTables.bootstrap.min.js"></script>
        <script src="https://cdn.datatables.net/responsive/2.1.1/js/dataTables.responsive.min.js"></script>
        <script src="https://cdn.datatables.net/responsive/2.1.1/js/responsive.bootstrap.min.js"></script>
        <script src="https://cdn.datatables.net/buttons/1.2.4/js/dataTables.buttons.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/2.5.0/jszip.min.js"></script>
        <script src="https://cdn.datatables.net/buttons/1.2.4/js/buttons.html5.min.js"></script>
        <script src="../adminlte2/plugins/select2/select2.full.min.js"></script>
        <script src="../adminlte2/plugins/datepicker/js/bootstrap-datepicker.min.js"></script>  
        <script src="../jquery-validation/js/jquery.validate.min.js"></script>
        <script src="../jquery-validation/js/additional-methods.min.js"></script>
        <jsp:include page="adminlte_js.jsp"/>
        <script type="text/javascript">
            var tableData;
            var tableEmail;
            var emailBody = '';
            var selectedData = 0;
            var thehtml = "";
            var tableData;
            var selectedCustomer = "";
            var selectCustomer = $("#selectCustomer");
            var inputFromDate = $("#inputFromDate");
            var inputToDate = $("#inputToDate");
            var inputNewsDate = $("#inputNewsDate");
            var sendEmail = $('form#sendEmail');
            var createData = $('form#createData');
            var editData = $('form#editData');
            var selectLanguage = $("#selectLanguage");
            var inputAddBtnLanguage = $("form#createData :input[id=inputLanguage]");
            var inputEditBtnLanguage = $("form#editData :input[id=inputLanguage]");
            var inputAddBtnCustomer = $("form#createData :input[id=inputCustomer]");
            var inputEditBtnCustomer = $("form#editData :input[id=inputCustomer]");
            var inputEmailBtnCustomer = $("form#sendEmail :input[id=inputCustomer]");
            var languages;
            var customers;
            
            loadTableData();
            
            $('[data-toggle="tooltip"]').tooltip();   
            
            $.ajax({
                type: "GET",
                url: '../dashboard?action=listCustomersForData',
                dataType: 'json'
            })
                    .done(function (data, textStatus, jqXHR) {
                        var response = data;
                        if (response.result === "error") {
                            ajaxHandleError(response);
                        } else {
                            customersData = data.customers;
                            loadCustomersFilter();
                            loadAddBtnCustomers();
                            loadEditBtnCustomers();
                            loadCustomersFilterEmail();
                        }
                    })
                    .fail(function (jqXHR, textStatus, errorThrown) {
                        var respJson = JSON.parse(jqXHR.responseText);
                        var response = jQuery.parseJSON(respJson);
                        $.alert(response.errorMsg, "Error !!");
                    });

            function loadAddBtnCustomers() {
                inputAddBtnCustomer.select2({
                    data: customersData,
                    placeholder: "Customer",
                    allowClear: true
                });
            }
            
            function loadEditBtnCustomers() {
                inputEditBtnCustomer.select2({
                    data: customersData,
                    placeholder: "Customer",
                    allowClear: true
                });
            }
            
            
            function loadCustomersFilterEmail() {
                inputEmailBtnCustomer.select2({
                    data: customersData,
                    placeholder: "Select Customer",
                    allowClear: true
                });
            }
            
            function loadCustomersFilter() {
                selectCustomer.select2({
                    data: customersData,
                    placeholder: "Select a Customer",
                    allowClear: true,
                }).on("change", function (e) {
                    if (typeof this.value !== "undefined"
                            && this.value !== '') {
                        selectedCustomer = $("#selectCustomer").val();
                        inputAddBtnCustomer.val(selectedCustomer).trigger("change");
                        inputEditBtnCustomer.val(selectedCustomer).trigger("change");    
                        inputEmailBtnCustomer.val(selectedCustomer).trigger("change");                        
                        tableData.ajax.reload(false);
                        enableButtons();
                    } else {
                        selectedCustomer = "";
                        disableButtons();
                    }
                    toggleDisableDataBtn();
                    toggleEditDataBtn();
                });
                selectCustomer.val("").trigger("change");
            }
            
            function disableButtons() {
                $('#btnCreateData').prop('disabled', true);
                $('#btnEditData').prop('disabled', true);
                $('#btnDisableData').prop('disabled', true);
                $('#btnSendEmail').prop('disabled', true);
            }
            
            function enableButtons() {
                $('#btnCreateData').prop('disabled', false);
                $('#btnEditData').prop('disabled', false);
                $('#btnDisableData').prop('disabled', false);
                $('#btnSendEmail').prop('disabled', false);
            }

            function loadTableData() {
                tableData = $('#list_data').DataTable({
                    "paging": true,
                    "responsive": true,
                    "processing": true,
                    "lengthChange": true,
                    "searching": true,
                    "ordering": true,
                    "info": true,
                    "autoWidth": true,
                    "dom": 'lBfrtip',
                    "stateSave": true,
                    "buttons": [
                        'excelHtml5'
                    ],
                    "ajax": {
                        "url": '../dashboard?action=listDataByContext',
                        "type": "POST",
                        "dataType": 'json',
                        "data": function (d) {
                            d.selectedCustomer = selectedCustomer;
                        }
                    },
                    "columns": [
                        {"data": "newsDate"},
                        {"data": "newsPaper"},
                        {"data": "language"},
                        {"data": "headline"},
                        {"data": "edition"},
                        {"data": "supplement"},
                        {"data": "source"},
                        {"data": "imageExists"},
                        {"data": "pageNo"},
                        {"data": "height"},
                        {"data": "width"},
                        {"data": "totalArticleSize"},
                        {"data": "circulationFigure"},
                        {"data": "customer"},
                        {"data": "journalistFactor"},
                        {"data": "lastUpdatedOn"},
                        {"data": ""}
                    ],
                    "columnDefs": [
                        {
                            type: "date-uk",
                            "render": function (data, type, columns) {
                                if (data === null || data === '' || typeof data === "undefined") {
                                    return '';
                                } else {
                                    return convertDate(data);

                                }
                            },
                            "targets": [0]
                        },
                        {
                            "render": function (data, type, columns) {
                                if (data === null || data === '' || typeof data === "undefined") {
                                    return '';
                                } else {
                                    return data;

                                }
                            },
                            "targets": [2]
                        },
                        {
                            "render": function (data, type, columns) {
                                if (data === null || data === '' || typeof data === "undefined") {
                                    return '';
                                } else {
                                    return data;

                                }
                            },
                            "targets": [7]
                        },
                        {
                            "render": function (data, type, columns) {
                                //                                console.log(columns["manager"]);
                                if (columns["fileSize"] === 0) {
                                    return '';
                                } else {
                                    return '<a href="image.jsp?imageId=' + columns["id"] + '" target="_blank" >img</a>';
                                }
                            }, "targets": [16], "visible": true
                        }

                    ]

                });
                
                $('#list_data tbody').on('click', 'tr', function () {
                    var data = tableData.row(this).data();
                    if ($(this).hasClass('active')) {
                        $(this).removeClass('active');
                    } else {
                        tableData.$('tr.active').removeClass('active');
                        $(this).addClass('active');
                        if (typeof data !== "undefined") {
                            selectedData = data["id"];
                        } else {
                            selectedData = 0;
                        }
                    }
                    $("form#disableData :input[id=inputId]").val(data["id"]);
                    toggleEditDataBtn();
                    toggleDisableDataBtn();
                });
                toggleEditDataBtn();
                toggleDisableDataBtn();

                $('#list_data tfoot th').each(function () {
                    var title = $('#list_data thead th').eq($(this).index()).text();
                    $(this).html('<input type="text" placeholder="Search ' + title + '" />');
                });
                
                tableData.columns().every(function () {
                    var that = this;

                    $('input', this.footer()).on('keyup change', function () {
                        if (that.search() !== this.value) {
                            that.search(this.value).draw();
                        }
                    });
                });
            }

            $("#resetCreateData").click(function () {
                $('form#createData .form-group').removeClass('has-error has-feedback has-success');
                newsDateAddObj.datepicker("update", "");
            });
            
            var newsDateAddObj = $("form#createData #newsDateDiv").datepicker({
                format: "dd-mm-yyyy",
                endDate: "0d",
                todayHighlight: true,
                autoclose: true
            });

            var newsDateEditObj = $("form#editData #newsDateDiv").datepicker({
                format: "dd-mm-yyyy",
                endDate: "0d",
                todayHighlight: true,
                autoclose: true
            });
            
            $("form#createData #todayBtn").click(function () {
                newsDateAddObj.datepicker("update", convertDate(null, true));
            });
//            
            $("form#createData #prevDayBtn").click(function () {
                if (newsDateAddObj.datepicker("getDate") !== "" &&
                        newsDateAddObj.datepicker("getDate") !== null) {
                    newsDateAddObj.datepicker("update", convertDate(new Date(newsDateAddObj.datepicker("getDate").getTime() - (60 * 60 * 24 * 1000))));
                } else {
                    newsDateAddObj.datepicker("update", convertDate(new Date(new Date().getTime() - (60 * 60 * 24 * 1000))));
                }
            });
            
            var _startDate = new Date(); //todays date
            var _endDate = new Date(_startDate.getTime() + (24 * 60 * 60 * 1000)); //plus 1 day
            inputFromDate.datepicker({
                format: 'dd-mm-yyyy',
                autoclose: true,
                //startDate: _startDate,
                todayHighlight: true
            }).on('changeDate', function (e) {
                _endDate = new Date(e.date.getTime() + (24 * 60 * 60 * 1000)); //get new end date
                inputToDate.datepicker('setStartDate', _endDate).focus(); //dynamically set new start date for #to
            });

            inputToDate.datepicker({
                format: 'dd-mm-yyyy',
                autoclose: true,
                startDate: _endDate,
                todayHighlight: false
            });

            function toggleEditDataBtn() {
                if (tableData.rows('.active').data().length === 1) {
                    $('#btnEditData').prop('disabled', false);
                } else {
                    $('#btnEditData').prop('disabled', true);
                }
            }

            function toggleDisableDataBtn() {
                if (tableData.rows('.active').data().length === 1) {
                    $('#btnDisableData').prop('disabled', false);
                } else {
                    $('#btnDisableData').prop('disabled', true);
                }
            }
            $('button#btnEditData').click(function () {
                $('form#editData .form-group').removeClass('has-error has-feedback has-success');
                var data = tableData.row('.active').data();
                // console.log(data["imageExists"]);
                $("form#editData :input[id=inputId]").val(data["id"]);
                newsDateEditObj.datepicker("update", convertDate(data["newsDate"]));
                $("form#editData :input[id=inputNewsPaper]").val(data["newsPaper"]);
                $("form#editData :input[id=inputLanguage]").val(data["language"]).trigger("change");
                $("form#editData :input[id=inputHeadline]").val(data["headline"]);
                $("form#editData :input[id=inputEdition]").val(data["edition"]);
                $("form#editData :input[id=inputSupplement]").val(data["supplement"]);
                $("form#editData :input[id=inputSource]").val(data["source"]);
                // $("form#editData :input[id=inputImageExists]").val(data["imageExists"]);
                $('form#editData :input:radio[name=inputImageExists]').filter('[value=' + data["imageExists"] + ']').prop('checked', true);
                $("form#editData :input[id=inputPageNo]").val(data["pageNo"]);
                $("form#editData :input[id=inputHeight]").val(data["height"]);
                $("form#editData :input[id=inputWidth]").val(data["width"]);
                $("form#editData :input[id=inputTotalArticleSize]").val(data["totalArticleSize"]);
                $("form#editData :input[id=inputCirculationFigure]").val(data["circulationFigure"]);
                $("form#editData :input[id=inputJournalistFactor]").val(data["journalistFactor"]);
                $("form#editData :input[id=inputCustomer]").val(data["customer"]);
                $("form#editData :input[id=inputImage]").val(data["image"]);
                $("form#editData :input[id=inputFileSize]").val(data["fileSize"]);
                $("form#editData :input[id=inputFileType]").val(data["fileType"]);
                //  console.log(data["language"]);
            });

            function submitCreateData() {
                $.ajax({
                    type: "POST",
                    url: "../dashboard?action=createAnalyticsData",
                    //            data: $('form#createUser').serialize(),
                    data: new FormData($('form#createData')[0]),
                    contentType: false,
                    processData: false,
                    dataType: 'json'
                })
                        .done(function (data, textStatus, jqXHR) {
                            var response = data;
                            if (response.result === "error") {
                                ajaxHandleError(response);
                            } else {
                                ajaxHandleSuccess(response);
                                $("#modalCreateData").modal('hide');
                                tableData.ajax.reload();
                                $('form#createData .form-group').removeClass('has-error has-feedback has-success');
                                createData.each(function(){
                                    this.reset();
                                });
                                newsDateAddObj.datepicker("update", "");
                            }
                        })
                        .fail(function (jqXHR, textStatus, errorThrown) {
                            var respJson = JSON.parse(jqXHR.responseText);
                            var response = jQuery.parseJSON(respJson);
                            $.alert(response.errorMsg, "Error !!");
                        });
            }

            function submitEditData() {
                $.ajax({
                    type: "POST",
                    url: "../dashboard?action=updateAnalyticsData",
                    data: new FormData($('form#editData')[0]),
                    contentType: false,
                    processData: false,
                    dataType: 'json'
                })
                        .done(function (data, textStatus, jqXHR) {
                            var response = data;
                            if (response.result === "error") {
                                ajaxHandleError(response);
                            } else {
                                ajaxHandleSuccess(response);
                                $("#modalEditData").modal('hide');
                                tableData.ajax.reload();
                                $('form#editData .form-group').removeClass('has-error has-feedback has-success');
                                editData.each(function(){
                                    this.reset();
                                });
                                newsDateEditObj.datepicker("update", "");
                            }
                        })
                        .fail(function (jqXHR, textStatus, errorThrown) {
                            var respJson = JSON.parse(jqXHR.responseText);
                            var response = jQuery.parseJSON(respJson);
                            $.alert(response.errorMsg, "Error !!");
                        });
            }

            $("#submitDisableData").on("click", function () {
                $.ajax({
                    type: "POST",
                    url: "../dashboard?action=disableData",
                    data: $("form#disableData").serialize(),
                    dataType: 'json'
                })
                        .done(function (data, textStatus, jqXHR) {
                            var response = data;
                            if (response.result === "error") {
                                ajaxHandleError(response);
                            } else {
                                ajaxHandleSuccess(response);
                                $("#modalDisableData").modal('hide');
                                tableData.ajax.reload();
                                sendEmail.each(function(){
                                    this.reset();
                                });
                            }
                        })
                        .fail(function (jqXHR, textStatus, errorThrown) {
                            var respJson = JSON.parse(jqXHR.responseText);
                            var response = jQuery.parseJSON(respJson);
                            $.alert(response.errorMsg, "Error !!");
                        });
            });

            function submitSendEmail() {
                $.ajax({
                    type: "POST",
                    url: "../dashboard?action=sendEmail",
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
                                tableData.ajax.reload();
                            }
                        })
                        .fail(function (jqXHR, textStatus, errorThrown) {
                            var respJson = JSON.parse(jqXHR.responseText);
                            var response = jQuery.parseJSON(respJson);
                            $.alert(response.errorMsg, "Error !!");
                        });
            }
            //  });

            var sendEmaill = sendEmail.validate({
                focusCleanup: true,
                rules: {
                    inputFromDate: {
                        required: true

                    },
                    inputToDate: {
                        required: true

                    },
                    inputEmailAddress: {
                        required: true
                    },
                    inputCustomer: {
                        required: true
                    }
                },
                messages: {
                    inputFromDate: {
                        equalTo: "Date is Mandatory"
                    },
                    inputToDate: {
                        equalTo: "Date is Mandatory"
                    },
                    inputEmailAddress: {
                        equalTo: "Email Address is Mandatory"
                    },
                    inputCustomer: {
                        equalTo: "Customer is Mandatory"
                    }
                },
                highlight: function (element) {
                    $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
                },
                success: function (element) {
                    $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
                },
                submitHandler: function (form) {
                    submitSendEmail();
                }
            });

            var createDataa = createData.validate({
                focusCleanup: true,
                rules: {
                    inputLanguage: {
                        required: true
                    },
                    inputCustomer: {
                        required: true
                    }
                },
                messages: {
                    inputLanguage: {
                        equalTo: "Language is Mandatory"
                    },
                    inputCustomer: {
                        equalTo: "Customer is Mandatory"
                    }
                },
                highlight: function (element) {
                    $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
                },
                success: function (element) {
                    $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
                },
                submitHandler: function (form) {
                    submitCreateData();
                }
            });
            
            var editDataa = editData.validate({
                focusCleanup: true,
                rules: {
                    inputLanguage: {
                        required: true
                    },
                    inputCustomer: {
                        required: true
                    }
                },
                messages: {
                    inputLanguage: {
                        equalTo: "Language is Mandatory"
                    },
                    inputCustomer: {
                        equalTo: "Customer is Mandatory"
                    }
                },
                highlight: function (element) {
                    $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
                },
                success: function (element) {
                    $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
                },
                submitHandler: function (form) {
                    submitEditData();
                }
            });

            $.ajax({
                type: "POST",
                url: "../dashboard?action=listDistinctLanguages",
                dataType: 'json'
            })
                    .done(function (data, textStatus, jqXHR) {
                        var response = data;
                        if (response.result === "error") {
                            ajaxHandleError(response);
                        } else {
                            languages = data.data;
                            loadAddBtnLanguage();
                            loadEditBtnLanguage();
                            loadSendEmailBtnLanguage();
                        }
                    })
                    .fail(function (jqXHR, textStatus, errorThrown) {
                        var respJson = JSON.parse(jqXHR.responseText);
                        var response = jQuery.parseJSON(respJson);
                        $.alert(response.errorMsg, "Error !!");
                    });

            function loadSendEmailBtnLanguage() {
                selectLanguage.select2({
                    data: languages,
                    placeholder: "Select Language",
                    allowClear: true
                });
                selectLanguage.val("").trigger("change");
            }

            function loadAddBtnLanguage() {
                inputAddBtnLanguage.select2({
                    tags: true,
                    data: languages,
                    placeholder: "Select Language",
                    allowClear: true
                });
                inputAddBtnLanguage.val("").trigger("change");
            }
            function loadEditBtnLanguage() {
                inputEditBtnLanguage.select2({
                    tags: true,
                    data: languages,
                    placeholder: "Select Language",
                    allowClear: true
                });
                inputEditBtnLanguage.val("").trigger("change");
            }
            // });
        </script> 

    </body>
</html>

