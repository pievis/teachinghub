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
var discRelUrl = "/jsp/discussion.jsp?id="

$(function() {
    //When the document is ready
    var sectionid = getParameterByName("sectionid");
    //do a get to the servlet
    $.get( "../GetThreads", { section: sectionid },
        function( data ){
            //This is the success function
            var xmlDoc = data;
//            var xmlString = (new XMLSerializer()).serializeToString(xmlDoc);
//            console.log("log. Data: " + xmlString);
            var $xml = $(xmlDoc);
            //TODO handle errors
            updateViewModel($xml);
        }
    );
});

//Updates the viewmodel with respect of the xml doc parameter
function updateViewModel($xml){
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
        thread.lastupdate.autor = $lastupdate.find( "autor" );
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
    sortArrayThreads("DESC", "creationdate");
}

function sortArrayThreads(order, attribute){
    var orderf;
    var cmpf = function(left,right){
        if(order === "DESC")
            return left.datetime > right.datetime ? -1 : 1;
        else if (order === "ASC")
            return left.datetime > right.datetime ? 1 : -1;
    };
    if(attribute == "creationdate"){
        orderf = function(left, right) {
            return left.datetime == right.datetime ? 0 : cmpf(left.datetime,right.datetime);
        };
    }
    if(attribute == "id"){
        orderf = function(left, right) {
            return left.id == right.id ? 0 : cmpf(left.id,right.id);
        };
    }
    if(attribute == "lastupdate"){
        orderf = function(left, right) {
            return left.lastupdate.datetime == right.lastupdate.datetime ? 0 : cmpf(left.lastupdate.datetime,right.lastupdate.datetime);
        };
    }
    viewModel.threads.sort(orderf);
    console.log("log. SORTING " + order + " " + attribute);
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

var viewModel = {
    threads: ko.observableArray(),
    displayAdvancedOptions : ko.observable(false) //Inizialmente invisibile
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

