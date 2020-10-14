package DAO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import modeli.Kategorija;
import modeli.Korisnik;
import modeli.Oglas;
import modeli.Recenzija;

public class KategorijeDAO {

	private HashMap<String, Kategorija> kategorije=new HashMap<>();
	
	public KategorijeDAO (String contextPath ) {
		
		loadKategorija(contextPath);
		
		/*
		
		kategorije.put("WOMEN CLOTHES", new Kategorija("WOMEN CLOTHES",
				"Here you can find all types of clothes for women with good price","odecaZene.jpg"));
		kategorije.put("MEN CLOTHES",new Kategorija("MEN CLOTHES",
				"Here you can find all types of clothes for men with good price","odecaMuski.jpg"));
		kategorije.put("KIDS CLOTHES",new Kategorija("KIDS CLOTHES",
				"Here you can find all types of clothes for kids with good price","odecaDeca.jpg"));
	
		kategorije.put("WOMEN SHOES",new Kategorija("WOMEN SHOES",
				"Here you can find all types of shoes for women with good price","obucaZene.jpg"));
		kategorije.put("MEN SHOES",new Kategorija("MEN SHOES",
				"Here you can find all types of shoes for men with good price","obucaMuski.jpg"));
		
		kategorije.put("KIDS SHOES",new Kategorija("KIDS SHOES",
				"Here you can find all types of shoes for kids with good price","obucaDeca.jpg")); */
	}

	
	
	
	public void loadKategorija(String contextPath) {
		try {
			File file = new File(contextPath + "/kategorije.json");
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			
			Kategorija[] kategorijaList = objectMapper.readValue(file, Kategorija[].class);
			
			for(Kategorija k : kategorijaList) {
				kategorije.put(k.naziv, k);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveKategorija(String contextPath) {
		try {
			File file = new File(contextPath + "/kategorije.json");
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			objectMapper.writeValue(file, kategorije.values());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public HashMap<String, Kategorija> getKategorije() {
		return kategorije;
	}

	public void setKategorije(HashMap<String, Kategorija> kategorije) {
		this.kategorije = kategorije;
	}
	
	public Kategorija find(Kategorija k)
	{
		for(Kategorija user:kategorije.values())
		{
			
			if(k.getNaziv().equals(user.getNaziv()))
			{
				
					
					return user;
				}
				
			}
		
		return null;
		
		
	} 
	public Kategorija findString (String s)
	{
		for(Kategorija user:kategorije.values())
		{
			
			if(user.getNaziv().equals(s))
			{
				
					
					return user;
				}
				
			}
		
		return null;
		
		
	} 
	public Collection<Kategorija> getKolekcije() {
		
			return kategorije.values();
		
	
		}


}
