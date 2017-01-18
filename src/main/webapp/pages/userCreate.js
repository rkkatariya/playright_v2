/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var cInputRole = $("#createUser :input[id=inputRole]");
var cInputCompany = $("#createUser :input[id=inputCompany]");
var cInputManager = $("#createUser :input[id=inputManager]");

$(function () {
    cInputRole.change(function () {
        var selectedRole = $(this).find('option:selected').attr('name');
        if (selectedRole === 'SysAdmin') {
            cInputCompany.select2("val", "");
            cInputCompany.empty();
            cInputCompany.prop("disabled", true);
            cInputManager.select2("val", "");
            cInputManager.empty();
            cInputManager.prop("disabled", true);
        } else if (selectedRole === 'PlayRightUser') {
            cInputCompany.prop("disabled", false);
            cInputManager.select2("val", "");
            cInputManager.empty();
            cInputManager.prop("disabled", true);
        } 
    });    

    cInputCompany.on("change", function () {
        $(this).valid(); //jquery validation script validate on change;
        cInputManager.select2("val", "");
        var selectedRole = $("#createUser :input[id=inputRole]").find('option:selected').attr('name');
        if ($(this).find('option:selected').val() === '') {
            cInputManager.prop("disabled", true);
        } else {
            if (selectedRole === 'SysAdmin' 
                || selectedRole === 'PlayRightUser') {
                cInputManager.prop("disabled", true);
            } else {
                cInputManager.prop("disabled", false);
            }
        }
    });    
    
    
    cInputManager.select2({
        placeholder: "Select a manager",
//        theme: "bootstrap",
        disabled: true,
        allowClear: true,
//        initSelection: function(element, callback) {                   
//        },
        ajax: {
            url: "../user",
            dataType: 'json',
            delay: 250,
            data: function (params) {
                return {
                    q: params.term,
                    action: 'getManagersStartingWith',
                    inputCompany: cInputCompany.val()
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
        minimumInputLength: 2,
        templateResult: function (repo) {
            if (repo.loading)
                return repo.text;
            return repo.managerName;
        },
        templateSelection: function (repo) {
            return repo.managerName || repo.text;
        }
    }).on("change", function (e) {
        $(this).valid(); //jquery validation script validate on change;
    });;    

    cInputCompany.select2({
        placeholder: "Select a company",
        disabled: true,
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
});

$(function () {
    var createUserForm = $('form#createUser');
    function submitCreateUser() {
        $.ajax({
            type: "POST",
            url: "../user?action=createUser",
            data: $('form#createUser').serialize(),
            dataType: 'json'
        })
        .done(function (data, textStatus, jqXHR) {
            var response = jQuery.parseJSON(data);
            if (response.result === "error") {
                ajaxHandleError(response);
            } else {
                ajaxHandleSuccess(response);
                $("#modalCreateUser").modal('hide');
                //This function should be implemented in the parent JSP
                reloadPage();
            }
        })
        .fail(function (jqXHR, textStatus, errorThrown) {
            var respJson = JSON.parse( jqXHR.responseText );
            var response = jQuery.parseJSON(respJson);
            $.alert(response.errorMsg, "Error !!");
        });
    }
    
    var createUserValidator = createUserForm.validate({
//        debug: true,
        focusCleanup: true,
        rules: {
            inputUserName: {
                required: true,
                email: true,
                remote: {
                      url: '../user?action=validateUserName',
                      type: "post",
                      data:
                      {
                          inputUserName: function()
                          {
                              return $('#createUser :input[id=inputUserName]').val();
                          }
                      }
                    }
            },
            inputFirstName: {
                required: true
            },
            inputLastName: {
                required: true
            },
            inputJobTitle: {
                required: true
            },
            inputDepartment: {
                required: true
            },
            inputMobileNo: {
                required: true,
                minlength: 10,
                maxlength: 10,
                remote: {
                      url: '../user?action=validateMobileNo',
                      type: "post",
                      data:
                      {
                          inputMobileNo: function()
                          {
                              return $('#createUser :input[id=inputMobileNo]').val();
                          }
                      }
                    }
            },
//            inputPassword: {
//                required: true,
//                maxlength: 20,
//(/^
//(?=.*\d)                //should contain at least one digit
//(?=.*[a-z])             //should contain at least one lower case
//(?=.*[A-Z])             //should contain at least one upper case
//[a-zA-Z0-9]{6,}         //should contain at least 6 from the mentioned characters
//$/)                
//                pattern: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{6,}$/
//            },
//            inputConfirmPassword: {
//                required: true,
//                equalTo: "#inputPassword"
//            },
            inputGender: {
                required: true
            },
            inputRole: {
                required: true
            },
            inputCompany: {
                required: true
            },
            inputManager: {
                required: true
            }
        },
        messages: {
            inputUserName: {
                remote: "UserName already used"
            },
            inputMobileNo: {
                minlength: "Enter a valid 10 digit Mobile No.",
                maxlength: "Enter a valid 10 digit Mobile No.",
                remote: "Mobile No. already used"
            }
//            inputPassword: {
//                maxlength: "Cannot be more than 20 characters",
//                pattern: "Must contain at least 6 characters, including 1 UPPERCASE, 1 lowercase and 1 number"
//            },
//            inputConfirmPassword: {
//                equalTo: "Password does not match"
//            }
        },
        highlight: function (element) {
            $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
        },
        success: function (element) {
            $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
        },
        errorPlacement: function (error, element) {
            if (element.parent().next("div").length === 1) {
                error.appendTo(element.parent().next("div"));
            } else if (element.parent().parent().next("div").length === 1) {
                error.appendTo(element.parent().parent().next("div"));
            } else {
                error.appendTo(element.parent().parent().parent().next("div"));
            }
        },
        submitHandler: function (form) {
            submitCreateUser();
        }
    });
    
    $("#resetCreateUser").click(function () {
        createUserForm.validate().resetForm();
        $('form#createUser .form-group').removeClass('has-error has-feedback has-success');
        cInputManager.select2("val", "");
        cInputManager.empty();
        cInputCompany.select2("val", "");
        cInputCompany.empty();
        cInputManager.prop("disabled", true);
        cInputCompany.prop("disabled", true);
    });
});



