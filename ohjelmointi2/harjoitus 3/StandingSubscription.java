
public class StandingSubscription extends Subscription {
	
	private int alennusprosentti;
	
	public StandingSubscription(String lehden_nimi, String tilaajan_nimi, 
			String toimitusosoite, double kuukausihinta, int alennusprosentti) {
		super(lehden_nimi, tilaajan_nimi, toimitusosoite, kuukausihinta);
		this.alennusprosentti = alennusprosentti;
	}
	public int getAlennusprosentti() {
		if (alennusprosentti > 0) {
			return alennusprosentti;
		}
		else {
			return 0;
		}
	}
	public void setAlennusprosentti(int alennusprosentti) {
		if (alennusprosentti > 0 && alennusprosentti <= 100) {
			this.alennusprosentti = alennusprosentti;
		}
		else {
			this.alennusprosentti = 0;
		}
	}
	public String getTilauksen_tyyppi() {
		return "Kestotilaus";
	}
	public double getTilauksen_hinta() {
		return 12*super.getKuukausihinta()*(100-alennusprosentti)/100;
	}
	public int getTilauksen_kesto() {
		return 12;
	}
	public void printInvoice() {
		super.printInvoice();
	}
}
