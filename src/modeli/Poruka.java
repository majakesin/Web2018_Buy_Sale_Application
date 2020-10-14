package modeli;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Poruka {
	public String id;
	public String primalac;
	public String posiljalac;
	public String naslovPoruke;
	public String sadrzajPoruke;
	public String datumPoruke;
	public String vremePoruke;
	
	
	
	public Poruka( String naslovPoruke, String sadrzajPoruke
			) {
	
		String ret[] = getVremeIDatum().split(" ");
		id="";
		String datumPoruke = ret[0];
		String vremePoruke = ret[1];
		this.primalac="";
		this.posiljalac = "";
		this.naslovPoruke = naslovPoruke;
		this.sadrzajPoruke = sadrzajPoruke;
		this.datumPoruke = datumPoruke;
		this.vremePoruke = vremePoruke;
	}
	
	public Poruka() {
		String ret[] = getVremeIDatum().split(" ");
		id="";
		String datumPoruke = ret[0];
		String vremePoruke = ret[1];
		
	}


	
	private String getVremeIDatum() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String datumIVreme = (formatter.format(date)).toString();
		System.out.println(datumIVreme);
		
		return datumIVreme;
	}
	




	public String getNaslovPoruke() {
		return naslovPoruke;
	}


	public void setNaslovPoruke(String naslovPoruke) {
		this.naslovPoruke = naslovPoruke;
	}


	public String getSadrzajPoruke() {
		return sadrzajPoruke;
	}


	public void setSadrzajPoruke(String sadrzajPoruke) {
		this.sadrzajPoruke = sadrzajPoruke;
	}


	public String getDatumPoruke() {
		return datumPoruke;
	}


	public void setDatumPoruke(String datumPoruke) {
		this.datumPoruke = datumPoruke;
	}


	public String getVremePoruke() {
		return vremePoruke;
	}


	public void setVremePoruke(String vremePoruke) {
		this.vremePoruke = vremePoruke;
	}
	
	
	
	
	
	
}
