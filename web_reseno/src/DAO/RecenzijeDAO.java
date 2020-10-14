package DAO;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import modeli.Recenzija;

public class RecenzijeDAO {

	public HashMap<String,Recenzija> recenzije = new HashMap<>();

	public HashMap<String, Recenzija> getRecenzije() {
		return recenzije;
	}

	public void setRecenzije(HashMap<String, Recenzija> recenzije) {
		this.recenzije = recenzije;
	}

	public RecenzijeDAO(String contextPath) {
		loadRecenzije(contextPath);
	
	}
	
	
	public void loadRecenzije(String contextPath) {
		try {
			File file = new File(contextPath + "/recenzije.json");
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			Recenzija[] recenzijeList = objectMapper.readValue(file, Recenzija[].class);
			
			for(Recenzija recenzija : recenzijeList) {
				if(!recenzije.containsKey(recenzija.naslovRecenzije+recenzija.nazivOglasa)) 
					recenzije.put(recenzija.naslovRecenzije+recenzija.nazivKategorije, recenzija);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveRecenzije(String contextPath) {
		try {
			File file = new File(contextPath + "/recenzije.json");
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			
			objectMapper.writeValue(file, recenzije.values());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
