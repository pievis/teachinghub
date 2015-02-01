<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--
Index del sito con contenuto variabile in base alle informazioni presenti in 
sessione.

header e footer sono due jsp fragment
il div container cambia di contenuto in base al contesto (sessione, utente, ecc)

-->
<html>
    <head>
        <title>Teaching Hub</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="style-sheets/main.css">
    </head>
    <body>
        <div id="wrapper">
            <header>
                <%@include file="/WEB-INF/jspf/header.jspf" %>
            </header>
            <div id="content">
                <div id="courses">
                    <h1>Corsi:</h1>
                    <section>Matematica</section>
                    <section>Scienze</section>
                    <section>Fisica</section>
                    <section>Informatica</section>
                </div>
            </div>
            <footer>
                <%@include file="/WEB-INF/jspf/footer.jspf" %>
            </footer>
        </div>
    </body>
</html>
