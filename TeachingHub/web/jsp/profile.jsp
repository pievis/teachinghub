<%@page import="asw1028.utils.SysKb"%>
<%@page import="asw1028.db.UsersManager"%>
<%@page import="asw.interfaces.IUser"%>
<%@page import="asw1028.db.structs.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Teaching Hub - Profile</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style-sheets/main.css">
    </head>
    <%
        String ctx = getServletContext().getRealPath("/");
        UsersManager um = new UsersManager(ctx);
        String sesUserId = null;
        boolean canUpdateProfile = false;
        if(session != null)
            sesUserId = (String) session.getAttribute("userid");
        String reqUserId = request.getParameter("userid");
        String ctxPath = request.getContextPath();
        //User info
        IUser user = um.getUserById(reqUserId);
        if(sesUserId != null && sesUserId.equals(reqUserId))
            canUpdateProfile = true;
    %>
    <body>
        <div id="wrapper">
            <header>
                <%@include file="/WEB-INF/jspf/header.jspf" %>
            </header>
            <div id="content" ctx-url="<%=request.getContextPath()%>">
                <h1 class="longh">Visualizza profilo di <%=reqUserId%></h1>
                <div class="container">
                    <div class="profile">
                        <div class="avatar">
                            <img data-bind="attr:{ src: avatarUrl, title: 'Avatar'}" 
                                id="avatar" src="<%=request.getContextPath()%>/multimedia/avatars/avatar0.png"/>
                        </div>
                        <div class="profileinfo">
                            <p>
                                <b>Username</b>: <span data-bind="text: username"></span>
                            </p>
                            <p>
                                <b>Nome</b>: <span data-bind="text: firstname"></span>
                            </p>
                            <p>
                                <b>Cognome</b>: <span data-bind="text: lastname"></span>
                            </p>
                            <p>
                                <b>Email</b>: <span data-bind="text: email"></span>
                            </p>
                            <p data-bind="visible: showClasse">
                                <b>Classe</b>: 
                                <span data-bind="text: classe"></span>
                            </p>
                            <p data-bind="visible: showHobby">
                                <b>Interessi</b>: <br>
                                <span data-bind="text: hobby"></span>
                            </p>
                            <p data-bind="visible: showSubjects">
                                <b>Materie</b>: <br>
                                <span data-bind="text: subjects"></span>
                            </p>
                        </div>
                    </div>
                </div>
                <%
                if(canUpdateProfile){
                    //Parte dell'html che deve gestire la modifica del profilo lato client
                    %>
                    <button class="btn" data-bind="click: updateProfileInfo, text: btnUpdateText">Modifica Profilo</button>
                <%
                }
                %>
                <div class="container" data-bind="visible: updateVisible">
                        <h3 class="longh">Modifica profilo</h3>
                            <p>
                                <b>Nome</b>: <input data-bind="value: firstname"></input>
                            </p>
                            <p>
                                <b>Cognome</b>: <input data-bind="value: lastname"></input>
                            </p>
                            <p>
                                <b>Email</b>: <input data-bind="value: email"></input>
                            </p>
                            <p data-bind="visible: showClasse">
                                <b>Classe</b>: 
                                <select data-bind="options: availableYears,
                                value: selectedYear"></select>
                                <select data-bind="options: availableClass,
                                value: selectedClass"></select>
                            </p>
                            <p data-bind="visible: showHobby">
                                <b>Interessi</b>: <br>
                                <textarea rows="5" class="areabox" data-bind="value: hobby"></textarea>
                            </p>
                            <p>
                                <b>Avatar</b>: <br>
                                <input id="selAvatar" type="file" name="avatarfile" accept="image/gif, image/jpeg, image/png"
                                       />
                                
                            
                            </p>
                            <p>
                                <button class="btn" data-bind="click: sendUpdatedProfile, visible: canUpdate">
                                    Aggiorna
                                </button>
                                <div class="loader" data-bind="visible: showLoader"></div>
                            </p>
                </div>
                <div class="errorbox" id="errorbox" data-bind="visible: showErrorMsg">
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
    <!--knockout-->
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/jquery/jquery.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/knockout/knockout-3.2.0.js"></script>
    <script src="<%=request.getContextPath()%>/js/checkprofile.js"></script>
</html>
