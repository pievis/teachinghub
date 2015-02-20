<%-- 
    Document   : section
    Created on : Feb 1, 2015, 4:22:21 PM
    Author     : Lorenzo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%
    String sectionId = request.getParameter("sectionid");
    boolean isTeacher = false;
    if(session != null)
        if(session.getAttribute("teacher") != null)
            isTeacher = true;
    %>
    <head>
        <title>Teaching Hub - <%=sectionId%></title>
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
                <h1 class="longh">
                    <img src="<%=request.getContextPath()%>/multimedia/icons/book245.png">
                    <%=sectionId%></h1>
                <h2 class="longh" data-bind="click: showHidePages">
                    <img src="<%=request.getContextPath()%>/multimedia/icons/book246.png">
                    Pagine & Appunti</h2>
                <%if(isTeacher){%>
                    <!--bottone che fa foward alla view per la nuova pagina-->
                    <button class="btn right" data-bind="click: fowardToNewPage">
                        Crea una nuova pagina</button>
                <%}%>
                <div class="container" data-bind="visible: pagesVisible">
                    <ul data-bind="foreach: pages">
                        <a data-bind="attr:{href: url}">
                            <li><span data-bind="text: name">Coffee</span>
                                <span class="autor" >(Creato da <span data-bind="text: autor">Qualcuno</span>)</span></li>
                        </a>
                    </ul>
                </div>
                <h2 class="longh">
                    <img src="<%=request.getContextPath()%>/multimedia/icons/laptops.png">
                    Domande & Discussioni</h2>
                    <%if(userid != null){%>
                    <!--bottone che fa foward alla view per la nuova discussione-->
                    <button class="btn right" data-bind="click: fowardToNewQuestion">
                        Crea una nuova discussione</button>
                    <%}%>
                    <div class="options">
                        <p>
                            Ordina per
                            <select data-bind="value: selectedAtr,
                                               options: atrs,
                                               optionsText: atrToText"
                                               ></select>
                            <select data-bind="value: selectedOrd,
                                               options: ords,
                                               optionsText: ordsToText"
                            ></select>
                        </p>
                    </div> 
                <div data-bind='fadeVisible: displayAdvancedOptions' class="container" id="threadsblock"
                            sectionid='<%=sectionId%>' ctx-url="<%=request.getContextPath()%>">
                    <div data-bind="foreach: threads" >
                        <a data-bind="attr: {href: url}" href="<%=request.getContextPath()%>/jsp/discussion.jsp?id=0">
                            <div class="dbox">
                                <p>
                                    <span data-bind="text: title" class="title">Titolo</span>
                                    <span data-bind="text: autor" class="autor right minor">creato da: Francesco Amadori</span>
                                </p>
                                <p>
                                    <span data-bind="text: description" class="desc">Descrizione</span>
                                    <span data-bind="text: datetime" class="data right minor">22/04/2007</span>
                                </p>
                                <p class="minor">
                                    Ultimo aggiornamento: 
                                    <span data-bind="text: lastupdate.datetime" class="data">datetime</span>
                                    di
                                    <span data-bind="text: lastupdate.autor" class="data">Autor</span>
                                </p>
                            </div>
                        </a>
                    </div>
                </div>
                <div class="errorbox" id="errorbox" data-bind="visible: showErrorMsg() >0">
                        <p>
                            <span data-bind="text: errorMsg"></span>
                        </p>
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
    <script src="<%=request.getContextPath()%>/js/managethreads.js"></script>
</html>
