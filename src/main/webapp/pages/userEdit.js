/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    var eInputManager = $("#editUser :input[id=inputManager]");
    var eInputCompany = $("#editUser :input[id=inputCompany]");

    eInputManager.select2({
        ajax: {
            url: "../user",
            dataType: 'json',
            delay: 250,
            data: function (params) {
                return {
                    q: params.term,
                    action: 'getManagersStartingWith',
                    inputCompany: eInputCompany.val()
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
    });    

//    eInputCompany.select2({
////        placeholder: "Select a company",
//        disabled: true,
//        allowClear: true,
//        ajax: {
//            url: "../project",
//            dataType: 'json',
//            delay: 250,
//            data: function (params) {
//                return {
//                    q: params.term,
//                    action: 'getCompaniesStartingWith'
//                };
//            },
//            processResults: function (data, params) {
//                return {
//                    results: data.items
//                };
//            },
//            cache: true
//        },
//        escapeMarkup: function (markup) {
//            return markup; // let our custom formatter work
//        },
//        minimumInputLength: 1,
//        templateResult: function (repo) {
//            if (repo.loading)
//                return repo.text;
//            return repo.name;
//        },
//        templateSelection: function (repo) {
//            return repo.name || repo.text;
//        }
//    });
});

$(function () {
    var editUserForm = $('form#editUser');  
    var editUserValidator = editUserForm.validate({
//        debug: true,
        rules: {
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
                              return $('#editUser :input[id=inputMobileNo]').val();
                          },
                          inputId: function()
                          {
                              return $('#editUser :input[id=inputId]').val();
                          }
                          
                      }
                    }
            },
            inputGender: {
                required: true
            },
            inputManager: {
                required: true
            }
        },
        messages: {
            inputMobileNo: {
                minlength: "Enter a valid 10 digit Mobile No.",
                maxlength: "Enter a valid 10 digit Mobile No.",
                remote: "Mobile No. already used"
            }
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
            submitEditUser();
        }
    });
    function submitEditUser() {
        //This is added because select no longer supports readonly
        //It is a workaround to submit disabled attribute
        var selectInputManager = $('#editUser :input[id=inputManager]');
        if (selectInputManager.is(':disabled')) {
            $('<input>').attr({
                type: 'hidden',
                id: 'inputManager',
                name: 'inputManager',
                value: selectInputManager.find(":selected").val()
            }).appendTo(editUserForm);
        }
        
        $.ajax({
            type: "POST",
            url: "../user?action=updateUser",
            data: $('form#editUser').serialize(),
            dataType: "json"
        })
        .done(function (data, textStatus, jqXHR) {
            var response = jQuery.parseJSON(data);
            if (response.result === "error") {
                ajaxHandleError(response);
            } else {
                ajaxHandleSuccess(response);
                $("#modalEditUser").modal('hide');
                //This function should be implemented in the parent JSP
                reloadPage();
            }

        })
        .fail(function (jqXHR, textStatus, errorThrown) {
            var respJson = JSON.parse( jqXHR.responseText );
            var response = jQuery.parseJSON(respJson);
            $.alert(response.errorMsg, "Error !!");
        });
//                    .always(function(jqXHROrData, textStatus, jqXHROrErrorThrown)     { alert("complete"); })
//                    ;
    }
    
});

