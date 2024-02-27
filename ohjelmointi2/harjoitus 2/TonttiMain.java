import java.util.Scanner;
import java.util.List; 
import java.util.ArrayList; 

public class TonttiMain {

	public static void main(String [] args) {
		
		String nimiTontti;
		String nimiAsukas;
		String sijaintiTontti;
		int alaTontti;
		int alaRakennus;
		int lkmHuoneet;
		String syntyma_aika;
		
		Scanner lukija = new Scanner(System.in);
		
		System.out.println("Anna asukkaan nimi: ");
		nimiAsukas = lukija.nextLine();
		System.out.println("Anna asukkaan syntym‰aika: ");
		syntyma_aika = lukija.nextLine();
		Asukas asukas1 = new Asukas(nimiAsukas, syntyma_aika);
		//System.out.println(asukas1);
		
		System.out.println("Anna asukkaan nimi: ");
		nimiAsukas = lukija.nextLine();
		System.out.println("Anna asukkaan syntym‰aika: ");
		syntyma_aika = lukija.nextLine();
		Asukas asukas2 = new Asukas(nimiAsukas, syntyma_aika);
		//System.out.println(asukas2);
		
		System.out.println("Anna asukkaan nimi: ");
		nimiAsukas = lukija.nextLine();
		System.out.println("Anna asukkaan syntym‰aika: ");
		syntyma_aika = lukija.nextLine();
		Asukas asukas3 = new Asukas(nimiAsukas, syntyma_aika);
		//System.out.println(asukas3);
		
		List<Asukas> asukkaat = new ArrayList<Asukas>();
		asukkaat.add(asukas1);
		asukkaat.add(asukas2);
		asukkaat.add(asukas3);
		
		System.out.println("Anna rakennuksen pinta-ala: ");
		alaRakennus = Integer.parseInt(lukija.nextLine());
		System.out.println("Anna huoneiden lukum‰‰r‰: ");
		lkmHuoneet = Integer.parseInt(lukija.nextLine());
		Rakennus rakennus = new Rakennus(alaRakennus, lkmHuoneet, asukkaat);
		//System.out.println(rakennus);

		System.out.println("Anna tontin nimi: ");
		nimiTontti = lukija.nextLine();
		
		System.out.println("Anna tontin sijainti: ");
		sijaintiTontti = lukija.nextLine();
		System.out.println("Anna tontin pinta-ala: ");
		alaTontti = Integer.parseInt(lukija.nextLine());
		Tontti tontti = new Tontti(nimiTontti, sijaintiTontti, alaTontti, rakennus);
		System.out.println(tontti);

		System.out.println("\nKiitos!");
		lukija.close();
	}
}

