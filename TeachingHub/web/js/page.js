/* 
 * File che gestirà la visualizzazione di una pagina scritta da un docente
 */

var sectionPartialUrl = "/jsp/section.jsp?sectionid=[ID]";
var ctxUrl = $("#content").attr("ctx-url");
var sectionId = $("#content").attr("sectionid");
var pageId = getParameterByName("id");
var profilePartialUrl = "/jsp/profile.jsp?userid=[USERID]"

$(function() {
    //setup section url
    viewModel.sectionUrl(ctxUrl + sectionPartialUrl.replace("[ID]", sectionId));
    viewModel.sectionTxt(sectionId);
    
    var requestData = "<getpage><id>"+pageId+"</id><section>"+sectionId+"</section></getpage>";
    //When the document is ready do a post to the servlet
    $.post("../GetPage",requestData,
        function(data) {
            //once data has arrived
            var $xml = $(data);
            //update the viewmodel (and with it, the view)
            updateViewModel($xml); 
        }
    );
});

function updateViewModel($xml){
    var $page = $xml.find("page");
    var title = $page.find("title").text();
    var description = $page.find("description").text();
    var msg = $page.find("msg").text();
    var autor = $page.find("autor").text();
    msg = encodeStringForWeb(msg);
//    console.log(".log INFO: " + title + " " + autor);
    viewModel.discTitleText(title);
    viewModel.discDescriptionText(description);
//    viewModel.autor(autor);
    viewModel.content(msg);
    viewModel.autorName(autor);
    var profileUrl = ctxUrl + profilePartialUrl.replace("[USERID]", autor);
    viewModel.autorProfileUrl(profileUrl);
    
    //aggiungi i files al viewmodel
    $page.find("datafile").each(function (){
        var $elem = $(this);
        var filename = $elem.find("name").text();
        var filepath = $elem.find("url").text();
//        console.log("FILE: " + filename + " " + filepath);
        var df = new DataFile(filename, filepath);
        viewModel.datafiles.push(df);
    });
}

//ViewModel
var DataFile = function(name, url) {
    this.name = name;
    this.url = ctxUrl + "/multimedia/attaches/" + url;
};

var viewModel = {
    datafiles: ko.observableArray(),
    newMsgContent : ko.observable(),
    errorMsg : ko.observable(""),
    //Invisible at the beginning
    showErrorMsg : ko.observable(false),
    discTitleText: ko.observable("Titolo"),
    discDescriptionText: ko.observable("Descrizione"),
    sectionUrl : ko.observable(),
    sectionTxt : ko.observable(),
    autor : ko.observable(),
    content: ko.observable(),
    autorProfileUrl: ko.observable(),
    autorName: ko.observable()
};

ko.applyBindings(viewModel);


//Utils

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function encodeStringForWeb(str){
    var newStr = str.replace(/\n/g, "<br />");
    return newStr;
}