<%-- 
    Document   : new_page
    Created on : Feb 1, 2015, 4:58:22 PM
    Author     : Pier
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Teaching Hub - Crea una nuova pagina</title>
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
            <div id="content" ctx-url="<%=request.getContextPath()%>" userid="<%=userid%>">
                <h1 class="longh">
                    <img src="<%=request.getContextPath()%>/multimedia/icons/book245.png">
                    <%=sectionid%></h1>
                <h2 class="longh">Crea una nuova pagina</h2>
                <div class="container" >
                    <form onsubmit="checkAndSendPage(); return false">
                        <h3 class="longh">Nome pagina</h3>
                        <p><input placeholder="Nome della pagina" type="text" name="pagename"></p>
                        <h3 class="longh">Descrizione</h3>
                        <p><input placeholder="Descrizione della pagina" class="longinput" type="text" name="description"></p>
                        <h3 class="longh">Contenuto Pagina</h3>
                        <p>
                            <textarea placeholder="Contenuto della pagina" class="longinput" name="pagecontent" rows="8"></textarea>
                        </p>
                        <h3 class="longh">Allegati</h3>
                        <input id="selFiles" type="file" name="files"
                               />
                        <p></p>
                    <p>
                        <!--<button class="btn">Annulla</button>-->
                        <input class="btn" type="submit" value="Invia" data-bind="visible: showSubmit">
                        <div class="loader" data-bind="visible: showLoader"></div>
                    </p>
                    </form>
                    <div class="errorbox" id="errorbox" data-bind="visible: showErrorMsg">
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
    <!--<script src="<%=request.getContextPath()%>/js/lib/date.js"></script>-->
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/knockout/knockout-3.2.0.js"></script>
    <script src="<%=request.getContextPath()%>/js/managenewmsg.js"></script>
</html>
