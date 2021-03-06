// Script for manage messages of the current discussion
// After the page has been loaded, an AJAX request is sent to the servlet
//  The client recive a response containing all of the messages stored in the db and load them into the page
// Furthermore, a long polling system starts to open e connection toward the server.
//  Client can receive message (in the form of XML) containing a new message sent by another client

// global (private) variable
var clientid = ""; //client's pseudo id
var xmlhttpComet;
var answer;
var sectionPartialUrl = "/jsp/section.jsp?sectionid=[ID]";
var ctxUrl = $("#content").attr("ctx-url");
var sectionId = $("#content").attr("sectionid");
var discussionId = $("#content").attr("discid");

// message class
var Message = function(autor, cont) {
    this.autor = autor;
    this.content = cont;
    this.lastupdate = {};
    this.avatarPath = "";
    this.hasFile = false;
    this.fileName = "";
    this.fileUrl = "";
    this.autorProfileUrl = "";
    this.setAvatarPath = function(path) {
        avatarPath = ctxUrl + "/multimedia/avatars/" + path;
    }
    this.getAvatarPath = function() {
        return avatarPath;
    }
    this.setFile = function(filename, filepath){
        this.hasFile = true;
        this.fileName = filename;
        this.fileUrl = ctxUrl + "/multimedia/attaches/" + filepath;
//        console.log("FILE SETTED " + filename);
    }
    this.setAutorProfilePath = function(autor){
        var profilePartialUrl = "/jsp/profile.jsp?userid=[USERID]";
        this.autorProfileUrl = ctxUrl + profilePartialUrl.replace("[USERID]", autor);
    }
};

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
            var $xml = $(data);
//            var stringXml = new XMLSerializer().serializeToString(data);  
//            console.log(stringXml);
            //update the viewmodel (and with it, the view)
            updateViewModel($xml, 'msg'); 
            //memorize the clientid
            clientid=$xml.find('clientid').text();
            askNewMsgs();
        }
    );
    getDiscussionInfo(sectionId, discussionId);
});

//Updates the viewmodel with respect of the xml doc parameter
function updateViewModel($xml, tagName){
    //foreach message
    $xml.find(tagName).each(function() {
        //take autor, content and the composed type lastupdate
        var $elem = $(this);
        var content = $elem.find("content").text();
        var autor = $elem.find( "autor:first" ).text();
        content = encodeStringForWeb(content); //Gestisce i newline        
        var msg = new Message(autor, content);
        
        msg.setAutorProfilePath(autor);
        //var profilePartialUrl = "/jsp/profile.jsp?userid=[USERID]";
        //msg.autorProfileUrl = ctxUrl + profilePartialUrl.replace("[USERID]", autor);

        var $lastupdate = $elem.find( "lastupdate" );
        var $datafile = $elem.find("datafile");
        //es: 22/04/2010 11:22:44      
        if($lastupdate.length > 0){
            msg.lastupdate.autor = $lastupdate.find( "autor" ).text();
            var datetimelu = $lastupdate.find( "date" ).text() + " " + $lastupdate.find( "time" ).text();
            msg.lastupdate.datetime = datetimelu;
        }
//        console.log("INFO: "  + autor );
//        console.log("--------------");
          
        if($datafile.length > 0){
            msg.setFile($datafile.find("name").text(), $datafile.find("url").text());
        }
        //ViewModelDisc.messages.push(msg);
        ViewModelDisc.displayAdvancedOptions(true); //animate
        // create the xml document that have to be send to the server
        var data = prepareAvatarRequest(autor);
        var stringXml = new XMLSerializer().serializeToString(data);    
        $.post("../Profile",stringXml,
            function(dataResp) {
                //once data has arrived
                var $xml = $(dataResp);
                var avatar = $xml.find('avatar').text();
                // clojure
                msg.avatarPath = ctxUrl + "/multimedia/avatars/" + avatar;
                ViewModelDisc.messages.push(msg);
                sortMessages("ASC");
            }
        );
    });
}

function sortMessages(order) {
    var cmpf = function(left,right){
        if(order === "DESC")
            return left > right ? -1 : 1;
        else
            return left > right ? 1 : -1;
    };
    var orderf = function(left, right) {
//            console.log( left);
            return left.lastupdate.datetime == right.lastupdate.datetime ? 0 : cmpf(parseDate(left.lastupdate.datetime),parseDate(right.lastupdate.datetime));
        };
    ViewModelDisc.messages.sort(orderf);
//    console.log("SORTED "  + ViewModelDisc.messages.length);
}

// function used for sending a new message
function getXmlHttpRequest() {
    
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
                //Messaggio di success
                updateErrorBox("Messaggio inviato!");
                //Resetta la textarea
//                console.log($(".messagebox textarea"));
                $("textarea#txtarea").val("");
                $("textarea#txtarea").text("");
                //Nascondi l'ErrorBox dopo qualche secondo
                setTimeout(function() {
                        ViewModelDisc.showErrorMsg(0);
                    } ,5000);
            }
        }
    };
    
    //push new message to the server
    var data = sendDataToServer();
    x.xmlhttp.send(data);
    
    return false; // blocks the refresh of the page
}

