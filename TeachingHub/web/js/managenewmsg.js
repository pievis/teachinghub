/*
 * Manages file for new_question & new_page
*/

//Variables

var ctxurl = document.getElementById('content').getAttribute('ctx-url');
var userid = document.getElementById('content').getAttribute('userid');
var sectionid = getParameterByName("sectionid");
var threadUrl = "/jsp/discussion.jsp?id=[ID]&sectionid=[SECTION]";
var pageUrl = "/jsp/page.jsp?id=[ID]&sectionid=[SECTION]";
var returnedFiles; //this is an xml element/node that stores the name of the uploaded files on the server
//it's null if no upload happened

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
            var xmlString = (new XMLSerializer()).serializeToString(xmlDoc);
//            console.log("log. Data: " + xmlString);
            var errorTags = xmlDoc.getElementsByTagName("error");
            if(errorTags.length > 0){
                var errorTag = errorTags[0];
                var text = errorTag.childNodes[0].nodeValue;
                updateErrorBox("Problema con i dati inseriti in form: "+text);
            }
            var successTags = xmlDoc.getElementsByTagName("success");
            if(successTags.length > 0){
                var threadid = $(successTags).find("threadid").text();
//                console.log("Thread id: " + threadid + " ");
                //Forward alla discussione
                updateErrorBox("Messaggio inviato con successo");
                forwardToThreadPage(threadid, sectionid);
            }
        }
    };
//    var xmlString = (new XMLSerializer()).serializeToString(data);
//    console.log("log. Data: " + xmlString);
    x.xmlhttp.send(data);
}

function forwardToThreadPage(threadid, sectionid){
    var url = ctxurl + threadUrl.replace("[ID]", threadid).replace("[SECTION]", sectionid);
    setTimeout(function() {
                        location.replace(url); 
                    } ,1000);
}

