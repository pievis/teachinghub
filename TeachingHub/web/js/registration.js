/* 
Contains function about form validations:
    Registration
 */

function validateRegistration(user,mail,pass,cpass){
    var error = false;
    
    if(!(pass == cpass))
    {
        viewModelReg.setMsg("I due campi password non corrispondono.");
        error = true;
    }
    if(pass.length < 5)
    {
        viewModelReg.setMsg("Password troppo corta");
        error = true;
    }
    if(user.length < 4)
    {
        viewModelReg.setMsg("Username troppo corto");
        error = true;
    }
    //TODO: add check on email
    if(!error){
        viewModelReg.showMsg(0);
        //getXmlHttpRequest();
        return true; //Tutto ok, invia i dati
    }
    else{
        return false;
    }
}

function getXmlHttpRequest(){
    
    //Pre-emptive check
    var contentUsr = $("form[name='regForm'] input[name='user']").val();
    var contentPass = $("form[name='regForm'] input[name='pass']").val();
    var confirmPass = $("form[name='regForm'] input[name='cpass']").val();
    var contentName = $("form[name='regForm'] input[name='nome']").val();
    var contentSurname = $("form[name='regForm'] input[name='cognome']").val();
    var contentClass = $("form[name='regForm'] input[name='classe']").val();
    var contentEmail = $("form[name='regForm'] input[name='email']").val();
    var canGo = validateRegistration(contentUsr, contentEmail, contentPass, confirmPass);
    if(canGo) {
        var x = this;
        //For modern browsers
        if(window.XMLHttpRequest)
            x.xmlhttp = new XMLHttpRequest();
        // lower IE7 versions
        else if(window.ActiveXObject){
            x.xmlhttp = new ActiveObject("Microsoft.XMLHTTP");
        }

        //method, ServletName, se è asincrono
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
                    
                    // gli diciamo che verrà reindirizzato alla pagina
                    updateErrorBox("Registrazione avvenuta! Sarai reindirizzato alla pagina principale in 5 secondi");
                    
                    //ricarica la pagina
                    var ctxPath = $("#content").attr("ctx-url");
                    console.log(ctxPath);
                    setTimeout(function() {
                        location.replace(ctxPath+"/index.jsp"); 
                    } ,5000);
                    //location.replace(ctxPath+"/index.jsp");
                }
            }
        };
        sendDataToServer(x.xmlhttp, contentUsr, contentPass, contentName, contentSurname, contentClass, contentEmail);
    }
}

function updateErrorBox(text) {
    viewModelReg.setMsg(text);
}

function sendDataToServer(xmlhttp, contentUsr, contentPass, contentName, contentSurname, contentClass, contentEmail){
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
    
//    var contentUsr = $("form[name='regForm'] input[name='user']").val();
//    var contentPass = $("form[name='regForm'] input[name='pass']").val();
//    var contentName = $("form[name='regForm'] input[name='nome']").val();
//    var contentSurname = $("form[name='regForm'] input[name='cognome']").val();
//    var contentClass = $("form[name='regForm'] input[name='classe']").val();
//    var contentEmail = $("form[name='regForm'] input[name='email']").val();
    //add the nodes to document
    usrId.appendChild(data.createTextNode(contentUsr));
    passId.appendChild(data.createTextNode(contentPass));
    nameId.appendChild(data.createTextNode(contentName));
    surnameId.appendChild(data.createTextNode(contentSurname));
    classId.appendChild(data.createTextNode(contentClass));
    emailId.appendChild(data.createTextNode(contentEmail));
    data.documentElement.appendChild(usrId);
    data.documentElement.appendChild(passId);
    data.documentElement.appendChild(nameId);
    data.documentElement.appendChild(surnameId);
    data.documentElement.appendChild(classId);
    data.documentElement.appendChild(emailId);
    var xmlString = (new XMLSerializer()).serializeToString(data);
    //console.log(xmlString);
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