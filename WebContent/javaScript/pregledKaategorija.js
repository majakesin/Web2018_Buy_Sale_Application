

$(document).ready(function() {
	$('#prikazRecenzija').hide();
	$('#prikazOglasad').show();
	pregledKategorijaSideBar();
	
});


function pregledKategorijaSideBar() {
	
	$('#prikazRecenzijah').hide();
	$('#prikazOglasad').show();
	$.ajax({
		
		url : "rest/pregledKategorijaSideBar"
		
	}).then(function(kategorije){

		
		var i=1;
		
		
		
		for(var k of kategorije) { 
			
		
		var newR= $('<li class="nav-item">  <button type="button" id="k'+i+'" class="btn btn-info">'+k.naziv+'</button></li><br> ');
				
	// $('.anchor').live(prikazOglasa(k.naziv));
		
		console.log('#k'+i+'');
		
		$('#sidebar').append(newR);

		$('#k'+i+'').click(prikazOglasa(k));
		
			
		i=i+1;
		
	
		}
	});
	
	
	}		


function prikazOglasa(k) {
	
	return function(event) {
		event.preventDefault();
		
		
		
		var nazivk=k.naziv;
	
	
	$.get({
		
		url : 'rest/prikaziOglas/'+nazivk,
	
		contentType:'application/json',
		success: function(oglasi) {
			
		$('#prikazOglasad').html("");
		var i=0;
		for(var o of oglasi) { 
			
			console.log(k.naziv);
			console.log(k.opis);
			var cena=o.cena;
			
			var slika=o.slika;
			if(slika==null) {
				slika="ikonica.jpg";
			}
			var newK = 
				
				$('<div class="col-sm-4"><br><div class="card text-center" style="width: 16rem;"><img class=+card-img-top" height="250" width="250" src="'+slika+'" align="center" alt="Card image cap"><div class="card-body"><h5 class="card-title">' + o.naziv + '</h5></a><p class="card-text">' + cena+" RSD \n"+o.opis + '</p><button  value="Buy product" id="kO'+i+'" class="button btn-danger"><i class="fa fa-money" aria-hidden="true"></i> Add to cart</button><br><br><button  value="review" id="review'+i+'" class="button btn-danger">See reviews</button>'
						+'<div class="card-footer"><div class="container"><a class="like"><i id="tu'+i+'" class="fa fa-thumbs-o-up"></i>'
				    +'</a><a class="dislike"><i id="td'+i+'" class="fa fa-thumbs-o-down"></i>' 
				    
				 + ' </a></div></div></div></div></div>');
			
			if(o.aktivan=="true") {
			$('#prikazOglasad').append(newK);
			
			}
			$('#tu'+i).click(pozitivnaOcena(o));
			$('#td'+i).click(negativnaOcena(o));
			proveriKupac('#kO'+i,o);
			$('#review'+i).click(pogledajRecenzije(o));
			console.log('#k'+i+'');
			
			i=i+1;
			}

		$('#prikazOglasad').show();
		$('#prikazRecenzijah').hide();	
			
		}
	});
	

	}
}	

