<%-- 
    Document   : section
    Created on : Feb 1, 2015, 4:22:21 PM
    Author     : Lorenzo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Teaching Hub - Section</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style-sheets/main.css">
    </head>
    <%
    String sectionId = request.getParameter("sectionid");
    //TODO prendere le info per le discussioni e pagine in base a sectionid
    %>
    <body>
        <div id="wrapper">
            <header>
                <%@include file="/WEB-INF/jspf/header.jspf" %>
            </header>
            <div id="content">
                <h1 class="longh"><%=sectionId%></h1>
                <h2 class="longh">Pagine & Appunti</h2>
                <div class="container">
                    <ul>
                        <li>Coffee</li>
                        <li>Tea</li>
                        <li>Milk</li>
                    </ul>
                </div>
                <h2 class="longh">Domande & Discussioni</h2>
                <div class="container">
                    <div class="dbox">
                        <span class="title">Titolo</span>
                        <span class="autor right">creato da: Francesco Amadori</span>
                        <br>
                        <span class="desc">Descrizione</span>
                        <span class="data right">22/04/2007</span>
                    </div>
                </div>
            </div>
            <footer>
                <%@include file="/WEB-INF/jspf/footer.jspf" %>
            </footer>
        </div>
    </body>
</html>
