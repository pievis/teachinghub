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
            <div id="content">
                <h1 class="longh">Registrati</h1>
                <form action="../src/java/it/asw/access/Registration">
			<p> Nome: <input type="text" name="nome"> </p>
			<p> Cognome: <input type="text" name="cognome"> </p>
                        <p> Classe: <input type="text" name="classe"> </p>
			<p> Username: <input type="text" name="user"> </p>
			<p> Password: <input type="text" name="pass"> </p>
                        <!-- Ricordarsi di inserire qualcosa tipo "License of Agreement" -->
			<input type="submit" value="invia dati registrazione">
		</form>
            </div>
            <footer>
                <%@include file="/WEB-INF/jspf/footer.jspf" %>
            </footer>
        </div>
    </body>
</html>
