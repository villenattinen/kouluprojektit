import java.util.Scanner;

public class Vt4main {
	
	public static void main(String [] args) {

		String kiinteiston_tyyppi;
		String kiinteiston_sijainti;
		double vakuutusarvo;
		double raja_arvo;
		Scanner lukija = new Scanner(System.in);
		InsInfoContainer ii_container = new InsInfoContainer();

		// 1. Tallennetaan v�hint��n viisi vakuutustieto-oliota tietos�ili��n.
		for (int i = 1; i < 6; i++) {
			System.out.println("Anna " + i + ". kiinteist�n tyyppi: ");
			kiinteiston_tyyppi = lukija.nextLine();
			System.out.println("Anna " + i +". kiinteist�n sijainti: ");
			kiinteiston_sijainti = lukija.nextLine();
			System.out.println("Anna " + i +". kiinteist�n vakuutusarvo: ");
			vakuutusarvo = Double.parseDouble(lukija.nextLine());
			Property p = new Property(kiinteiston_tyyppi, kiinteiston_sijainti);
			InsuranceInfo ii = new InsuranceInfo(p);
			ii.setVakuutusarvo(vakuutusarvo);
			ii_container.addToContainer(ii);
		}
		
		// 2. Tulostetaan s�ili�n koko sis�lt�.
		ii_container.printContainer();
		
		/*
		3. Sy�tet��n n�pp�imist�lt� arvo ja tulostetaan kiinteist�t, joiden vakuutusarvo on
		t�t� pienempi.
		*/
		System.out.println("Anna yl�raja tulostuksille: ");
		raja_arvo = Double.parseDouble(lukija.nextLine());
		ii_container.printLowerInsValue(raja_arvo);
		
		/*
		4. Sy�tet��n n�pp�imist�lt� arvo ja tulostetaan kiinteist�t, joiden vakuutusarvo on
		t�t� suurempi.
		*/
		System.out.println("Anna alaraja tulostuksille: ");
		raja_arvo = Double.parseDouble(lukija.nextLine());
		ii_container.printHigherInsValue(raja_arvo);
		
		System.out.println("\nKiitos!");
		lukija.close();
	}
}
