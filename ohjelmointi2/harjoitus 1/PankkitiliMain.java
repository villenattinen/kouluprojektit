import java.util.Scanner;

public class PankkitiliMain {

	public static void main(String [] args) {
		
		String tiliNro;
		String tilinOmistaja;
		double saldo;
		double otto;
		double pano;
		Scanner lukija = new Scanner(System.in);

		System.out.println("Anna pankkitilin numero: ");
		tiliNro = lukija.nextLine();
		System.out.println("Anna pankkitilin omistajan nimi: ");
		tilinOmistaja = lukija.nextLine();
		System.out.println("Anna pankkitilin saldo: ");
		saldo = lukija.nextDouble();
		Pankkitili pankkitili = new Pankkitili(tiliNro, tilinOmistaja, saldo);
		System.out.println(pankkitili);
		
		System.out.println("\nAnna tililtä nostettava määrä: ");
		otto = lukija.nextDouble();
		pankkitili.tiliOtto(otto);
		System.out.println(pankkitili);
		
		System.out.println("\nAnna tille pantava summa: ");
		pano = lukija.nextDouble();
		pankkitili.tiliTalletus(pano);
		System.out.println(pankkitili);
		
		System.out.println("\nKiitos!");
		lukija.close();
	}
}
