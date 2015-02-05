/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var viewModel = {
    showMsg: ko.observable(0), //1 is true
    errorMsg: ko.observable(""),    
    setMsg : function(text) {
        this.showMsg(1);
        this.errorMsg(text);
    }
};

ko.applyBindings(viewModel);