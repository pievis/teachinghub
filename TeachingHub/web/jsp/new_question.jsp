<%-- 
    Document   : new_discussion
    Created on : Feb 1, 2015, 4:58:22 PM
    Author     : Lorenzo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Teaching Hub - Create a new discussion</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style-sheets/main.css">
    </head>
    <body>
        <%
        String sectionid = request.getParameter("sectionid");
        %>
        <div id="wrapper">
            <header>
                <%@include file="/WEB-INF/jspf/header.jspf" %>
            </header>
            <div id="content" action="NewDiscussion">
                <h1 class="longh">
                    <img src="<%=request.getContextPath()%>/multimedia/icons/book245.png">
                    <%=sectionid%></h1>
                <h2 class="longh">Crea una nuova discussione</h2>
                <div class="container" >
                    <form onsubmit="checkAndSendDiscussion(); return false">
                        <h3 class="longh">Titolo</h3>
                        <p><input placeholder="Titolo discussione" type="text" name="title"></p>
                        <h3 class="longh">Descrizione</h3>
                        <p><input placeholder="Descrizione del messaggio" class="longinput" type="text" name="description"></p>
                        <h3 class="longh">Contenuto Messaggio</h3>
                        <p>
                            <textarea placeholder="Testo del messaggio" class="longinput" name="message" rows="8"></textarea>
                        </p>
                    <p>
                        <!--<button class="btn">Annulla</button>-->
                        <input class="btn" type="submit" value="Invia">
                    </p>
                    </form>
                    <div class="errorbox" id="errorbox" data-bind="visible: showErrorMsg">
                        <!--<img src="<%=request.getContextPath()%>/multimedia/icons/delete85.png"-->
                        <p>
                            <span data-bind="text: errorMsg"></span>
                        </p>
                    </div>
                </div>
            </div>
        </div>
            <footer>
                <%@include file="/WEB-INF/jspf/footer.jspf" %>
            </footer>
    </body>
    <!-- Questa posizione è richiesta da knockout -->
    <script src="<%=request.getContextPath()%>/js/lib/jquery/jquery.js"></script>
    <script src="<%=request.getContextPath()%>/js/lib/date.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/knockout/knockout-3.2.0.js"></script>
    <script src="<%=request.getContextPath()%>/js/managenewmsg.js"></script>
</html>
