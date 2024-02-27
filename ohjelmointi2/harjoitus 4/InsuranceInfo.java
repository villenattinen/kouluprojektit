public class InsuranceInfo {

	private Property kiinteisto;
	private double vakuutusarvo;

	public InsuranceInfo(Property kiinteisto) {
		this.kiinteisto = kiinteisto;
	}
	public InsuranceInfo(Property kiinteisto, double vakuutusarvo) {
		this.kiinteisto = kiinteisto;
		this.vakuutusarvo = vakuutusarvo;
	}
	public String getKiinteiston_tyyppi() {
		return kiinteisto.getKiinteiston_tyyppi();
	}
	public void setKiinteiston_tyyppi(String kiinteiston_tyyppi) {
		kiinteisto.setKiinteiston_tyyppi(kiinteiston_tyyppi);
	}
	public String getKiinteiston_sijainti() {
		return kiinteisto.getKiinteiston_sijainti();
	}
	public void setKiinteiston_sijainti(String kiinteiston_sijainti) {
		kiinteisto.setKiinteiston_sijainti(kiinteiston_sijainti);
	}
	public double getVakuutusarvo() {
		return vakuutusarvo;
	}
	public void setVakuutusarvo(double vakuutusarvo) {
	    if (vakuutusarvo >= 0) {
	    	this.vakuutusarvo = vakuutusarvo;
	    }
	}
	@Override
	public String toString() {
		return String.format("%sVakuutusarvo: %s\n", kiinteisto, vakuutusarvo);
	}
}
