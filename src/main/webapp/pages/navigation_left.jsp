<%@page import="com.revvster.playright.model.Settings"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.revvster.playright.model.Setting"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.revvster.playright.access.AuthorizationManager"%>
<%@page import="com.revvster.playright.access.Resource"%>
<%@page import="com.revvster.playright.access.Action"%>
<%@page import="com.revvster.playright.model.UserEntitlement"%>
<%@page import="com.revvster.playright.model.User"%>
<%@page import="com.revvster.playright.util.SystemConstants"%>
<%
    User user = new User();
    if (session.getAttribute(SystemConstants.LoggedInUser) != null) {
        user = (User) session.getAttribute(SystemConstants.LoggedInUser);
    }
    HashMap<Settings, String> sysSettings = new HashMap<>();
    if (session.getAttribute(SystemConstants.SystemSettings) != null) {
        sysSettings = (HashMap<Settings, String>) session.getAttribute(SystemConstants.SystemSettings);
    }
    String contextMenu = "Projects";
    boolean viewLocationMenu = false;
    boolean viewProjectMenu = false;
    boolean viewCompanyMenu = false;
    boolean viewUserMenu = false;
    boolean viewSettingsMenu = false;
    boolean viewSetupMenu = false;
    boolean viewHomeMenu = false;

    if (user.getUserEntitlements() != null) {
        List<UserEntitlement> ues = user.getUserEntitlements();
        List<UserEntitlement> companyEnts = AuthorizationManager
                .getUserEntsWithResource(ues, Resource.company);
        List<UserEntitlement> projectEnts = AuthorizationManager
                .getUserEntsWithResource(ues, Resource.project);
        List<UserEntitlement> locationEnts = AuthorizationManager
                .getUserEntsWithResource(ues, Resource.location);
        List<UserEntitlement> userEnts = AuthorizationManager
                .getUserEntsWithResource(ues, Resource.user);
        List<UserEntitlement> setupEnts = AuthorizationManager
                .getUserEntsWithResource(ues, Resource.setup);
        List<UserEntitlement> settingEnts = AuthorizationManager
                .getUserEntsWithResource(ues, Resource.setting);
        List<UserEntitlement> unitsEnts = AuthorizationManager
                .getUserEntsWithResource(ues, Resource.setup);
        List<UserEntitlement> homeEnts = AuthorizationManager
                .getUserEntsWithResource(ues, Resource.home);

        if (!companyEnts.isEmpty()) {
            contextMenu = "Companies / Projects";
        }

        if ("true".equalsIgnoreCase(sysSettings.get(Settings.EnableLocation))) {
            if (!locationEnts.isEmpty()) {
                viewLocationMenu = true;
            }
        }

        if (!projectEnts.isEmpty()) {
            UserEntitlement listProj = AuthorizationManager
                    .getUserEntitlement(projectEnts, new UserEntitlement(Resource.project, Action.view));
            if (listProj != null && listProj.getId() != null) {
                viewProjectMenu = true;
            }
        }

        if (!homeEnts.isEmpty()) {
            UserEntitlement listHome = AuthorizationManager
                    .getUserEntitlement(homeEnts, new UserEntitlement(Resource.home, Action.view));
            if (listHome != null && listHome.getId() != null) {
                viewHomeMenu = true;
            }
        }

        if (!companyEnts.isEmpty()) {
            viewCompanyMenu = true;
        }

        if (!userEnts.isEmpty()) {
            UserEntitlement listUser = AuthorizationManager
                    .getUserEntitlement(userEnts, new UserEntitlement(Resource.user, Action.list));
            if (listUser != null && listUser.getId() != null) {
                viewUserMenu = true;
            }
        }

        if (!setupEnts.isEmpty()) {
            UserEntitlement listSetup = AuthorizationManager
                    .getUserEntitlement(setupEnts, new UserEntitlement(Resource.setup, Action.list));
            if (listSetup != null && listSetup.getId() != null) {
                viewSetupMenu = true;
            }
        }
        if (!settingEnts.isEmpty()) {
            UserEntitlement listSetting = AuthorizationManager
                    .getUserEntitlement(settingEnts, new UserEntitlement(Resource.setting, Action.list));
            if (listSetting != null && listSetting.getId() != null) {
                viewSettingsMenu = true;
            }
        }
    }


%>
<!-- Left side column. contains the logo and sidebar -->
<aside class="main-sidebar">

    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">

        <!-- Sidebar user panel (optional) -->
        <div class="user-panel">
            <div class="pull-left image">
                <img src="../images/icon_white.png">
            </div>
            <!--            <div class="pull-left info">
                            <p></p>
                             Status 
                            <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
                        </div>-->
            <div class="info">
                <p class="text-bold"><%=user.getCompanyName()%></p>
                <i class="text-gray"><%=user.getRoleName()%></i>
            </div>
        </div>

        <!-- search form (Optional) -->
        <!--        <form action="#" method="get" class="sidebar-form">
                    <div class="input-group">
                        <input type="text" name="q" class="form-control" placeholder="Search...">
                        <span class="input-group-btn">
                            <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i></button>
                        </span>
                    </div>
                </form>-->
        <!-- /.search form -->

        <!-- Sidebar Menu -->
        <ul class="sidebar-menu">
            <!--<li class="header">HEADER</li>-->
            <!-- Optionally, you can add icons to the links -->

            <li id="home_jsp"><a href="home.jsp"><i class="fa fa-home"></i> <span>Home</span></a></li>
            <li id="data_jsp"><a href="data.jsp"><i class="fa fa-list"></i> <span>Data</span></a></li>
                <%if (viewCompanyMenu) {%>
                <li id="company_jsp"><a href="company.jsp"><i class="fa fa-building-o"></i> <span>Companies</span></a></li>
                <li id="projects_jsp"><a href="projects.jsp"><i class="fa fa-file-powerpoint-o"></i> <span>Projects</span></a></li>
            <%} else if (viewProjectMenu) { %>
                <li id="projects_jsp"><a href="projects.jsp"><i class="fa fa-file-powerpoint-o"></i> <span>Projects</span></a></li>
            <%}
                if (viewUserMenu) {%>
            <li id="users_jsp"><a href="users.jsp"><i class="fa fa-user"></i> <span>Users</span></a></li>
                <%}%>                    
            <li id="analytics_jsp"><a href="analytics.jsp"><i class="fa fa-area-chart"></i> <span>Analytics</span></a></li>       
            <%if (viewSetupMenu || viewSettingsMenu) {%>

            <li id="emailConfig_jsp"><a href="emailConfig.jsp"><i class="fa fa-envelope-o" ></i> <span>Email Setup</span></a></li>
            <li id="settings_jsp"><a href="settings.jsp"><i class="fa fa-gear" ></i> <span>Settings</span></a></li>                    

            <%}%>
        </ul><!-- /.sidebar-menu -->

    </section>
    <!-- /.sidebar -->
</aside>
