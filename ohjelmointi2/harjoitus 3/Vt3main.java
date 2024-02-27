import java.util.Scanner;

public class Vt3main {
	public static void main (String [] args) {
	
		String lehden_nimi;
		String tilaajan_nimi;
		String toimitusosoite;
		double kuukausihinta;
		int tilauksen_kesto;
		int alennusprosentti;
		Scanner lukija = new Scanner(System.in);
		
		System.out.println("\nTavallinen tilaus\n");
		System.out.println("Anna lehden nimi: ");
		lehden_nimi = lukija.nextLine();
		System.out.println("Anna tilaajan nimi: ");
		tilaajan_nimi = lukija.nextLine();
		System.out.println("Anna toimitusosoite: ");
		toimitusosoite = lukija.nextLine();
		System.out.println("Anna kuukausihinta: ");
		kuukausihinta = lukija.nextDouble();
		System.out.println("Anna tilauksen kesto: ");
		tilauksen_kesto = lukija.nextInt();
		lukija.nextLine();
		
		Subscription subs = new RegularSubscription(lehden_nimi, tilaajan_nimi, toimitusosoite, kuukausihinta, tilauksen_kesto);
		printSubscriptionInvoice(subs);
		
		System.out.println("\nKestotilaus\n");
		System.out.println("Anna lehden nimi: ");
		lehden_nimi = lukija.nextLine();
		System.out.println("Anna tilaajan nimi: ");
		tilaajan_nimi = lukija.nextLine();
		System.out.println("Anna toimitusosoite: ");
		toimitusosoite = lukija.nextLine();
		System.out.println("Anna kuukausihinta: ");
		kuukausihinta = lukija.nextDouble();
		System.out.println("Anna alennusprosentti: ");
		alennusprosentti = lukija.nextInt();
		lukija.nextLine();

		subs = new StandingSubscription(lehden_nimi, tilaajan_nimi, toimitusosoite, kuukausihinta, alennusprosentti);
		printSubscriptionInvoice(subs);

		lukija.close();	
		System.out.println("\nKiitos!");
	}
	static void printSubscriptionInvoice(Subscription subs) {
		System.out.println("\n------------------------------------");
		subs.printInvoice();
		System.out.println("------------------------------------\n");
	}
}