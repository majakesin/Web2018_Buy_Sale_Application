$(document).ready(function (){
	
	ucitajPrimaoce();
	ispisiPorukePoslate();
	ispisiPorukePrimljene();
	$('#poslataPoruka').click(posaljiPoruku());
});


function ucitajPrimaoce() {
	
	$.ajax({
		url: 'rest/uzmiPrimaoce'
			
	}).then (function(primaoci){
		if(primaoci!=null) {
		for(p of primaoci) {
			
			var newO = $('<option value="'+p.username+'">'+p.username+'</option>');
			
			$('#listaSvihPrimalaca').append(newO);
			
		}
		}
	});
	
}

function ispisiPorukePoslate() {
	
	$.ajax({
		
		url: 'rest/preuzmiPoslate'
		
	}).then(function(poruke){
		
		if(poruke!=null) {
			
			$('#tabelaPoslate').html("");
			var po=$('<thead><tr><th scope="col"></th><th scope="col">Tittle</th><th scope="col">Receiver:</th><th scope="col">Date and time:</th><th scope="col">Delete</th><th scope="col">Change</th></tr></thead><tbody>');
			$('#tabelaPoslate').append(po);
			var i=0;
			for(p of poruke) {
				var newR= $('<tr><th scope="row">'+i+'</th><td>'+p.naslovPoruke+'</td><td>'+p.primalac+'</td><td>'+p.datumPoruke+" "+p.vremePoruke+'</td><td> <button type="button" class="btn btn-danger"  id="rpP' +i + '"><i class="fa fa-trash" aria-hidden="true"></i></button> </td><td> <button type="button" class="btn btn-danger" id="upP' + i + '"><i class="fa fa-pencil" aria-hidden="true"></i>change</button> </td><td><button class="btn btn-danger" id="showS'+i+'" ><i class="fa fa-eye" aria-hidden="true"></i></button><td></tr> ');
			
				$('#tabelaPoslate').append(newR);
				$('#rpP'+i).click(obrisiPorukuPoslata(p));
				$('#showS'+i).click(pogledajPorukuPoslatu(p));
				$('#upP'+i).click(izmeniPoruku(p));
				i=i+1;
			}
		}
		
	});
	
}


function odgovoriNaPoruku(p) {
	
	return function(event) {
	event.preventDefault();
	$('#primalacPrimljena').val(p.posiljalac);
	$('#naslovPrimljena').val(p.naslovPoruke);
	$('#sadrzajPrimljena').val(p.sadrzajPoruke);
	
	
	$('#prikazPorukePrimljene').modal();
	
	$('#prikazPorukePrimljene').on('click','#odgovoriNaPoruku',function() {
	
		$('#primalacOdgovor').val(p.posiljalac);
		
		$('#odgovoriNaPorukuModal').modal();
		
		$('#odgovoriNaPorukuModal').on('click','#SendPoruka',function() {
			
			var naslovPoruke=$('#naslovOdgovor').val();
			var sadrzajPoruke=$('#sadrzajOdgovor').val();
			var korisnickoIme=p.posiljalac;
			
			$.ajax({
				
				type: 'POST',
				url: 'rest/posaljiPoruku/'+korisnickoIme,
				data: JSON.stringify({ naslovPoruke : naslovPoruke, sadrzajPoruke : sadrzajPoruke}),
				contentType: 'application/json',
				success : function() {
					
					alert("Message successfuly sent.");
					ispisiPorukePoslate();
					ispisiPorukePrimljene();
				}, 
				error: function() {
					alert("Cant find reciver.");
				}
					
			});
		});
		
	});
	
	}
	
}

function izmeniPoruku(p) {
	
	return function(event) {
		
		event.preventDefault();
		
		$('#primalacIzmeni').val(p.primalac);
		$('#naslovIzmeni').val(p.naslovPoruke);
		$('#sadrzajIzmeni').val(p.sadrzajPoruke);
		$('#izmeniPorukuModal').modal();
		$('#izmeniPorukuModal').on('click','#UpdatePoruka',function() {
			
			var naslovPoruke = $('#naslovIzmeni').val();
			var sadrzajPoruke = $('#sadrzajIzmeni').val();
			
			var id = p.id;
			var primalac = p.primalac;
			var posiljalac =p.posiljalac;
		
			var datumPoruke = p.datumPoruke;
			var vremePoruke = p.vremePoruke;
			
			
			$.ajax({
				
				type:'POST',
				url : 'rest/izmeniPoruku',
				data: JSON.stringify({id : id,primalac : primalac, posiljalac : posiljalac ,naslovPoruke : naslovPoruke , sadrzajPoruke : sadrzajPoruke, datumPoruke : datumPoruke , vremePoruke : vremePoruke}),
				contentType : 'application/json',
				success : function() {
					
					alert("Successfuly changed message.");
					ispisiPorukePoslate();
				},error: function () {
					
					alert("Something went wrong.");
				}
				
				
			});
		
		});
		
		
	}
	
	
	
	
	
	
}

