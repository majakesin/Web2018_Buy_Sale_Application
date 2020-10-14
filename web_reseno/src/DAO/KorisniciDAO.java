package DAO;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


import modeli.Korisnik;


public class KorisniciDAO {

	public HashMap<String, Korisnik> korisnici=new HashMap<>();

	//pretpostavljam da im je kljuc korisnicko ime :)
	public KorisniciDAO(String contextPath){
		
		korisnici.put("admin", new Korisnik("admin","administrator","admin","Pero","Peric","","",""));
		/*korisnici.put("pera", new Korisnik("pera","customer","pera","Pero","Peric","","",""));
		korisnici.put("maja", new Korisnik("maja","salesman","maja","Pero","Peric","","","")); */
		loadKorisnik(contextPath);
	}
	
	
	
	public void loadKorisnik(String contextPath) {
		try {
			File file = new File(contextPath + "/korisnici.json");
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			
			Korisnik[] korisniciK = objectMapper.readValue(file, Korisnik[].class);
			
			for(Korisnik korisnik : korisniciK) {
				if(!korisnici.containsKey(korisnik.getUsername())) {
					korisnici.put(korisnik.getUsername(), korisnik);
				}
			}
		}	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void saveKorisnik(String contextPath) {
		try {
			File file = new File(contextPath + "/korisnici.json");
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			
			objectMapper.writeValue(file, korisnici.values());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HashMap<String, Korisnik> getKorisnici() {
		return korisnici;
	}

	public void setKorisnici(HashMap<String, Korisnik> korisnici) {
		this.korisnici = korisnici;
	}
	
	
	public Korisnik find(Korisnik k)
	{
		for(Korisnik user:korisnici.values())
		{
			
			if(k.getKorisnickoIme().equals(user.getKorisnickoIme()))
			{
				
					System.out.println(k.getKorisnickoIme()+k.getLozinka());
					return user;
				
				
			}
		}
		return null;
		
		
	}
	
	public Collection<Korisnik> vratiKorisnike() {
		
		return korisnici.values();
	}
}
