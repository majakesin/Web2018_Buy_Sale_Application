$(document).ready(function() {
	
	$('#korpa').hide();
	$('#profil').hide();
	$('#logout').hide();
	$('#podesavanja').hide();
	$('#oglasid').hide();
	$('#porukeN').hide();

	proveraLogina();
	$('#logout').click(function () {
        logoutUser();
    });
	
});



	
	

function proveraLogina() {
	
	
	
 	$.ajax({
	url : "rest/proveraLogin"
}).then(function(user) {

	if (user != undefined) {
		$('#porukeN').show();
		$('#loginN').hide();
		$('#signupN').hide();
		$('#profil').text(user.username);
		$('#profil').show();
		$('#logout').show();
		if (user.uloga=="customer") {
			$('#korpa').show();
			console.log("bio sam ovdje! :D");
			console.log(user.uloga);
		}
		if (user.uloga == "administrator") {
			$('#podesavanja').show();
			
			console.log(user.uloga);
		}
		if(user.uloga=="salesman" ) {
				
			$('#oglasid').show();
			
			console.log("Uloga je :" ,user.uloga);
			}
		
		}
	});
}

function logoutUser() {
    $.ajax({
		url: "rest/logout",

		success : function() {
			alert("Successfuly loged out!")
			window.location='./Pocetna.html';
		},
		
		error : function() {
			alert("Something went wrong");
		}
    });
}



