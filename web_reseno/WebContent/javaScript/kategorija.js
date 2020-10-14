var slika;
var urlSlike;
$(document).ready(function (){
	
	
	$('#update').hide();
	

	$('#dodajK').submit(dodajKategoriju());
	
	pregledKategorija();
	
	pregledKorisnika();
	
	pregledSvihOglasa();
	
	
});





function dodajKategoriju () {
	
	return function(event){
	
	event.preventDefault();
	

	
	var naziv= $('#imeKategorije').val();
	var opis=$('#opisKategorije').val();
	
	
	$.post ({
		
		url:"rest/dodajKategoriju",
		data: JSON.stringify({ naziv : naziv, opis: opis ,urlSlike : urlSlike }),
		contentType: 'application/json',
			success: function() {
				alert("Successfuly added new category.");
				
				window.location='./Pocetna.html';
				
				
			},
		error: function() {
			alert("Problem with adding new category.");
		}
	});
	
	
	
	
	
	}
}




function pregledKorisnika() {
	
	$.ajax({
		url : "rest/preuzmiKorisnike"
			
	}).then(function(korisnici) {
		var i=1;
		$('#tabelaKorisnika').html("");
		;
		var po=$('<thead><tr><th scope="col"></th><th scope="col">Name</th><th scope="col">Surname</th><th scope="col">Username</th><th scope="col">Email</th><th scope="col">Role</th><th scope="col">Change Role</th></tr></thead><tbody>');
		$('#tabelaKorisnika').append(po);
		
		for(var k of korisnici) {
			
			var newR= $('<tr><th scope="row">'+i+'</th><td>'+k.ime+'</td><td>'+k.prezime+'</td><td>'+k.username+'</td><td>'+k.email+'</td><td>'+k.uloga+'</td><td> <button type="button" class="btn btn-danger"  id="r' +i + '"><i class="fa fa-pencil" aria-hidden="true"></i>change</button></td></tr> ');
			$('#tabelaKorisnika').append(newR);
			
			$('#r'+i+'').click(izmeniUlogu(k));
			i=i+1;
		}
		var end=$('</tbody>')
		$('#tabelaKorisnika').append(end);
		
		
	});
	
	
}

function pregledSvihOglasa() {
	
	$.ajax({
		url : "rest/preuzmiSveOglase"
			
	}).then(function(oglasi) {
		
		$('#tabelaOglasa').html("");
		;
		var po=$('<thead><tr><th scope="col"></th><th scope="col">Tittle</th><th scope="col">Description</th><th scope="col">Image</th><th scope="col">Forbid</th><th scope="col">Change</th></tr></thead><tbody>');
		$('#tabelaOglasa').append(po);
		
		var i=0;
		for(var o of oglasi) {
			var slika=o.slika;
			if(slika==null) {
				slika="ikonica.jpg";
			}
			
			var newR= $('<tr><th scope="row">'+i+'</th><td>'+o.naziv+'</td><td>'+o.opis+'</td><td><img src="'+slika+'" height="100" width="100"></td><td><button type="button" class="btn btn-danger"  id="f' +i + '"><i class="fa fa-times-circle" aria-hidden="true"></i>Forbid</button></td><td> <button type="button" class="btn btn-danger"  id="upd' +i + '"><i class="fa fa-pencil" aria-hidden="true"></i>change</button></td></tr> ');
			$('#tabelaOglasa').append(newR);
				$('#f'+i).click(zabraniOglas(o,i));
				$('#upd'+i+'').click(izmeniOglas(o));
			i=i+1;
		}
		var end=$('</tbody>')
		$('#tabelaOglasa').append(end);
		
		
	});
	
	
	
}

function readImage2(input) {
    if (input.files && input.files[0]) {
        var FR = new FileReader();
        FR.onload = function (e) {
            $('#slika').text(e.target.result);
            slika = e.target.result;
        };
        FR.readAsDataURL(input.files[0]);
    }
}

function readImage3(input) {
    if (input.files && input.files[0]) {
        var FR = new FileReader();
        FR.onload = function (e) {
            $('#urlSlike').text(e.target.result);
            urlSlike = e.target.result;
        };
        FR.readAsDataURL(input.files[0]);
    }
}

