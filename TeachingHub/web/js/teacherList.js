/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// global fields


// class def
var Teacher = function(name) { //for future implementation
    
    this.teacherProfileUrl;
}



// viewModel
var viewModel = {
    errorMsg : ko.observable(""),
    //Invisible at the beginning
    showErrorMsg : ko.observable(false),
    teachers : ko.observableArray()
};

ko.applyBindings(viewModel);

// Utils
