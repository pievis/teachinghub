/*
 * Classe usata per la visualizzazione e modifica del profilo di un utente
*/

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

//
var ctxurl = document.getElementById('content').getAttribute('ctx-url');
var sectionUrl = "jsp/section.jsp?sectionid=";
var avatarPath = "/multimedia/avatars/";
//

$(function() {
    //When the document is ready
    //do a get to the servlet
    var id = getParameterByName("userid");
    $.get( "../GetProfile", {userid: id},
        function( data ){
            //This is the success function
            var xmlDoc = data;
//            var xmlString = (new XMLSerializer()).serializeToString(xmlDoc);
//            console.log("log. Data: " + xmlString);
            var $xml = $(xmlDoc);
            updateViewModel($xml);
        }
    );
});

function updateViewModel($xml){
    var isStudent = false;
    if($xml.find("student").length) //se esiste il nodo student
        isStudent = true;
    var userid = $xml.find("id").text();
    var email = $xml.find("email").text();
    var firstname = $xml.find("firstname").text();
    var lastname = $xml.find("lastname").text();
    var avatar = $xml.find("avatar").text();
    var firstname = $xml.find("firstname").text();
    if(isStudent){
        var classe = $xml.find("classe").text();
        var hobby = $xml.find("hobby").text();
    }
//    console.log("isStudent: " + isStudent + " id " + userid + $xml.find("student"));
    setUserView(userid, email, firstname, lastname, avatar);
}

function setUserView(userid, email, firstname, lastname, avatar){
    viewModel.username(userid);
    viewModel.firstname(firstname);
    viewModel.lastname(lastname);
    viewModel.avatarUrl(ctxurl + avatarPath + avatar);
    viewModel.email(email);
}

//ViewModel
var viewModel = {
    showSubjects : ko.observable(false),
    showHobby : ko.observable(false),
    showErrorMsg: ko.observable(false),
    showClasse: ko.observable(false),
    errorMsg : ko.observable("Errore"),
    firstname : ko.observable(""),
    classe : ko.observable(""),
    lastname : ko.observable(""),
    username : ko.observable(""),
    email : ko.observable(""),
    hobby : ko.observable(""),
    subjects : ko.observable(""), //E' una stringa con le materie di fila
    avatarUrl : ko.observable()
};

ko.applyBindings(viewModel);