

var slika;


$(document).ready(function(){
	

	
	proveraUloge();
	
});

function readImage1(input) {
    if (input.files && input.files[0]) {
        var FR = new FileReader();
        FR.onload = function (e) {
            $('#slika').text(e.target.result);
            slika = e.target.result;
        };
        FR.readAsDataURL(input.files[0]);
    }
}


function proveraUloge() {
	
	$.ajax({
		url: "rest/proveraLogin"
	}).then (function(user) {
			

		if (user != undefined) {
			if (user.uloga == "administrator") {
				$('#KupacProfil').hide();
				$('#ProdavacProfil').hide();
			} else if (user.uloga == "salesman") {
				$('#KupacProfil').hide();
				ispisiObjavljeneOglase();
				preuzmiLajkove();
				ispisiPostojeceRecenzije();
				
				$('#ProdavacProfil').show();
			} else if (user.uloga == "customer") {
				$('#ProdavacProfil').hide();
				ispisiOmiljeneOglase();
				ispisiPoruceneOglase();
				ispisiRecenzijeKupac()
				ispisiDostavljeneOglase();
				$('#KupacProfil').show();
			}
		}
		
	});
	
}

function izmeni(r) {
	
	return function(event) {
		event.preventDefault();
		
		
		$('#recenzijaU').val(r.sadrzajRecenzije);
		
		
		if(r.tacnostOglasa=="true") {
		$('#korektnostU').prop('checked', true);
		}
		
		if(r.ispostovanostDogovora=="true"){
			$('#ispostovanostU').prop('checked', true);
		}
		
		$('#izmeniRecenziju').modal();
		
		$('#izmeniRecenziju').on('click','#DugmeRecenzijaIzmeni',function() {
			
		
			var nazivKategorije = r.nazivKategorije;
			var nazivOglasa= r.nazivOglasa;
			
			var naslovRecenzije= r.naslovRecenzije;
			var sadrzajRecenzije = $('#recenzijaU').val();
			
			var tacnostOglasa="false";
			if ($('#korektnostU').is(":checked"))
			{
			  tacnostOglasa="true";
			}
			
			var ispostovanostDogovora="false";
			if ($('#ispostovanostU').is(":checked"))
			{
			  ispostovanostDogovora="true";
			}
			
		
			$.post ({
				url : "rest/izmeniRecenziju",
				
				data : JSON.stringify ({nazivKategorije : nazivKategorije, nazivOglasa : nazivOglasa, naslovRecenzije : naslovRecenzije, sadrzajRecenzije : sadrzajRecenzije, slika : slika , tacnostOglasa : tacnostOglasa , ispostovanostDogovora : ispostovanostDogovora}),
				contentType :'application/json',
				success : function() {
					
					alert('Successfully changed review.');
					ispisiPoruceneOglase();
					ispisiDostavljeneOglase();
					ispisiRecenzijeKupac();
				},
				error : function() {
					alert('....');
					
				}
			});
	});
	
	}	
}
function ispisiPostojeceRecenzije() { 
	
	$.ajax({
		url: 'rest/preuzmiRecenzijeProdavaca'
	}).then (function(recenzije) {
	

		var i=0;
		
		$('#tabelaRecenzijaProdavaca').html("");
		var naslov=$('	<thead><tr><th scope="col"></th><th scope="col">Name</th><th scope="col">Content</th><th scope="col">Image</th><th scope="col"></th></tr></thead><tbody>');
		$('#tabelaRecenzijaProdavaca').append(naslov);
		
		for(var r of recenzije){
			var slika=r.slika;
			if(slika==null) {
				slika="review.jpg";
			}
			
			var newR= $('<tr><th scope="row">'+i+'</th><td>'+r.naslovRecenzije+'</td><td>'+r.sadrzajRecenzije+'</td><td><img src="'+slika+'" height="80" width="80"> </td><td></tr> ');
			
			$('#tabelaRecenzijaProdavaca').append(newR);
			
			i=i+1;
	}
	
});
		
}

