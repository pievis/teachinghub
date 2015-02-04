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
        <script src="<%=request.getContextPath()%>/js/login.js"></script>
        <!-- <script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/jquery/jquery.js"></script> -->
    </head>
    <body>
        <div id="wrapper">
            <header>
                <%@include file="/WEB-INF/jspf/header.jspf" %>
            </header>
            <div id="content">
                <h1 class="longh">Login</h1>
                <div class="container">
                    <form onsubmit="getXmlHttpRequest(); return false;">
                            <p> Username: <input type="text" name="user" id="txtUser"> </p>
                            <p> Password:<input type="password" name="pass" id="txtPass"> </p>
                            <input class="btn" type="submit" value="invia dati login">
                    </form>
                    <div class="errorbox" id="errorbox">
                                <!--<img src="<%=request.getContextPath()%>/multimedia/icons/delete85.png"-->
                                <p>
                                    Problema con i dati inseriti in form.
                                </p>
                    </div>
                </div>
            </div>
        </div>
        <footer>
          <%@include file="/WEB-INF/jspf/footer.jspf" %>
        </footer>
    </body>
</html>
