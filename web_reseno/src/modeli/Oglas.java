package modeli;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Oglas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String naziv;
	public String kupac;
	public Integer omiljeni;
	public Double cena;
	public String opis;
	public Integer brojPozOcena;
	public Integer brojNegOcena;
	public String slika;
	public String datumPostavljanja;
	public String datumIsticanja;
	public String aktivan;
	public String nazivKategorije;
	public String grad;
	public String prodavacUsername;
	
	protected ArrayList<Recenzija> listaRecenzija= new ArrayList<Recenzija>();
	
	public Oglas(String naziv, Double cena, String opis, Integer brojPozOcena, Integer brojNegOcena, String slika,
			 String datumIsticanja, String aktivan, String grad,String nazivKategorije,String prodavacUsername,
			ArrayList<Recenzija> listaRecenzija) {
		this.omiljeni =0;
		listaRecenzija= new ArrayList<Recenzija> ();
		this.kupac ="";
		this.nazivKategorije=nazivKategorije;		
		this.naziv = naziv;
		this.cena = cena;
		this.opis = opis;
		this.brojPozOcena = brojPozOcena;
		this.brojNegOcena = brojNegOcena;
		this.slika = slika;
		this.datumPostavljanja = getVremeIDatum();
		this.datumIsticanja = datumIsticanja;
		this.aktivan = aktivan;
		this.grad = grad;
		this.listaRecenzija = listaRecenzija;
		this.prodavacUsername=prodavacUsername;
	}
	
	public void povecaj() {
		omiljeni=omiljeni+1;
		
	}
	// DATUM i VREME
		private String getVremeIDatum() {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			String datumIVreme = (formatter.format(date)).toString();
			String ret[] = datumIVreme.split(" ");
			
			return ret[0];
		}
	
	public Oglas () {
		
		listaRecenzija = new ArrayList<Recenzija> ();
		
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public Double getCena() {
		return cena;
	}

	public void setCena(Double cena) {
		this.cena = cena;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public Integer getBrojPozOcena() {
		return brojPozOcena;
	}

	public void setBrojPozOcena(Integer brojPozOcena) {
		this.brojPozOcena = brojPozOcena;
	}

	public Integer getBrojNegOcena() {
		return brojNegOcena;
	}

	public void setBrojNegOcena(Integer brojNegOcena) {
		this.brojNegOcena = brojNegOcena;
	}

	public String getSlika() {
		return slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
	}

	public String getDatumPostavljanja() {
		return datumPostavljanja;
	}

	public void setDatumPostavljanja(String datumPostavljanja) {
		this.datumPostavljanja = datumPostavljanja;
	}

	public String getDatumIsticanja() {
		return datumIsticanja;
	}

	public void setDatumIsticanja(String datumIsticanja) {
		this.datumIsticanja = datumIsticanja;
	}

	public String getAktivan() {
		return aktivan;
	}

	public void setAktivan(String aktivan) {
		this.aktivan = aktivan;
	}

	public String getGrad() {
		return grad;
	}

	public void setGrad(String grad) {
		this.grad = grad;
	}

	public ArrayList<Recenzija> getListaRecenzija() {
		return listaRecenzija;
	}

	public void setListaRecenzija(ArrayList<Recenzija> listaRecenzija) {
		this.listaRecenzija = listaRecenzija;
	}
	
	
	public boolean dodajRecenziju(Recenzija r) {
		
		if(!listaRecenzija.contains(r)) {
			
			listaRecenzija.add(r);
			return true;
		}
		return false;
		
	}
	
	public boolean izbrisiRecenziju(Recenzija r) {
		
		for(int i=0;i<listaRecenzija.size();i++) {
				
			if(listaRecenzija.get(i).naslovRecenzije.equals(r.naslovRecenzije)) {
				if(listaRecenzija.get(i).getSadrzajRecenzije().equals(r.sadrzajRecenzije)) 
				listaRecenzija.remove(i);
			}
		}
		return false;
		
	}
	
	
	
}
