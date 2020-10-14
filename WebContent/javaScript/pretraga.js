$(document).ready(function() {
	
	dodajGradove();
	preuzmiGradoveKorisnik();
	
	$('#prikazOglasaPretraga').show();
	$('#prikazKorisnikaPretraga').show();
	$('#pronadjeniOglasi').hide();
	$('#pronadjeniKorisnici').hide();
	$('#DugmeOglasPretrazi').click(pretraga());
	$('#DugmeKorisnikPretrazi').click(pretragaKorisnika());
	
});


function dodajGradove() {
	
$.ajax({
	
	url : 'rest/preuzmiGradove'
}).then (function(gradovi){
	
	for(g of gradovi) {
		
		var newO = $('<option value="'+g+'">'+g+'</option>');
		console.log(g);
		$('#gradPretraga').append(newO);
		
	}
	
});
	
}


function preuzmiGradoveKorisnik() {
	$.ajax({
	url : 'rest/preuzmiGradoveKorisnik'
	}).then (function(gradovi){
	
	for(g of gradovi) {
		
		var newO = $('<option value="'+g+'">'+g+'</option>');
		console.log(g);
		$('#gradKorisnika').append(newO);
		
	}
	
});
	
}
function pretragaKorisnika() {
	
	return function(event) {
		event.preventDefault();
		
		var ime= $('#imeKorisinika').val();
		if(ime=="") {
			ime="un";
		}
		var grad = $('#gradKorisnika option:selected').text();
		if(grad == "") {
			grad="un";
		}
		var string = ime+'~'+grad;
		$.ajax({
		
			url : 'rest/pretragaKorisnika/'+string
		}).then (function(korisnici) {
			
			
			$('#tabelaKorisnikaPretraga').html("");
			var naslov=$('	<thead><tr><th scope="col"></th><th scope="col">Name</th><th scope="col">City</th><th scope="col">Role</th></tr></thead><tbody>');
			$('#tabelaKorisnikaPretraga').append(naslov);
			var i=0;
			for(var k of korisnici){
				
				
				var newR= $('<tr><th scope="row">'+i+'</th><td>'+k.ime+'</td><td>'+k.grad+'</td><<td>'+k.uloga+'</td></tr> ');
				
				$('#tabelaKorisnikaPretraga').append(newR);
				
				
				i=i+1;
			}
			
			
		});
		$('#prikazKorisnikaPretraga').hide();
		$('#prikazOglasaPretraga').hide();
		$('#pronadjeniOglasi').hide();
		$('#pronadjeniKorisnici').show();
	}
	
	
}
function pretraga() {
	

	return function(event) {
		event.preventDefault();
	
	$('#prikazOglasaPretraga').hide();
		$('#pronadjeniOglasi').show();
	
		
			
			var naziv = $('#imeOglasaP').val();
			
			if(naziv=="") {
				naziv="un";
			}
			
			var minCena = $('#minCena').val();
			

			if(minCena=="") {
				minCena="un";
			} 
			
			var maxCena = $('#maxCena').val();

			if(maxCena=="") {
				maxCena="un";
			} 
			
			var pozOcenaMin = $('#minOcena').val();
			

			if(pozOcenaMin=="") {
				pozOcenaMin="un";
			} 
			
			var pozOcenaMax = $('#maxOcena').val();
			

			if(pozOcenaMax=="") {
				pozOcenaMax="un";
			} 
			
			var datumIsticanjaMin = $('#datumIsticanjaMin').val();
			

			if(datumIsticanjaMin=="") {
				datumIsticanjaMin="un";
			} 
			
			var datumIsticanjaMax = $('#datumIsticanjaMax').val();
			

			if(datumIsticanjaMax=="") {
				datumIsticanjaMax="un";
			} 
			
			var grad = $('#gradPretraga option:selected').text();
			

			if(grad=="") {
				grad="un";
			}
			 
			var string =naziv+'~'+minCena+'~'+maxCena+'~'+pozOcenaMin+'~'+pozOcenaMax+'~'+datumIsticanjaMin+'~'+
			datumIsticanjaMax+'~'+grad;
			$.ajax({
				
				url : 'rest/'+string
			}).then(function(oglasi){
				
				
				var i=0;
				if(oglasi!=null){
				for(var o of oglasi) { 
					
			
					var cena=o.cena;
					
					var slika=o.slika;
					if(slika==null) {
						slika="ikonica.jpg";
					}
					var newK = 
						
						$('<div class="col-sm-4"><br><div class="card text-center" style="width: 16rem;"><img class=+card-img-top" height="250" width="250" src="'+slika+'" align="center" alt="Card image cap"><div class="card-body"><h5 class="card-title">' + o.naziv + '</h5></a><p class="card-text">' + cena+" RSD \n"+o.opis + '</p><button  value="Buy product" id="kTO'+i+'" class="button btn-danger">Add to cart</button><br><br><button  value="review" id="reviewT'+i+'" class="button btn-danger">See reviews</button>'
								+'<div class="card-footer"><div class="container"><a class="like"><i id="tuT'+i+'" class="fa fa-thumbs-o-up"></i>'
						    +'</a><a class="dislike"><i id="tdT'+i+'" class="fa fa-thumbs-o-down"></i>' 
						    
						 + ' </a></div></div></div></div></div>');
					
					if(o.aktivan=="true") {
					$('#pronadjeniOglasi').append(newK);
					
					}
					$('#tuT'+i).click(pozitivnaOcenaT(o));
					$('#tdT'+i).click(negativnaOcenaT(o));
					proveriKupacT('#kTO'+i,o);
					$('#reviewT'+i).click(pogledajRecenzijeT(o));
					console.log('#k'+i+'');
					
					i=i+1;
					

				}	
				}
				else {
					
					var al=$('<div class="alert alert-warning" role="alert">Ad nof found. Please try again!</div>');
					$('#pronadjeniOglasi').append(al);
					
				}
				
			
			});
			$('#prikazKorisnikaPretraga').hide();
			$('#pronadjeniKorisnici').hide();
			$('#prikazOglasaPretraga').hide();
			$('#pronadjeniOglasi').show();

			
			
	
		
		
	}
	
}


