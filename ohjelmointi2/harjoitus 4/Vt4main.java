import java.util.Scanner;

public class Vt4main {
	
	public static void main(String [] args) {

		String kiinteiston_tyyppi;
		String kiinteiston_sijainti;
		double vakuutusarvo;
		double raja_arvo;
		Scanner lukija = new Scanner(System.in);
		InsInfoContainer ii_container = new InsInfoContainer();

		// 1. Tallennetaan vähintään viisi vakuutustieto-oliota tietosäiliöön.
		for (int i = 1; i < 6; i++) {
			System.out.println("Anna " + i + ". kiinteistön tyyppi: ");
			kiinteiston_tyyppi = lukija.nextLine();
			System.out.println("Anna " + i +". kiinteistön sijainti: ");
			kiinteiston_sijainti = lukija.nextLine();
			System.out.println("Anna " + i +". kiinteistön vakuutusarvo: ");
			vakuutusarvo = Double.parseDouble(lukija.nextLine());
			Property p = new Property(kiinteiston_tyyppi, kiinteiston_sijainti);
			InsuranceInfo ii = new InsuranceInfo(p);
			ii.setVakuutusarvo(vakuutusarvo);
			ii_container.addToContainer(ii);
		}
		
		// 2. Tulostetaan säiliön koko sisältö.
		ii_container.printContainer();
		
		/*
		3. Syötetään näppäimistöltä arvo ja tulostetaan kiinteistöt, joiden vakuutusarvo on
		tätä pienempi.
		*/
		System.out.println("Anna yläraja tulostuksille: ");
		raja_arvo = Double.parseDouble(lukija.nextLine());
		ii_container.printLowerInsValue(raja_arvo);
		
		/*
		4. Syötetään näppäimistöltä arvo ja tulostetaan kiinteistöt, joiden vakuutusarvo on
		tätä suurempi.
		*/
		System.out.println("Anna alaraja tulostuksille: ");
		raja_arvo = Double.parseDouble(lukija.nextLine());
		ii_container.printHigherInsValue(raja_arvo);
		
		System.out.println("\nKiitos!");
		lukija.close();
	}
}
