<%-- 
    Document   : teachersList
    Created on : Feb 22, 2015, 6:30:42 PM
    Author     : Lorenzo
--%>

<%@page import="asw1028.db.structs.Subjects"%>
<%@page import="asw1028.db.structs.Teacher"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style-sheets/main.css">
    </head>
    <body>
        <%
        List<Teacher> teachers = (List<Teacher>) request.getAttribute("teachers");
        %>
        <div id="wrapper">
            <header>
                <%@include file="/WEB-INF/jspf/header.jspf" %>
            </header>
            <div>
                <h1 class="longh">Lista docenti</h1>
                <div class="container">
                    <%
                    for(Teacher t : teachers) { //for each teacher, print important information
                        Subjects subs = t.getSubjects();
                    %>
                    <div class="profileinfo">
                        <p>
                            <b>Username</b>: <%=t.getId()%>
                        </p>
                        <p>
                            <b>Nome</b>: <%=t.getFirstname()%>
                        </p>
                        <p>
                            <b>Cognome</b>: <%=t.getLastname()%>
                        </p>
                        <p>
                            <b>Email</b>: <%=t.getEmail()%>
                        </p>
                        <%
                        //Also print the subjects
                        for(String s : subs.getSubject()) {
                        %>
                        <p>
                            <b>Materia</b>: <%=s%>
                        </p>
                        <%
                        }
                        %>
                    </div>
                    <%
                    }
                    %>
                </div>
            </div>
        </div>
        <footer>
          <%@include file="/WEB-INF/jspf/footer.jspf" %>
        </footer>
    </body>
</html>
