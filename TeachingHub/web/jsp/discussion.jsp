<%-- 
    Document   : discussion
    Created on : Feb 1, 2015, 4:23:19 PM
    Author     : Lorenzo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%
    String sectionId = request.getParameter("sectionid");
    String discussionId = request.getParameter("id"); //TODO: discid - discussion
    //String userId = request.getParameter("userid");
    %>
    <head>
        <title>Teaching Hub - <%=discussionId%></title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style-sheets/main.css">
    </head>
    <body>
        <div id="wrapper">
            <header>
                <%@include file="/WEB-INF/jspf/header.jspf" %>
            </header>
            <div id="content" sectionid="<%=sectionId%>" discid="<%=discussionId%>" userid="<%=userid%>" ctx-url="<%=request.getContextPath()%>">
                <h1 class="longh"> 
                    <a href="<%=request.getContextPath()%>/jsp/section.jsp?sectionid=Matematica"
                       data-bind="attr:{href: sectionUrl}">
                        <span data-bind="text: sectionTxt" class="backSection">Sezione</span> 
                    </a>
                </h1>
                <h2 class="longh">
                    <span data-bind="text: discTitleText" class="titleTxt"></span>
                    <p>
                        <span data-bind="text: discDescriptionText" class="descriptionTxt"></span>
                    </p>
                </h2>
                
                <div data-bind="foreach: messages" class="container">
                    <div class="message">
                        <div class="avatar">
                            <img src="<%=request.getContextPath()%>/multimedia/avatars/avatar0.png"/>
                            <b><span data-bind="text: autor"></span></b>
                        </div>
                        <div class="content">
                            <span data-bind="text: content"></span>
                        </div>
                        <div class="files">
                            <span class="attach">
                                file.pdf
                            </span>
                        </div>
                    </div>
                </div>
                <!-- Da fare solo se sei loggato -->
                <h1 class="longh">Rispondi</h1>
                <div class="container">
                    <form onsubmit="getXmlHttpRequest('newMsg'); return false;">
                        Contenuto Messaggio: <br>
                        <textarea data-bind="value: newMsgContent" class="longinput" name="message" rows="8"></textarea>
                        <p>Allegato: <input type="file" name="datafile" size="40">
                        </p>
                        <input class="btn" type="submit" value="Invia">    
                    </form>
                </div>
                <div class="errorbox" id="errorbox" data-bind="visible: showErrorMsg() >0">
                        <!--<img src="<%=request.getContextPath()%>/multimedia/icons/delete85.png"-->
                        <p>
                            Errore - <span data-bind="text: errorMsg"></span>
                        </p>
                </div>
            </div>
        </div>
            <footer>
                <%@include file="/WEB-INF/jspf/footer.jspf" %>
            </footer>
    </body>
    <script src="<%=request.getContextPath()%>/js/lib/jquery/jquery.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/knockout/knockout-3.2.0.js"></script>
    <script src="<%=request.getContextPath()%>/js/discussion.js"></script>
</html>
