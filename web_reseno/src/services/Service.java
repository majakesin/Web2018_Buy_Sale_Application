package services;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import DAO.KategorijeDAO;
import DAO.KorisniciDAO;
import DAO.OglasiDAO;
import DAO.PorukeDAO;
import DAO.RecenzijeDAO;
import modeli.Kategorija;
import modeli.Korisnik;
import modeli.Oglas;
import modeli.Poruka;
import modeli.Recenzija;





@Path("")
public class Service {
	

	@Context
	ServletContext ctx;
	
	
	
	/* Napomena svaki put kad sam nesto dodavala,menjala ili brisala onda moram da ponovo 
	 * sacuvam u datotetku. :) 
	 */
	
	
	@POST
	@Path("/loginn")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response ulogujSe(Korisnik k, @Context HttpServletRequest request) {
		
		
		KorisniciDAO korisnici = (KorisniciDAO) ctx.getAttribute("KorisniciDAO");
		
		
		Korisnik korisnik = korisnici.find(k);


		if (korisnik == null) {
			System.out.println("nije uspjesno prijavljen.");
			return Response.status(400).build();

		}
		
		// za ovo nema potrebe ajde pokusacu bez ovoga da vidim da li radi :)
		//ctx.setAttribute("KorisniciDAO", korisnici);
		HttpSession session = request.getSession();
		
		session.setAttribute("user", korisnik);
		if(korisnik.uloga.equals("customer")) {
		
			
			session.setAttribute("cart", new ArrayList<Oglas>());
			session.setAttribute("omiljeni", new ArrayList<Oglas>());
		}
		if(korisnik.uloga.equals("salesman")) {
			
			OglasiDAO oglasi = (OglasiDAO) ctx.getAttribute("OglasiDAO");
			
			// Koliko sam skontala ja sam ovde preuzimala broj poz i negativnih ocjena iz oglasiDTO i
			//setovala te vrijednosti kod liste objavljenih oglasa (moglo je i bolje da se uradi :) )
			
			
		}
		return Response.status(200).build();
	}

	
	@POST
	@Path("/registracija")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registracija(Korisnik k, @Context HttpServletRequest request) {
		
		//preuzela sa sesije atribut korisnici pogledala da li postoji vec neki 
		//kontam sa istim k. imenom i sifrom i ako postoji onda vraca error js, da
		//kaze da je invalidno,da nesto promjeni
		String contextPath = ctx.getRealPath("");
		KorisniciDAO users = (KorisniciDAO) ctx.getAttribute("KorisniciDAO");
		System.out.println("dsdssd");
	
		Korisnik korisnik = users.find(k);

		if (korisnik != null) {
			return Response.status(400).build();
		}
		
	

		users.getKorisnici().put(k.getKorisnickoIme(), k);

		ctx.setAttribute("KorisniciDAO", users);
		//onda ovde sacuva :) u datotetku
		users.saveKorisnik(contextPath);
		return Response.status(200).build();
		
	}

	@POST
	@Path("/dodajKategoriju")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response dodajKategoriju(Kategorija k, @Context HttpServletRequest request) {
		
		String contextPath = ctx.getRealPath("");
		KategorijeDAO users = (KategorijeDAO) ctx.getAttribute("KategorijeDAO");
	
		
		
		//ako postoji vec jedna takva kategorija nemoj dodati :)
		
		if(users.find(k)!=null) {
			return Response.status(400).build();
		}

		//ovde treba setovati to je ok. Izmjenila sam sadrzaj atributa
		users.getKategorije().put(k.getNaziv(), k);

		ctx.setAttribute("KategorijeDAO", users);
		users.saveKategorija(contextPath);

		return Response.status(200).build();
	}
	
	
	@POST
	@Path("/izmeniPoruku")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response izmeniPoruku(Poruka p, @Context HttpServletRequest request) {
		String contextPath = ctx.getRealPath("");
		KorisniciDAO korisniciDAO = (KorisniciDAO) ctx.getAttribute("KorisniciDAO");
		HttpSession session = request.getSession();
		//uzme username primaoca i pronadje ga preko mape svih primaoca :)
		Korisnik primalac = korisniciDAO.getKorisnici().get(p.primalac);
		//posaljilac je user -> kaze ako nema primaoca ili posaljioca vrati gresku :)
		Korisnik user = (Korisnik) session.getAttribute("user");
		
		if(primalac== null || user == null) {
			return Response.status(400).build();
		}
		
	
		//menjaju se poruke i kod posiljaoca i kod primaoca :) To je ok.
		
		for(int i=0;i<user.getPorukePoslate().size();i++) {
			
			if(user.getPorukePoslate().get(i).id.equals(p.id)) {
				user.getPorukePoslate().get(i).setNaslovPoruke(p.naslovPoruke);
				user.getPorukePoslate().get(i).setSadrzajPoruke(p.sadrzajPoruke);
				
			}
		}
		
		for(int i=0;i<primalac.getPorukePrimljene().size();i++) {
			
			if(primalac.getPorukePrimljene().get(i).id.equals(p.id)) {
				primalac.getPorukePrimljene().get(i).setNaslovPoruke(p.naslovPoruke);
				primalac.getPorukePrimljene().get(i).setSadrzajPoruke(p.sadrzajPoruke);
				
			}
		}
		korisniciDAO.saveKorisnik(contextPath);
		return Response.status(200).build();
		
	}
	@POST
	@Path("/posaljiPoruku/{korisnickoIme}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response posaljiPoruku(@PathParam("korisnickoIme") String korisnickoIme, Poruka p, @Context HttpServletRequest request) {
		String contextPath = ctx.getRealPath("");
		KorisniciDAO korisniciDAO = (KorisniciDAO) ctx.getAttribute("KorisniciDAO");
		System.out.println(korisnickoIme);
		Korisnik primalac = korisniciDAO.getKorisnici().get(korisnickoIme);
		if(primalac==null) {
			return Response.status(400).build();
			
		}
		//ovo je sve ok :)
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		Poruka poruka = new Poruka();
		poruka.id=UUID.randomUUID().toString();
		poruka.primalac=primalac.username;
		poruka.posiljalac=user.username;
		poruka.naslovPoruke=p.naslovPoruke;
		poruka.sadrzajPoruke = p.sadrzajPoruke;
		
		
		user.getPorukePoslate().add(poruka);
		
		(korisniciDAO).getKorisnici().get(korisnickoIme).getPorukePrimljene().add(poruka);
		System.out.println("jesam li ovde? poruka");
		korisniciDAO.saveKorisnik(contextPath);
		return Response.status(200).build();
		
		
	}
	
	
	
	
	
	@POST
	@Path("/kupljenProizvod")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addToCart(Oglas o,@Context  HttpServletRequest request) {
		
		String contextPath = ctx.getRealPath("");
		OglasiDAO oglasi = (OglasiDAO) ctx.getAttribute("OglasiDAO");
		
		KategorijeDAO kategorije = (KategorijeDAO) ctx.getAttribute("KategorijeDAO");
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		o.kupac=user.username;
		o.aktivan="false";
		Oglas pom=oglasi.getOglasiMapa().get(o.naziv+o.nazivKategorije);
		pom.aktivan="false";
		
		System.out.println(user.ime +user.uloga);
		
		if(user != null) {
			if(user.uloga.equals("customer")) {
				
				
				List<Oglas> stavkePorudzbine= (List<Oglas>) session.getAttribute("cart");
				if(stavkePorudzbine.add(o)) {
				
				
					
					oglasi.getOglasiMapa().remove(o.naziv+o.nazivKategorije);
					oglasi.getOglasiMapa().put(o.naziv+o.nazivKategorije, pom);
					
					kategorije.getKategorije().get(o.nazivKategorije).izbrisiOglas(o.naziv);
					kategorije.getKategorije().get(o.nazivKategorije).dodajOglas(pom);
					oglasi.oglasiPoruceni.add(o);
					kategorije.saveKategorija(contextPath);
					session.setAttribute("cart", stavkePorudzbine);
					kategorije.saveKategorija(contextPath);
					oglasi.saveOglasi(contextPath);
					
					return Response.status(200).build();
				} else {
					return Response.status(400).build();
				}		
			}
			else {
				return Response.status(400).build();
			}
		}
		else {
			return Response.status(400).build();
		}
	
		
	}
	
	
	
	
	
	
	//standardna get metoda uzima proizvode za ispis u tabelu prilikom 
	//ucitavanja stranice za korpu
	@GET
	@Path("/uzmiProizvode")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Oglas> showCartItems(@Context  HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");

		List<Oglas> stavkePorudzbine = new ArrayList<Oglas>();
		
		if(user != null) {
			if(user.uloga.equals("customer")) {
				stavkePorudzbine = (List<Oglas>) session.getAttribute("cart");
				
				}
			}
		
		
		return stavkePorudzbine;
	}
	
	
	
