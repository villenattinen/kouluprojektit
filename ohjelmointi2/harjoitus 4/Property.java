public class Property {

	private String kiinteiston_tyyppi;
	private String kiinteiston_sijainti;
	
	public Property(String kiinteiston_tyyppi, String kiinteiston_sijainti) {
		this.kiinteiston_tyyppi = kiinteiston_tyyppi;
		this.kiinteiston_sijainti = kiinteiston_sijainti;
	}
	public String getKiinteiston_tyyppi() {
		return kiinteiston_tyyppi;
	}
	public void setKiinteiston_tyyppi(String kiinteiston_tyyppi) {
		this.kiinteiston_tyyppi = kiinteiston_tyyppi;
	}
	public String getKiinteiston_sijainti() {
		return kiinteiston_sijainti;
	}
	public void setKiinteiston_sijainti(String kiinteiston_sijainti) {
		this.kiinteiston_sijainti = kiinteiston_sijainti;
	}
	@Override
	public String toString() {
		return String.format("\nKiinteistön tyyppi: %s\nKiinteistön sijainti: %s\n",
				kiinteiston_tyyppi, kiinteiston_sijainti);
	}
}
