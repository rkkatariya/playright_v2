<%-- 
    Document   : check.jsp
    Created on : Oct 17, 2015, 10:45:18 AM
    Author     : Rahul
--%>

<%@page import="java.util.Enumeration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.revvster.playright.util.SystemConstants"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <table border="1">
            <%
            //allow access only if session exists
                String user = null;
                if (session != null) {
                    Enumeration<String> e = session.getAttributeNames();
                    while (e.hasMoreElements()) {
                        String name = e.nextElement();
            %>
            <tr>
                <td><%=name%></td>
                <td><%=session.getAttribute(name)%></td>
            </tr>
            <%
                    }
                }
            %>            

        </table>
    </body>
</html>
