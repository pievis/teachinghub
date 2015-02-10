
// Here's a custom Knockout binding that makes elements shown/hidden via jQuery's fadeIn()/fadeOut() methods
ko.bindingHandlers.fadeVisible = {
    init: function(element, valueAccessor) {
        // Initially set the element to be instantly visible/hidden depending on the value
        var value = valueAccessor();
        $(element).toggle(ko.unwrap(value)); // Use "unwrapObservable" so we can handle values that may or may not be observable
    },
    update: function(element, valueAccessor) {
        // Whenever the value subsequently changes, slowly fade the element in or out
        var value = valueAccessor();
        ko.unwrap(value) ? $(element).fadeIn() : $(element).fadeOut();
    }
};

function getXmlHttpRequest(actionType) {
    console.log("Dentro la xmlhttpreq")
    var x = this;
    //For modern browsers
    if(window.XMLHttpRequest)
        x.xmlhttp = new XMLHttpRequest();
    // lower IE7 versions
    else if(window.ActiveXObject){
        x.xmlhttp = new ActiveObject("Microsoft.XMLHTTP");
    }

    //method, ServletName, se è asincrono
    x.xmlhttp.open("POST", "../NewMessage", true);
    x.xmlhttp.setRequestHeader('Content-Type', 'text/xml');
    
    //Callback
    x.xmlhttp.onreadystatechange = function (){
        if(x.xmlhttp.readyState == 4){
            var xmlDoc = x.xmlhttp.responseXML;
            var errorTags = xmlDoc.getElementsByTagName("error");
            if(errorTags.length > 0){
                var errorTag = errorTags[0];
                var text = errorTag.childNodes[0].nodeValue;
                updateErrorBox("Problema! "+text);
            }
            var successTags = xmlDoc.getElementsByTagName("success");
            if(successTags.length > 0){
                //ricarica la pagina
                updateErrorBox("Messaggio inviato!!!");
                
            }
        }
    };
    
    console.log("prma della send")
    var data;
    if(actionType=="newMsg") //invia il messaggio
        data = sendDataToServer();
    if(actionType=="subscribe") //dice di essere interessato ai messaggi
        data = askComet();
    x.xmlhttp.send(data);
    console.log("fuori dalla send --> fine");
    return false; // blocks the refresh of the page
}

function sendDataToServer() {
    console.log("dentro la send");
    //create the new xml message
    var data = document.implementation.createDocument("","newMsg", null);
    //nodes in data
    var usrId = data.createElement("userid");
    var sectionId = data.createElement("section");
    var discussionId = data.createElement("iddisc");
    var content = data.createElement("content");
    // content of the nodes
    var txtDisc = data.createTextNode($("#content").attr("discid"));
    var txtSection = data.createTextNode($("#content").attr("sectionid"));
    var txtUser = data.createTextNode($("#content").attr("userid"));
    var txtContent = data.createTextNode(ViewModelDisc.newMsgContent());
    // adds content in the nodes
    usrId.appendChild(txtUser);
    sectionId.appendChild(txtSection);
    discussionId.appendChild(txtDisc);
    content.appendChild(txtContent);
    // add nodes in the document
    data.documentElement.appendChild(usrId);
    data.documentElement.appendChild(sectionId);
    data.documentElement.appendChild(discussionId);
    data.documentElement.appendChild(content);
    return data;
}

// message class
var Message = function(section, disc, user, cont) {
    this.section = section;
    this.disc = disc;
    this.user = user;
    this.content = cont;
};

function updateErrorBox(text){
    ViewModelDisc.errorMsg(text);
    ViewModelDisc.showErrorMsg(1);
}

//ViewModel
var ViewModelDisc = {
    messages : ko.observableArray(),
    newMsgContent : ko.observable(),
    showErrorMsg : ko.observable(false), //inizialmente invisibile
    errorMsg : ko.observable("Nessun problema")
};

ko.applyBindings(ViewModelDisc);