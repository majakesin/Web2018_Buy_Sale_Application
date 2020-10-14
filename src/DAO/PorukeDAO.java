package DAO;

import java.util.HashMap;

import modeli.Poruka;

public class PorukeDAO {

	private HashMap<String, Poruka> poruke=new HashMap<>();

	
	public PorukeDAO () {
		
		
	}
	public HashMap<String, Poruka> getPoruke() {
		return poruke;
	}

	public void setPoruke(HashMap<String, Poruka> poruke) {
		this.poruke = poruke;
	}
	
	
}
