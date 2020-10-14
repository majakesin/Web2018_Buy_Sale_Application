package DAO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import modeli.Oglas;
import modeli.Recenzija;

/* Oglas(String naziv, Double cena, String opis, Integer brojPozOcena, Integer brojNegOcena, String slika,
			 String datumIsticanja, String aktivan, String grad,String nazivKategorije,
			ArrayList<Recenzija> listaRecenzija) */


public class OglasiDAO {

	public  ArrayList<Oglas>  oglasiPoruceni = new ArrayList<Oglas>();
	public HashMap<String, Oglas> oglasi=new HashMap<>();
	
	public OglasiDAO (String contextPath) {
		
		loadOglasi(contextPath);
		
		/*
		Oglas oglas1 = new Oglas("SHIRT",1299.00,"This is a man shirt.",0,0,"muskaMajica.jpg","30/9/2019","1","Novi Sad","MEN CLOTHES",new ArrayList<Recenzija>());
		Oglas oglas2 = new Oglas("PANTS",2399.00,"This is a man pants.",0,0,"hlaceMuske.jpg","30/9/2019","1","Novi Sad","MEN CLOTHES",new ArrayList<Recenzija>());
		Oglas oglas3 = new Oglas("SUIT",2000.00,"This is a man suit.",0,0,"sakoMuski.jpg","30/9/2019","1","Novi Sad","MEN CLOTHES",new ArrayList<Recenzija>());

		oglasi.put("SHIRTMEN CLOTHES",oglas1);
		oglasi.put("PANTSMEN CLOTHES", oglas2);
		oglasi. */
		
	}
	
	public void loadOglasi(String contextPath) {
		try {
			File file = new File(contextPath + "/oglasi.json");
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			
			Oglas[] oglasiList = objectMapper.readValue(file, Oglas[].class);
			
			for(Oglas oglas : oglasiList) {
				if(!oglasi.containsKey(oglas.naziv+oglas.nazivKategorije))
					oglasi.put(oglas.naziv+oglas.nazivKategorije, oglas);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveOglasi(String contextPath) {
		try {
			File file = new File(contextPath + "/oglasi.json");
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			
			objectMapper.writeValue(file, oglasi.values());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Collection<Oglas> getOglasi() {
		return oglasi.values();
	}
	public HashMap<String,Oglas> getOglasiMapa() {
		
		return oglasi;
	}

	public void setOglasi(HashMap<String, Oglas> oglasi) {
		this.oglasi = oglasi;
	}
	
	
}
