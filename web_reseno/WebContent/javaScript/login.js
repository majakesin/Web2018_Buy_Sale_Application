

$(document).ready(function() {
	
	$('#login').submit(login());
	$('#signup').submit(registracija());

});
	
	
function login() {
		
	return function(event) {
		
		event.preventDefault();
		let username=$('#username').val();
		let password=$('#password').val();
		console.log('username', username);
		console.log('password', password);
		
		
		$.ajax({
			type: 'POST',
			url: 'rest/loginn/',
			data: JSON.stringify({username : username, password : password}),
			contentType: 'application/json',
			success: function() {
				
				alert("You are loged in.")
				
				window.location='./Pocetna.html';
				$('#username').text("");
				$('#password').text("");
				
				
				
				
				
				
				
					
				
			},
			error: function() {
				$('#username').text("");
				$('#password').text("");
				console.log("funkcija nnije uspjela.");
				alert('Wrong username/password!');
			}
		});
	}
}

function registracija() {
	
	return function(event) {
		event.preventDefault();
		
		let ime=$('input[name="name"]').val();
		let prezime=$('input[name="surname"]').val();
		
		var grad=$('#citySignUp').val();
		let kontaktTelefon=$('input[name="mobilePhone"]').val();
		let email=$('input[name="email"]').val();
		var uloga = $('#role option:selected').text();
		console.log("uloga nakon sto se registruje: ",uloga);
		let username=$('input[name="uname"]').val();
		let password=$('input[name="passwordS"]').val();
		
		console.log('uname',uname);
		console.log('passwordS',passwordS);
		
		
		
		//$.ajax({
		$.post({
		//	type : 'POST',
			url: 'rest/registracija',
			data: JSON.stringify({ ime : ime, prezime : prezime, uloga : uloga ,
		grad : grad, kontaktTelefon : kontaktTelefon, 
		email : email, username : username, password : password}),
			contentType: 'application/json',
			success: function() {
				
				alert("You are signedUp.");
				
				
				window.location='./Pocetna.html';
				
					
				
			},
			error: function() {
				console.log("funkcija nnije uspjela.");
				console.log('uname',uname);
				alert('This username already exists.Please try with different.');
			}
		});
		
	}
	
}	
		
	

	

		
	

