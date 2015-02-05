/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var viewModel = {
    loginOk: ko.observable(1), //1 is true
    errorMsg: ko.observable("")
};

ko.applyBindings(viewModel);