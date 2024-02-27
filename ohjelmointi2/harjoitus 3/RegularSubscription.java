
public class RegularSubscription extends Subscription {
	
	private int tilauksen_kesto;

	public RegularSubscription(String lehden_nimi, String tilaajan_nimi, 
			String toimitusosoite, double kuukausihinta, int tilauksen_kesto) {
		super(lehden_nimi, tilaajan_nimi, toimitusosoite, kuukausihinta);
		this.tilauksen_kesto = tilauksen_kesto;
	}
	public int getTilauksen_kesto() {
		if (tilauksen_kesto > 0) {
			return tilauksen_kesto;
		}
		else {
			return 0;
		}
	}
	public void setTilauksen_kesto(int tilauksen_kesto) {
		if (tilauksen_kesto > 0) {
			this.tilauksen_kesto = tilauksen_kesto;
		}
	}
	public String getTilauksen_tyyppi() {
		return "Tavallinen tilaus";
	}
	public double getTilauksen_hinta() {
		return tilauksen_kesto*super.getKuukausihinta();
	}
	public void printInvoice() {
		super.printInvoice();
	}
}
