package modeli;

import java.io.Serializable;

public class Recenzija implements Serializable {
	
	public Oglas oglas;
	public Korisnik recezent;
	public String naslovRecenzije;
	public String sadrzajRecenzije;
	public String slika;
	public String nazivOglasa;
	public String nazivKategorije;
	public String tacnostOglasa;
	public String ispostovanostDogovora;
	
	public Recenzija(Oglas oglas,String nazivOglasa, String nazivKategorije, Korisnik recezent, String naslovRecenzije, String sadrzajRecenzije, String slika,
			String tacnostOglasa, String ispostovanostDogovora) {
		
		this.oglas = oglas;
		this.recezent = recezent;
		this.naslovRecenzije = naslovRecenzije;
		this.sadrzajRecenzije = sadrzajRecenzije;
		this.slika = slika;
		this.tacnostOglasa = tacnostOglasa;
		this.ispostovanostDogovora = ispostovanostDogovora;
		this.nazivKategorije=nazivKategorije;
		this.nazivOglasa=nazivOglasa;
	}
	
	
	public Recenzija() {};

	public Oglas getOglas() {
		return oglas;
	}

	public void setOglas(Oglas oglas) {
		this.oglas = oglas;
	}

	public Korisnik getRecezent() {
		return recezent;
	}

	public void setRecezent(Korisnik recezent) {
		this.recezent = recezent;
	}

	public String getNaslovRecenzije() {
		return naslovRecenzije;
	}

	public void setNaslovRecenzije(String naslovRecenzije) {
		this.naslovRecenzije = naslovRecenzije;
	}

	public String getSadrzajRecenzije() {
		return sadrzajRecenzije;
	}

	public void setSadrzajRecenzije(String sadrzajRecenzije) {
		this.sadrzajRecenzije = sadrzajRecenzije;
	}

	public String getSlika() {
		return slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
	}

	public String getTacnostOglasa() {
		return tacnostOglasa;
	}

	public void setTacnostOglasa(String tacnostOglasa) {
		this.tacnostOglasa = tacnostOglasa;
	}

	public String getIspostovanostDogovora() {
		return ispostovanostDogovora;
	}

	public void setIspostovanostDogovora(String ispostovanostDogovora) {
		this.ispostovanostDogovora = ispostovanostDogovora;
	}

	
	
	
	

}
