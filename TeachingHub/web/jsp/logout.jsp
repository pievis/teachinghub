<%-- 
    Document   : login
    Created on : Feb 1, 2015, 4:22:39 PM
    Author     : Lorenzo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Teaching Hub</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style-sheets/main.css">
    </head>
    <body>
        <%
        session.invalidate();
        %>
        <div id="wrapper">
            <header>
                <%@include file="/WEB-INF/jspf/header.jspf" %>
            </header>
            <div id="content">
                <h1 class="longh">Logout</h1>
                <div class="container">
                    <p>You Logged out.</p>
                </div>
            </div>
            <footer>
                <%@include file="/WEB-INF/jspf/footer.jspf" %>
            </footer>
        </div>
    </body>
</html>
