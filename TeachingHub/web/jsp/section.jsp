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
                <h1 class="longh">
                    <img src="<%=request.getContextPath()%>/multimedia/icons/book245.png">
                    <%=sectionId%></h1>
                <h2 class="longh">
                    <img src="<%=request.getContextPath()%>/multimedia/icons/book246.png">
                    Pagine & Appunti</h2>
                <div class="container">
                    <ul>
                        <li>Coffee</li>
                        <li>Tea</li>
                        <li>Milk</li>
                    </ul>
                </div>
                <h2 class="longh">
                    <img src="<%=request.getContextPath()%>/multimedia/icons/laptops.png">
                    Domande & Discussioni</h2>
                <div class="container">
                    <div class="dbox">
                        <span class="title">Titolo</span>
                        <span class="autor right minor">creato da: Francesco Amadori</span>
                        <br>
                        <span class="desc">Descrizione</span>
                        <span class="data right minor">22/04/2007</span>
                    </div>
                    <div class="dbox">
                        <span class="title">Come integrare la pasta</span>
                        <span class="autor right minor">creato da: Francesco Amadori</span>
                        <br>
                        <span class="desc">Credevo che l'integrale fosse un tipo di pasta, mi si è aperto un mondo</span>
                        <span class="data right minor">22/04/2007</span>
                    </div>
                </div>
            </div>
            <footer>
                <%@include file="/WEB-INF/jspf/footer.jspf" %>
            </footer>
        </div>
    </body>
</html>
