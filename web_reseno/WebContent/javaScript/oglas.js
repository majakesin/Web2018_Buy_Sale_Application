

var slika;

$(document).ready(function (){
	
	pregledKategorija();
	pregledOglasa();
	
});



function izmeniOglasf(o){
	
	 return function (event) {
		event.preventDefault();
		
	
		$('#opisOU').val(o.opis);
		$('#cenaOU').val(o.cena);
		$('#vremeIsticanjaOU').val(o.datumIsticanja);
		
		$('#izmeniOglasModal').modal();
		
		
		
		$('#izmeniOglasModal').on('click','#OglasDugmeU' ,function() {
			var omiljeni= o.omiljeni;
			var prodavacUsername= o.prodavacUsername;
			var naziv= o.naziv;
			var opis= $('#opisOU').val();
			var cena = $('#cenaOU').val();
			var datumIsticanja = $('#vremeIsticanjaOU').val();
			var brojPozOcena = o.brojPozOcena;
			var brojNegOcena = o.brojNegOcena;
			var grad = $('#gradOU').val();
			var aktivan = o.aktivan;
			
			var nazivKategorije= o.nazivKategorije
			
			if(slika==null) {
				slika= o.slika;
			}
			
			
			
			$.ajax({
			
				url : 'rest/izmeniOglasA',
				type : 'POST',
				data : JSON.stringify({ brojPozOcena : brojPozOcena, brojNegOcena : brojNegOcena,omiljeni : omiljeni , prodavacUsername : prodavacUsername, naziv : naziv,aktivan : aktivan,opis : opis,nazivKategorije: nazivKategorije, grad : grad, cena : cena,datumIsticanja: datumIsticanja, slika : slika }),
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


function pregledOglasa() {
	
$.ajax({
		
		url : "rest/preuzmiObjavljeneOglase"
		
	}).then(function(oglasi){

		var i=1;
		$('#tabelaOglasaAd').html("");
		var po=$('<thead><tr><th scope="col"></th><th scope="col">Title</th><th scope="col">Description</th><th scope="col">Price</th><th scope="col">Update</th><th scope="col">Remove</th></tr></thead><tbody>');
		$('#tabelaOglasaAd').append(po);
		for(var o of oglasi) { 
		var newR= $('<tr><th scope="row">'+i+'</th><td>'+o.naziv+'</td><td>'+o.opis+'</td><td>'+o.cena+'</td><td> <button type="button" class="btn btn-danger"  id="uo' +i + '"><i class="fa fa-pencil" aria-hidden="true"></i>update</button></td><td> <button type="button" class="btn btn-danger"  id="ur' +i + '"><i class="fa fa-trash" aria-hidden="true"></i>remove</button> </td><td> </tr> ');
				
			$('#tabelaOglasaAd').append(newR);

			
			
			$('#uo'+i+'').click(izmeniOglasf(o));
			
			$('#ur'+i+'').click(izbrisiOglas(o));
			
			i=i+1;
			
			
		}
		
		
		
	});
	
}

function pregledKategorija() {
	

	
	$.ajax({
		
		url : "rest/preuzmiKategorije"
		
	}).then(function(kategorije){

		var i=1;
		$('#tabelaKategorijaAd').html("");
		var po=$('<thead><tr><th scope="col"></th><th scope="col">Name</th><th scope="col">Description</th><th scope="col">Add</th>></tr></thead><tbody>');
		$('#tabelaKategorijaAd').append(po);
		for(var k of kategorije) { 
		var newR= $('<tr><th scope="row">'+i+'</th><td>'+k.naziv+'</td><td>'+k.opis+'</td><td> <button type="button" class="btn btn-danger"  id="nazivu' +i + '"><i class="fa fa-plus" aria-hidden="true"></i></button> </td><td> </tr> ');
				
			$('#tabelaKategorijaAd').append(newR);

			
			$('#nazivu'+i+'').on('click',dodajOglas(k));
			
			console.log('#naziv'+i+'');
			
			i=i+1;
			
			
		}
		var end=$('</tbody>')
		$('#tabelaKategorijaAd').append(end);
		
		
	});
}


function readImage(input) {
    if (input.files && input.files[0]) {
        var FR = new FileReader();
        FR.onload = function (e) {
            $('#slika').text(e.target.result);
            slika = e.target.result;
        };
        FR.readAsDataURL(input.files[0]);
    }
}

	
function izbrisiOglas(o) {
	
	
	return function(event) {
		event.preventDefault();
		$.ajax ({
			type: 'DELETE',
			url: 'rest/izbrisiOglas',
			data : JSON.stringify({
				naziv : o.naziv,
				opis : o.opis,
				nazivKategorije : o.nazivKategorije
			}),
			contentType : 'application/json',

			success : function () {
				alert("Successfuly deleted ad!");
				pregledKategorija();
				pregledOglasa();
		
				
			},
			error : function () {
				alert('Something went wrong. Can not find ad');
			}
		});
	}
	
}
	

function dodajOglas(k) {
	
	return function(event) {
		event.preventDefault();
		
		$('#cenaO').val(0);
		$('#dodajOglas').modal();
		
		
		
		var nazivkategorije = k.naziv;

		$('#dodajOglas').on('click','#dodajOglasDugme' ,function() {
		
			var omiljeni = "0";
			var naziv = $('#nazivO').val();
			var opis= $('#opisO').val();
			var cena = $('#cenaO').val();
			var datumIsticanja = $('#vremeIsticanjaO').val();
			var nazivKategorije = nazivkategorije;
			var grad = $('#gradO').val();
			 
			var aktivan="true";
			
			
			
			if(cena==null) {
				cena=0;
			}
			
			var brojPozOcena =0;
			var brojNegOcena = 0;
			
			$.ajax({
			
			
			
			
			url  : 'rest/'+nazivkategorije+'/dodajOglas',
			type : 'PUT',
			data : JSON.stringify({omiljeni : omiljeni, naziv : naziv,brojPozOcena :brojPozOcena, brojNegOcena : brojNegOcena,aktivan:aktivan,opis : opis,nazivKategorije: nazivKategorije, grad : grad, cena : cena,datumIsticanja: datumIsticanja, slika : slika }),
			contentType : 'application/json',
			success : function() {
				alert("Successfuly added advertisment.");
				pregledOglasa();
				
			},
		error : function () {
				alert("Something went wrong. Please try again.");
				pregledOglasa();
			}
			
			});	
			});
	}
}	
