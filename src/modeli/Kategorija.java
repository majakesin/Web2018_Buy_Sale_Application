package modeli;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Kategorija {

	public String naziv;
	public String opis;
	public String urlSlike;
	public List<Oglas> oglasi;
	
	public Kategorija(String naziv, String opis,String urlSlike, List<Oglas> oglasi) {
		
		oglasi= new ArrayList<Oglas> ();
		
		
		this.naziv = naziv;
		this.opis = opis;
		this.urlSlike=urlSlike;
		this.oglasi=oglasi;
		
	}
	
public Kategorija(String naziv, String opis,String urlSlike) {
		
		oglasi= new ArrayList<Oglas> ();
		
		
		this.naziv = naziv;
		this.opis = opis;
		this.urlSlike=urlSlike;
}
		
	
	public String getUrlSlike() {
	return urlSlike;
}
public void setUrlSlike(String urlSlike) {
	this.urlSlike = urlSlike;
}
	public Kategorija() 
	{
		oglasi= new ArrayList<Oglas>();
		
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public Collection<Oglas> getOglasi() {
		
	return	oglasi;
	}

	public void setOglasi(List<Oglas> oglasi) {
		this.oglasi = oglasi;
	}
	
	
	public boolean dodajOglas(Oglas o) {
		
		if(!oglasi.contains(o)) {
			
			oglasi.add(o);
			return true;
		}
		return false;
	}
	
	public boolean izbrisiOglas(String naziv) 
	{
		ArrayList<Oglas> oglasiA= new ArrayList<Oglas> (oglasi);
		for(int i=0;i<oglasiA.size();i++) {
			
			if(oglasiA.get(i).getNaziv().equals(naziv)) {
				
				oglasiA.remove(i);
				oglasi.clear();
				oglasi.addAll(oglasiA);
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Oglas> ListToArray() {
		
		ArrayList<Oglas> temp = new ArrayList<Oglas> ();
		

			
			temp.addAll(this.oglasi);
			return temp;
	
		
	}
	
}