function forwardToNewPage(threadid, sectionid){
    var url = ctxurl + pageUrl.replace("[ID]", threadid).replace("[SECTION]", sectionid);
    setTimeout(function() {
                        location.replace(url); 
                    } ,1000);
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

//Carica i file allegati sul server, poi invia i dati sulla discussione/pagina
function uploadFilesAndSendInfo(input, callingfn){
    //show loadere and hide submit
    viewModel.showSubmit(false);
    viewModel.showLoader(true);
    //
    var dataf = new FormData(); //form data per l'invio dei file
    //key, value
    dataf.append("filetype", "attachement");
    //prendi i file dall'input
    for(var i = 0; i < input.files.length; i++){
        dataf.append("file"+i, input.files[i]);
//        console.log("sel file" + input.files[0]);
    }
    
    $.ajax({
        url: '../UploadFile',
        type: 'POST',
        data: dataf,
        cache: false,
        processData: false, // Don't process the files
        contentType: false, // Set content type to false as jQuery will tell the server its a query string request
        success: function(data, textStatus, jqXHR)
        {
            $xml = $(data);
            var errorTags = $xml.find("error");
            if(errorTags.length == 0)
            {
                // tutto ok,
                // estrapolo il nome dei files caricati
                $xml = $(data);
                returnedFiles = $xml.find("file")
                // invio i dati della form
                if(callingfn == "newpage")
                   sendNewPageToServer(userid, sectionid, title, desc, msg); //deve includere i file salvati
                else
                    sendNewThreadToServer();
            }
            else
            {
                // Errori
                viewModel.showSubmit(true);
                viewModel.showLoader(false);
                showErrorMsg(errorTags.find("error").text());
                console.log('ERRORS: ' + errorTags.find("error").text());
            }
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            //Errori
            console.log('ERRORS: ' + textStatus);
            showErrorMsg("Internal server error: "+textStatus);
            //Nascondi il loader
            viewModel.showSubmit(true);
            viewModel.showLoader(false);
        }
    });
}

////////////////////////////////////////////////////////////////////////////////
//new_page.jsp

function checkValuesPage(title, desc, msg){
//    console.log("Msg:: " + title + desc + msg);
    if(title == undefined || title.length < 4){
        showErrorMsg("Nome pagina troppo corto");
        return false;
    }
    if(msg == undefined || msg.length < 10){
        showErrorMsg("Il contenuto della pagina deve essere significativo.");
        return false;
    }
    return true;
}

var title;
var desc;
var msg;

function checkAndSendPage(){
    title = $("input[name=pagename]").val();
    desc = $("input[name=description]").val();
    msg = $("textarea[name=pagecontent]").val();
    if(!checkValuesPage(title, desc, msg))
        return false;
    var input = $('#selFiles')[0];
    if(input.files.length == 0) //se non hai selezionato file, semplicemente invia
        sendNewPageToServer(userid, sectionid, title, desc, msg);
    else
        uploadFilesAndSendInfo(input, "newpage"); //altrimenti prima esegui l'upload dei file
    return false;
}

function sendNewPageToServer(userid, sectionid, title, description, msg){
    //Porto in xml
    var data = document.implementation.createDocument("", "data", null);
    var useridE = data.createElement("userid");
    useridE.appendChild(data.createTextNode(userid));
    var sectionidE = data.createElement("sectionid");
    sectionidE.appendChild(data.createTextNode(sectionid));
    var titleE = data.createElement("pagename");
    titleE.appendChild(data.createTextNode(title));
    var descE = data.createElement("description");
    descE.appendChild(data.createTextNode(description));
    var msgE = data.createElement("msg");
//    msg = encodeStringForWeb(msg);
    msgE.appendChild(data.createTextNode(msg));
    var contentE = data.createElement("content");
    var filesE = data.createElement("files");
    //manage uploaded files
    if(returnedFiles != null && returnedFiles != undefined)
    {
        returnedFiles.each(function () {
           filesE.appendChild(this); //aggiunge il file
        });
    }
    contentE.appendChild(filesE);
    contentE.appendChild(descE);
    contentE.appendChild(titleE);
    contentE.appendChild(msgE);
    data.documentElement.appendChild(useridE);
    data.documentElement.appendChild(sectionidE);
    data.documentElement.appendChild(contentE);
    
//    var xmlString = (new XMLSerializer()).serializeToString(data);
//    console.log("log. SentData: " + xmlString);
    
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
    x.xmlhttp.open("POST", "../NewPage", true);
    x.xmlhttp.setRequestHeader('Content-Type', 'text/xml');
    //Callback
    x.xmlhttp.onreadystatechange = function (){
        if(x.xmlhttp.readyState == 4){
            var xmlDoc = x.xmlhttp.responseXML;
//            var xmlString = (new XMLSerializer()).serializeToString(xmlDoc);
//            console.log("log. Data: " + xmlString);
            var errorTags = xmlDoc.getElementsByTagName("error");
            if(errorTags.length > 0){
                var errorTag = errorTags[0];
                var text = errorTag.childNodes[0].nodeValue;
                updateErrorBox("Problema con i dati inseriti in form: "+text);
            }
            var successTags = xmlDoc.getElementsByTagName("success");
            if(successTags.length > 0){
                var pageid = $(successTags).find("pageid").text();
//                console.log("Thread id: " + threadid + " ");
                //Forward alla discussione
                updateErrorBox("Messaggio inviato con successo");
                forwardToNewPage(pageid, sectionid);
            }
        }
    };
//    var xmlString = (new XMLSerializer()).serializeToString(data);
//    console.log("log. Data: " + xmlString);
    x.xmlhttp.send(data);
}

//View Mode
var viewModel = {
    showErrorMsg : ko.observable(false), //inizialmente invisibile
    errorMsg : ko.observable("Nessun problema"),
    showSubmit: ko.observable(true),
    showLoader: ko.observable(false)
};

ko.applyBindings(viewModel);

//Utils

function encodeStringForWeb(str){
    var newStr = str.replace(/\n/g, "<br />");
    return newStr;
}