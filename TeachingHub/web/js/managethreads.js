/* 
 * This script is used to retrive and manage threads/discussions
 */

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

//for the urls
var ctxurl = document.getElementById('threadsblock').getAttribute('ctx-url');
var sectionid = document.getElementById('threadsblock').getAttribute('sectionid');
var newQuestionUrl = "/jsp/new_question.jsp?sectionid=";
var newPageUrl = "/jsp/new_page.jsp?sectionid=";
var discRelUrl = "/jsp/discussion.jsp?id=";
var sectionPartialUrl = "/jsp/section.jsp?sectionid=[ID]";
var pageUrl = "/jsp/page.jsp?id=[ID]&sectionid=[SECTION]";

$(function() {
    //When the document is ready
    var sectionid = getParameterByName("sectionid");
    //get information from the server
    getThreads();
    getPages();
});

function getThreads(){
    var data = document.implementation.createDocument("","getDiscussion", null);
    //nodes in data
    var sectionId = data.createElement("section");
    // content of the nodes
    var txtSection = data.createTextNode(sectionid);
    // adds content in the nodes
    sectionId.appendChild(txtSection);
    // add nodes in the document
    data.documentElement.appendChild(sectionId);
    var stringXml = new XMLSerializer().serializeToString(data);
    $.post( "../ManageDiscussions", stringXml,
        function( data ){
            //This is the success function
            var xmlDoc = data;
//            var xmlString = (new XMLSerializer()).serializeToString(xmlDoc);
//            console.log("log. Data: " + xmlString);
            var $xml = $(xmlDoc);
            //TODO handle errors
            updateViewModelThreads($xml);
        }
    );
        }

function getPages(){
    var requestData = "<getpages><section>"+sectionid+"</section></getpages>";
    $.post( "../GetPage", requestData,
        function( data ){
            //This is the success function
            var xmlDoc = data;
//            console.log(data);
//            var xmlString = (new XMLSerializer()).serializeToString(xmlDoc);
//            console.log("log. Data: " + xmlString);
            var $xml = $(xmlDoc);
            //TODO handle errors
            updateViewModelPages($xml);
        }
    );
}

//Updates the viewmodel with respect of the xml doc parameter
function updateViewModelThreads($xml){
    $xml.find('thread').each(function() {
      //foreach thread element
      var $elem = $(this);
      var id = $elem.find( "id" ).text();
      var title = $elem.find( "title" ).text();
      var description = $elem.find( "description" ).text();
      var autor = $elem.find( "autor:first" ).text();
      var $datetime = $elem.find( "datetime:first" );
      var $lastupdate = $elem.find( "lastupdate" );
      //es: 22/04/2010 11:22:44
      var datetime = $datetime.find( "date" ).text() + " " + $datetime.find( "time" ).text();
      var thread = new Thread(id, title, description, autor, datetime);
      if($lastupdate != null){
        thread.lastupdate.autor = $lastupdate.find( "autor" ).text();
        var datetimelu = $lastupdate.find( "date" ).text() + " " + $lastupdate.find( "time" ).text();
        thread.lastupdate.datetime = datetimelu;
      }
//      console.log("INFO: "  + id );
//      console.log("INFO: "  + title );
//      console.log("INFO: "  + autor );
//      console.log("INFO: "  + datetime );
//      console.log("--------------");
//      
      viewModel.threads.push(thread);
      viewModel.displayAdvancedOptions(true); //animate
    });
//    console.log("log. SORTING");
    sortArrayThreads("DESC", "lastupdate");
}

