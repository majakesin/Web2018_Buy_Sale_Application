


$(document).ready(function() {
	
	prikaziKupljeneProizvode();
	$('#kupiNaruceno').click(zavrsiPorudzbinu());
});

function prikaziKupljeneProizvode() {

	var suma=0;
	
	$.ajax({
		
		url : 'rest/uzmiProizvode'
		
	}).then (function(proizvodi) {
		
		var i=0;
		$('#tabelaProizvoda').html("");
		var naslov=$('	<thead><tr><th scope="col"></th><th scope="col">Name</th><th scope="col">Price</th><th scope="col">Remove</th><th scope="col">Favorite</th><th scope="col"></th></tr></thead><tbody>');
		$('#tabelaProizvoda').append(naslov);
		for(var p of proizvodi) { 
			var newR= $('<tr><th scope="row">'+i+'</th><td>'+p.naziv+'</td><td>'+p.cena+'</td><td> <button type="button" class="btn btn-danger"  id="p' +i + '"><i class="fa fa-times" aria-hidden="true"></i></button></td> <td> <button type="button" class="btn btn-danger"  id="f' +i + '"><i class="fa fa-heart" aria-hidden="true"></i></button> </td><td></tr> ');
			
			var cena=p.cena;
			$('#tabelaProizvoda').append(newR);
			
			$('#f'+i).click(DodajUOmiljene(p));
			
			suma=suma+cena;
			i=i+1;
		}
		
		$('#allOrderPrice').append(suma);
		$('#allOrderPrice').append("RSD");
		
		
	});
}

function DodajUOmiljene(p) {
	
	return function(event) {
		event.preventDefault();
	
	
	var naziv=p.naziv;
	var omiljeni = p.omiljeni;
	var aktivan = p.aktivan;
	var opis =p.opis;
	var cena= p.cena;
	var brojPozOcena = p.brojPozOcena;
	var brojNegOcena = p.brojNegOcena;
	var slika= p.slika;
	var datumIsticanja =p.datumIsticanja;
	var nazivKategorije =p.nazivKategorije;
	var grad =p.grad;
	$.ajax ({
		
		
		
		url : 'rest/staviuOmiljene',
		type : 'POST',
		data : JSON.stringify({omiljeni  : omiljeni, naziv : naziv , aktivan : aktivan , opis : opis, cena : cena, brojPozOcena : brojPozOcena, brojNegOcena : brojNegOcena , slika : slika , datumIsticanja : datumIsticanja,
			nazivKategorije : nazivKategorije, grad : grad}),
		contentType : 'application/json',
		success : function() {
			alert("Successfuly added to favorites.");
			
		},
		error : function() {
			
			alert("This ad is already in lists of favorites.");
		}
	});
	
	}
}


function zavrsiPorudzbinu() {
	
	return function (event) {
	event.preventDefault();
	$.ajax ({
		
		type : "POST",
		url : "rest/zavrsiPorudzbinu",
		contentType : 'application/json',
			success : function() {
				alert("Successfuly bought products.");
				$('#tabelaProizvoda').html("");
				var naslov=$('	<thead><tr><th scope="col"></th><th scope="col">Name</th><th scope="col">Price</th><th scope="col">Remove</th><th scope="col">Favorite</th><th scope="col"></th></tr></thead><tbody>');
				$('#tabelaProizvoda').append(naslov);
				
			},
			error : function() {
				
				alert("something went wrong.");
			}	
	
	});
	}
}
	