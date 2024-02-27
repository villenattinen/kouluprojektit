import java.util.Scanner;
import java.util.InputMismatchException;

public class Vt6main {
	public static void main(String [] args) {
		int arvausten_lkm = 0;
		Character merkki;
		Scanner lukija = new Scanner(System.in);
		/*
		 * Pyydet‰‰n arvausten lukum‰‰r‰. V‰hint‰‰n yksi, jotta p‰‰st‰‰n pelaamaan
		 * ja korkeintaan 29 suomalaisen aakkoston mukaan. Ei tietenk‰‰n kovin
		 * mielek‰s m‰‰r‰ k‰yt‰nnˆss‰.
		 */
		System.out.println("Anna arvausten lukum‰‰r‰. "
		+ "Yleens‰ 6 (piirret‰‰n ukko) tai 10 (piirret‰‰n hirsipuu ja ukko).");
		do {
			System.out.println("Anna arvausten m‰‰r‰ kokonaislukuna v‰lilt‰ 1-29:");
			try {
				arvausten_lkm = lukija.nextInt();
			}
			catch (InputMismatchException e) { 
				lukija.next();
			}
		} while(arvausten_lkm < 1 || arvausten_lkm > 29);
		
		Hirsipuu hp = new Hirsipuu(new Sanalista("sanalista.txt"), arvausten_lkm);

		/*
		 * Tulostetaan k‰ytett‰viss‰ olevien arvausten m‰‰r‰, arvattavan sanan n‰kyv‰ osuus 
		 * ja jo arvatut kirjaimet. Kysyt‰‰n uutta merkki‰ ja varmistetaan, ett‰ se on
		 * kirjain. Mik‰li syˆtet‰‰n enemm‰n kuin yksi merkki kerrallaan, 
		 * niin otetaan vain ensimm‰inen kirjain huomioon. Jatketaan, kunnes p‰‰ttymisehto 
		 * Hirsipuu-luokassa on t‰yttynyt.
		 */
		while (!hp.onLoppu()) {
			System.out.println("\nArvauksia j‰ljell‰: " + hp.arvauksiaOnJaljella());
			System.out.println("Arvattava sana: " + hp.nakyva_sana());
			System.out.println("Arvatut kirjaimet: " + hp.arvaukset());
			do {
				System.out.println("Arvaa kirjain:");
				merkki = lukija.next().toLowerCase().charAt(0);
			} while(!Character.isLetter(merkki));
			hp.arvaa(merkki);
		}	
		if (hp.nakyva_sana().equals(hp.sana())) {
			System.out.println("__________");
			System.out.println("|/       |");
			System.out.println("|        |");
			System.out.println("|            --- _ O");
			System.out.println("|           --- __/\\");
			System.out.println("|___         ---- \\");
			System.out.println("Voitit pelin!");
		} else {
			System.out.println("__________");
			System.out.println("|/       |");
			System.out.println("|        O");
			System.out.println("|       /|\\");
			System.out.println("|       / \\");
			System.out.println("|___");
			System.out.println("H‰visit pelin.");
		}
		System.out.println("Arvattava sana oli: " + hp.sana());
		lukija.close();
	}
}
