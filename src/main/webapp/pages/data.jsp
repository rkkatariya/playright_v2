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

//    RoleEntContext addCtx = null;
//    RoleEntContext editCtx = null;
//    RoleEntContext deleteCtx = null;
//
//    if (user.getUserEntitlements() != null) {
//        List<UserEntitlement> ues = user.getUserEntitlements();
//        addCtx = AuthorizationManager.getRoleEntContext(ues,
//                new UserEntitlement(Resource.user, Action.add));
//
//        editCtx = AuthorizationManager.getRoleEntContext(ues,
//                new UserEntitlement(Resource.user, Action.update));
//        deleteCtx = AuthorizationManager.getRoleEntContext(ues,
//                new UserEntitlement(Resource.user, Action.delete));
//    }
%>
<html lang="en">
    <head>     
        <title>Data</title>

        <jsp:include page="header.jsp"/>
        <jsp:include page="datatables_css.jsp"/>

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
                                        <label for="inputNewsDate">News Date</label>
                                        <div class="input-group date">
                                            <input type="text" class="form-control" id="inputNewsDate" name="inputNewsDate" data-provide="datepicker">
                                            <div class="input-group-addon">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="inputNewsPaper">News Paper</label>
                                        <input type="text" class="form-control" id="inputNewsPaper" name="inputNewsPaper" placeholder="News Paper">
                                    </div>
                                    <div class="form-group">
                                        <label for="inputLanguage">Language</label>
                                        <select id="inputLanguage" name="inputLanguage" class="form-control" style="width: 100%;" >
                                            <option value="English">English</option>
                                            <option value="Kannada">Kannada</option>
                                            <option value="Telugu">Telugu</option>
                                            <option value="Urdu">Urdu</option>
                                            <option value="Malayalam">Malayalam</option>
                                            <option value="Marathi">Marathi</option>
                                            <option value="Hindi">Hindi</option>
                                            <option value="Tamil">Tamil</option>
                                            <option value="Bengali">Bengali</option>
                                            <option value="Others">Others</option>
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

                                    <div class="form-group">
                                        <label for="inputPageNo">Page No</label>
                                        <input type="number" class="form-control" id="inputPageNo" name="inputPageNo" placeholder="Page No">
                                    </div>
                                </div>
                                <div class="col-sm-2">                                    
                                </div>                                                                
                                <div class="col-sm-5">
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
                                        <label for="inputQuantitativeAVE">Quantitative AVE</label>
                                        <input type="number" class="form-control" id="inputQuantitativeAVE" name="inputQuantitativeAVE" placeholder="Quantitative AVE">
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
                                        <!--<p class="help-block"></p>-->
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-3">                            
                                </div>
                                <div class="col-sm-3"> 
                                    <button type="button" class="btn btn-primary pull-right" id="submitCreateData">Submit</button>
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
                                        <label for="inputNewsDate">News Date</label>
                                        <div class="input-group date">
                                            <input type="text" class="form-control" id="inputNewsDate" name="inputNewsDate" data-provide="datepicker">
                                            <div class="input-group-addon">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="inputNewsPaper">News Paper</label>
                                        <input type="text" class="form-control" id="inputNewsPaper" name="inputNewsPaper" placeholder="News Paper">
                                    </div>
                                    <div class="form-group">
                                        <label for="inputLanguage">Language</label>
                                        <select id="inputLanguage" name="inputLanguage" class="form-control" style="width: 100%;" >
                                            <option value="English">English</option>
                                            <option value="kannada">Kannada</option>
                                            <option value="Telugu">Telugu</option>
                                            <option value="urdu">Urdu</option>
                                            <option value="malayalam">Malayalam</option>
                                            <option value="marathi">Marathi</option>
                                            <option value="Hindi">Hindi</option>
                                            <option value="Tamil">Tamil</option>
                                            <option value="bengali">Bengali</option>
                                            <option value="others">Others</option>
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

                                    <div class="form-group">
                                        <label for="inputPageNo">Page No</label>
                                        <input type="text" class="form-control" id="inputPageNo" name="inputPageNo" placeholder="Page No">
                                    </div>
                                </div>
                                <div class="col-sm-2">                                    
                                </div>                                                                
                                <div class="col-sm-5">

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
                                        <label for="inputQuantitativeAVE">Quantitative AVE</label>
                                        <input type="text" class="form-control" id="inputQuantitativeAVE" name="inputQuantitativeAVE" placeholder="Quantitative AVE">
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
                                        <!--<p class="help-block"></p>-->
                                    </div>
                                </div>

                            </div>

                            <div class="row">
                                <div class="col-sm-3">                            
                                </div>
                                <div class="col-sm-3"> 
                                    <button type="button" class="btn btn-primary pull-right" id="submitEditData">Submit</button>
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
                            <!--                            <input type="hidden" id="inputId" name="inputId"  class="form-control">
                                                        <input type="hidden" id="inputActive" name="inputActive"  class="form-control">                             -->
                        </form>                       
                    </div>
                    <div class="modal-footer">                        
                        <button type="button" class="btn btn-primary" id="submitDisableData">Yes</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                    </div>
                </div>
            </div>            
        </div>
        <div class="modal fade bs-example-modal-sm" id="modalSendEmail" role="dialog" aria-labelledby="sendEmail">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="titleUser">Send Email To</h4>
                    </div>
                    <div class="modal-body">
                        <form role="form" id="sendEmail">    
                            <input type="hidden" id="emailBody" name="emailBody"  class="form-control">
                            <div class="form-group">
                                <label>Select dates</label>
                                <div class="input-group input-daterange">                                    
                                    <input type="text" class="form-control" id="inputFromDate" name="inputFromDate" data-provide="datepicker">
                                    <span class="input-group-addon">to</span>
                                    <input type="text" class="form-control" id="inputToDate" name="inputToDate" data-provide="datepicker">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="inputLanguage">Language</label>
                                <select id="inputLanguage" name="inputLanguage" class="form-control" style="width: 100%;" >
                                    <option value="">Select Language</option>
                                    <option value="English">English</option>
                                    <option value="Kannada">Kannada</option>
                                    <option value="Telugu">Telugu</option>
                                    <option value="Urdu">Urdu</option>
                                    <option value="Malayalam">Malayalam</option>
                                    <option value="Marathi">Marathi</option>
                                    <option value="Hindi">Hindi</option>
                                    <option value="Tamil">Tamil</option>
                                    <option value="Bengali">Bengali</option>                                           
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="inputEdition">Edition</label>
                                <select id="inputEdition" name="inputEdition" class="form-control" style="width: 100%;" >
                                    <option value="">Select Edition</option>
                                    <option value="Mumbai">Mumbai</option>
                                    <option value="Bangalore">Bangalore</option>
                                    <option value="New Delh">New Delhi</option>
                                    <!--                                            <option value="urdu">Urdu</option>
                                                                                <option value="malayalam">Malayalam</option>
                                                                                <option value="marathi">Marathi</option>
                                                                                <option value="hindi">Hindi</option>
                                                                                <option value="tamil">Tamil</option>
                                                                                <option value="bengali">Bengali</option>
                                                                                <option value="others">Others</option>-->
                                </select>
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
                                <label for="inputEmailAddress">Recipient Email Address</label>
                                <input type="text" class="form-control" id="inputEmailAddress" name="inputEmailAddress" placeholder="Email">
                            </div>
                            <div class="form-group">
                                <label for="inputSubject">Subject</label>
                                <input type="text" class="form-control" id="inputSubject" name="inputSubject" placeholder="Subject">
                            </div>                                                    

                            <div>
                                <button type="submit" class="btn btn-primary" id="btnSubmitSendEmail">Submit</button>
                            </div>
                        </form>
                    </div>


                    <div class="modal-footer">                   

                        <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
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
                        Data
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
                                </div>
                                <div class="box-body table-responsive">
                                    <table id="list_data" class="table table-bordered table-striped">
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
                                                <th>Quantitative AVE</th>
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
                                                <th>Quantitative AVE</th>
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
        <jsp:include page="datatables_js.jsp"/>
        <script src="../adminlte2/plugins/select2/select2.full.min.js"></script>
        <script src="../adminlte2/plugins/datepicker/js/bootstrap-datepicker.min.js"></script>  
        <script src="../adminlte2/plugins/datatables/extensions/jszip/jszip.min.js"></script> 
        <script src="../DataTables/Buttons-1.1.0/js/buttons.html5.min.js"></script>
        <script src="../DataTables/Buttons-1.1.0/js/buttons.print.min.js"></script>
        <script src="../DataTables/Buttons-1.1.0/js/buttons.colVis.min.js"></script>
        <script src="../adminlte2/plugins/datepicker/js/bootstrap-datepicker.min.js"></script>
        <script src="../jquery-validation/js/jquery.validate.min.js"></script>
        <script src="../jquery-validation/js/additional-methods.min.js"></script>
        <jsp:include page="adminlte_js.jsp"/>
        <script type="text/javascript">
            var table;
            var tableEmail;
            var emailBody = '';
            var selectedData = 0;
            var thehtml = "";
            var tableData;
            var inputFromDate = $("#inputFromDate");
            var inputToDate = $("#inputToDate");
            var inputNewsDate = $("#inputNewsDate");
            var sendEmail = $('form#sendEmail');
            $(function () {
                table = $('#list_data').DataTable({
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
                        "url": "../dashboard?action=listDataByContext",
                        "type": "POST",
                        "dataType": 'json',
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
                        {"data": "quantitativeAVE"},
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
                                //                                console.log(columns["manager"]);
                                if (columns["imageFileName"] === 0) {
                                    return '';
                                } else {
                                    return '<a href="image.jsp?imageId=' + columns["id"] + '" target="_blank" >img</a>';
                                }
                            }, "targets": [16], "visible": true
                        }

                    ]

                });

                $('#list_data tbody').on('click', 'tr', function () {
                    var data = table.row(this).data();
                    if ($(this).hasClass('active')) {
                        $(this).removeClass('active');
                    } else {
                        table.$('tr.active').removeClass('active');
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
                table.columns().every(function () {
                    var that = this;

                    $('input', this.footer()).on('keyup change', function () {
                        if (that.search() !== this.value) {
                            that.search(this.value).draw();
                        }
                    });
                });

                $("#resetCreateData").click(function () {
                    // createUserForm.validate().resetForm();
                    $('form#createData .form-group').removeClass('has-error has-feedback has-success');
                    //                cInputManager.select2("val", "");
                    //                cInputManager.empty();
                    //                cInputCompany.select2("val", "");
                    //                cInputCompany.empty();
                    //                cInputManager.prop("disabled", true);
                    //                cInputCompany.prop("disabled", true);
                });

                $("form#createData #inputNewsDate").datepicker({
                    format: "dd-mm-yyyy",
                    startDate: "-6m",
                    endDate: "0d",
                    todayHighlight: true,
                    autoclose: true
                });

                $("form#editData #inputNewsDate").datepicker({
                    format: "dd-mm-yyyy",
                    startDate: "-6m",
                    endDate: "0d",
                    todayHighlight: true,
                    autoclose: true
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

                function toggleEditDataBtn() {
                    if (table.rows('.active').data().length === 1) {
                        $('#btnEditData').prop('disabled', false);
                    } else {
                        $('#btnEditData').prop('disabled', true);
                    }
                }

                function toggleDisableDataBtn() {
                    if (table.rows('.active').data().length === 1) {
                        $('#btnDisableData').prop('disabled', false);
                    } else {
                        $('#btnDisableData').prop('disabled', true);
                    }
                }
                $('button#btnEditData').click(function () {
                    var data = table.row('.active').data();
                    // console.log(data["imageExists"]);
                    $("form#editData :input[id=inputId]").val(data["id"]);
                    $("form#editData :input[id=inputNewsDate]").val(convertDate(data["newsDate"]));
                    $("form#editData :input[id=inputNewsPaper]").val(data["newsPaper"]);
                    $("form#editData :input[id=inputLanguage]").val(data["language"]);
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
                    $("form#editData :input[id=inputQuantitativeAVE]").val(data["quantitativeAVE"]);
                    $("form#editData :input[id=inputImage]").val("");
                    $("form#editData :input[id=inputFileSize]").val(data["fileSize"]);
                    $("form#editData :input[id=inputFileType]").val(data["fileType"]);
                    //console.log(data["binaryData"]);
                });

                $("#submitCreateData").on("click", function () {
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
                                    table.ajax.reload();
                                }
                            })
                            .fail(function (jqXHR, textStatus, errorThrown) {
                                var respJson = JSON.parse(jqXHR.responseText);
                                var response = jQuery.parseJSON(respJson);
                                $.alert(response.errorMsg, "Error !!");
                            });
                });

                $("#submitEditData").on("click", function () {
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
                                    table.ajax.reload();
                                }
                            })
                            .fail(function (jqXHR, textStatus, errorThrown) {
                                var respJson = JSON.parse(jqXHR.responseText);
                                var response = jQuery.parseJSON(respJson);
                                $.alert(response.errorMsg, "Error !!");
                            });
                });

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
                                    table.ajax.reload();
                                }
                            })
                            .fail(function (jqXHR, textStatus, errorThrown) {
                                var respJson = JSON.parse(jqXHR.responseText);
                                var response = jQuery.parseJSON(respJson);
                                $.alert(response.errorMsg, "Error !!");
                            });
                });

                //     $('button#btnSendEmail').click(function () {
                //         sendBody();
                //     });

                //  $("#btnSubmitSendEmail").on("click", function () {
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
                                    table.ajax.reload();
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
                    //        debug: true,
                    focusCleanup: true,
                    rules: {
                        inputFromDate: {
                            required: true

                        },
                        inputToDate: {
                            required: true

                        }

                    },
                    messages: {
                        inputFromDate: {
                            equalTo: "Date is Mandatory"
                        },
                        inputToDate: {
                            equalTo: "Date is Mandatory"
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




            });
        </script> 

    </body>
</html>

