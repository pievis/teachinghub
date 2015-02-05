/* 
 * This script is used to retrive and manage threads/discussions
 */

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

var ctxurl = document.getElementById('threadsblock').getAttribute('ctx-url');
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
      var datetime = $datetime.find( "date" ).text() + " " + $datetime.find( "time" ).text();
      var thread = new Thread(id, title, description, autor, datetime);
//      console.log("INFO: "  + id );
//      console.log("INFO: "  + title );
      console.log("INFO: "  + autor );
      console.log("INFO: "  + datetime );
//      console.log("--------------");
//      
      viewModel.threads.push(thread);
    });
//    console.log("log. SORTING");
    //TODO sort
}

//view model with knockout

// Define a Thread class that tracks its own name and descriptions
var Thread = function(id, title, description, autor, datetime) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.autor = autor;
    this.datetime = datetime;
    this.url = ctxurl + discRelUrl + id;
}

var viewModel = {
    threads: ko.observableArray()
};

ko.applyBindings(viewModel);