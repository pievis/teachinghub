<%-- 
    Document   : aboutus
    Created on : Feb 22, 2015, 5:11:43 PM
    Author     : Pievis
--%>

<%@page import="asw1028.db.structs.Lastupdate"%>
<%@page import="asw1028.db.structs.Aboutus"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Teaching Hub - About us</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style-sheets/main.css">
        
    </head>
    <%
        //Informazioni sull'about us
        Aboutus au = (Aboutus) request.getAttribute("aboutus");
        String content = au.getContent();
        Lastupdate lu = au.getLastupdate();
        String lastupdate = lu.getDatetime().getDate() + " " + lu.getDatetime().getTime() + " by " + lu.getAutor();
    %>
    <body>
        <div id="wrapper">
            <header>
                <%@include file="/WEB-INF/jspf/header.jspf" %>
            </header>
            <div>
                <h1 class="longh">About us</h1>
                <div class="container">
                    <%=content%>
                </div>
                    <h1 class="longh">Ultimo aggiornamento: 
                    <span>
                        <%=lastupdate%>
                    </span>
                    </h1>
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
