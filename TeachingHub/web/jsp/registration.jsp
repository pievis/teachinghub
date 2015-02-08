<%-- 
    Document   : registration
    Created on : Feb 1, 2015, 4:22:56 PM
    Author     : Lorenzo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Teaching Hub - Registration</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style-sheets/main.css">
        
    </head>
    <body>
        <div id="wrapper">
            <header>
                <%@include file="/WEB-INF/jspf/header.jspf" %>
            </header>
            <div id="content" id="content" ctx-url ="<%=request.getContextPath()%>">
                <h1 class="longh">Registrati</h1>
                <div class="container">
                    <form name="regForm" method="post" onsubmit="getXmlHttpRequest(); return false;">
                            <p> Nome: <br> <input type="text" name="nome"> </p><br>
                            <p> Cognome: <br> <input type="text" name="cognome"> </p><br>
                            <p> Classe: <br> <input type="text" name="classe"> </p><br>
                            <p> Email: <br> <input type="text" name="email"> </p><br>
                            <p> Username: <br> <input type="text" name="user"> </p><br>
                            <p> Password: <br> <input type="password" name="pass"> </p><br>
                            <p> Conferma Password: <br> <input type="password" name="cpass"> </p><br>
                            <!-- Ricordarsi di inserire qualcosa tipo "License of Agreement" -->
                            <input class="btn" type="submit" value="invia dati registrazione"><br>
                            <div class="errorbox" id="errorbox" data-bind="visible: showMsg() >0">
                                <p>
                                    <span data-bind="text: errorMsg"></span>
                                </p>
                            </div>
                            <br>
                    </form>
                </div>
            </div>
        </div>
            <footer>
                <%@include file="/WEB-INF/jspf/footer.jspf" %>
            </footer>
    </body>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/jquery/jquery.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/knockout/knockout-3.2.0.js"></script>
    <script src="<%=request.getContextPath()%>/js/registration.js"></script>
    <!--script src="<%=request.getContextPath()%>/js/formvalidation.js"></script-->
</html>