function ispisiPorukePrimljene() {
	
	
	$.ajax({
		url: 'rest/preuzmiPrimljene'
		
	}).then(function(poruke){
		
		
			
			$('#tabelaPrimljene').html("");
			var po=$('<thead><tr><th scope="col"></th><th scope="col">Tittle</th><th scope="col">Receiver:</th><th scope="col">Date and time:</th><th scope="col">Delete</th></tr></thead><tbody>');
			$('#tabelaPrimljene').append(po);
			var i=0;
			for(p of poruke) {
				var newR= $('<tr><th scope="row">'+i+'</th><td>'+p.naslovPoruke+'</td><td>'+p.primalac+'</td><td>'+p.datumPoruke+" "+p.vremePoruke+'</td><td> <button type="button" class="btn btn-danger"  id="rpR' +i + '"><i class="fa fa-trash" aria-hidden="true"></i></button> </td><td><button class="btn btn-danger" id="showR'+i+'" ><i class="fa fa-eye" aria-hidden="true"></i></button></td></tr> ');
			
				$('#tabelaPrimljene').append(newR);
				$('#rpR'+i).click(obrisiPorukuPrimljena(p));
				$('#showR'+i).click(odgovoriNaPoruku(p));
				i=i+1;
			}
		
		
	});
	
}



function obrisiPorukuPrimljena(p) {
	
	return function(event) {
		event.preventDefault();
	
		var id = p.id;
		var primalac = p.primalac;
		var posiljalac =p.posiljalac;
		var naslovPoruke = p.naslovPoruke;
		var sadrzajPoruke = p.sadrzajPoruke;
		var datumPoruke = p.datumPoruke;
		var vremePoruke = p.vremePoruke;
		
	$.ajax({
		type :'POST',
		url:'rest/obrisiPorukuPrimljena',
		data: JSON.stringify({id : id,primalac : primalac, posiljalac : posiljalac ,naslovPoruke : naslovPoruke , sadrzajPoruke : sadrzajPoruke, datumPoruke : datumPoruke , vremePoruke : vremePoruke}),
		contentType : 'application/json',
		success : function() {
			alert("Successfuly deleted message.");

			ispisiPorukePrimljene();
			
		},
		error : function() {
			
			alert("Can't find sender or reciever.Please try again.");
		}
	});
	
	}
}


function pogledajPorukuPoslatu(p) {
	
	return function(event) {
		event.preventDefault();
		
		/*      <label>Sent to:</label>
        <input type="text" readonly id="primalacPoslata">
        <label>Message title:</label>
        <input type="text" readonly id="naslovPoslata">
        <label>Message content:</label>
        <textarea rows="5" id="sadrzajPoslata" readonly></textarea>*/
		
		$('#primalacPoslata').val(p.primalac);
		$('#naslovPoslata').val(p.naslovPoruke);
		$('#sadrzajPoslata').val(p.sadrzajPoruke);
		
		
		$('#prikazPorukePoslate').modal();
		
	}
	
	
}

function obrisiPorukuPoslata(p) {
	
	return function(event) {
		event.preventDefault();
		/*
		public String primalac;
		public String posiljalac;
		public String naslovPoruke;
		public String sadrzajPoruke;
		public String datumPoruke;
		public String vremePoruke; */
		var id = p.id;
		var primalac = p.primalac;
		var posiljalac =p.posiljalac;
		var naslovPoruke = p.naslovPoruke;
		var sadrzajPoruke = p.sadrzajPoruke;
		var datumPoruke = p.datumPoruke;
		var vremePoruke = p.vremePoruke;
		
	$.ajax({
		type :'POST',
		url:'rest/obrisiPorukuPoslata',
		data: JSON.stringify({id : id, primalac : primalac, posiljalac : posiljalac ,naslovPoruke : naslovPoruke , sadrzajPoruke : sadrzajPoruke, datumPoruke : datumPoruke , vremePoruke : vremePoruke}),
		contentType : 'application/json',
		success : function() {
			alert("Successfuly deleted message.");
			ispisiPorukePoslate();
		
		},
		error : function() {
			
			alert("Can't find sender or reciever.Please try again.");
		}
	});
	
	}
}
function posaljiPoruku() {
	
	return function(event) {
		event.preventDefault();
		var naslovPoruke=$('#nazivPoruke').val();
		var sadrzajPoruke=$('#sadrzajPoruke').val();
		
		var korisnickoIme = $('#listaSvihPrimalaca option:selected').text();
		
		
		$.ajax({
			
			type: 'POST',
			url: 'rest/posaljiPoruku/'+korisnickoIme,
			data: JSON.stringify({ naslovPoruke : naslovPoruke, sadrzajPoruke : sadrzajPoruke}),
			contentType: 'application/json',
			success : function() {
				
				alert("Message successfuly sent.");
				ispisiPorukePoslate();
				ispisiPorukePrimljene();
			}, 
			error: function() {
				alert("Cant find reciver.");
			}
				
			
			
		});
	}
	
	
	
}