//prepare the "newMsg" xml document
function sendDataToServer() {
    //console.log("dentro la send");
    //create the new xml message
    var data = document.implementation.createDocument("","newMsg", null);
    //nodes in data
    var usrId = data.createElement("autor");
    var sectionId = data.createElement("section");
    var discussionId = data.createElement("iddisc");
    var clientIdNode = data.createElement("idclient");
    var content = data.createElement("content");
    // content of the nodes
    var txtDisc = data.createTextNode($("#content").attr("discid"));
    var txtSection = data.createTextNode($("#content").attr("sectionid"));
    var txtUser = data.createTextNode($("#content").attr("userid"));
    var txtClientId = data.createTextNode(clientid);
    var txtContent = data.createTextNode(ViewModelDisc.newMsgContent());
    // adds content in the nodes
    usrId.appendChild(txtUser);
    sectionId.appendChild(txtSection);
    discussionId.appendChild(txtDisc);
    clientIdNode.appendChild(txtClientId);
    content.appendChild(txtContent);
    // add nodes in the document
    data.documentElement.appendChild(usrId);
    data.documentElement.appendChild(sectionId);
    data.documentElement.appendChild(discussionId);
    data.documentElement.appendChild(clientIdNode);
    data.documentElement.appendChild(content);
    return data;
}

//prepare the "first time access" xml document, used when client contact the servlet for the first time
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

// create the xml document used to ask the avatar path
function prepareAvatarRequest(autor) {
    //creates the new xml messge
    var data = document.implementation.createDocument("", "get", null);
    // nodes in data
    var userId = data.createElement("userid");
    //content for userId nodes
    var txtUser = data.createTextNode(autor);
    userId.appendChild(txtUser);
    data.documentElement.appendChild(userId);
    return data;
}

// xmlhttpComet, answer is global
function askNewMsgs() {
    var data;
    xmlhttpComet = new XMLHttpRequest();
    xmlhttpComet.open("POST", "../NewMessage", true);
    xmlhttpComet.onreadystatechange=function(){
        if (xmlhttpComet.readyState == 4 && xmlhttpComet.status==200) {                            
            var answer = xmlhttpComet.responseXML;
//            console.log("RISPOSTA: "+answer);
            if(answer != null && answer != undefined){
                var expectedTag = answer.documentElement.tagName;
                if (expectedTag == "newMsg" || expectedTag == "cometmsgs") {
    //                console.log(expectedTag);
                    var $xml = $(answer);
                    updateViewModel($xml, expectedTag); //update the viewmodel (and with it, the view)
                }
            }
            askNewMsgs();
        }
        if(xmlhttpComet.readyState == 4 && (xmlhttpComet.status==500 || xmlhttpComet.status==404)){
            setTimeout(function(){
                var xmlForGreeting = sendGreetings();
                var stringXml = new XMLSerializer().serializeToString(xmlForGreeting);    
                //When the document is ready do a post to the servlet
                $.post("../NewMessage",stringXml,
                    function(data) {
                        //once data has arrived
                        var $xml = $(data);
                        ViewModelDisc.messages.removeAll();
                        updateViewModel($xml, 'msg'); 
                        //memorize the clientid
                        clientid=$xml.find('clientid').text();
                        askNewMsgs();
                    }
                );   
            },5000);
        }
    };
    data = document.implementation.createDocument("", "waitMsg", null); 
    //nodes in data
    var sectionId = data.createElement("section");
    var discussionId = data.createElement("iddisc");
    var clientIdNode = data.createElement("idclient");
    // content of the nodes
    var txtDisc = data.createTextNode($("#content").attr("discid"));
    var txtSection = data.createTextNode($("#content").attr("sectionid"));
    var txtClientId = data.createTextNode(clientid);
    // adds content in the nodes
    sectionId.appendChild(txtSection);
    discussionId.appendChild(txtDisc);
    clientIdNode.appendChild(txtClientId);
    // add nodes in the document
    data.documentElement.appendChild(sectionId);
    data.documentElement.appendChild(discussionId);
    data.documentElement.appendChild(clientIdNode);
    xmlhttpComet.send(data);
    //console.log("Async request sent");
}

// function to ask title and description of a specific discussion
function getDiscussionInfo(sect, disc) {
    var data = document.implementation.createDocument("","getDiscussionInfo", null);
    //nodes in data
    var sectionId = data.createElement("section");
    var discussionId = data.createElement("iddisc");
    // content of the nodes
    var txtSection = data.createTextNode(sect);
    var txtDisc = data.createTextNode(disc);
    // adds content in the nodes
    sectionId.appendChild(txtSection);
    discussionId.appendChild(txtDisc);
    // add nodes in the document
    data.documentElement.appendChild(sectionId);
    data.documentElement.appendChild(discussionId);
    var stringXml = new XMLSerializer().serializeToString(data);
    $.post( "../ManageDiscussions", stringXml,
        function( data ){
            //This is the success function
            var xmlDoc = data;
//            var xmlString = (new XMLSerializer()).serializeToString(xmlDoc);
//            console.log("log. Data: " + xmlString);
            var $xml = $(xmlDoc);
            var hasError = handleIfError($xml);
            if(!hasError) {
                ViewModelDisc.discTitleText($xml.find("title").text());
                ViewModelDisc.discDescriptionText($xml.find("description").text());
            }
                
        }
    ).fail(function(){
        updateErrorBox("Errore nel contattare il server remoto.");
    });
}

function updateErrorBox(text){
    ViewModelDisc.errorMsg(text);
    ViewModelDisc.showErrorMsg(1);
}

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

ko.applyBindings(ViewModelDisc);

//Utils
function encodeStringForWeb(str){
    var newStr = str.replace(/\n/g, "<br />");
    return newStr;
}

//return the milliseconds from date
//specified in the format dd/MM/yyyy HH:mm:ss
function parseDate(dateStr){
    var dateStr0 = dateStr.replace(/\s/g, ""); 
    var value = Date.parseExact(dateStr0, "dd/MM/yyyyHH:mm:ss");
//    console.log("DATA: " + value + " origin "+ dateStr0);
    return value.getTime();
}

//returns true if the message contains an error 
function handleIfError($xml){
    var errorMsg = $xml.find("error").text();
    if(errorMsg == null || errorMsg == undefined || errorMsg == '')
        return false;
    updateErrorBox(errorMsg);
    return true;
}