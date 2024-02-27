import java.util.Scanner;
import java.util.ArrayList; 

public class TonttiMain {

	public static void main(String [] args) {
		/*
		 * Luodaan oliot asukkaista, yhdest‰ rakennustyypist‰ (teht‰v‰nannosta en ymm‰rt‰nyt
		 * olisiko kaikista pit‰nyt luoda omat, mutta tontille mahtuu kuitenkin vain yksi rakennus)
		 * ja yhdest‰ tontista
		 * Tulostetaan tontin tiedot (t‰ss‰ rakennuksen eri tyypit laitettu 
		 * kommentteihin vaihtamisen helpottamiseksi)
		 */
		String tontin_nimi;
		String tontin_osoite;
		int tontin_ala;
		int asuntojen_lkm; 
		int asunnon_ala;
		int huoneiden_lkm;
		int asukkaiden_lkm = 3; // montako Asukas-oliota luodaan
		String asukkaan_nimi;
		
		Scanner lukija = new Scanner(System.in);
		ArrayList<Asukas> asukkaat = new ArrayList<>();
		
		// Kysyt‰‰n asukkaiden nimet
		for (int i = 1; i <= asukkaiden_lkm; i++) {
			System.out.println("Anna " + i + ". asukkaan nimi: ");
			asukkaan_nimi = lukija.nextLine();
			Asukas asukas = new Asukas(asukkaan_nimi);
			asukkaat.add(asukas);
		}	
		//Luodaan olio yhdest‰ rakennustyypist‰

		//Rakennus rakennus = new Rakennus(asukkaat);		
		//Omakotitalo rakennus = new Omakotitalo(asukkaat);
		Kerrostalo rakennus = new Kerrostalo(asukkaat);
		//Rivitalo rakennus = new Rivitalo(asukkaat);
		
		System.out.println("Anna asuntojen lukum‰‰r‰: ");
		asuntojen_lkm = Integer.parseInt(lukija.nextLine());
		rakennus.setAsuntojen_lkm(asuntojen_lkm);
		
		// Kysyt‰‰n ja asetetaan asuntojen pinta-alat ja huoneiden lkm asuntojen lukum‰‰r‰n mukaan
		for (int i = 1; i <= asuntojen_lkm; i++) {
				System.out.println("Anna " + i + ". asunnon pinta-ala: ");
				asunnon_ala = Integer.parseInt(lukija.nextLine());
				rakennus.setAsunnon_ala(asunnon_ala);
				System.out.println("Anna " + i + ". asunnon huoneiden lukum‰‰r‰: ");
				huoneiden_lkm = Integer.parseInt(lukija.nextLine());
				rakennus.setHuoneiden_lkm(huoneiden_lkm);
		}		
		// Kysyt‰‰n tontin tiedot ja luodaan uusi tontti
		System.out.println("Anna tontin nimi: ");
		tontin_nimi = lukija.nextLine();
		System.out.println("Anna tontin osoite: ");
		tontin_osoite = lukija.nextLine();
		System.out.println("Anna tontin pinta-ala: ");
		tontin_ala = Integer.parseInt(lukija.nextLine());
		Tontti tontti = new Tontti(tontin_nimi, tontin_osoite, rakennus);
		tontti.setPinta_ala(tontin_ala);

		// Tulostetaan tontin tiedot luokan omalla tulostusmetodilla
		tontti.printTontin_tiedot();
		
		lukija.close();
	}
}
