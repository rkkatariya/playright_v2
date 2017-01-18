<%-- 
    Document   : resetPassword
    Created on : Jul 13, 2016, 5:04:47 PM
    Author     : REVV-03
--%>

<%@page import="com.revvster.playright.util.SystemConstants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

    <head>

        <title>Reset Password</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <!-- Bootstrap 3.3.5 -->
        <link rel="stylesheet" href="../adminlte2/bootstrap/css/bootstrap.min.css">
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
        <!-- Ionicons -->
        <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">        
        <!-- Admin LTE -->
        <link rel="stylesheet" href="../adminlte2/dist/css/AdminLTE.min.css">
        <link rel="stylesheet" href="../adminlte2/dist/css/skins/skin-blue.min.css">

        <link rel="stylesheet" href="../adminlte2/plugins/iCheck/square/blue.css">
        <style>
            .title1 {
                font-family: 'Lucida Sans Typewriter', 'Lucida Typewriter', monospace;
                /*font-weight: bold;*/
                letter-spacing: 2px;
                color: #339ADD;
                border-bottom: 3px solid #3E3E3E;
            }
            .title2 {
                font-family: 'Lucida Sans Typewriter', 'Lucida Typewriter', monospace;
                /*font-weight: bold;*/
                letter-spacing: 2px;
                color: #3E3E3E;
                border-bottom: 3px solid #339ADD;
            }
        </style>

    </head>

    <body class="hold-transition login-page">            

        <div class="login-box">
            <div class="login-logo">
                <!--<span class="title1">PLAY</span><span class="title2">RIGHT</span>-->
                <!--<a href="#"><b>Sales</b>Buddy</a>-->
                <img src="../images/logo.png" alt="PlayRight Analytics" title="PlayRight Analytics" border="0" height="74" width="165"/>
            </div>
            <div class="login-box-body">
                <p class="login-box-msg">Create new password to sign in</p>
                <form role="form" id="resetPassword"> 
                    <div class="form-group">
                        <label for="inputNewPassword">New Password</label>
                        <input type="password" class="form-control" id="inputNewPassword" name="inputNewPassword" placeholder="New Password">
                    </div>
                    <!--                    <div class="form-group has-feedback">
                                            <input type="password" class="form-control" id="inputNewPassword" placeholder="NewPassword" name="NewPassword">
                                            <span class="glyphicon glyphicon-lock form-control-feedback"></span>
                                        </div>    -->

                    <div class="form-group">
                        <label for="inputConfirmPassword">Confirm Password</label>
                        <input type="password" class="form-control" id="inputConfirmPassword" name="inputConfirmPassword" placeholder="Confirm Password">
                    </div>
                    <div class="row"> 
                        <div class="col-xs-6">                            
                        </div><!-- /.col -->
                        <div class="col-xs-6">
                            <!--<button type="button" id="submitChangePassword" class="btn btn-primary btn-block btn-flat" onclick="Redirect();">Change Password</button>-->

                            <button type="submit" class="btn btn-primary" id="submitChangePassword" >Change Password</button>

                        </div><!-- /.col -->
                    </div>

                </form>               

            </div><!-- /.login-box-body -->
        </div><!-- /.login-box -->

        <!-- jQuery 2.1.4 -->
        <script src="../adminlte2/plugins/jQuery/jquery-2.2.3.min.js"></script>
        <!-- Bootstrap 3.3.5 -->
        <script src="../adminlte2/bootstrap/js/bootstrap.min.js"></script>

        <script src="../jquery-validation/js/jquery.validate.min.js"></script>

        <script src="../jquery-validation/js/additional-methods.min.js"></script>

        <script src="../jquery-alert/js/jquery-alert.js"></script>

        <!-- AdminLTE App -->
        <script src="../adminlte2/dist/js/app.min.js"></script>       

        <script src="../adminlte2/plugins/iCheck/icheck.min.js"></script>
        <script>
            $(function () {
                $('input').iCheck({
                    checkboxClass: 'icheckbox_square-blue',
                    radioClass: 'iradio_square-blue',
                    increaseArea: '20%' // optional
                });
                var resetPwd = $('form#resetPassword');
                function submitChangePassword() {
                    $.ajax({
                        type: "POST",
                        url: "../user?action=changePwd",
                        data: resetPwd.serialize(),
                        dataType: 'json'
                    })
                            .done(function (data, textStatus, jqXHR) {
                                var response = data;
                                if (response.result === "error") {
                                    ajaxHandleError(response);
                                } else {
                                    ajaxHandleSuccess(response);
                                    window.location = "../login.jsp";
                                }
                            })
                            .fail(function (jqXHR, textStatus, errorThrown) {
                                var respJson = JSON.parse(jqXHR.responseText);
                                var response = jQuery.parseJSON(respJson);
                                $.alert(response.errorMsg, "Error !!");
                            });

                }
                var resetPasswod = resetPwd.validate({
                    //        debug: true,
                    focusCleanup: true,
                    rules: {
                        inputNewPassword: {
                            required: true,
                            maxlength: 20,
                            //(/^
                            //(?=.*\d)                //should contain at least one digit
                            //(?=.*[a-z])             //should contain at least one lower case
                            //(?=.*[A-Z])             //should contain at least one upper case
                            //[a-zA-Z0-9]{6,}         //should contain at least 6 from the mentioned characters
                            //$/)                
                            pattern: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{6,}$/
                        },
                        inputConfirmPassword: {
                            required: true,
                            equalTo: $("form#resetPassword :input[id=inputNewPassword]")
                        }

                    },
                    messages: {
                        inputNewPassword: {
                            maxlength: "Cannot be more than 20 characters",
                            pattern: "Must contain at least 6 characters, including 1 UPPERCASE, 1 lowercase and 1 number"
                        },
                        inputConfirmPassword: {
                            equalTo: "Password does not match"
                        }
                    },
                    highlight: function (element) {
                        $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
                    },
                    success: function (element) {
                        $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
                    },
                    submitHandler: function (form) {
                        submitChangePassword();
                    }
                });

                function ajaxHandleError(response) {
                    $.alert('There was an error.', {
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
