$(document).ready(function() {
	

	
	
	ucitajKategorije();
	
	
});


function ucitajKategorije() {
	
	$.ajax({
		url: "rest/preuzmiKategorije"
	}).then(function(kategorije){
		
		$('#dodavanjeKategorija').html("");
		for(var k of kategorije) { 
			
			console.log(k.naziv);
			console.log(k.opis);
			var newK = 
	$('<div class="col-sm-4"><br><div class="card text-center" style="width: 18rem;"><img class=+card-img-top" height="250" width="250" src="'+k.urlSlike+'" alt="Card image cap"><div class="card-body"><h5 class="card-title">' + k.naziv + '</h5></a><p class="card-text">' + k.opis + '</p><form action="pregledOglasa.html"><input type="submit" value="Go for more" class="button btn-primary"></form></div></div></div>');
			
			$('#dodavanjeKategorija').append(newK);
			
		
		 }
		 
	});
	
}