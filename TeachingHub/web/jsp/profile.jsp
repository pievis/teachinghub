<%-- 
    Document   : login
    Created on : Feb 1, 2015, 4:22:39 PM
    Author     : Lorenzo
--%>

<%@page import="asw1028.utils.SysKb"%>
<%@page import="asw1028.db.UsersManager"%>
<%@page import="asw.interfaces.IUser"%>
<%@page import="asw1028.db.structs.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Teaching Hub - Profile</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style-sheets/main.css">
    </head>
    <%
        String ctx = getServletContext().getRealPath("/");
        UsersManager um = new UsersManager(ctx);
        String sesUserId = null;
        boolean canUpdateProfile = false;
        if(session != null)
            sesUserId = (String) session.getAttribute("userid");
        String reqUserId = request.getParameter("userid");
        String ctxPath = request.getContextPath();
        //User info
        IUser user = um.getUserById(reqUserId);
        if(sesUserId != null && sesUserId.equals(reqUserId))
            canUpdateProfile = true;
    %>
    <body>
        <div id="wrapper">
            <header>
                <%@include file="/WEB-INF/jspf/header.jspf" %>
            </header>
            <div id="content">
                <h1 class="longh">Visualizza profilo di <%=reqUserId%></h1>
                <div class="container">
                    <%if(user == null)
                        out.println("<p>Errore nel reperire il profilo utente richiesto.</p>");
                    else{            
            String userId = user.getId();
            String firstName = user.getFirstname();
            String lastName = user.getLastname();
            String email = user.getEmail();
            String avatarPath = ctxPath +  SysKb.avatarPath + user.getAvatar();
                    %>
                    <div class="profile">
                        <div class="avatar">
                            <img src="<%=avatarPath%>"/>
                        </div>
                        <div class="profileinfo">
                            <p>
                                <b>Username</b>: <%= userId %>
                            </p>
                            <p>
                                <b>Nome</b>: <%= firstName %>
                            </p>
                            <p>
                                <b>Cognome</b>: <%= lastName %>
                            </p>
                            <p>
                                <b>Email</b>: <%= email %>
                            </p>
                            <p>
                                <b>Interessi</b>: <br>
                                BLABLABLBALBALBALBLABLBALBLABLBLABLLBALBALBAL
                                LBALBALLBALBALBALBLABLBALBLABLBLABLLBALBALBAL
                                LBALBALLBALBALBALBLABLBALBLABLBLABLLBALBALBAL
                                LBALBALLBALBALBALBLABLBALBLABLBLABLLBALBALBAL
                                LBALBAL
                            </p>
                        </div>
                    </div>
                    <%}%>
                </div>
            </div>
        </div>
            <footer>
                <%@include file="/WEB-INF/jspf/footer.jspf" %>
            </footer>
    </body>
</html>
