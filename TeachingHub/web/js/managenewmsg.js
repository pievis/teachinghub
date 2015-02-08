//Variables

//functions
function checkAndSendDiscussion(){
    var title = $("input[name=title]").val();
    var desc = $("input[name=description]").val();
    var msg = $("input[name=message]").val();
    if(!checkValues(title, desc, msg))
        return;
    
}

function checkValues(title, desc, msg){
    console.log("Msg:: " + title + desc + msg);
    if(title == undefined || title.length < 5){
        showErrorMsg("Titolo troppo corto.");
        return false;
    }
    if(title == undefined || msg.lenght < 5){
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

//View Mode
var viewModel = {
    showErrorMsg : ko.observable(false), //inizialmente invisibile
    errorMsg : ko.observable("Nessun problema")
};

ko.applyBindings(viewModel);