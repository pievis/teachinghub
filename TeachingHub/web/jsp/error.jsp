<%-- 
    Document   : error
    Created on : Feb 22, 2015, 5:11:43 PM
    Author     : Pievis
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Teaching Hub - Error</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style-sheets/main.css">
        
    </head>
    <%
        String msg = (String) request.getAttribute("message");
        String specifics;
        specifics = ((Exception) request.getAttribute("exception")).getMessage();
    %>
    <body>
        <div id="wrapper">
            <header>
                <%@include file="/WEB-INF/jspf/header.jspf" %>
            </header>
            <div>
                <h1 class="longh">Errore</h1>
                <div class="container">
                    <p>Si è verificato un errore sul server remoto.</p>
                    <p>Messaggio: <span> <%=msg%> </span> </p>
                    <h3 class="longh">Info</h3>
                    <p>Dettagli:</p>
                    <p>
                        <span> <%=specifics%> </span>
                    </p>
                </div>
            </div>
        </div>
        <footer>
          <%@include file="/WEB-INF/jspf/footer.jspf" %>
        </footer>
    </body>
    <!-- Questa posizione è richiesta da knockout -->
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/jquery/jquery.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/knockout/knockout-3.2.0.js"></script>
    <script src="<%=request.getContextPath()%>/js/login.js"></script>
</html>
