//Variables

var ctxurl = document.getElementById('content').getAttribute('ctx-url');
var userid = document.getElementById('content').getAttribute('userid');
var sectionid = getParameterByName("sectionid");

//functions
function checkAndSendDiscussion(){
    var title = $("input[name=title]").val();
    var desc = $("input[name=description]").val();
    var msg = $("textarea[name=message]").val();
    if(!checkValues(title, desc, msg))
        return false;
    sendNewThreadToServer(userid, sectionid, title, desc, msg);
    return false;
}

function checkValues(title, desc, msg){
//    console.log("Msg:: " + title + desc + msg);
    if(title == undefined || title.length < 4){
        showErrorMsg("Titolo troppo corto.");
        return false;
    }
    if(msg == undefined || msg.length < 5){
        showErrorMsg("Scrivi un messaggio significativo.");
        return false;
    }
    return true;
}

function showErrorMsg(text){
    if(text == "" || text == null){
        viewModel.showErrorMsg(false); //Nascondi il box se non c'è niente da mostrare
        return;
    }
    viewModel.errorMsg(text);
    viewModel.showErrorMsg(true);
}


function sendNewThreadToServer(userid, sectionid, title, description, msg){
    //Porto in xml
    var data = document.implementation.createDocument("", "data", null);
    var useridE = data.createElement("userid");
    useridE.appendChild(data.createTextNode(userid));
    var sectionidE = data.createElement("sectionid");
    sectionidE.appendChild(data.createTextNode(sectionid));
    var titleE = data.createElement("title");
    titleE.appendChild(data.createTextNode(title));
    var descE = data.createElement("description");
    descE.appendChild(data.createTextNode(description));
    var msgE = data.createElement("msg");
    msgE.appendChild(data.createTextNode(msg));
    var contentE = data.createElement("content");
    contentE.appendChild(descE);
    contentE.appendChild(titleE);
    contentE.appendChild(msgE);
    data.documentElement.appendChild(useridE);
    data.documentElement.appendChild(sectionidE);
    data.documentElement.appendChild(contentE);
    
    //Invio
    var x = this;
    //For modern browsers
    if(window.XMLHttpRequest)
        x.xmlhttp = new XMLHttpRequest();
    // lower IE7 versions
    else if(window.ActiveXObject){
        x.xmlhttp = new ActiveObject("Microsoft.XMLHTTP");
    }
    
    //method, ServletName, se è sincrono
    x.xmlhttp.open("POST", "../NewDiscussion", true);
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
                var threadid = $(xmlDoc).find("threadid").text();
                console.log("Thread id "+ threadid + " " + $(successTags).find("threadid"));
                //Forward alla discussione
                updateErrorBox("Messaggio inviato con successo");
                //location.reload();
            }
        }
    };
//    var xmlString = (new XMLSerializer()).serializeToString(data);
//    console.log("log. Data: " + xmlString);
    x.xmlhttp.send(data);
}

function updateErrorBox(text){
    viewModel.errorMsg(text);
    viewModel.showErrorMsg(true);
}

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

//View Mode
var viewModel = {
    showErrorMsg : ko.observable(false), //inizialmente invisibile
    errorMsg : ko.observable("Nessun problema")
};

ko.applyBindings(viewModel);