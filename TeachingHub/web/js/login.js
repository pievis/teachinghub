/*
 * script che si occupa di gestire il form di login
 * e i messaggi di errori ricevuti dal server.
*/

function getXmlHttpRequest(){
    
    var x = this;
    //For modern browsers
    if(window.XMLHttpRequest)
        x.xmlhttp = new XMLHttpRequest();
    // lower IE7 versions
    else if(window.ActiveXObject){
        x.xmlhttp = new ActiveObject("Microsoft.XMLHTTP");
    }

    //method, ServletName, se è sincrono
    x.xmlhttp.open("POST", "../Login", true);
    x.xmlhttp.setRequestHeader('Content-Type', 'text/xml');
    //Callback
    x.xmlhttp.onreadystatechange = function (){
        if(x.xmlhttp.readyState == 4){
            var xmlDoc = x.xmlhttp.responseXML;
            var errorTags = xmlDoc.getElementsByTagName("error");
            if(errorTags.length > 0){
                var errorTag = errorTags[0];
                var text = errorTag.childNodes[0].nodeValue;
                updateErrorBox(text);
            }
            var successTags = xmlDoc.getElementsByTagName("success");
            if(successTags.length > 0){
                //ricarica la pagina
                updateErrorBox("Accesso avvenuto!");
                location.reload();
            }
        }
    };
    
    sendDataToServer(x.xmlhttp);
}

function updateErrorBox(text){
    var errorDiv = document.getElementById("errorbox");
    errorDiv.style.visibility = "visible";
    errorDiv.innerHTML = "<p>" + text +"</p>"
}

function sendDataToServer(xmlhttp){
    //create the data to send
    var data = document.implementation.createDocument("", "login", null);
    var usrId = data.createElement("userid");
    var passId = data.createElement("password");
    var contentUsr = data.createTextNode(document.getElementById("txtUser").value);
    var contentPass = data.createTextNode(document.getElementById("txtPass").value);
    usrId.appendChild(contentUsr);
    passId.appendChild(contentPass);
    data.documentElement.appendChild(usrId);
    data.documentElement.appendChild(passId);
//    updateErrorBox(data);
    //Invio al server
    xmlhttp.send(data);
}