function negativnaOcena(o) {
	return function(event){
		
		
		var prodavacUsername=o.prodavacUsername;
		
		var naziv=o.naziv;
		var aktivan = o.aktivan;
		var opis =o.opis;
		var cena= o.cena;
		var brojPozOcena = o.brojPozOcena;
		var brojNegOcena = o.brojNegOcena;
		var slika= o.slika;
		
		var datumIsticanja =o.datumIsticanja;
		var nazivKategorije =o.nazivKategorije;
		var grad =o.grad;
	$.ajax({
		type:'POST',
		url : 'rest/poz',
	
			data : JSON.stringify({naziv : naziv , aktivan : aktivan , opis : opis, cena : cena, brojPozOcena : brojPozOcena, brojNegOcena : brojNegOcena , slika : slika , datumIsticanja : datumIsticanja,
				nazivKategorije : nazivKategorije, grad : grad,prodavacUsername : prodavacUsername}),
			contentType : 'application/json',
			success : function() {
				alert("Disliked.");
				$(this).css('color','red');
				
				
			},
		error : function () {
				alert("Something went wrong. Please try again.");
			}
			
			
		});
	}	

	
	
}
function pozitivnaOcena(o) {
	return function(event){
	
		
		var prodavacUsername=o.prodavacUsername;
		
		
		var naziv=o.naziv;
		var aktivan = o.aktivan;
		var opis =o.opis;
		var cena= o.cena;
		var brojPozOcena = o.brojPozOcena;
		var brojNegOcena = o.brojNegOcena;
		var slika= o.slika;
		var datumIsticanja =o.datumIsticanja;
	
		var nazivKategorije =o.nazivKategorije;
		var grad =o.grad;
	$.ajax({
		type:'POST',
		url : 'rest/neg',
	
			data : JSON.stringify({naziv : naziv , aktivan : aktivan , opis : opis, cena : cena, brojPozOcena : brojPozOcena, brojNegOcena : brojNegOcena , slika : slika , datumIsticanja : datumIsticanja,
				nazivKategorije : nazivKategorije, grad : grad,prodavacUsername : prodavacUsername}),
			contentType : 'application/json',
			success : function() {
				alert("Liked.");
				$(this).css('color','red');
				
				
			},
		error : function () {
				alert("Something went wrong. Please try again.");
			}
			
			
		});
	}	

	
}
function pogledajRecenzije(o) {

	return function(event) {
	
		
		var id= o.naziv+o.nazivKategorije;

		var slika;
		$.ajax ({
		
		
		url: 'rest/preuzmiRecenzija/'+id
		 
	}).	then (function(recenzije) {
	
		$('#prikazRecenzijah').html("");
		for(r of recenzije) {
			
			if(r.slika==null) {
				slika="review.jpg"
			}
			else {
				slika= r.slika;
			}
			var div1=$('<div class="item active"><div class="col-md-4 col-sm-6"><div class="block-text rel zmin"><a title="" href="#" id="naslovRecenzije">'+r.naslovRecenzije+'</a> <p>'+r.sadrzajRecenzije+'</p><img id="slikaR" height="70" width="70" src="'+slika+'" align="center"><ins class="ab zmin sprite sprite-i-triangle block"></ins></div><div class="person-text rel"></div>');
				
			$('#prikazRecenzijah').append(div1);
			
		
		  
			
			
		}
		$('#prikazOglasad').hide();
		$('#prikazRecenzijah').show();  

	});
		
	}
	
}

function proveriKupac(id,o) {
	
	console.log(id);
	$.ajax({
	 url : "rest/proveraLogin"
	}).then(function(user) {
		
		if(user.uloga=="customer") {
			
			$(id).click(ispisioOglasu(o));
		}
		else if (user.uloga=="salesman"  || user.uloga=="administrator" ) {
			
			alert("Only customer can buy product.")
			
		}
	});
}
	
function ispisioOglasu(o) {
	
	return function(event) {
		

		event.preventDefault();
		
		
		var naziv=o.naziv;
		var aktivan = o.aktivan;
		var opis =o.opis;
		var cena= o.cena;
		var brojPozOcena = o.brojPozOcena;
		var brojNegOcena = o.brojNegOcena;
		var slika= o.slika;
		var datumIsticanja =o.datumIsticanja;
		var nazivKategorije =o.nazivKategorije;
		var grad =o.grad;
		
	
	var ob=o;
	$.ajax ({
		
		type:'POST',
		url: 'rest/kupljenProizvod',
		 
		data : JSON.stringify({naziv : naziv , aktivan : aktivan , opis : opis, cena : cena, brojPozOcena : brojPozOcena, brojNegOcena : brojNegOcena , slika : slika , datumIsticanja : datumIsticanja,
			nazivKategorije : nazivKategorije, grad : grad}),
		contentType : 'application/json',
		success : function() {
			alert("Successfuly added to cart.");
			window.location='./korpa1.html';
			
		},
	error : function () {
			alert("Something went wrong. Please try again.");
		}
		
		});	
		}
}

		

	
	

	
	

