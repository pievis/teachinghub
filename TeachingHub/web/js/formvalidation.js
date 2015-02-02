/* 
Contains function about form validations:
    Registration
 */

function validateRegistration(){
    var user = document.forms["regForm"]["user"].value;
    var email = document.forms["regForm"]["email"].value;
    var pass = document.forms["regForm"]["pass"].value;
    var cpass = document.forms["regForm"]["cpass"].value;
    var errorDiv = document.getElementById("errorbox");
    var error = false;
    errorDiv.innerHTML= ""
    
    if(!(pass == cpass))
    {
        errorDiv.style.visibility = "visible";
        errorDiv.innerHTML+= "<br>I due campi password non corrispondono."
        error = true;
    }
    if(pass.length < 5)
    {
        errorDiv.style.visibility = "visible";
        errorDiv.innerHTML+= "<br>Password troppo corta."
        error = true;
    }
    if(user.length < 4)
    {
        errorDiv.style.visibility = "visible";
        errorDiv.innerHTML+= "<br>Username troppo corto."
        error = true;
    }
    if(!error){
        errorDiv.style.visibility = "hidden";
        return true; //Tutto ok, invia i dati
    }
    else{
        return false;
    }
}
