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
    <body>
        <div id="wrapper">
            <header>
                <%@include file="/WEB-INF/jspf/header.jspf" %>
            </header>
            <div id="content">
                <h1 class="longh">Login</h1>
                <div class="container">
                    <form action="../Login">
                            <p> Username: <input type="text" name="user"> </p>
                            <p> Password: <input type="text" name="pass"> </p>
                            <input type="submit" value="invia dati login">
                    </form>
                </div>
            </div>
            <footer>
                <%@include file="/WEB-INF/jspf/footer.jspf" %>
            </footer>
        </div>
    </body>
</html>
