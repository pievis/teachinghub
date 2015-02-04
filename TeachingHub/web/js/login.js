/*
 * script che si occupa di gestire il form di login
 * e i messaggi di errori ricevuti dal server.
*/

function getXmlHttpRequest(){
    console.log("CIAO");
    var x = this;
    //For modern browsers
    if(window.XMLHttpRequest)
        x.xmlhttp = new XMLHttpRequest();
    // lower IE7 versions
    else if(window.ActiveXObject){
        x.xmlhttp = new ActiveObject("Microsoft.XMLHTTP");
    }
    
    //method, ServletName, se è sincrono
    x.xmlhttp.open("POST", "Login", true);
    x.xmlhttp.setRequestHeader('Content-Type', 'text/xml');
    //Callback
    x.xmlhttp.onreadystatechange = function (){
        if(x.xmlhttp.readyState == 4){
            console.log("Ok");
            updateErrorBox("Prova");
        }
    }
    //Invio al server
    sendDataToServer(x.xmlhttp);
}

function updateErrorBox(text){
    var errorDiv = document.getElementById("errorbox");
    errorDiv.style.visibility = "visible";
    errorDiv.innerHTML+= "<p>" + text +"</p>"
}

function sendDataToServer(xmlhttp){
    
}