<%-- 
    Document   : login
    Created on : Feb 1, 2015, 4:22:39 PM
    Author     : Lorenzo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Teaching Hub - Login</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style-sheets/main.css">
    </head>
    <%
        String sesUserId = null;
        boolean canUpdateProfile = false;
        if(session != null)
            sesUserId = (String) session.getAttribute("userid");
        String reqUserId = request.getParameter("userid");
        String ctxPath = request.getContextPath();
        //User info
        String userId = sesUserId; 
        String firstName = "Pierluigi";
        String lastName = "Montagna";
        String avatarPath = ctxPath +  "multimedia/avatar0.png";
        if(sesUserId != null && sesUserId.equals(reqUserId))
            canUpdateProfile = true;
    %>
    <body>
        <div id="wrapper">
            <header>
                <%@include file="/WEB-INF/jspf/header.jspf" %>
            </header>
            <div id="content">
                <h1 class="longh">Login</h1>
                <div class="container">
                    <div id="profile">
                        <div class="avatar">
                            <img src="<%=avatarPath%>"/>
                        </div>
                    </div>
                </div>
            </div>
            <footer>
                <%@include file="/WEB-INF/jspf/footer.jspf" %>
            </footer>
        </div>
    </body>
</html>