	//mislim da je to preuzimanje kad zelim da namjestim kome salje poruku admin
	//on moze svima sem sebi :),kupac moze prodavacima, a prodavac kupcima opet obicna get metoda koja vraca listu
	@GET
	@Path("/uzmiPrimaoce")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Korisnik> preuzmiPrimaoce(@Context  HttpServletRequest request) {
		
		KorisniciDAO korisnici = (KorisniciDAO) ctx.getAttribute("KorisniciDAO");
		ArrayList<Korisnik> listaPrimaoca = new ArrayList<Korisnik> ();
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");

		if(user.uloga.equals("administrator")) {
			
		
			
			listaPrimaoca.addAll(korisnici.getKorisnici().values());
			for(int i=0;i<listaPrimaoca.size();i++) {
				if(listaPrimaoca.get(i).username.equals(user.username)) {
					listaPrimaoca.remove(i);
				}
			}
			
			return listaPrimaoca;
		}
		if(user.uloga.equals("salesman")) {
			
			ArrayList<Korisnik> pom= new ArrayList<Korisnik> (korisnici.getKorisnici().values());
			
			for(int i=0;i<pom.size();i++) {
				
				if(pom.get(i).uloga.equals("customer")) {
					listaPrimaoca.add(pom.get(i));
				}
			}
			return listaPrimaoca;
			
			
		}
		if(user.uloga.equals("customer")) {
			
			ArrayList<Korisnik> pom= new ArrayList<Korisnik> (korisnici.getKorisnici().values());
			
			for(int i=0;i<pom.size();i++) {
				
				if(pom.get(i).uloga.equals("salesman")) {
					listaPrimaoca.add(pom.get(i));
				}
			}
			return listaPrimaoca;
			
			
		}
		return null;
	}
	
	
	
	//vracam recenzije u odnosu na oglas :)
	
	@GET
	@Path("/preuzmiRecenzija/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Recenzija> prikaziRecenzije (@PathParam ("id") String id,@Context  HttpServletRequest request) {
		
		
		OglasiDAO oglasi = (OglasiDAO) ctx.getAttribute("OglasiDAO");
		Oglas o=oglasi.getOglasiMapa().get(id);
		
		if(o!=null) {
			return o.getListaRecenzija();
		}
		return null;
	}
	
	
	//vracam primljene poruke
	
	@GET
	@Path("/preuzmiPrimljene")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Poruka> preuzmiPrimljenePoruke (@Context  HttpServletRequest request) {
		
		HttpSession sesija = request.getSession();
		Korisnik korisnik = (Korisnik) sesija.getAttribute("user");
		
		return korisnik.getPorukePrimljene();
	}
	
	//standardno brisanje poruke
	
	@POST
	@Path("/obrisiPorukuPoslata")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response obrisiPoruku(Poruka p, @Context HttpServletRequest request) {
		String contextPath = ctx.getRealPath("");
		KorisniciDAO korisnici = (KorisniciDAO) ctx.getAttribute("KorisniciDAO");
		
		Korisnik primalac = korisnici.getKorisnici().get(p.primalac);
		Korisnik posiljalac = korisnici.getKorisnici().get(p.posiljalac);
		
		
		if(primalac== null || posiljalac == null) {
			return Response.status(400).build();
		}
		
	
		
		for(int i=0;i<posiljalac.getPorukePoslate().size();i++) {
			
			if(posiljalac.getPorukePoslate().get(i).id.equals(p.id)) {
				posiljalac.getPorukePoslate().remove(i);
			}
		}
		korisnici.saveKorisnik(contextPath);
		return Response.status(200).build();
		
		
	}
	
	//standardno brisanje primljene
	
	@POST
	@Path("/obrisiPorukuPrimljena")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response obrisiPorukuPrimljena(Poruka p, @Context HttpServletRequest request) {
		String contextPath = ctx.getRealPath("");
		KorisniciDAO korisnici = (KorisniciDAO) ctx.getAttribute("KorisniciDAO");
		
		Korisnik primalac = korisnici.getKorisnici().get(p.primalac);
		Korisnik posiljalac = korisnici.getKorisnici().get(p.posiljalac);
		
		
		if(primalac== null || posiljalac == null) {
			return Response.status(400).build();
		}
		
	
		
		for(int i=0;i<primalac.getPorukePrimljene().size();i++) {
			
			if(primalac.getPorukePrimljene().get(i).id.equals(p.id)) {
				primalac.getPorukePrimljene().remove(i);
			}
		}
		korisnici.saveKorisnik(contextPath);
		return Response.status(200).build();
		
		
	}
	
	//obican get za ispis u tabelu poslatih poruka
	
	@GET
	@Path("/preuzmiPoslate")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Poruka> preuzmiPoslatePoruke (@Context  HttpServletRequest request) {
		
		HttpSession sesija = request.getSession();
		Korisnik korisnik = (Korisnik) sesija.getAttribute("user");
		
		return korisnik.getPorukePoslate();
	}
	
	
	//preuzima sve oglase
	@GET
	@Path("/preuzmiSveOglase")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Oglas> preuzmiSveOglase (@Context  HttpServletRequest request) {
		
		
		OglasiDAO oglasi = (OglasiDAO) ctx.getAttribute("OglasiDAO");
		
		return oglasi.getOglasi();
	}
	