function ispisiRecenzijeKupac() {
	
	$.ajax({
		url: 'rest/preuzmiRecenzijeKupac'
	}).then (function(recenzije) {
	

		var i=0;
		
		$('#tabelaRecenzijaKupac').html("");
		var naslov=$('	<thead><tr><th scope="col"></th><th scope="col">Name</th><th scope="col">Content</th><th scope="col">Image</th><th scope="col">Remove</th><th scope="col">Update</th></tr></thead><tbody>');
		$('#tabelaRecenzijaKupac').append(naslov);
		
		for(var r of recenzije){
			var slika=r.slika;
			if(slika==null) {
				slika="review.jpg";
			}
			
			var newR= $('<tr><th scope="row">'+i+'</th><td>'+r.naslovRecenzije+'</td><td>'+r.sadrzajRecenzije+'</td><td><img src="'+slika+'" height="80" width="80"></td><td><button class="btn btn-danger" id="izbrisiR'+i+'"><i class="fa fa-trash" aria-hidden="true"></i>Remove</button></td><td><button class="btn btn-danger" id="izmeniR'+i+'"><i class="fa fa-pencil" aria-hidden="true"></i>Update</button></td><td></tr> ');
			
			
			$('#tabelaRecenzijaKupac').append(newR);
			$('#izbrisiR'+i).click(izbrisi(r));
			$('#izmeniR'+i).click(izmeni(r));
			i=i+1;
	}
	
});
}

function izbrisi(r) {
	return function (event) {
		

		var nazivKategorije = r.nazivKategorije;
		var nazivOglasa= r.nazivOglasa;
		
		var naslovRecenzije= r.naslovRecenzije;
		var sadrzajRecenzije = r.sadrzajRecenzije;
		var tacnostOglasa = r.tacnostOglasa;
		var ispostovanostDogovora = r.ispostovanostDogovora;
		$.ajax({
			
			type: 'DELETE',
			url : 'rest/izbrisiRecenziju/'+nazivOglasa+'/'+nazivKategorije,
			data : JSON.stringify({naslovRecenzije : naslovRecenzije, sadrzajRecenzije : sadrzajRecenzije, tacnostOglasa : tacnostOglasa , ispostovanostDogovora : ispostovanostDogovora }),
			contentType : 'application/json',
			success : function() {
				alert("Successfuly removed review.");
				ispisiPostojeceRecenzije();
				
			},
			error : function() {
				alert("Something went wrong.");
			}
				
		});
		
	}
	
}
function ispisiObjavljeneOglase() {
	
	$.ajax({
		url: 'rest/preuzmiObjavljeneOglase'
	}).then (function(oglasi) {
	
		var i=0;
		
		$('#tabelaObjavljenihOglasa').html("");
		var naslov=$('	<thead><tr><th scope="col"></th><th scope="col">Name</th><th scope="col">Price</th><th scope="col">Description</th><th scope="col">Image</th><th scope="col"></th></tr></thead><tbody>');
		$('#tabelaObjavljenihOglasa').append(naslov);
		
		for(var o of oglasi){
			var slika=o.slika;
			if(slika==null) {
				slika="ikonica.jpg";
			}
			
			var newR= $('<tr><th scope="row">'+i+'</th><td>'+o.naziv+'</td><td>'+o.cena+'</td><<td>'+o.opis+'</td><td><img src="'+slika+'" height="80" width="80"> </td><td></tr> ');
			
			$('#tabelaObjavljenihOglasa').append(newR);
			
			i=i+1;
	}
	
});
	
}
function ispisiOmiljeneOglase() {
	
	
	$.ajax({
		url: 'rest/preuzmiOmiljeneOglase'
	}).then (function(oglasi) {
		
		var i=0;
		
			$('#tabelaOmiljenihOglasa').html("");
			var naslov=$('	<thead><tr><th scope="col"></th><th scope="col">Name</th><th scope="col">Price</th><th scope="col">Description</th><th scope="col">Image</th><th scope="col"></th></tr></thead><tbody>');
			$('#tabelaOmiljenihOglasa').append(naslov);
			
			for(var o of oglasi){
				var slika=o.slika;
				if(slika==null) {
					slika="ikonica.jpg";
				}
				
				var newR= $('<tr><th scope="row">'+i+'</th><td>'+o.naziv+'</td><td>'+o.cena+'</td><<td>'+o.opis+'</td><td><img src="'+slika+'" height="80" width="80"> </td><td></tr> ');
				
				$('#tabelaOmiljenihOglasa').append(newR);
				
				i=i+1;
		}
		
	});
}
function preuzmiLajkove() {
	
	$.ajax({
		url :'rest/preuzmiLajkove'
	}).then(function(lajkovi){
		
	var p = lajkovi[1];
	var n= lajkovi[0];
	
	$('#poz').text('Broj pozitivnih ocjena:'+p);
	$('#neg').text('Broj negativnih ocjena:'+n);
	
	});
	
	
	
}

