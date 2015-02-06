/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    
    //TODO: Pre-emptive check on password

    //method, ServletName, se è sincrono
    x.xmlhttp.open("POST", "../Registration", true);
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
                updateErrorBox("Registrazione avvenuta!");
                // gli diciamo che verrà reindirizzato alla pagina?
                //location.reload();
            }
        }
    };
    
    sendDataToServer(x.xmlhttp);
}

function updateErrorBox(text) {
    viewModel.setMsg(text);
}

function sendDataToServer(xmlhttp){
    //create the data to send
    var data = document.implementation.createDocument("", "registration", null);
    //Create the main nodes of xml file
    var usrId = data.createElement("userid");
    var passId = data.createElement("password");
    var nameId = data.createElement("name");
    var surnameId = data.createElement("surname");
    var classId = data.createElement("class");
    var emailId = data.createElement("email");
    //insert text within the nodes
    console.log($("form[name='regForm'] input[name='user']").val());
    var contentUsr = data.createTextNode(document.getElementByName("user").value);
    var contentPass = data.createTextNode(document.getElementByName("pass").value);
    var contentName = data.createTextNode(document.getElementByName("nome").value);
    var contentSurname = data.createTextNode(document.getElementByName("cognome").value);
    var contentClass = data.createTextNode(document.getElementByName("classe").value);
    var contentEmail = data.createTextNode(document.getElementByName("email").value);
    //add the nodes to document
    usrId.appendChild(contentUsr);
    passId.appendChild(contentPass);
    nameId.appendChild(contentName);
    surnameId.appendChild(contentSurname);
    classId.appendChild(contentClass);
    emailId.appendChild(contentEmail);
    data.documentElement.appendChild(usrId);
    data.documentElement.appendChild(passId);
    data.documentElement.appendChild(nameId);
    data.documentElement.appendChild(surnameId);
    data.documentElement.appendChild(classId);
    data.documentElement.appendChild(emailId);
    var xmlString = (new XMLSerializer()).serializeToString(data);
    console.log(xmlString);
//    updateErrorBox(data);
    //Invio al server
    xmlhttp.send(data);
}

var viewModelReg = {
    showMsg: ko.observable(0), //1 is true
    errorMsg: ko.observable(""),    
    setMsg : function(text) {
        this.showMsg(1);
        this.errorMsg(text);
    }
};

ko.applyBindings(viewModelReg);