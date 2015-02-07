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
            <div id="content" ctx-url="<%=request.getContextPath()%>">
                <h1 class="longh">Corsi</h1>
                <div id="courses" data-bind="fadeVisible: displayAdvancedOptions, foreach: sections">
                        <a href="jsp/section.jsp?sectionid=Matematica" data-bind="attr: {href: url}">
                            <div class="section" data-bind="style: {'background-image': 'url(\'' + icon + '\')' }">
                                <span data-bind="text: name">Nome Sezione</span>
                                <p>
                                    <span data-bind="text: description">Descrizione</span>
                                </p>
                            </div>
                        </a>
                </div>
            </div>
        </div>
            <footer>
                <%@include file="/WEB-INF/jspf/footer.jspf" %>
            </footer>
    </body>
    <script src="<%=request.getContextPath()%>/js/lib/jquery/jquery.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/knockout/knockout-3.2.0.js"></script>
    <script src="<%=request.getContextPath()%>/js/managesections.js"></script>
</html>
