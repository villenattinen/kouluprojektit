
public class Tontti {

	private String nimi;
	private String sijainti;
	private int pinta_ala;
	private Rakennus rakennus;
	
	public Tontti(String nimi, String sijainti, int pinta_ala, Rakennus rakennus) {
		this.nimi = nimi;
		this.sijainti = sijainti;
		this.pinta_ala = pinta_ala;
		this.rakennus = rakennus;
	}
	public String getNimi() {
		return nimi;
	}
	public void setNimi(String nimi) {
		this.nimi = nimi;
	}
	public String getSijainti() {
		return sijainti;
	}
	public void setSijainti(String sijainti) {
		this.sijainti = sijainti;
	}
	public int getPinta_ala() {
		return pinta_ala;
	}
	public void setPinta_ala(int pinta_ala) {
		if (pinta_ala >= 0) {
			this.pinta_ala = pinta_ala;
		}
	}
	@Override
	public String toString() {
		return String.format("\nTontin nimi: %s\nTontin sijainti: %s\nTontin pinta-ala: %o\n"
				+ "Tontin rakennus: %s\n", nimi, sijainti, pinta_ala, rakennus); 
	}
}