function sortArrayThreads(order, attribute){
    var orderf;
    var cmpf = function(left,right){
        if(order === "DESC")
            return left > right ? -1 : 1;
        else
            return left > right ? 1 : -1;
    };
    if(attribute == "creationdate"){
        orderf = function(left, right) {
            return left.datetime == right.datetime ? 0 : cmpf(parseDate(left.datetime),parseDate(right.datetime));
        };
    }
    if(attribute == "id"){
        orderf = function(left, right) {
            return left.id == right.id ? 0 : cmpf(left.id,right.id);
        };
    }
    if(attribute == "autor"){
        orderf = function(left, right) {
            return left.autor == right.autor ? 0 : cmpf(left.autor,right.autor);
        };
    }
    if(attribute == "lastupdate"){
        orderf = function(left, right) {
            return left.lastupdate.datetime == right.lastupdate.datetime ? 0 : cmpf(parseDate(left.lastupdate.datetime),parseDate(right.lastupdate.datetime));
        };
    }
    viewModel.threads.sort(orderf);
//    console.log("log. SORTING " + order + " " + attribute);
}

function updateViewModelPages($xml){
    $xml.find("page").each(function (){
       //for each page tag found
       var $elem = $(this);
       var id = $elem.find("id").text();
       var title = $elem.find("title").text();
       var autor = $elem.find("autor").text();
//       console.log("id" + id +
//                "title" + title);
       //aggiungo la pagina al viewmodel
       var purl = pageUrl.replace("[ID]", id).replace("[SECTION]", sectionid);
       var url = ctxurl + purl;
       var p = new Page(title, autor, url);
       viewModel.pages.push(p);
    });
}

//return the milliseconds from date
//specified in the format dd/MM/yyyy HH:mm:ss
function parseDate(dateStr){
    var dateStr0 = dateStr.replace(/\s/g, ""); 
    var value = Date.parseExact(dateStr0, "dd/MM/yyyyHH:mm:ss");
//    console.log("DATA: " + value.getTime() + " origin "+ dateStr0);
    return value.getTime();
}

//view model with knockout

// Define a Thread class that tracks its own name and descriptions
var Thread = function(id, title, description, autor, datetime) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.autor = autor;
    this.datetime = datetime;
    //discussion.jsp?id=0&sectionid=Matematica
    this.url = ctxurl + discRelUrl + id + "&sectionid=" + sectionid;
    //other
    this.lastupdate = {};
}

var Page = function (name, autor, url){
    this.name = name;
    this.autor = autor;
    this.url = url;
}

var viewModel = {
    threads: ko.observableArray(),
    displayAdvancedOptions : ko.observable(false), //Inizialmente invisibile
    selectedAtr: ko.observable(),
    atrs: ["lastupdate", "creationdate", "autor"],
    atrToText: function(item){
        if(item == "creationdate")
            return "Data Creazione";
        if(item == "autor")
            return "Autore";
        if(item == "lastupdate")
            return "Ultimo Aggiornamento";
    },
    selectedOrd: ko.observable(),
    ords: ["DESC", "ASC"],
    ordsToText: function(item){
        if(item == "DESC")
            return "Descrescente";
        if(item == "ASC")
            return "Crescente";
    },
    pagesVisible: ko.observable(true), //pagine inizialmente visibili
    showHidePages: function(){
        var value = this.pagesVisible();
        this.pagesVisible(!value);
//        console.log("CLICKED " + value);
    },
    fowardToNewQuestion: function(){
        window.location.href = ctxurl + newQuestionUrl + sectionid;
    },
    fowardToNewPage: function(){
        window.location.href = ctxurl + newPageUrl + sectionid;
    },
    pages : ko.observableArray()
};

// Here's a custom Knockout binding that makes elements shown/hidden via jQuery's fadeIn()/fadeOut() methods
// Could be stored in a separate utility library
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

ko.applyBindings(viewModel);

//Eventi di riordinamento per le opzioni dei thread
viewModel.selectedAtr.subscribe(function(atrValue){
    var selOrder = viewModel.selectedOrd();
//    console.log("SEL ORDER " + selOrder);
    sortArrayThreads(selOrder,atrValue);
});

viewModel.selectedOrd.subscribe(function(ordValue){
    var selAttribute = viewModel.selectedAtr();
//    console.log("SEL ORDER " + selOrder);
    sortArrayThreads(ordValue,selAttribute);
});
