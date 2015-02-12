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
var changedAvatar = false;
var currentAvatar;
//

$(function() {
    //When the document is ready
    //do a get to the servlet
    changedAvatar = false;
    var userid = getParameterByName("userid");
    var requestData = "<get><userid>"+userid+"</userid></get>";
    $.post( "../Profile", requestData,
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

//Avatar in grande è nascosto
$("#avatar").hover(function(){
  $(".hiddenImage").show(); //mostra l'avatar in grande
}
);
$(".hiddenImage").hover(null,function(){
    $(".hiddenImage").hide(); //quando esco da hiddenimage
}
);

function showError(text){
    viewModel.showErrorMsg(true);
    viewModel.errorMsg(text);
}

var isStudent;
function updateViewModel($xml){
    isStudent = false;
    if($xml.find("student").length) //se esiste il nodo student
        isStudent = true;
    var userid = $xml.find("id").text();
    var email = $xml.find("email").text();
    var firstname = $xml.find("firstname").text();
    var lastname = $xml.find("lastname").text();
    var avatar = $xml.find("avatar").text();
    currentAvatar = avatar; //memorizza l'avatar corrente da reinviare
    var firstname = $xml.find("firstname").text();
    if(isStudent){
        var classe = $xml.find("classe").text();
        var hobby = $xml.find("hobby").text();
        setStudent(classe, hobby);
    }else{
        //è un insegnante
        var subjects = "";
        $xml.find("subject").each(function (){
            subjects += $(this).text() + ", "; 
        });
        subjects = subjects.substring(0, subjects.length - 2);
        setTeacher(subjects);
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

function setStudent(classe, hobby){
    updateClass(classe);
    viewModel.hobby(hobby);
    viewModel.showClasse(true);
    viewModel.showHobby(true);
}

function setTeacher(subjects){
    viewModel.subjects(subjects);
    viewModel.showSubjects(true);
}

var avatarFile; //salvo l'avatar se viene cambiato

function readUrlImage(input){
//    console.log("I CHANGED");
    if (input.files && input.files[0]) {
//        console.log(input.files[0]);
        avatarFile = input.files[0];
        var reader = new FileReader();
        reader.onload = function (e) {
            viewModel.avatarUrl(e.target.result);
        };
        reader.readAsDataURL(input.files[0]);
    }
}

//Carica l'immagine sul server poi effettua la richiesta di aggiornamento
function uploadImageAndSendInfo(){
    var dataf = new FormData(); //form data per l'invio dei file
    //key, value
    dataf.append("filetype", "avatar");
    dataf.append("file", avatarFile);
    
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
                // estrapolo il nome del nuovo avatar
                $xml = $(data);
                currentAvatar = $xml.find("avatar").text();
                // invio i dati della form
                sendUpdatedInfo();
            }
            else
            {
                // Errori
                viewModel.canUpdate(true);
                viewModel.showLoader(false);
                showError(errorTags.find("error").text());
                console.log('ERRORS: ' + errorTags.find("error").text());
            }
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            //Errori
            console.log('ERRORS: ' + textStatus);
            //Nascondi il loader
            viewModel.canUpdate(true);
            viewModel.showLoader(false);
        }
    });
}

function updateClass(text){
    text = text.replace(" ", "");
    var ctxt = text.charAt(1);
    var ytxt = text.charAt(0);
    //Es: 5A
    viewModel.classe(ytxt + ctxt);
    viewModel.selectedYear(ytxt);
    viewModel.selectedClass(ctxt);
}

function sendUpdatedInfo(){
    var doc = document.implementation.createDocument(null, "update", null);
    var userE;
    var useridE = doc.createElement("userid");
    useridE.appendChild(doc.createTextNode(viewModel.username()));
    var avatarE = doc.createElement("avatar");
    avatarE.appendChild(doc.createTextNode(currentAvatar)); //this avatar can change from it's original
    var firstNameE = doc.createElement("firstname");
    firstNameE.appendChild(doc.createTextNode(viewModel.firstname()));
    var lastNameE = doc.createElement("lastname");
    lastNameE.appendChild(doc.createTextNode(viewModel.lastname()));
    var emailE = doc.createElement("email");
    emailE.appendChild(doc.createTextNode(viewModel.email()));
    //distinguo tra studente e insegnante
    if(isStudent){
        userE = doc.createElement("student");
        var hobbyE = doc.createElement("hobby");
        hobbyE.appendChild(doc.createTextNode(viewModel.hobby()));
        var classeE = doc.createElement("classe");
        classeE.appendChild(doc.createTextNode(viewModel.selectedYear() + viewModel.selectedClass() ));
        userE.appendChild(hobbyE);
        userE.appendChild(classeE);
    }
    else{
        userE = doc.createElement("teacher");
    }
    userE.appendChild(useridE);
    userE.appendChild(firstNameE);
    userE.appendChild(lastNameE);
    userE.appendChild(emailE);
    userE.appendChild(avatarE)
    doc.documentElement.appendChild(userE);
    var updateData = new XMLSerializer().serializeToString(doc.documentElement);
//    console.log(updateData);
    //Invio i dati
    $.post( "../Profile", updateData,
        function( data ){
            //This is the success function
            var xmlDoc = data;
            var xmlString = (new XMLSerializer()).serializeToString(xmlDoc);
            console.log("log. Data: " + xmlString);
            location.reload(); //ricarica la pagina a profilo aggiornato
        }
    );
}


//cambia la view dell'avatar
$("#selAvatar").change(function (){
    changedAvatar = true;
    readUrlImage(this);
});

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
    avatarUrl : ko.observable(),
    updateVisible: ko.observable(false), //Invisibile l'update
    btnUpdateText: ko.observable("Aggiorna Profilo"),
    updateOpen : false,
    updateProfileInfo: function(){
        //Apre / chiude il menù per aggiornare il profilo
        if(!this.updateOpen){
            this.updateOpen = true;
            this.updateVisible(true);
            this.btnUpdateText("Annulla");
        }else{
            this.updateOpen = false;
            this.updateVisible(false);
            this.btnUpdateText("Modifica il profilo");
        }
    },
    availableYears: ko.observableArray([
        "1","2","3","4","5"
        ]),
    selectedYear: ko.observable(),
    availableClass: ko.observableArray([
        "A","B","C","D","E","F","G","H","I","L",
        "M","N","O","P","Q","R","S","T","U","V","Z"
        ]),
    selectedClass: ko.observable(),
    sendUpdatedProfile : function(){
        if(changedAvatar){ //se ho aggiornato l'avatar prima faccio l'upload dell'immagine
            this.showLoader(true); //mostra un caricamento
            this.canUpdate(false); //nascondi il bottone
            uploadImageAndSendInfo();
        }
        else{ //altrimenti invia direttamente
            sendUpdatedInfo();
        }
    },
    showLoader: ko.observable(false),
    canUpdate: ko.observable(true)
};

ko.applyBindings(viewModel);