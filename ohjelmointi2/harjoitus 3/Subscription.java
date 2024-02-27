
public class Subscription {
	
	private String lehden_nimi;
	private String tilaajan_nimi;
	private String toimitusosoite;
	private double kuukausihinta;
	
	public Subscription(String lehden_nimi, String tilaajan_nimi, String toimitusosoite, double kuukausihinta) {
		this.lehden_nimi = lehden_nimi;
		this.tilaajan_nimi = tilaajan_nimi;
		this.toimitusosoite = toimitusosoite;
		this.kuukausihinta = kuukausihinta;
	}
	public String getLehden_nimi() {
		return lehden_nimi;
	}
	public void setLehden_nimi(String lehden_nimi) {
		this.lehden_nimi = lehden_nimi;
	}
	public String getTilaajan_nimi() {
		return tilaajan_nimi;
	}
	public void setTilaajan_nimi(String tilaajan_nimi) {
		this.tilaajan_nimi = tilaajan_nimi;
	}
	public String getToimitusosoite() {
		return toimitusosoite;
	}
	public void setToimitusosoite(String toimitusosoite) {
		this.toimitusosoite = toimitusosoite;
	}
	public double getKuukausihinta() {
		if (kuukausihinta > 0) {
			return kuukausihinta;
		}
		else {
			return 0;
		}
	}
	public void setKuukausihinta(double kuukausihinta) {
		if (kuukausihinta > 0) {
			this.kuukausihinta = kuukausihinta;
		}
	}
	public String getTilauksen_tyyppi() {
		return "Ei tilausta.";
	}
	public double getTilauksen_hinta() {
		return 0;
	}
	public int getTilauksen_kesto() {
		return 0;
	}
	public void printInvoice() {
		System.out.println("Tilauksen tyyppi: " + getTilauksen_tyyppi());
		System.out.println("Lehden nimi: " + lehden_nimi);
		System.out.println("Tilaajan nimi: " + tilaajan_nimi);
		System.out.println("Toimitusosoite: " + toimitusosoite);
		System.out.println("Laskutettavat kuukaudet: " + getTilauksen_kesto());
		System.out.println("Tilauksen hinta: " + getTilauksen_hinta());
	}
}
