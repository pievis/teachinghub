//
var ctxurl = document.getElementById('content').getAttribute('ctx-url');
var sectionUrl = "jsp/section.jsp?sectionid=";
//

$(function() {
    //When the document is ready
    //do a get to the servlet
    $.get( "GetSections", null,
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

function updateViewModel($xml){
    $xml.find("section").each(function(){
       var $elem = $(this);
       var name = $elem.find("name").text();
       var description = $elem.find("description").text();
       var icon = ctxurl + $elem.find("icon").text();
       var url = sectionUrl + name;
//       console.log(name + " " + description);
       viewModel.sections.push(
               new Section(name, description, icon, url));
    });
    viewModel.displayAdvancedOptions(true);
}

//View model
//
//simple section class
var Section = function(name, description, icon, url) {
    this.name = name;
    this.description = description;
    this.icon = icon;
    this.url = url;
}

var viewModel = {
    sections: ko.observableArray(),
    displayAdvancedOptions : ko.observable(false) //Inizialmente invisibile
};

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

ko.applyBindings(viewModel);
