<%-- 
    Document   : discussion
    Created on : Feb 1, 2015, 4:23:19 PM
    Author     : Lorenzo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Teaching Hub - Discussion</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style-sheets/main.css">
    </head>
    <%
    String userIdSession = null;
    if(session != null)
        userIdSession = (String) session.getAttribute("userid");
    String discId = request.getParameter("id");
    boolean discExist = true;
    //TODO get info sulla discussione
    String discName = "Titolo disc";
    String discDesc = "Descrizione";
    
    %>
    <body>
        <div id="wrapper">
            <header>
                <%@include file="/WEB-INF/jspf/header.jspf" %>
            </header>
            <div id="content">
                <%
                    if(discExist){
                %>
                <h1 class="longh"> <%=discName%> </h1>
                <h2 class="longh"> <%=discDesc%> </h2>
                <% 
                //Cicla tra tutti i messaggi della discussione
                int msgN = 5;
                for(int i = 0; i<msgN; i++) {
                %>
                <div class="message">
                    <div class="avatar">
                        <img src="<%=request.getContextPath()%>/multimedia/avatars/avatar0.png"/>
                        <b><span>Username</span></b>
                    </div>
                    <div class="content">
marked as duplicate by Salman A Dec 20 '14 at 16:44

This question has been asked before and already has an answer. If those answers do not fully address your question, please ask a new question.

13	
 		
I didn't know about the overflow:auto trick - I'd always used the clearing div approach. Thanks for the tip. –  Herb Caudill Oct 20 '08 at 18:37
4	
 		
Tip: Solution 4 seems to work for Firefox 3+, but not IE7. For that you need solution 3 –  Manos Dilaverakis Sep 3 '09 at 14:02
  	
 		
Thanks for the info, I went wiby Salman A Dec 20 '14 at 16:44

This question has been asked before and already has an answer. If those answers do not fully address your question, please ask a new question.

13	
 		
I didn't know about the overflow:auto trick - I'd always used the clearing div approach. Thanks for the tip. –  Herb Caudill Oct 20 '08 at 18:37
4	
 		
Tip: Solution 4 seems to work for Firefox 3+, but not IE7. For that you need solution 3 –  Manos Dilaverakis Sep 3 '09 at 14:02
  	
 		
Thanks for the info, I went wiby Salman A Dec 20 '14 at 16:44

This question has been asked before and already has an answer. If those answers do not fully address your question, please ask a new question.

13	
 		
I didn't know about the overflow:auto trick - I'd always used the clearing div approach. Thanks for the tip. –  Herb Caudill Oct 20 '08 at 18:37
4	
 		
Tip: Solution 4 seems to work for Firefox 3+, but not IE7. For that you need solution 3 –  Manos Dilaverakis Sep 3 '09 at 14:02
  	
 		
Thanks for the info, I went wiby Salman A Dec 20 '14 at 16:44

This question has been asked before and already has an answer. If those answers do not fully address your question, please ask a new question.

13	
 		
I didn't know about the overflow:auto trick - I'd always used the clearing div approach. Thanks for the tip. –  Herb Caudill Oct 20 '08 at 18:37
4	
 		
Tip: Solution 4 seems to work for Firefox 3+, but not IE7. For that you need solution 3 –  Manos Dilaverakis Sep 3 '09 at 14:02
  	
 		
Thanks for the info, I went wiby Salman A Dec 20 '14 at 16:44

This question has been asked before and already has an answer. If those answers do not fully address your question, please ask a new question.

13	
 		
I didn't know about the overflow:auto trick - I'd always used the clearing div approach. Thanks for the tip. –  Herb Caudill Oct 20 '08 at 18:37
4	
 		
Tip: Solution 4 seems to work for Firefox 3+, but not IE7. For that you need solution 3 –  Manos Dilaverakis Sep 3 '09 at 14:02
  	
 		
Thanks for the info, I went with 3, couldn't get the first solution to work, perhaps I had a parent parent without float, like you suggested :) –  Doug Molineux Mar 15 '11 at 2:13
                    </div>
                        <div class="files">
                            <span class="attach" >
                                <img src="<%=request.getContextPath()%>/multimedia/icons/attach13.png"/>
                                file.pdf
                            </span>
                        </div>
                </div>
                
                <%
                }//Chiusura del for messaggi
                    if(userIdSession != null){
                        %>
                        <h1 class="longh">Rispondi</h1>
                        <div class="container">
                            <form>
                                Contenuto Messaggio: <br>
                                <textarea class="longinput" name="message" rows="8"></textarea>
                                <p>Allegato: <input type="file" name="datafile" size="40">
                                </p>
                                <input class="btn" type="submit" value="Invia">    
                            </form>
                        </div>
                <%
                    }
                %>
                <%
                    }else{
                        %>
                <p>Errore: impossibile trovare la discussione richiesta.</p>               
                <%
                    }
                %>
            </div>
        </div>
            <footer>
                <%@include file="/WEB-INF/jspf/footer.jspf" %>
            </footer>
    </body>
</html>