	//ovo je za profil kupca (preuzme ukupne lajkove i dislajkove
	//tako sto sabira lajkove i dislajkove pojedinacnih oglasa koje je on objavio
	//vraca listu sa dve vrijednosti da u jednoj js funkciji to moze da odradi ispis :)
	@GET
	@Path("/preuzmiLajkove")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> prodavacPozNegOcena(@Context  HttpServletRequest request) {
		
		HttpSession sesija = request.getSession();
		Korisnik korisnik = (Korisnik) sesija.getAttribute("user");
		ArrayList<Integer> vrijednosti = new ArrayList<Integer> ();
		if(korisnik.uloga.equals("salesman")) {
			
			/*for(int i=0;i<korisnik.getObjavljeniOglasi().size();i++) {
				
				
				
				poz+=(Integer)korisnik.getObjavljeniOglasi().get(i).getBrojPozOcena();
				neg+= (Integer) korisnik.getObjavljeniOglasi().get(i).getBrojNegOcena();
			}
		}
		korisnik.brojLajkova=poz;
		korisnik.brojDislajkova=neg;
	
		ArrayList<Integer> vrijednosti = new ArrayList<Integer> ();
		vrijednosti.add(poz);
		vrijednosti.add(neg);*/
		
			vrijednosti.add(korisnik.brojLajkova);
			vrijednosti.add(korisnik.brojDislajkova);
		return vrijednosti;
		}
		return vrijednosti;
	}
	
	
	// e ovde se broj poz menja kod ukupnih oglasa,ovo se moglo popraviti
	//kad bi u modelu oglas imalo odma i ime ko ga je objavio da prilikom logina
	//ne moramo menjati rezultate (ovo nije dovoljno konukretno na ovaj nacin)
	//MOZE BOLJE!
	@POST
	@Path("/poz")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON) 
	public Response dodajPozOcenu(Oglas o,@Context  HttpServletRequest request) {
		String contextPath = ctx.getRealPath("");
		OglasiDAO oglasi = (OglasiDAO) ctx.getAttribute("OglasiDAO");
		KorisniciDAO korisnici = (KorisniciDAO) ctx.getAttribute("KorisniciDAO");
		KategorijeDAO kategorije = (KategorijeDAO) ctx.getAttribute("KategorijeDAO");
		
		Kategorija k=kategorije.findString(o.nazivKategorije);
		ArrayList<Oglas> pomocni = k.ListToArray();
		
		for(int i=0;i<pomocni.size();i++) {
			if( pomocni.get(i).getNaziv().equals(o.getNaziv())) {
				pomocni.get(i).setBrojPozOcena(pomocni.get(i).getBrojPozOcena()+1);
			}
		}
		k.setOglasi(pomocni);
		

		
		oglasi.getOglasiMapa().get(o.naziv+o.nazivKategorije).brojPozOcena+=1;
		System.out.println("Da li je vidljiv username prodavaca:"+o.prodavacUsername);
		
		
		
		Korisnik prodavac= korisnici.getKorisnici().get(o.prodavacUsername);
		for(int i=0;i<prodavac.getObjavljeniOglasi().size();i++) {
			
			if(prodavac.getObjavljeniOglasi().get(i).getNaziv().equals(o.getNaziv())) {
				if(prodavac.getObjavljeniOglasi().get(i).nazivKategorije.contentEquals(o.nazivKategorije)) {
					prodavac.getObjavljeniOglasi().get(i).setBrojPozOcena(prodavac.getObjavljeniOglasi().get(i).getBrojPozOcena()+1);
					prodavac.povecajPoz();
					
				}
			}
		}
		
		
		oglasi.saveOglasi(contextPath);
		kategorije.saveKategorija(contextPath);
		return Response.status(200).build();
	}
	
	//Isto kao i za pozitivnu ocjenu
	@POST
	@Path("/neg")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON) 
	public Response dodajNegOcenu(Oglas o,@Context  HttpServletRequest request) {
		String contextPath = ctx.getRealPath("");
		OglasiDAO oglasi = (OglasiDAO) ctx.getAttribute("OglasiDAO");
		KorisniciDAO korisnici = (KorisniciDAO) ctx.getAttribute("KorisniciDAO");
		KategorijeDAO kategorije = (KategorijeDAO) ctx.getAttribute("KategorijeDAO");
		Kategorija k=kategorije.findString(o.nazivKategorije);
		
		ArrayList<Oglas> pomocni = k.ListToArray();
		
		for(int i=0;i<pomocni.size();i++) {
			if( pomocni.get(i).getNaziv().equals(o.getNaziv())) {
				pomocni.get(i).setBrojPozOcena(pomocni.get(i).getBrojPozOcena()+1);
			}
		}
		k.setOglasi(pomocni);
		
		oglasi.getOglasiMapa().get(o.naziv+o.nazivKategorije).brojNegOcena+=1;

		
		//menjam i kod samog prodavaca direktno
		Korisnik prodavac= korisnici.getKorisnici().get(o.prodavacUsername);
		for(int i=0;i<prodavac.getObjavljeniOglasi().size();i++) {
			
			if(prodavac.getObjavljeniOglasi().get(i).getNaziv().equals(o.getNaziv())) {
				if(prodavac.getObjavljeniOglasi().get(i).nazivKategorije.contentEquals(o.nazivKategorije)) {
					prodavac.getObjavljeniOglasi().get(i).setBrojNegOcena(prodavac.getObjavljeniOglasi().get(i).getBrojNegOcena()+1);
					prodavac.povecajNeg();
				}
			}
		}
		
		
		
		oglasi.saveOglasi(contextPath);
		kategorije.saveKategorija(contextPath);
		return Response.status(200).build();
	}
	
	
	//ovde ima greska?
	@POST
	@Path("/zabraniOglas")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON) 
	public Response zabraniOglas(Oglas o,@Context  HttpServletRequest request) {
		String contextPath = ctx.getRealPath("");
		
		ArrayList<String> kupci = new ArrayList<String> ();
		KorisniciDAO korisnici = (KorisniciDAO) ctx.getAttribute("KorisniciDAO");
		OglasiDAO oglasi = (OglasiDAO) ctx.getAttribute("OglasiDAO");
		 
		Korisnik prodavac= korisnici.getKorisnici().get(o.prodavacUsername);
		prodavac.getPorukePrimljene().add(new Poruka("Poruka od adminisitratora","Vas oglas je zabranjen. Za vise informacija posaljite poruku na odgovarajuci mail."));

		//oglasi.oglasiPoruceni() -> lista svih trenutno porucenih oglasa
		//znaci kad ga dostavim moram izbrisati iz liste ukupno porucenih?
		boolean kupljen = false;
		System.out.println("velicina porucenih:"+oglasi.oglasiPoruceni.size());
		for(int i=0;i<oglasi.oglasiPoruceni.size();i++) {
			if(oglasi.oglasiPoruceni.get(i).getNaziv().equals(o.naziv)) {
				if(oglasi.oglasiPoruceni.get(i).nazivKategorije.equals(o.nazivKategorije)) {
					
					kupci.add(oglasi.oglasiPoruceni.get(i).kupac);
					kupljen = true;
				}
			}
		}
		
		for(int i=0;i<kupci.size();i++) {
			Korisnik kupacTemp = korisnici.getKorisnici().get(kupci.get(i));
			System.out.println();
			kupacTemp.getPorukePrimljene().add(new Poruka("Poruka od adminisitratora","Vas oglas je zabranjen. Za vise informacija posaljite poruku na odgovarajuci mail."));
		}
		
		if(kupljen!=true && o.aktivan.equals("false")) {
			return Response.status(400).build();
		}
		
		
		
		Oglas pom=oglasi.getOglasiMapa().get(o.naziv+o.nazivKategorije);
		pom.aktivan="false";
		oglasi.getOglasiMapa().remove(o.naziv+o.nazivKategorije);
		oglasi.getOglasiMapa().put(o.naziv+o.nazivKategorije, pom);
		
		KategorijeDAO kategorije = (KategorijeDAO) ctx.getAttribute("KategorijeDAO");
		kategorije.getKategorije().get(o.nazivKategorije).izbrisiOglas(o.naziv);
		kategorije.getKategorije().get(o.nazivKategorije).dodajOglas(pom);
		
		/*oglasi.getOglasiMapa().replace(pom.naziv+pom.nazivKategorije, pom);

		KategorijeDAO kategorije = (KategorijeDAO) ctx.getAttribute("KategorijeDAO");
		Kategorija k=kategorije.findString(o.nazivKategorije);
		k.getOglasi().remove(o);
		k.getOglasi().add(pom); */
		
		oglasi.saveOglasi(contextPath);
		kategorije.saveKategorija(contextPath);
		return Response.status(200).build();
		
	}
	
	//preuzima objavljene oglase , opet nije dobro zbog konkurentnosti rjesenje
	//trebala se dodati jos jedno polje u oglasu koje bi prilikom izmjena mjenjalo
	//direktno i oglase tog i tog kupca, tako da ne bi bila ovolika kobasica koda
	@GET
	@Path("/preuzmiObjavljeneOglase")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Oglas> prodavacOglasiObjavljeni (@Context  HttpServletRequest request) {
	
	
	
		
		OglasiDAO oglasi = (OglasiDAO) ctx.getAttribute("OglasiDAO");
	
		HttpSession sesija = request.getSession();
		Korisnik korisnik = (Korisnik) sesija.getAttribute("user");
		
	/*
		
		if(korisnik.uloga.equals("salesman")) {
			
			for(int i=0;i<korisnik.getObjavljeniOglasi().size();i++) {
				String id= korisnik.getObjavljeniOglasi().get(i).getNaziv()+korisnik.getObjavljeniOglasi().get(i).nazivKategorije;
				
				if(oglasi.getOglasiMapa().containsKey(id)) {
					Oglas pom = oglasi.getOglasiMapa().get(id);
					
					korisnik.getObjavljeniOglasi().get(i).setCena(pom.cena);
					korisnik.getObjavljeniOglasi().get(i).setDatumIsticanja(pom.datumIsticanja);
					korisnik.getObjavljeniOglasi().get(i).setGrad(pom.grad);
					korisnik.getObjavljeniOglasi().get(i).setOpis(pom.opis);
					korisnik.getObjavljeniOglasi().get(i).setSlika(pom.slika);
					korisnik.getObjavljeniOglasi().get(i).setAktivan(pom.aktivan);
					
				}
					
				else {
					//u slucaju da se obrise citava kategorija? I onda ako sam obrisao
					//element i mi treba ostati isto?
					korisnik.getObjavljeniOglasi().remove(korisnik.getObjavljeniOglasi().get(i));
				
					i-=1;
					
				}
			}*/
			return korisnik.getObjavljeniOglasi();
		//}
		//return null;
	}
	
	//obicni get preko oglasa koje je objavio, pa dodam u jednu jedinstvenu listu i
	//sve ih sakupim
	@GET
	@Path("/preuzmiRecenzijeProdavaca")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Recenzija> prodavacRecenzije (@Context  HttpServletRequest request) {
		
		HttpSession sesija = request.getSession();
		Korisnik korisnik = (Korisnik) sesija.getAttribute("user");
		ArrayList<Recenzija> recenzije= new ArrayList<Recenzija>();
		
		if(korisnik.uloga.equals("salesman")) {
			
			for(int i=0;i<korisnik.getObjavljeniOglasi().size();i++) {
				
				for(int j=0;j<korisnik.getObjavljeniOglasi().get(i).getListaRecenzija().size();j++) {
					recenzije.add(korisnik.getObjavljeniOglasi().get(i).getListaRecenzija().get(j));
				}
			}
			return recenzije;
		}
		return null;
	}
	
	
	//ovo je vjerovatno kad zelim da mu se ispisu recenzije oglasu,koji je kupcu prikazan
	//prije nego sto ga kupi pa ovde samo sa tog konkretnog oglasa prikazujem recenzije jer je 
	@GET
	@Path("/preuzmiRecenzijeKupac")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Recenzija> kupacRecenzije (@Context  HttpServletRequest request) {
		
		HttpSession sesija = request.getSession();
		Korisnik korisnik = (Korisnik) sesija.getAttribute("user");
	
		
		if(korisnik.uloga.equals("customer")) {
			
			return korisnik.listaOstavljenihRecenzija;
		}
		return null;
	}
	
	//za administratora
	
	@GET
	@Path("/preuzmiKorisnike")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	
	public Collection<Korisnik> vratiKorisnike(@Context  HttpServletRequest request) {
		
		KorisniciDAO korisnici = (KorisniciDAO) ctx.getAttribute("KorisniciDAO");
		
		if(korisnici.getKorisnici()!=null) {
			return korisnici.getKorisnici().values();
		}
		return null;
		
	}
	
	//za select naredbu prilikom pretrage
	
	@GET
	@Path("/preuzmiGradove")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	
	public ArrayList<String> preuzmiGradove(@Context  HttpServletRequest request) {
		

	OglasiDAO oglasiDAO = (OglasiDAO) ctx.getAttribute("OglasiDAO");
	
	ArrayList<Oglas>oglasi = new ArrayList<Oglas> (oglasiDAO.getOglasi());
	ArrayList<String> gradovi = new ArrayList<String> ();
	
	for(int i=0;i<oglasi.size();i++) {
		
		if(!gradovi.contains(oglasi.get(i).grad)) {
			gradovi.add(oglasi.get(i).grad);
		}
	}
	return gradovi;
		
	}	
	
	
	@GET
	@Path("/pretragaKorisnika/{string}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	
	public ArrayList<Korisnik> preuzmiKorisnikePretraga(@PathParam("string") String string ,@Context  HttpServletRequest request) {
	
		String [] pomNiz = string.split("~"); //tako se na http zahtevu prikaze " "
		String ime = pomNiz[0];
		String grad = pomNiz[1];
		KorisniciDAO korisniciDAO = (KorisniciDAO) ctx.getAttribute("KorisniciDAO");
		ArrayList<Korisnik> korisnici = new ArrayList<Korisnik> (korisniciDAO.getKorisnici().values());
		
		//radjeno je sa izbacivanjem izbacujemo sve one koje ne zadovoljavaju to ime ili grad
		//takodje un je kad nesto nije uneseno
		if(!ime.equals("un")) {
		for(int i=0;i<korisnici.size();i++) {
			
			if(!korisnici.get(i).ime.equals(ime)) {
				korisnici.remove(i);
				i=i-1;
			}
		}
		}
		if(!grad.equals("un")) {
		for(int i=0;i<korisnici.size();i++) {
			
			if(!korisnici.get(i).grad.equals(grad)) {
				korisnici.remove(i);
				i=i-1;
			}
		}
		}
		return korisnici;
	}
	
	//ovo je za select gradova kod korisnika
	//opet sigurno pomocna lista Stringova gdje cu smjestati nazive gradova
	
	@GET
	@Path("/preuzmiGradoveKorisnik")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	
	public ArrayList<String> preuzmiGradoveKorisnik(@Context  HttpServletRequest request) {
		
		KorisniciDAO korisniciDAO = (KorisniciDAO) ctx.getAttribute("KorisniciDAO");
		ArrayList<Korisnik> korisnici = new ArrayList<Korisnik> (korisniciDAO.getKorisnici().values());
	ArrayList<String> gradovi = new ArrayList<String> ();
	
	for(int i=0;i<korisnici.size();i++) {
		
		if(!gradovi.contains(korisnici.get(i).grad)) {
			gradovi.add(korisnici.get(i).grad);
			System.out.println(korisnici.get(i).grad);
		}
	}
	return gradovi;
		
	}
	
	
	@POST
	@Path("/izmeniOglasA")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response izmeniOglasFun (Oglas o, @Context HttpServletRequest request) {
		String contextPath = ctx.getRealPath("");
		HttpSession session = request.getSession();
		Korisnik korisnik =(Korisnik) session.getAttribute("user");
		
		
		OglasiDAO oglasi = (OglasiDAO) ctx.getAttribute("OglasiDAO");
		KorisniciDAO korisnici = (KorisniciDAO) ctx.getAttribute("KorisniciDAO");
		//ako nema oglasa
		if(oglasi.getOglasiMapa()==null) {
			
			return Response.status(400).build();
		}
		
		//u listu svih oglasa stavi izmjenjen oglas (sad bih dodala da se i izmjeni
		//kod onog ko ga je kreirao nije paralelno! to ima uradjeno? Sto ne rade onda 
		//lajk i dislajk??
		
		oglasi.getOglasiMapa().remove(o.naziv+o.nazivKategorije);
		oglasi.getOglasiMapa().put(o.naziv+o.nazivKategorije, o);
		
		KategorijeDAO kategorije = (KategorijeDAO) ctx.getAttribute("KategorijeDAO");
		
		
		String nazivKategorije= o.nazivKategorije;
		
		//to sam uradila u slucaju kategorije :D
		
		Kategorija k=kategorije.findString(nazivKategorije);
		
		k.izbrisiOglas(o.naziv);
		k.dodajOglas(o);
		kategorije.saveKategorija(contextPath);
		//ovde je greska :) ajde sad cu da izmenjim kod korisnika bilo kog
		/*if(korisnik.uloga.equals("salesman")) {
		
		for(int i=0;i<korisnik.getObjavljeniOglasi().size();i++) {
			
			if(korisnik.getObjavljeniOglasi().get(i).getNaziv().equals(o.naziv)) {
				
				korisnik.getObjavljeniOglasi().remove(i);
				korisnik.getObjavljeniOglasi().add(o);
			}
		}
		
		} */
		String prodavacString=o.prodavacUsername;
		Korisnik prodavac1 = korisnici.getKorisnici().get(prodavacString);
		for(int i=0;i<prodavac1.getObjavljeniOglasi().size();i++) {
			
			if(prodavac1.getObjavljeniOglasi().get(i).getNaziv().equals(o.naziv)) {
				
				prodavac1.getObjavljeniOglasi().remove(i);
				prodavac1.getObjavljeniOglasi().add(o);
			}
		}
		
		
		if(korisnik.uloga.equals("administrator")) {
			ArrayList<String> kupci = new ArrayList<String> ();
			
			Korisnik prodavac= korisnici.getKorisnici().get(o.prodavacUsername);
			prodavac.getPorukePrimljene().add(new Poruka("Poruka od adminisitratora","Vas oglas je izmenjen."));
			for(int i=0;i<oglasi.oglasiPoruceni.size();i++) {
				if(oglasi.oglasiPoruceni.get(i).getNaziv().equals(o.naziv)) {
					if(oglasi.oglasiPoruceni.get(i).nazivKategorije.equals(o.nazivKategorije)) {
						kupci.add(oglasi.oglasiPoruceni.get(i).kupac);
					}
				}
			}
			
			for(int i=0;i<kupci.size();i++) {
				Korisnik kupacTemp = korisnici.getKorisnici().get(kupci.get(i));
				kupacTemp.getPorukePrimljene().add(new Poruka("Poruka od adminisitratora","Vas oglas je izmenjen. Za vise informacija posaljite poruku na odgovarajuci mail."));
			}
		}
		oglasi.saveOglasi(contextPath);
		
		return Response.status(200).build();
	}
	
	
	@GET
	@Path("/{string}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	
	public ArrayList<Oglas> pronadjeniOglasi(@PathParam("string") String string,@Context HttpServletRequest request) {
		
		OglasiDAO oglasiDAO = (OglasiDAO) ctx.getAttribute("OglasiDAO");
		ArrayList<Oglas>oglasiPostojeci = new ArrayList<Oglas> (oglasiDAO.getOglasi());
		
		//da li je nesto unijeto :)
		
		String provera = "un";
		ArrayList<Oglas> trazeniOglasi= new ArrayList<Oglas>();
		trazeniOglasi.addAll(oglasiPostojeci);
		
		String [] pomNiz = string.split("~");
		String naziv, datumIsticanjaMin,datumIsticanjaMax,grad;
		Double minCena,maxCena;
		Integer pozOcenaMin,pozOcenaMax;
		
		
		
		if(!pomNiz[0].equals(provera)) {
		 naziv = pomNiz[0];
		 
				
				for(int i=0;i<oglasiPostojeci.size();i++) {
					
					if(!oglasiPostojeci.get(i).naziv.equals(naziv)) {
						trazeniOglasi.remove(oglasiPostojeci.get(i));
						i-=1;
					}
				}
			
		}
		System.out.println("minCena:"+pomNiz[1]);
		if(!pomNiz[1].equals(provera)) {
		System.out.println("udje li ovdje?"); 
		minCena = Double.parseDouble(pomNiz[1]);
		 
		
		 for(int i=0;i<trazeniOglasi.size();i++) {
				
				if(trazeniOglasi.get(i).cena<minCena) {
					trazeniOglasi.remove(i);
					i-=1;
				}
			}
		 
		}
		if(!pomNiz[2].equals(provera)) {
		 maxCena = Double.parseDouble(pomNiz[2]);
	
		 for(int i=0;i<trazeniOglasi.size();i++) {
				
				if(trazeniOglasi.get(i).cena> maxCena) {
					trazeniOglasi.remove(i);
					i-=1;
				}
			}
		}
		if(!pomNiz[5].equals(provera)) {
		 datumIsticanjaMin = pomNiz[5];
		 
			String [] pom = datumIsticanjaMin.split("\\/");
			int danMin=Integer.parseInt(pom[0]);
			int mesecMin = Integer.parseInt(pom[1]);
			int godinaMin = Integer.parseInt(pom[2]);
			
			

			for(int i=0;i<trazeniOglasi.size();i++) {
				String datumIsticanjaTemp=trazeniOglasi.get(i).datumIsticanja;
				String [] pomT = datumIsticanjaTemp.split("\\/");
				int danTemp=Integer.parseInt(pomT[0]);
				int mesecTemp = Integer.parseInt(pomT[1]);
				int godinaTemp = Integer.parseInt(pomT[2]);
				if(godinaTemp<godinaMin) {
					trazeniOglasi.remove(i);
					i-=1;
					
				}else {
					if(mesecTemp<mesecMin) {
						trazeniOglasi.remove(i);
						i-=1;
					}
					else {
						if(danTemp<danMin) {
							trazeniOglasi.remove(i);
							i-=1;
						}
					}
				}
			}
		 
		}
		if(!pomNiz[6].equals(provera)) {
		datumIsticanjaMax = pomNiz[6];
		
		String [] pom = datumIsticanjaMax.split("\\/");
		int danMax=Integer.parseInt(pom[0]);
		int mesecMax = Integer.parseInt(pom[1]);
		int godinaMax = Integer.parseInt(pom[2]);
		
		

		for(int i=0;i<trazeniOglasi.size();i++) {
			String datumIsticanjaTemp=trazeniOglasi.get(i).datumIsticanja;
			String [] pomT = datumIsticanjaTemp.split("\\/");
			int danTemp=Integer.parseInt(pomT[0]);
			int mesecTemp = Integer.parseInt(pomT[1]);
			int godinaTemp = Integer.parseInt(pomT[2]);
			if(godinaTemp>godinaMax) {
				trazeniOglasi.remove(i);
				i-=1;
				
			}else {
				if(mesecTemp>mesecMax) {
					trazeniOglasi.remove(i);
					i=i-1;
				}
				else {
					if(danTemp>danMax) {
						trazeniOglasi.remove(i);
						i=i-1;
					}
				}
			
		}
	}
		
		}
		if(!pomNiz[3].equals(provera)) {
		pozOcenaMin = Integer.parseInt(pomNiz[3]);
		
		for(int i=0;i<trazeniOglasi.size();i++) {
			
			if(trazeniOglasi.get(i).brojPozOcena<pozOcenaMin) {
				trazeniOglasi.remove(i);
				i=i-1;
			}
		}
		
		}
		if(!pomNiz[4].equals(provera)) {
		pozOcenaMax = Integer.parseInt(pomNiz[4]);
	
	for(int i=0;i<trazeniOglasi.size();i++) {
			
			if(trazeniOglasi.get(i).brojPozOcena>pozOcenaMax) {
				trazeniOglasi.remove(i);
				i=i-1;
			}
		}
		
		}
		
		if(!pomNiz[7].equals(provera)) {
		grad = pomNiz[7];
		
		for(int i=0;i<trazeniOglasi.size();i++) {
			System.out.println("Da li udje u if i?"+i);
			if(!(trazeniOglasi.get(i).grad.equals(grad))) {
				System.out.println("Obrisan je: "+trazeniOglasi.get(i).grad);
				trazeniOglasi.remove(i);
				i=i-1;
				System.out.println(i);
				System.out.println("Usao je u if!"+i);
			}
		}
		
		}
		
	
	
	
	return trazeniOglasi;
	
}
	
	
	
	
	
	@PUT
	@Path("/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response izmeniKategoriju (@PathParam("naziv") String naziv,Kategorija k, @Context HttpServletRequest request) {
		
		KategorijeDAO users = (KategorijeDAO) ctx.getAttribute("KategorijeDAO");
		Kategorija pom=users.findString(naziv);
		if(pom==null) {
			return Response.status(400).build();
		}
		String contextPath = ctx.getRealPath("");
		users.getKategorije().remove(naziv);
		users.getKategorije().put(k.naziv, k);
		ctx.setAttribute("KategorijeDAO", users);
		users.saveKategorija(contextPath);
		return Response.status(200).build();
		
	}
	
	@PUT
	@Path("/{nazivkategorije}/dodajOglas")
	@Produces(MediaType.APPLICATION_JSON)
	
	public Response dodajOglas (@PathParam("nazivkategorije") String nazivkategorije,Oglas o, @Context HttpServletRequest request) {
		o.aktivan="true";
		String contextPath = ctx.getRealPath("");
		HttpSession session = request.getSession();
		Korisnik korisnik= (Korisnik) session.getAttribute("user");
		OglasiDAO oglasi = (OglasiDAO) ctx.getAttribute("OglasiDAO");
		KorisniciDAO korisnici = (KorisniciDAO) ctx.getAttribute("KorisniciDAO");
		if(oglasi.getOglasiMapa().containsKey(o.getNaziv()+nazivkategorije)) {
			
			return Response.status(400).build();
		} else {
		
	
			
		o.prodavacUsername=korisnik.getUsername();
			System.out.println("jako bitna provjera:"+o.prodavacUsername);
		
		KategorijeDAO users = (KategorijeDAO) ctx.getAttribute("KategorijeDAO");
		
		Kategorija k= users.findString(nazivkategorije);
		
		if(k.dodajOglas(o)==false) {
		
			return Response.status(400).build();
		}
		
		if(korisnik.uloga.equals("salesman")) {
		
		if(oglasi.getOglasiMapa().containsKey(o.naziv+nazivkategorije)!=false) {
			return Response.status(400).build();
		}
		oglasi.getOglasiMapa().put(o.getNaziv()+nazivkategorije,o);
		ctx.setAttribute("OglasiDAO", oglasi);
		korisnik.getObjavljeniOglasi().add(o);
		korisnici.getKorisnici().replace(korisnik.getUsername(),korisnik);
		users.saveKategorija(contextPath);
		oglasi.saveOglasi(contextPath);
		
		return Response.status(200).build();
		
		}
		else {
			return Response.status(400).build();
		}
		
		}
	}
	
	
	
	//obicna get metoda
	@GET
	@Path("/prikaziOglas/{nazivk}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Oglas> prikaziOglase (@PathParam("nazivk") String naziv, @Context HttpServletRequest request) {
		System.out.println("BIO SAM OVDJE !!!!!!  1 ");
		
		
		
		KategorijeDAO users = (KategorijeDAO) ctx.getAttribute("KategorijeDAO");
		Kategorija k= users.findString(naziv);
		
			if(users.getKategorije()!=null) {
				System.out.println("BIO SAM OVDJE !!!!!! ");
				return k.getOglasi();
			}
			return null;
		
		
	}
	
	
	//ako si ulogovan vrati usera, ako nisi vrati null
	//(ovde nisam u servicu redirektovala nikako)
	@GET
	@Path("/proveraLogin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Korisnik proveraLogina (@Context  HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		Korisnik user = (Korisnik) session.getAttribute("user");

		if (user != null) {
			return user;
		} else {
			return null;
		}
		
	}
	
	
	//obicna get metoda
	@GET
	@Path("/preuzmiKategorije")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Collection<Kategorija> preuzmiKategorije (@Context  HttpServletRequest request) {
	
		
		KategorijeDAO kategorijeDAO = (KategorijeDAO) ctx.getAttribute("KategorijeDAO");
			System.out.println("duzina"+kategorijeDAO.getKolekcije().size());
			if(kategorijeDAO!=null) {
			Collection<Kategorija> kategorije=kategorijeDAO.getKolekcije();
			System.out.println(kategorije);
			return kategorije;
			}
			return null;
			
		
	}
	
	//obicna get metoda
	@GET
	@Path("/preuzmiOglase")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Collection<Oglas> preuzmiOglase (@Context  HttpServletRequest request) {
	
		
		OglasiDAO oglasiDAO = (OglasiDAO) ctx.getAttribute("OglasiDAO");
			
			if(oglasiDAO!=null) {
			return oglasiDAO.getOglasi();
			}
			return null;
			
		
	}

	
	//opet obicna get samo moramo znati ko je trenutno ulogovan 
	//da znamo cije omiljene oglase uzimamo
	@GET
	@Path("/preuzmiOmiljeneOglase")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Collection<Oglas> preuzmiOmiljeneOglase (@Context  HttpServletRequest request) {
	
		
		
			
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		
		if(user.uloga.equals("customer")) {
			
			return user.getOmiljeniOglasi();
			
		}
		return null;
			
		
	}
	//isto
	@GET
	@Path("/preuzmiPoruceneOglase")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Collection<Oglas> preuzmiPoruceneOglase (@Context  HttpServletRequest request) {
	
		
		
			
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		
		if(user.uloga.equals("customer")) {
			
			return user.getOglasiPoruceni();
			
		}
		return null;
			
		
	}
	
	//isto
	@GET
	@Path("/preuzmiDostavljeneOglase")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Collection<Oglas> preuzmiDostavljeneOglase (@Context  HttpServletRequest request) {
	
		
		
			
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		
		if(user.uloga.equals("customer")) {
			
			return user.getOglasiDostavljeni();
			
		}
		return null;
			
		
	}
	
	
	//tu je greska on postaje aktivan 
	@POST
	@Path("/ostaviRecenziju/{idOglasa}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response ostaviRecenziju (@PathParam ("idOglasa") String idOglasa,Recenzija r,@Context  HttpServletRequest request  ) {
	
		
			String contextPath = ctx.getRealPath("");
			
			
			OglasiDAO oglasiDAO = (OglasiDAO) ctx.getAttribute("OglasiDAO");
			Oglas o=oglasiDAO.getOglasiMapa().get(idOglasa);
			o.aktivan="true";
			o.dodajRecenziju(r) ;
			oglasiDAO.getOglasiMapa().replace(idOglasa,o);
			
			KategorijeDAO kategorijeDAO = (KategorijeDAO) ctx.getAttribute("KategorijeDAO");
			Kategorija k=kategorijeDAO.findString(o.nazivKategorije);
			
			ArrayList<Oglas> oglasi=(ArrayList<Oglas>) k.getOglasi();
			for(int i=0;i<k.oglasi.size();i++) {
				
				if(oglasi.get(i).getNaziv().equals(o.naziv)) {
					//ovu liniju sam dodala da vidim gdje grijesim
					oglasi.get(i).setAktivan("true");
					oglasi.get(i).dodajRecenziju(r);
				}
			}
			k.setOglasi(oglasi);
			
			RecenzijeDAO recenzijeDAO = (RecenzijeDAO) ctx.getAttribute("RecenzijeDAO");
			recenzijeDAO.getRecenzije().put(r.naslovRecenzije+o.naziv, r);
			
			KorisniciDAO korisnici = (KorisniciDAO) ctx.getAttribute("KorisniciDAO");
			HttpSession session = request.getSession();
			Korisnik user = (Korisnik) session.getAttribute("user");
			
			if(user.uloga.equals("customer")) {
				
				user.listaOstavljenihRecenzija.add(r);
				user.izbrisiPoruceni(o.naziv);
				user.oglasiDostavljeni.add(o);
				korisnici.getKorisnici().replace(user.getUsername(), user);
				
				for(int i=0;i<oglasiDAO.oglasiPoruceni.size();i++) {
					//brisem ga iz liste ukupnih porucenih oglasa
					
					if(oglasiDAO.oglasiPoruceni.get(i).naziv.equals(o.naziv)) {
						if(oglasiDAO.oglasiPoruceni.get(i).nazivKategorije.equals(o.nazivKategorije)) {
							oglasiDAO.oglasiPoruceni.remove(i);
						}
					}
				}
			}
			recenzijeDAO.saveRecenzije(contextPath);
			oglasiDAO.saveOglasi(contextPath);
			kategorijeDAO.saveKategorija(contextPath);
			
			return Response.status(200).build();
		
			
		
	}
	
	
	
	@GET
	@Path("/pregledKategorijaSideBar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Collection<Kategorija> pregledKategorijaSide (@Context  HttpServletRequest request) {
	
		
		KategorijeDAO kategorijeDAO = (KategorijeDAO) ctx.getAttribute("KategorijeDAO");
			System.out.println("duzina"+kategorijeDAO.getKolekcije().size());
			if(kategorijeDAO!=null) {
			Collection<Kategorija> kategorije=kategorijeDAO.getKolekcije();
			System.out.println(kategorije);
			return kategorije;
			}
			return null;
			
		
	}
	@POST
	@Path("/izmeniUloguKorisnika")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	
	public Response izmeniUlogu(Korisnik k,@Context  HttpServletRequest request ) {
		String contextPath = ctx.getRealPath("");
		KorisniciDAO korisnici = (KorisniciDAO) ctx.getAttribute("KorisniciDAO");
		
		
		Korisnik korisnik = korisnici.find(k);
		
		
		if(korisnik==null) {
			return Response.status(400).build();
		}
		
		korisnik.setUloga(k.uloga);
	
		System.out.println(k.uloga);
		System.out.println(korisnik.uloga);
		//korisnici.getKorisnici().remove(korisnik.getKorisnickoIme());
		//korisnici.getKorisnici().put(korisnik.getKorisnickoIme(),korisnik);
		korisnici.saveKorisnik(contextPath);
		return Response.status(200).build();
		
	}

	//obican booble sort
	@GET
	@Path("/mostPopular")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> mostPopular(@Context HttpServletRequest request) {
		OglasiDAO oglasi = (OglasiDAO) ctx.getAttribute("OglasiDAO");
		ArrayList<Oglas> list = new ArrayList<Oglas>();
		
		ArrayList<Oglas> oglasiSortirani = new ArrayList<Oglas> (oglasi.getOglasi());
		
		for (int i = 0; i < oglasiSortirani.size()-1; i++) {
			for(int j = i + 1; j < oglasiSortirani.size(); j++) {
				Oglas temp;
				if(oglasiSortirani.get(i).omiljeni < oglasiSortirani.get(j).omiljeni) { //sortiraj ih po opadajucem redosledu
					temp = oglasiSortirani.get(i);
					oglasiSortirani.set(i, oglasiSortirani.get(j));
					oglasiSortirani.set(j, temp);
				}
			}
			
		}
		
		int i = 0;
		while(i < oglasiSortirani.size()) { //i dodaj u novu listu
		
				list.add(oglasiSortirani.get(i));
			i++;
			System.out.println();
			if(i >= 10)
				break;
		}
		
		for(Oglas a : list) {
			System.out.println("FAVOURITE OCCURENCES SORTED, ARE:" + a.omiljeni);
		}
		
		return list;
	}
	@POST
	@Path("/staviuOmiljene")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	
	public Response dodajuOmijene(Oglas o,@Context  HttpServletRequest request ) {
		String contextPath = ctx.getRealPath("");
		OglasiDAO oglasi = (OglasiDAO) ctx.getAttribute("OglasiDAO");
		KategorijeDAO kategorije = (KategorijeDAO) ctx.getAttribute("KategorijeDAO");
		KorisniciDAO korisnici = (KorisniciDAO) ctx.getAttribute("KorisniciDAO");
		HttpSession session = request.getSession();
		Korisnik k =(Korisnik) session.getAttribute("user");
		
		if(k.uloga.equals("customer")) {
			
			//dodala da se dva puta ne moze dodati isti oglas
			boolean dodaj= true;
			for(int i =0;i<k.getOmiljeniOglasi().size();i++) {
				if(k.getOmiljeniOglasi().get(i).getNaziv().equals(o.getNaziv())) {
					if(k.getOmiljeniOglasi().get(i).nazivKategorije.equals(o.nazivKategorije)) {
						dodaj=false;
					}
				}
			}
			if(dodaj!=false) {
					
			k.getOmiljeniOglasi().add(o); }
				
			else {
				return Response.status(400).build();
			}}
			korisnici.getKorisnici().replace(k.getUsername(), k);
			
			korisnici.saveKorisnik(contextPath);
			kategorije.getKategorije().get(o.nazivKategorije).izbrisiOglas(o.naziv);
			oglasi.getOglasiMapa().get(o.naziv+o.nazivKategorije).povecaj();
			kategorije.getKategorije().get(o.nazivKategorije).dodajOglas(o);
			oglasi.saveOglasi(contextPath);
			kategorije.saveKategorija(contextPath);
			return Response.status(200).build();
		
		
	
		
	
	}
	
	
	@POST
	@Path("/zavrsiPorudzbinu")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	
	public Response kupiProizvode(@Context  HttpServletRequest request ) {
		String contextPath = ctx.getRealPath("");
		KorisniciDAO korisnici = (KorisniciDAO) ctx.getAttribute("KorisniciDAO");
		HttpSession session = request.getSession();
		Korisnik k =(Korisnik) session.getAttribute("user");
		
		if(k.uloga.equals("customer")) {
			
		
			
			ArrayList<Oglas> poruceni = (ArrayList<Oglas>) session.getAttribute("cart");
			session.setAttribute("cart", new ArrayList<Oglas>());
			
			k.getOglasiPoruceni().addAll(poruceni);
			korisnici.getKorisnici().replace(k.getUsername(), k);
			korisnici.saveKorisnik(contextPath);
			return Response.status(200).build();
		
		}
		return Response.status(400).build();
		
	}
	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(@Context HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		
		if(user != null) {
			//to znaci da mu sve atribute brisem kod sesije
			session.invalidate();
			return Response.status(200).build();
		}
		else {
			return Response.status(400).entity("User is already loged out!").build();
		}
	}
	
	@DELETE
	@Path("/izbrisiKategoriju")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response obrisiKategoriju(Kategorija k,@Context  HttpServletRequest request) {
		
		String contextPath = ctx.getRealPath("");
		KategorijeDAO kategorijeDAO = (KategorijeDAO) ctx.getAttribute("KategorijeDAO");
		OglasiDAO oglasiDAO = (OglasiDAO) ctx.getAttribute("OglasiDAO");
		if(kategorijeDAO.find(k)!=null) {
			
			Kategorija pom = kategorijeDAO.findString(k.getNaziv());
			ArrayList<Oglas> oglasi = new ArrayList<Oglas> (pom.getOglasi());
			for(int i=0;i<oglasi.size();i++) {
				
				Oglas temp= oglasi.get(i);
				oglasiDAO.getOglasiMapa().remove(temp.naziv+temp.nazivKategorije);
				
				
			}
			
			kategorijeDAO.getKategorije().remove(k.getNaziv());
			
			ctx.setAttribute("KategorijeDAO", kategorijeDAO);
			kategorijeDAO.saveKategorija(contextPath);
			return Response.status(200).build();
		}
		else {
			
			return Response.status(400).build();
		}
		
		
	}
	
	
	
	//NEMOJ ZABORAVITI KOD IZMENE OGLASA NESTO!!!
	@DELETE
	//brisem na svim mjestima gdje se taj oglas nalazio
	
	@Path("/izbrisiOglas")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response obrisiOglas(Oglas o,@Context  HttpServletRequest request) {
		String contextPath = ctx.getRealPath("");
		HttpSession session = request.getSession();
		
		Korisnik korisnik =(Korisnik) session.getAttribute("user");
		
		
		
		OglasiDAO oglasiDAO = (OglasiDAO) ctx.getAttribute("OglasiDAO");
		KategorijeDAO kategorijeDAO = (KategorijeDAO) ctx.getAttribute("KategorijeDAO");
		
		oglasiDAO.getOglasiMapa().remove(o.naziv+o.nazivKategorije);
		
		String kategorija=o.nazivKategorije;
		
			System.out.println("KATEGORIJA/n"+kategorija);
			Kategorija k=kategorijeDAO.findString(kategorija);
		
			//ispada opet da moze samo da brise salesman a moze i admin tu je greska,al aj nek ostane.
			korisnik.getObjavljeniOglasi().remove(o);
		
			System.out.println(k.getNaziv()); 
				
			
			
					k.izbrisiOglas(o.naziv);
					if(korisnik.uloga.equals("salesman")) {
					for(int i=0;i<korisnik.getObjavljeniOglasi().size();i++) {
						if(korisnik.getObjavljeniOglasi().get(i).getNaziv().equals(o.naziv)) {
							korisnik.getObjavljeniOglasi().remove(i);
						}
					}
					kategorijeDAO.saveKategorija(contextPath);
					}
					else {
						return Response.status(400).build();
					}
					
					for(int i=0;i<oglasiDAO.oglasiPoruceni.size();i++) {
						if(oglasiDAO.oglasiPoruceni.get(i).getNaziv().equals(o.naziv)) {
							if(oglasiDAO.oglasiPoruceni.get(i).nazivKategorije.equals(o.nazivKategorije)) {
								oglasiDAO.oglasiPoruceni.remove(i);
							}
						}
					}
					
					oglasiDAO.saveOglasi(contextPath);
				return Response.status(200).build();
		}
	
		
		
		
	
	
	//recenzija se mjenja u sklopu ukupne liste recenzija a i mjenja se kod oglasa 
	//za kog je recenzija vezana
	@POST
	@Path("/izmeniRecenziju")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response izmeniRecenziju(Recenzija r,@Context  HttpServletRequest request) {
		String contextPath = ctx.getRealPath("");
		HttpSession session = request.getSession();
		Korisnik korisnik =(Korisnik) session.getAttribute("user");
		
		if(korisnik.uloga.equals("customer")) {
			
			
			for(int i=0;i<korisnik.listaOstavljenihRecenzija.size();i++) {
				if(korisnik.listaOstavljenihRecenzija.get(i).getNaslovRecenzije().equals(r.naslovRecenzije)) {
					 {
						korisnik.listaOstavljenihRecenzija.remove(i);
						korisnik.listaOstavljenihRecenzija.add(r);
					}
				}
			}
			
		}else {
			return Response.status(400).build();
		}
		OglasiDAO oglasiDAO = (OglasiDAO) ctx.getAttribute("OglasiDAO");
		KategorijeDAO kategorijeDAO = (KategorijeDAO) ctx.getAttribute("KategorijeDAO");
		System.out.println(r.nazivOglasa+r.nazivKategorije);
		Oglas o=oglasiDAO.getOglasiMapa().get(r.nazivOglasa+r.nazivKategorije);
		
		for(int i=0;i<o.getListaRecenzija().size();i++) {
			
			if(o.getListaRecenzija().get(i).naslovRecenzije.equals(r.naslovRecenzije)) {
				o.getListaRecenzija().remove(i);
				o.getListaRecenzija().add(r);
			}
		}
		
		Kategorija k=kategorijeDAO.findString(r.nazivKategorije);
		
		ArrayList<Oglas> oglasi=(ArrayList<Oglas>) k.getOglasi();
		for(int i=0;i<k.oglasi.size();i++) {
			
			if(oglasi.get(i).getNaziv().equals(r.nazivOglasa)) {
				
			ArrayList<Recenzija> rc= oglasi.get(i).getListaRecenzija();
				for(int j=0;j<rc.size();j++) {
					if(r.getNaslovRecenzije().equals(rc.get(j).getNaslovRecenzije())) {
						rc.remove(j);
						rc.add(r);
						oglasi.get(i).setListaRecenzija(rc);
						break;
					}
				}
			}
		}
		k.setOglasi(oglasi);
		kategorijeDAO.getKategorije().replace(k.naziv, k);
		
		RecenzijeDAO recenzijeDAO = (RecenzijeDAO) ctx.getAttribute("RecenzijeDAO");
		recenzijeDAO.getRecenzije().remove(r.naslovRecenzije+r.nazivOglasa);
		recenzijeDAO.getRecenzije().put(r.naslovRecenzije+r.nazivOglasa, r);
		
		
		recenzijeDAO.saveRecenzije(contextPath);
		oglasiDAO.saveOglasi(contextPath);
		kategorijeDAO.saveKategorija(contextPath);
		return Response.status(200).build();
		
	}
	
	@DELETE
	@Path("/izbrisiRecenziju/{nazivOglasa}/{nazivKategorije}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response obrisiRecenziju(@PathParam("nazivOglasa") String nazivOglasa,@PathParam("nazivKategorije") String nazivKategorije,Recenzija r,@Context  HttpServletRequest request) {
		
		String contextPath = ctx.getRealPath("");
		HttpSession session = request.getSession();
		Korisnik korisnik =(Korisnik) session.getAttribute("user");
		
		OglasiDAO oglasiDAO = (OglasiDAO) ctx.getAttribute("OglasiDAO");
		KategorijeDAO kategorijeDAO = (KategorijeDAO) ctx.getAttribute("KategorijeDAO");
		String idOglasa = nazivOglasa+nazivKategorije;
		Oglas o=oglasiDAO.getOglasiMapa().get(idOglasa);
		
		System.out.println("\nO oglasu:\n"+o.naziv+o.nazivKategorije);
		
		//izbrisana kod "ukupnog" oglasa 
		if(o.izbrisiRecenziju(r)) {
			System.out.println("uspjesno obrisana recenzija");
			
			
		}

				
		
		Kategorija k=kategorijeDAO.findString(nazivKategorije);
		
		ArrayList<Oglas> oglasi=(ArrayList<Oglas>) k.getOglasi();
		for(int i=0;i<k.oglasi.size();i++) {
			
			if(oglasi.get(i).getNaziv().equals(nazivOglasa)) {
				
				oglasi.get(i).izbrisiRecenziju(r);
			}
		}
		k.setOglasi(oglasi);
		kategorijeDAO.getKategorije().replace(k.naziv, k);
		
		//brisem kod "ukupnih" recenzija
		
		RecenzijeDAO recenzijeDAO = (RecenzijeDAO) ctx.getAttribute("RecenzijeDAO");
		recenzijeDAO.getRecenzije().remove(r.naslovRecenzije+nazivOglasa);
	
		
		//brisem kod korisnika koji je ostavio recenziju
			if(korisnik.uloga.equals("customer")) {
				
			
				for(int i=0;i<korisnik.listaOstavljenihRecenzija.size();i++) {
					if(korisnik.listaOstavljenihRecenzija.get(i).getNaslovRecenzije().equals(r.naslovRecenzije)) {
						if(korisnik.listaOstavljenihRecenzija.get(i).getSadrzajRecenzije().equals(r.sadrzajRecenzije)) {
							korisnik.listaOstavljenihRecenzija.remove(i);
						}
					}
				}
				
			}else {
				return Response.status(400).build();
			}
				
			recenzijeDAO.saveRecenzije(contextPath);
			oglasiDAO.saveOglasi(contextPath);
			kategorijeDAO.saveKategorija(contextPath);
			
			return Response.status(200).build();
			
		
		
		
	}
	
	
	//inicijalizacija prvi put kad pokrenem aplikaciju ctx postavljam atribute :)
	@PostConstruct
	public void init() {
		
		String contextPath = ctx.getRealPath("");
		System.out.println(contextPath);
		
		
		if (ctx.getAttribute("KorisniciDAO") == null) {
			System.out.println("Incijalizacija");
			
			
			ctx.setAttribute("KorisniciDAO", new KorisniciDAO(contextPath));
		}
		
		if(ctx.getAttribute("KategorijeDAO")==null) {
			System.out.println("Dodjem li ovde?");
			KategorijeDAO k=new KategorijeDAO(contextPath);
			System.out.println(k.getKolekcije().size());			
			ctx.setAttribute("KategorijeDAO", k);
		}
		if(ctx.getAttribute("OglasiDAO")==null) {
			ctx.setAttribute("OglasiDAO", new OglasiDAO(contextPath));
		}
		if(ctx.getAttribute("RecenzijeDAO")==null) {
			ctx.setAttribute("RecenzijeDAO", new RecenzijeDAO(contextPath));
		}
		if(ctx.getAttribute("PorukeDAO")==null) {
			ctx.setAttribute("PorukeDAO", new PorukeDAO());
		}
		
		
	}
	

	
}