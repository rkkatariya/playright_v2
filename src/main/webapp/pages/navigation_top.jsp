<%@page import="com.revvster.playright.access.Action"%>
<%@page import="com.revvster.playright.access.Resource"%>
<%@page import="com.revvster.playright.model.UserEntitlement"%>
<%@page import="java.util.List"%>
<%@page import="com.revvster.playright.access.AuthorizationManager"%>
<%@page import="com.revvster.playright.model.User"%>
<%@page import="com.revvster.playright.util.SystemConstants"%>
<%
    User user = null;
    if (session.getAttribute(SystemConstants.LoggedInUser) == null) {
        user = new User();
    } else {
        user = (User) session.getAttribute(SystemConstants.LoggedInUser);
    }

    boolean viewUserProfile = false;
    if (user.getUserEntitlements() != null) {
        List<UserEntitlement> ues = user.getUserEntitlements();
        List<UserEntitlement> userEnts = AuthorizationManager
                .getUserEntsWithResource(ues, Resource.user);

        if (!userEnts.isEmpty()) {
            UserEntitlement listHome = AuthorizationManager
                    .getUserEntitlement(userEnts, new UserEntitlement(Resource.user, Action.view));
            if (listHome != null && listHome.getId() != null) {
                viewUserProfile = true;
            }
        }
    }