function negativnaOcenaT(o) {
	return function(event){
		
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
				nazivKategorije : nazivKategorije, grad : grad}),
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
function pozitivnaOcenaT(o) {
	return function(event){
		
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
				nazivKategorije : nazivKategorije, grad : grad}),
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

function pogledajRecenzijeT(o) {

	return function(event) {
	
		
		var id= o.naziv+o.nazivKategorije;

		var slika;
		$.ajax ({
		
		
		url: 'rest/preuzmiRecenzija/'+id
		 
	}).	then (function(recenzije) {
	
		$('#prikazRecenzijaPretraga').html("");
		for(r of recenzije) {
			
			if(r.slika==null) {
				slika="review.jpg"
			}
			else {
				slika= r.slika;
			}
			var div1=$('<div class="item active"><div class="col-md-4 col-sm-6"><div class="block-text rel zmin"><a title="" href="#" id="naslovRecenzije">'+r.naslovRecenzije+'</a> <p>'+r.sadrzajRecenzije+'</p><img id="slikaR" height="70" width="70" src="'+slika+'" align="center"><ins class="ab zmin sprite sprite-i-triangle block"></ins></div><div class="person-text rel"></div>');
				
			$('#prikazRecenzijaPretraga').append(div1);
			
			$('#prikazOglasaPretraga').hide();
			$('#pronadjeniOglasi').hide();
			$('#prikazRecenzijaPretraga').show();
		  
			
			
		}
		
	});
		
	}
}
	


function proveriKupacT(id,o) {
	
	console.log(id);
	$.ajax({
	 url : "rest/proveraLogin"
	}).then(function(user) {
		
		if(user.uloga=="customer") {
			
			$(id).click(ispisioOglasuT(o));
		}
		else if (user.uloga=="salesman"  || user.uloga=="administrator" ) {
			
			alert("Only customer can buy product.")
			
		}
	});
}
	
function ispisioOglasuT(o) {
	
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

		