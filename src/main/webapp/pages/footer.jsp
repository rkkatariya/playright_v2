<%-- 
    Document   : footer
    Created on : Oct 1, 2015, 11:26:49 AM
    Author     : Rahul
--%>

<!-- Main Footer -->
<footer class="main-footer" id="footer">
    <!-- To the right -->
    <div class="pull-right hidden-xs">
<!--        PlayRight-->
    </div>
    <!-- Default to the left -->
    <strong>Copyright &copy; 2017 <a href="http://revvster.in/">Revvster Technologies India Pvt Ltd</a>.</strong> All rights reserved.
</footer>

<!-- REQUIRED JS SCRIPTS -->

<!-- jQuery 2.2.3 -->
<script src="../adminlte2/plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- Bootstrap 3.3.5 -->
<script src="../adminlte2/bootstrap/js/bootstrap.min.js"></script>
<script src="../jquery-alert/js/jquery-alert.js"></script>
<script src="platform-util.js"></script>

<!-- Optionally, you can add Slimscroll and FastClick plugins.
     Both of these plugins are recommended to enhance the
     user experience. Slimscroll is required when using the
     fixed layout. -->

<%
    String uri = request.getRequestURI();
    String pageName = uri.substring(uri.lastIndexOf("/") + 1).replace(".", "_");

%>    
<script>
    var pageId = '#' + '<%=pageName%>';
//        alert(pageId);
    $(pageId).addClass('active');
    var parentId = '#' + $(pageId).parent().parent().attr('id');
    if ($(parentId).attr('class') === 'treeview' && $(parentId).get(0).tagName === 'LI') {
        $(parentId).addClass('active');
    }
    var parentParentId = '#' + $(parentId).parent().parent().attr('id');
    if ($(parentParentId).attr('class') === 'treeview' && $(parentParentId).get(0).tagName === 'LI') {
        $(parentParentId).addClass('active');
    }

    function ajaxHandleError(response) {
//                     $("#alertid").removeClass("in").show();
//                     $("#alertid").delay(300).addClass("in").fadeOut(2000);
//                    alert(response.errorMsg);
//                    $('#alertModal').modal('show');
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
</script>