%>
<!-- Main Header -->
<header class="main-header">
    <style>
        .skin-red .main-header .logo {
            background-color: #222d32;
            color: #ffffff;
            border-bottom: 0 solid transparent;            
        }
        .title1 {
            font-family: 'Lucida Sans Typewriter', 'Lucida Typewriter', monospace;
            /*font-weight: bold;*/
            letter-spacing: 2px;
            color: #c0392b;
            border-bottom: 3px solid #C0C0C0;
        }
        .title2 {
            font-family: 'Lucida Sans Typewriter', 'Lucida Typewriter', monospace;
            /*font-weight: bold;*/
            letter-spacing: 2px;
            color: #C0C0C0;
            border-bottom: 3px solid #c0392b;
        }
    </style>
    <!-- Logo -->
    <a href="http://playright.in/" class="logo">
        <!-- mini logo for sidebar mini 50x50 pixels -->
        <span class="logo-mini"><span class="title1">P</span><span class="title2">R</span></span>
        <!-- logo for regular state and mobile devices -->
        <div class="logo-lg" >           
            <!--<img class="img-responsive" src="../images/text_logo_white.png"></img>-->     
            <span class="title1">PLAY</span><span class="title2">RIGHT</span>
        </div>
    </a>

    <!-- Header Navbar -->
    <nav class="navbar navbar-static-top" role="navigation">
        <!-- Sidebar toggle button-->
        <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">Toggle navigation</span>
        </a>
        <!-- Navbar Right Menu -->
        <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">
                <!-- Messages: style can be found in dropdown.less-->
                <!--                <li class="dropdown messages-menu">
                                     Menu toggle button 
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                        <i class="fa fa-envelope-o"></i>
                                        <span class="label label-success">4</span>
                                    </a>
                                    <ul class="dropdown-menu">
                                        <li class="header">You have 4 messages</li>
                                        <li>
                                             inner menu: contains the messages 
                                            <ul class="menu">
                                                <li> start message 
                                                    <a href="#">
                                                        <div class="pull-left">
                                                             User Image 
                                                            <img src="../adminlte/dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
                                                        </div>
                                                         Message title and timestamp 
                                                        <h4>
                                                            Support Team
                                                            <small><i class="fa fa-clock-o"></i> 5 mins</small>
                                                        </h4>
                                                         The message 
                                                        <p>Why not buy a new awesome theme?</p>
                                                    </a>
                                                </li> end message 
                                            </ul> /.menu 
                                        </li>
                                        <li class="footer"><a href="#">See All Messages</a></li>
                                    </ul>
                                </li> /.messages-menu 
                
                                 Notifications Menu 
                                <li class="dropdown notifications-menu">
                                     Menu toggle button 
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                        <i class="fa fa-bell-o"></i>
                                        <span class="label label-warning">10</span>
                                    </a>
                                    <ul class="dropdown-menu">
                                        <li class="header">You have 10 notifications</li>
                                        <li>
                                             Inner Menu: contains the notifications 
                                            <ul class="menu">
                                                <li> start notification 
                                                    <a href="#">
                                                        <i class="fa fa-users text-aqua"></i> 5 new members joined today
                                                    </a>
                                                </li> end notification 
                                            </ul>
                                        </li>
                                        <li class="footer"><a href="#">View all</a></li>
                                    </ul>
                                </li>
                                 Tasks Menu 
                                <li class="dropdown tasks-menu">
                                     Menu Toggle Button 
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                        <i class="fa fa-flag-o"></i>
                                        <span class="label label-danger">9</span>
                                    </a>
                                    <ul class="dropdown-menu">
                                        <li class="header">You have 9 tasks</li>
                                        <li>
                                             Inner menu: contains the tasks 
                                            <ul class="menu">
                                                <li> Task item 
                                                    <a href="#">
                                                         Task title and progress text 
                                                        <h3>
                                                            Design some buttons
                                                            <small class="pull-right">20%</small>
                                                        </h3>
                                                         The progress bar 
                                                        <div class="progress xs">
                                                             Change the css width attribute to simulate progress 
                                                            <div class="progress-bar progress-bar-aqua" style="width: 20%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                                                <span class="sr-only">20% Complete</span>
                                                            </div>
                                                        </div>
                                                    </a>
                                                </li> end task item 
                                            </ul>
                                        </li>
                                        <li class="footer">
                                            <a href="#">View all tasks</a>
                                        </li>
                                    </ul>
                                </li>-->
                <!-- User Account Menu -->
                <li class="dropdown user user-menu">

                    <!-- Menu Toggle Button -->

                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">

                        <!-- The user image in the navbar-->
                        <% if (user.getGender().equals("Male")) {%>
                        <img src="../adminlte2/dist/img/male.png" class="user-image" alt="User Image">
                        <% } else {%>
                        <img src="../adminlte2/dist/img/female.jpg" class="user-image" alt="User Image">
                        <%}%>

                        <!-- hidden-xs hides the username on small devices so only the image appears. -->
                        <span class="hidden-xs"><%=user.getFullName()%></span>
                    </a>
                    <ul class="dropdown-menu">                        
                        <!-- The user image in the menu -->
                        <li class="user-header">                             
                            <% if (user.getGender().equals("Male")) {%>
                            <img class="profile-user-img img-responsive img-circle" src="../adminlte2/dist/img/male.png" alt="User profile picture">
                            <% } else {%>
                            <img class="profile-user-img img-responsive img-circle" src="../adminlte2/dist/img/female.jpg" alt="User profile picture">
                            <%}%>
                            <p>
                                <%=user.getFullName()%>
                                <small><em><%=user.getRoleName()%></em></small>
                                <small><%=user.getCompanyName()%></small>
                            </p>
                        </li>
                        <!-- Menu Body -->
                        <!--                        <li class="user-body">
                                                    <div class="col-sm-4 text-center">
                                                        <a href="#">Followers</a>
                                                    </div>
                                                    <div class="col-sm-4 text-center">
                                                        <a href="#">Sales</a>
                                                    </div>
                                                    <div class="col-sm-4 text-center">
                                                        <a href="#">Friends</a>
                                                    </div>
                                                </li>-->
                        <!-- Menu Footer-->
                        <li class="user-footer">
                            <%if (viewUserProfile) {%>
                            <div class="pull-left">
                                <a href="userProfile.jsp?id=<%=user.getId()%>" class="btn btn-default btn-flat">Profile</a>
                            </div>
                            <%}%>
                            <div class="pull-right">
                                <a href="logout" class="btn btn-default btn-flat">Sign out</a>
                            </div>
                        </li>
                    </ul>
                </li>
                <!-- Control Sidebar Toggle Button -->
                <!--                <li>
                                    <a href="#" data-toggle="control-sidebar"><i class="fa fa-gears"></i></a>
                                </li>-->
            </ul>
        </div>
    </nav>
</header>