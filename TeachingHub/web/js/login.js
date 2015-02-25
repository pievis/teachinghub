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
                updateErrorBox("Problema con i dati inseriti in form: "+text);
            }
            var successTags = xmlDoc.getElementsByTagName("success");
            if(successTags.length > 0){
                updateErrorBox("Accesso avvenuto!!!");
//                location.reload();
                //Torna alla pagina precedent
                window.onpopstate = function(event) {    
                    if(event && event.state) {
//                        console.log("EVENTO RELOAD");
                        location.reload();
                    }
                };
                window.history.back();
            }
        }
    };
    
    sendDataToServer(x.xmlhttp);
}

function updateErrorBox(text){
    //qui ora proviamo knockout.js
    /*var errorDiv = document.getElementById("errorbox");
    errorDiv.style.visibility = "visible";
    errorDiv.innerHTML = "<p>" + text +"</p>"*/
    //viewModel.errorMsg = text;
    //viewModel.showMsg = 1;
    viewModel.errorMsg(text);
    viewModel.showMsg(1);
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

var viewModel = {
    showMsg: ko.observable(0), //1 is true
    errorMsg: ko.observable("")
};

ko.applyBindings(viewModel);