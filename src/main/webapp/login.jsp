<%-- 
    Document   : home
    Created on : Sep 29, 2015, 5:07:59 PM
    Author     : Rahul
--%>

<%@page import="com.revvster.playright.util.SystemConstants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

    <head>

        <title>Login</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <!-- Bootstrap 3.3.5 -->
        <link rel="stylesheet" href="adminlte2/bootstrap/css/bootstrap.min.css">
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
        <!-- Ionicons -->
        <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">        
        <!-- Admin LTE -->
        <link rel="stylesheet" href="adminlte2/dist/css/AdminLTE.min.css">
        <link rel="stylesheet" href="adminlte2/dist/css/skins/skin-red.min.css">

        <link rel="stylesheet" href="adminlte2/plugins/iCheck/square/red.css">
        <style>
            .title1 {
                font-family: "Lucida Sans Unicode", "Lucida Grande", sans-serif, monospace;
                font-weight: bold;
                letter-spacing: 2px;
                color: #c0392b;
                border-bottom: 3px solid #3E3E3E;
            }
            .title2 {
                font-family: "Lucida Sans Unicode", "Lucida Grande", sans-serif, monospace;
                font-weight: bold;
                letter-spacing: 2px;
                color: #3E3E3E;
                border-bottom: 3px solid #c0392b;
            }
        </style>

    </head>

    <body class="hold-transition login-page">
        <div class="modal fade bs-example-modal-sm" id="modalForgotPwd" role="dialog" aria-labelledby="forgotPwd"> 
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">Forgot Password</h4>
                    </div>
                    <div class="modal-body">                    
                        <form role="form" id="forgotPwd">
                            <input type="hidden" id="inputResetPwd" name="inputResetPwd" value="1" class="form-control">
                            <div class="form-group">
                                <label for="inputUserName">UserName</label>
                                <input type="text" class="form-control" id="inputUserName" name="inputUserName" placeholder="Enter your UserName">
                            </div>
                            <div>
                                <button type="button" class="btn btn-primary" id="btnsubmitForgotPwd">Submit</button>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                    </div>
                </div>
            </div>
        </div>       

        <div class="login-box">
            <div class="login-logo">
                <!--<span class="title1">PLAY</span><span class="title2">RIGHT</span>-->
                <!--<a href="#"><b>Sales</b>Buddy</a>-->
                <img src="images/logo.png" alt="PlayRight Analytics" title="PlayRight Analytics" border="0" height="74" width="165"/>
            </div><!-- /.login-logo -->
            <div class="login-box-body">
                <p class="login-box-msg">Sign in to start your session</p>
                <form action="login" method="post">
                    <div class="form-group has-feedback">
                        <input type="email" class="form-control" placeholder="Email" name="loginEmail" autofocus>
                        <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
                    </div>
                    <div class="form-group has-feedback">
                        <input type="password" class="form-control" placeholder="Password" name="loginPassword">
                        <span class="glyphicon glyphicon-lock form-control-feedback"></span>
                    </div>
                    <% if (null != request.getAttribute("errorMsg")) {%>
                    <div>
                        <div>
                            <h5 class="text-red"><%=request.getAttribute("errorMsg")%></h5>
                        </div><!-- /.col -->
                    </div>
                    <%}%>
                    <div id="resetPwdMsg" class="hidden">
                        <div>
                            <h5 class="text-red">Check your email and login with temporary password.</h5>
                        </div><!-- /.col -->
                    </div>
                    <div class="row">
                        <div class="col-xs-8">
                            <!--                            <div class="checkbox icheck">
                                                            <label>
                                                                <input type="checkbox"> Remember Me
                                                            </label>
                                                        </div>-->
                        </div><!-- /.col -->
                        <div class="col-xs-4">
                            <button type="submit" class="btn btn-primary btn-block btn-flat">Sign In</button>
                        </div><!-- /.col -->
                    </div>
                </form>

                <a data-toggle="modal" href="#modalForgotPwd" >I forgot my password</a><br>

            </div><!-- /.login-box-body -->
        </div><!-- /.login-box -->

        <!-- jQuery 2.1.4 -->
        <script src="adminlte2/plugins/jQuery/jquery-2.2.3.min.js"></script>
        <!-- Bootstrap 3.3.5 -->
        <script src="adminlte2/bootstrap/js/bootstrap.min.js"></script>
        <script src="jquery-alert/js/jquery-alert.js"></script>
        <!-- AdminLTE App -->
        <script src="adminlte2/dist/js/app.min.js"></script>       

        <script src="adminlte2/plugins/iCheck/icheck.min.js"></script>
        <script>
            $(function () {
                $('input').iCheck({
                    checkboxClass: 'icheckbox_square-blue',
                    radioClass: 'iradio_square-blue',
                    increaseArea: '20%' // optional
                });
                $("#btnsubmitForgotPwd").on("click", function () {
                    $.ajax({
                        type: "POST",
                        url: "user?action=forgotPwd",
                        data: $("form#forgotPwd").serialize(),
                        dataType: 'json'
                    })
                            .done(function (data, textStatus, jqXHR) {
                                var response = JSON.parse(data);
                                if (response.result === "error") {
                                    ajaxHandleError(response);
                                } else {
                                    ajaxHandleSuccess(response);
                                    $("#modalForgotPwd").modal('hide');
                                    $("#resetPwdMsg").removeClass("hidden")
                                }
                            })
                            .fail(function (jqXHR, textStatus, errorThrown) {
                                var respJson = JSON.parse(jqXHR.responseText);
                                var response = jQuery.parseJSON(respJson);
                                $.alert(response.errorMsg, "Error !!");
                            });
                });
                function ajaxHandleError(response) {
                    $.alert(response.errorMsg, {
                        title: 'Error !!',
                        closeTime: 2000,
                        autoClose: 'checked',
                        position: ['bottom-right'],
                        type: 'danger',
                        isOnly: 'true'
                    });
                }
                ;
                function ajaxHandleSuccess(response) {
                    $.alert('Action Completed', {
                        title: 'Success !!',
                        closeTime: 2000,
                        autoClose: 'checked',
                        position: ['bottom-right'],
                        type: 'success',
                        isOnly: 'true'
                    });
                }
                ;
            });
//            document.getElementById("footer").style.visibility = 'hidden';

        </script>
    </body>
</html>