function ispisiPoruceneOglase() {
	
	$.ajax({
		url: 'rest/preuzmiPoruceneOglase'
	}).then (function(oglasi) {
		
		$('#tabelaPorucenihOglasa').html("");
		var naslov=$('	<thead><tr><th scope="col"></th><th scope="col">Name</th><th scope="col">Price</th><th scope="col">Description</th><th scope="col">Image</th><th scope="col">Is Deliverd</th></tr></thead><tbody>');
		$('#tabelaPorucenihOglasa').append(naslov);
		var i=0;
		for(var o of oglasi){
			var slika=o.slika;
			if(slika==null) {
				slika="ikonica.jpg";
			}
			
			var newR= $('<tr><th scope="row">'+i+'</th><td>'+o.naziv+'</td><td>'+o.cena+'</td><<td>'+o.opis+'</td><td><img src="'+slika+'" height="80" width="80"> </td><td><button class="btn btn-danger" id="dostavljeno'+i+'">Deliverd</button></td><td></tr> ');
			
			$('#tabelaPorucenihOglasa').append(newR);
			$('#dostavljeno'+i).click(dostavljenoFun(o));
			
			i=i+1;
		}
		
	});
}

function ispisiDostavljeneOglase() {
	
	$.ajax({
		url: 'rest/preuzmiDostavljeneOglase'
	}).then (function(oglasi) {
		
		$('#tabelaDostavljenihOglasa').html("");
		var naslov=$('	<thead><tr><th scope="col"></th><th scope="col">Name</th><th scope="col">Price</th><th scope="col">Description</th><th scope="col">Image</th><th scope="col"></th></tr></thead><tbody>');
		$('#tabelaDostavljenihOglasa').append(naslov);
		var i=0;
		for(var o of oglasi){
			var slika=o.slika;
			if(slika==null) {
				slika="ikonica.jpg";
			}
			
			var newR= $('<tr><th scope="row">'+i+'</th><td>'+o.naziv+'</td><td>'+o.cena+'</td><<td>'+o.opis+'</td><td><img src="'+slika+'" height="80" width="80"> </td></tr> ');
			
			$('#tabelaDostavljenihOglasa').append(newR);
			
			
			i=i+1;
		}
		
	});
}

function dostavljenoFun(o) {
	
	return function(event) {
		event.preventDefault();
		$('#dostavljenoRecenzijaModal').modal();
		
		alert("Please check for delevering ad.")
		$('#dostavljenoRecenzijaModal').on('click','#dugmeRecenzija',function(){
		
			var dostavljenos="false";
		if ($('#dostavljeno').is(":checked"))
		{
		  dostavljenos="true";
		}
		
		var nazivOglasa = o.naziv;
		
		var nazivKategorije = o.nazivKategorije;
		var naslovRecenzije = $('#imeRecenzije').val();
		var prodavacUsername = o.prodavacUsername;
		var sadrzajRecenzije = $('#recenzija').val();

	
		

		
		var tacnostOglasa="false";
		if ($('#korektnost').is(":checked"))
		{
		  tacnostOglasa="true";
		}
		
		var ispostovanostDogovora="false";
		if ($('#ispostovanost').is(":checked"))
		{
		  ispostovanostDogovora="true";
		}
		
	
		
		
		var idOglasa = o.naziv+o.nazivKategorije;
		
			
		
		
		$.post ({
			url : "rest/ostaviRecenziju/"+idOglasa,
			
			data : JSON.stringify ({prodavacUsername :prodavacUsername, nazivOglasa : nazivOglasa, nazivKategorije : nazivKategorije, naslovRecenzije : naslovRecenzije, sadrzajRecenzije : sadrzajRecenzije, slika : slika , tacnostOglasa : tacnostOglasa , ispostovanostDogovora : ispostovanostDogovora}),
			 
			contentType : 'application/json',
			
			success : function() {
				
				alert('Successfully added review and changed ad from ordered to delivered.');
				ispisiPoruceneOglase();
				ispisiDostavljeneOglase();
				ispisiRecenzijeKupac();
			},
			error : function() {
				alert('....');
				
			}
		
		
		});
		
		});
	}
}	
