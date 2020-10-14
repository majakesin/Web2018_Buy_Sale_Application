package modeli;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class Korisnik implements Serializable {

	
	public String username;
	public boolean poruka= false;
	public String password;
	public String ime;
	public String prezime;
	public String uloga;
	public String kontaktTelefon;
	public String grad;
	public String email;
	public String datumRegistracije;
	//kupac
	public ArrayList<Oglas> oglasiPoruceni= new ArrayList<Oglas>();
	public ArrayList<Oglas> oglasiDostavljeni=new ArrayList<Oglas>();
	public ArrayList<Oglas> omiljeniOglasi=new ArrayList<Oglas>();
	public ArrayList<Recenzija> listaOstavljenihRecenzija = new ArrayList<Recenzija>();
	
	//prodavac
	public Integer brojLajkova;
	public Integer brojDislajkova;
	
	public ArrayList<Oglas> objavljeniOglasi=new ArrayList<Oglas>();
	public ArrayList<Oglas> isporuceniOglasi=new ArrayList<Oglas>();
	
	
	
	//poruke
	public ArrayList<Poruka> porukePoslate=new ArrayList<Poruka>();
	public ArrayList<Poruka> porukePrimljene=new ArrayList<Poruka>();
	
	public Korisnik(String korisnickoIme, String lozinka) {
		this.brojDislajkova=0;
		this.brojLajkova=0;
		this.username=korisnickoIme;
		this.password=lozinka;
		this.ime = "";
		this.prezime = "";
		this.uloga = Uloga.KUPAC;
		this.kontaktTelefon = "";
		this.grad = "";
		this.email = "";
		this.datumRegistracije = "";
		
		oglasiPoruceni= new ArrayList<Oglas> ();
		oglasiDostavljeni = new ArrayList<Oglas> ();
		omiljeniOglasi = new ArrayList<Oglas> ();
		porukePrimljene=new ArrayList<Poruka>();
		
	}
	
	public void izbrisiPoruceni(String naziv) {
	
		for(int i=0;i<oglasiPoruceni.size();i++) {
			if(oglasiPoruceni.get(i).getNaziv().equals(naziv)) {
				
				oglasiPoruceni.remove(i);
			}
		}
	}
	
	public Korisnik(String korisnickoIme, String lozinka, String ime, String prezime, 
			String kontaktTelefon, String grad, String email) {
		
		this.brojDislajkova=0;
		this.brojLajkova=0;
		this.username = korisnickoIme;
		this.password= lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.uloga = "customer";
		this.kontaktTelefon = kontaktTelefon;
		this.grad = grad;
		this.email = email;
		this.datumRegistracije =getVremeIDatum();
		
		oglasiPoruceni= new ArrayList<Oglas> ();
		oglasiDostavljeni = new ArrayList<Oglas> ();
		omiljeniOglasi = new ArrayList<Oglas> ();
		
		objavljeniOglasi= new ArrayList<Oglas> ();
		isporuceniOglasi = new ArrayList<Oglas> ();
		porukePoslate=new ArrayList<Poruka>();
		porukePrimljene=new ArrayList<Poruka>();
		
	}
	public Korisnik(String korisnickoIme,String uloga, String lozinka, String ime, String prezime, 
			String kontaktTelefon, String grad, String email) {
		
		
		this.username = korisnickoIme;
		this.password= lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.uloga = uloga;
		this.kontaktTelefon = kontaktTelefon;
		this.grad = grad;
		this.email = email;
		this.datumRegistracije =getVremeIDatum();
		
		oglasiPoruceni= new ArrayList<Oglas> ();
		oglasiDostavljeni = new ArrayList<Oglas> ();
		omiljeniOglasi = new ArrayList<Oglas> ();
	
		objavljeniOglasi= new ArrayList<Oglas> ();
		isporuceniOglasi = new ArrayList<Oglas> ();
		
		porukePrimljene=new ArrayList<Poruka>();
		porukePoslate=new ArrayList<Poruka>();
	
	}


	private String getVremeIDatum() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String datumIVreme = (formatter.format(date)).toString();
		String ret[] = datumIVreme.split(" ");
		
		return ret[0];
	}
	
	
	public ArrayList<Oglas> getOglasiPoruceni() {
		return oglasiPoruceni;
	}


	public void setOglasiPoruceni(ArrayList<Oglas> oglasiPoruceni) {
		this.oglasiPoruceni = oglasiPoruceni;
	}


	public ArrayList<Oglas> getOglasiDostavljeni() {
		return oglasiDostavljeni;
	}


	public void setOglasiDostavljeni(ArrayList<Oglas> oglasiDostavljeni) {
		this.oglasiDostavljeni = oglasiDostavljeni;
	}


	public ArrayList<Oglas> getOmiljeniOglasi() {
		return omiljeniOglasi;
	}


	public void setOmiljeniOglasi(ArrayList<Oglas> omiljeniOglasi) {
		this.omiljeniOglasi = omiljeniOglasi;
	}


	public ArrayList<Oglas> getObjavljeniOglasi() {
		return objavljeniOglasi;
	}


	public void setObjavljeniOglasi(ArrayList<Oglas> objavljeniOglasi) {
		this.objavljeniOglasi = objavljeniOglasi;
	}


	public ArrayList<Oglas> getIsporuceniOglasi() {
		return isporuceniOglasi;
	}


	public void setIsporuceniOglasi(ArrayList<Oglas> isporuceniOglasi) {
		this.isporuceniOglasi = isporuceniOglasi;
	}





	public ArrayList<Poruka> getPorukePoslate() {
		return porukePoslate;
	}

	public void setPorukePoslate(ArrayList<Poruka> porukePoslate) {
		this.porukePoslate = porukePoslate;
	}

	public ArrayList<Poruka> getPorukePrimljene() {
		return porukePrimljene;
	}

	public void setPorukePrimljene(ArrayList<Poruka> porukePrimljene) {
		this.porukePrimljene = porukePrimljene;
	}

	public Korisnik() {}
	
	public String getKorisnickoIme() {
		return username;
	}


	public void setKorisnickoIme(String korisnickoIme) {
		this.username = korisnickoIme;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public Integer getBrojLajkova() {
		return brojLajkova;
	}


	public void setBrojLajkova(Integer brojLajkova) {
		this.brojLajkova = brojLajkova;
	}


	public Integer getBrojDislajkova() {
		return brojDislajkova;
	}


	public void setBrojDislajkova(Integer brojDislajkova) {
		this.brojDislajkova = brojDislajkova;
	}


	public String getLozinka() {
		return password;
	}


	public void setLozinka(String lozinka) {
		this.password = lozinka;
	}


	public String getIme() {
		return ime;
	}


	public void setIme(String ime) {
		this.ime = ime;
	}


	public String getPrezime() {
		return prezime;
	}


	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}


	public String getUloga() {
		return uloga;
	}


	public void setUloga(String uloga) {
		this.uloga = uloga;
	}


	public String getKontaktTelefon() {
		return kontaktTelefon;
	}


	public void setKontaktTelefon(String kontaktTelefon) {
		this.kontaktTelefon = kontaktTelefon;
	}


	public String getGrad() {
		return grad;
	}


	public void setGrad(String grad) {
		this.grad = grad;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getDatumRegistracije() {
		return datumRegistracije;
	}


	public void setDatumRegistracije(String datumRegistracije) {
		this.datumRegistracije = datumRegistracije;
	}
	
	
	public void povecajPoz() {
		
		this.brojLajkova+=1;
	}
	public void povecajNeg() {
		
		this.brojDislajkova+=1;
	}
	
	
}
