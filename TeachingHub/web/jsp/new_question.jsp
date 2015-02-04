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
        <div id="wrapper">
            <header>
                <%@include file="/WEB-INF/jspf/header.jspf" %>
            </header>
            <div id="content" action="NewDiscussion">
                <h1 class="longh">Nome del corso</h1>
                <h2 class="longh">Crea una nuova discussione</h2>
                <div class="container" >
                    <form>
                        Titolo:<br> <input type="text" name="title"><br>
                        Descrizione:<br> <input class="longinput" type="text" name="description">
                        <br><br>
                        Contenuto Messaggio: <br>
                        <textarea class="longinput" name="message" rows="8"></textarea>
                    <br><br>
                    <input class="btn" type="submit" value="Invia">    
                    </form>
                </div>
            </div>
        </div>
            <footer>
                <%@include file="/WEB-INF/jspf/footer.jspf" %>
            </footer>
    </body>
</html>
