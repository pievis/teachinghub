<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%
    String sectionId = request.getParameter("sectionid");
    String pageId = request.getParameter("id");
    %>
    <head>
        <title>Teaching Hub - <%=pageId%></title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style-sheets/main.css">
    </head>
    <body>
        <div id="wrapper">
            <header>
                <%@include file="/WEB-INF/jspf/header.jspf" %>
            </header>
            <div id="content" sectionid="<%=sectionId%>" pageid="<%=pageId%>" ctx-url="<%=request.getContextPath()%>">
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
                
                <div class="container">
                    <div class="message">
                        <div class="avatar">
                            <img src="<%=request.getContextPath()%>/multimedia/avatars/avatar0.png"/>
                            <b><span data-bind="text: autor"></span></b>
                        </div>
                        <div class="content">
                            <span data-bind="text: content"></span>
                        </div>
                        <div class="files" data-bind="foreach: datafiles">
                            <a src="" data-bind="attr:{href:url}">
                                <span class="attach" data-bind="text: name ">
                                    file.pdf
                                </span>
                            </a>
                        </div>
                    </div>
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
    <script src="<%=request.getContextPath()%>/js/page.js"></script>
</html>
