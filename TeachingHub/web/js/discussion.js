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

var sectionPartialUrl = "/jsp/section.jsp?sectionid=[ID]";
var ctxUrl = $("#content").attr("ctx-url");
var sectionId = $("#content").attr("sectionid");

$(function() {
    //setup section url
    ViewModelDisc.sectionUrl(ctxUrl + sectionPartialUrl.replace("[ID]", sectionId));
    ViewModelDisc.sectionTxt(sectionId);
    //do the greeting
    var xmlForGreeting = sendGreetings();
    var stringXml = new XMLSerializer().serializeToString(xmlForGreeting);    
    //When the document is ready do a post to the servlet
    $.post("../NewMessage",stringXml,
        function(data) {
            //once data has arrived
            console.log(data);
            var $xml = $(data);
            updateViewModel($xml); //update the viewmodel (and with it, the view)
        }
    );
});

//Updates the viewmodel with respect of the xml doc parameter
function updateViewModel($xml){
    //foreach message
    $xml.find('msg').each(function() {
      //take autor, content and the composed type lastupdate
      var $elem = $(this);
      var content = $elem.find("content").text();
      var autor = $elem.find( "autor:first" ).text();
      var msg = new Message(autor, content);
      var $lastupdate = $elem.find( "lastupdate" );
      //es: 22/04/2010 11:22:44      
      if($lastupdate != null){
        msg.lastupdate.autor = $lastupdate.find( "autor" ).text();
        var datetimelu = $lastupdate.find( "date" ).text() + " " + $lastupdate.find( "time" ).text();
        msg.lastupdate.datetime = datetimelu;
      }
//      console.log("INFO: "  + id );
//      console.log("INFO: "  + title );
//      console.log("INFO: "  + autor );
//      console.log("INFO: "  + datetime );
//      console.log("--------------");
//      
      ViewModelDisc.messages.push(msg);
      ViewModelDisc.displayAdvancedOptions(true); //animate
    });
//    console.log("log. SORTING");
    //sortArrayThreads("DESC", "lastupdate");
}

function getXmlHttpRequest(actionType) {
    //console.log("Dentro la xmlhttpreq")
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
    
    //console.log("prma della send")
    var data;
    if(actionType=="newMsg") //send the new message to the server
        data = sendDataToServer();
    if(actionType=="subscribe") //require push operation for new message
        data = askComet();
    x.xmlhttp.send(data);
    //console.log("fuori dalla send --> fine");
    return false; // blocks the refresh of the page
}

function sendDataToServer() {
    //console.log("dentro la send");
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

function sendGreetings() {
    //create the new xml message
    var data = document.implementation.createDocument("","firstTimeAcces", null);
    //nodes in data
    var sectionId = data.createElement("section");
    var discussionId = data.createElement("iddisc");
    // content of the nodes
    var txtDisc = data.createTextNode($("#content").attr("discid"));
    var txtSection = data.createTextNode($("#content").attr("sectionid"));
    // adds content in the nodes
    sectionId.appendChild(txtSection);
    discussionId.appendChild(txtDisc);
    // add nodes in the document
    data.documentElement.appendChild(sectionId);
    data.documentElement.appendChild(discussionId);
    return data;
}

// message class
var Message = function(autor, cont) {
    this.autor = autor;
    this.content = cont;
    this.lastupdate = {};
};
//    XXX private String id;
//    private String content;
//    private Lastupdate lastupdate;
//    private String autor;
//    MISSING private Datafiles datafiles;

function updateErrorBox(text){
    ViewModelDisc.errorMsg(text);
    ViewModelDisc.showErrorMsg(1);
}

//ViewModel
var ViewModelDisc = {
    messages : ko.observableArray(),
    newMsgContent : ko.observable(),
    errorMsg : ko.observable("Nessun problema"),
    //Invisible at the beginning
    showErrorMsg : ko.observable(false), 
    displayAdvancedOptions : ko.observable(false),
    discTitleText: ko.observable("Titolo"),
    discDescriptionText: ko.observable("Descrizione"),
    sectionUrl : ko.observable(),
    sectionTxt : ko.observable()
};

ko.applyBindings(ViewModelDisc);