function izmeniOglas(o) {
	
	 return function (event) {
			event.preventDefault();
			
			
			$('#opisOUA').val(o.opis);
			$('#cenaOUA').val(o.cena);
			$('#vremeIsticanjaOUA').val(o.datumIsticanja);
			$('#nazivOU').val(o.naziv);
			$('#izmeniOglasModalA').modal();
			
			
			
			
			$('#izmeniOglasModalA').on('click','#OglasDugmeUA' ,function() {
				var omiljeni = o.omiljeni;
				var prodavacUsername= o.prodavacUsername;
				var naziv= o.naziv;
				var opis= $('#opisOUA').val();
				var cena = $('#cenaOUA').val();
				var datumIsticanja = $('#vremeIsticanjaOUA').val();
				var brojPozOcena = o.brojPozOcena;
				var brojNegOcena = o.brojNegOcena;
				var grad = $('#gradOUA').val();
				var aktivan = o.aktivan;
				
				var nazivKategorije= o.nazivKategorije
				
				if(slika==null) {
					slika= o.slika;
				}
				$.ajax({
				
					url : 'rest/izmeniOglasA',
					type : 'POST',
					data : JSON.stringify({ brojPozOcena : brojPozOcena , brojNegOcena : brojNegOcena , omiljeni : omiljeni, prodavacUsername : prodavacUsername, naziv : naziv,aktivan : aktivan,opis : opis,nazivKategorije: nazivKategorije, grad : grad, cena : cena,datumIsticanja: datumIsticanja, slika : slika }),
				contentType : 'application/json',
				success : function() {
					
						alert("Successfuly changed ad.");
						pregledKategorija();
						pregledOglasa();
					
				},
				error : function() {
					alert("Something went wrong");
				}
				
				});
				
				
			});
			
		
		 }
		
		
	
	
}
function zabraniOglas(o,i) {
	
return function(event) {
		

		event.preventDefault();
		
		var prodavacUsername= o.prodavacUsername;
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
		url: 'rest/zabraniOglas',
		 
		data : JSON.stringify({prodavacUsername : prodavacUsername, naziv : naziv , aktivan : aktivan , opis : opis, cena : cena, brojPozOcena : brojPozOcena, brojNegOcena : brojNegOcena , slika : slika , datumIsticanja : datumIsticanja,
			nazivKategorije : nazivKategorije, grad : grad}),
		contentType : 'application/json',
		success : function() {
			alert("Successfuly forbiden ad.It wont show in homepage or in any views.");
			pregledSvihOglasa();
			
			
		},error : function() {
			
			alert("This ad is already forbiden.");
			 $('#f'+i).prop('disabled', true);
		}
	});
	
	}
}

function izmeniUlogu(k) {
	
	return function(event) {
		event.preventDefault();
		
		$('#updateRole').modal();
		
		
	
		
	
		$('#updateRole').on('click','#updateDugmeRole' ,function() {
		
			var uloga = $('#selectUloge option:selected').text();
			
			var username= k.username;
			var password = k.password;
			
			$.ajax({
			
			
			
			
			url  : 'rest/izmeniUloguKorisnika',
			type : 'POST',
			data : JSON.stringify({username : username,password: password, uloga : uloga}),
			contentType : 'application/json',
			success : function() {
				alert("Successfuly changed role");
				pregledKorisnika();
			},
		error : function () {
				alert("Something went wrong. Please try again.");
			}
			
		});
		});
	}
}


function pregledKategorija() {
	

	
	$.ajax({
		
		url : "rest/preuzmiKategorije"
		
	}).then(function(kategorije){

		var i=1;
		$('#tabelaKategorija').html("");
		var po=$('<thead><tr><th scope="col"></th><th scope="col">Name</th><th scope="col">Description</th><th scope="col">Remove</th><th scope="col">Update</th></tr></thead><tbody>');
		$('#tabelaKategorija').append(po);
		for(var k of kategorije) { 
		var newR= $('<tr><th scope="row">'+i+'</th><td>'+k.naziv+'</td><td>'+k.opis+'</td><td> <button type="button" class="btn btn-danger"  id="naziv' +i + '"><i class="fa fa-trash" aria-hidden="true"></i></button> </td><td> <button type="button" class="btn btn-danger" id="u' + i + '"><i class="fa fa-pencil" aria-hidden="true"></i>update</button> </td></tr> ');
				
			$('#tabelaKategorija').append(newR);

			$('#u'+i+'').click(izmeniKategoriju(k));
			$('#naziv'+i+'').on('click',izbrisiKategoriju(k));
			
			console.log('#naziv'+i+'');
			
			i=i+1;
			
			
		}
		var end=$('</tbody>')
		$('#tabelaKategorija').append(end);
		
		var po1=$('<h3> Existing users: </h3>');
		$('#kategorije').append(po1)
	});
	

	
}


function izmeniKategoriju(k){
	
	 return function (event) {
		event.preventDefault();
		
	
		$('#update').modal(); 
	
		$('#opisKupdate').val(k.opis);
		$('#urlUpdate').val(k.urlSlike);
		$('#nazivKategorijeUP').val(k.naziv);
		
		
		$('#update').on('click','#updateDugme' ,function() {
			
			
			var opis =$('#opisKupdate').val();
			
			var naziv = $('#nazivKategorijeUP').val();
			var nazivk=k.naziv;
			
			if(urlSlike==null) {
				urlSlike= k.urlSlike;
			}
			
			$.ajax({
			
				url : 'rest/'+nazivk+'',
				type : 'PUT',
				data : JSON.stringify({
				naziv : naziv,
				opis : opis,
				urlSlike : urlSlike,
			}),
			contentType : 'application/json',
			success : function() {
				
					alert("Successfuly changed category.");
					window.location='./Pocetna.html'
				
			},
			error : function() {
				alert("Something went wrong");
			}
			
			});
			
			
		});
		
	
	 }
	
	
	
	
}

function izbrisiKategoriju(k) {
	
		return function(event) {
			event.preventDefault();
			$.ajax ({
				type: 'DELETE',
				url: 'rest/izbrisiKategoriju',
				data : JSON.stringify({
					naziv : k.naziv,
					opis : k.opis
				}),
				contentType : 'application/json',

				success : function () {
					alert("Successfuly deleted category!");
					pregledKategorija();
					window.location='./Pocetna.html';
			
					
				},
				error : function () {
					alert('Something went wrong. Can not find category.');
				}
			});
		}
		
	
}