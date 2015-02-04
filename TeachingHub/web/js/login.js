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
            console.log("Ok");
            updateErrorBox("Prova");
        }
    }
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
    //Invio al server
    x.xmlhttp.send(data);
}

function updateErrorBox(text){
    var errorDiv = document.getElementById("errorbox");
    errorDiv.style.visibility = "visible";
    errorDiv.innerHTML+= "<p>" + text +"</p>"
}

function sendDataToServer(xmlhttp){
    
}