
public class Tontti {

	private String nimi;
	private String osoite;
	private int pinta_ala;
	private Rakennus rakennus;
	
	public Tontti(String nimi, String osoite, Rakennus rakennus) {
		this.nimi = nimi;
		this.osoite = osoite;
		this.rakennus = rakennus;
	}
	public Tontti(String nimi, String osoite, int pinta_ala, Rakennus rakennus) {
		this.nimi = nimi;
		this.osoite = osoite;
		this.pinta_ala = pinta_ala;
		this.rakennus = rakennus;
	}
	public String getNimi() {
		return nimi;
	}
	public void setNimi(String nimi) {
		this.nimi = nimi;
	}
	public String getOsoite() {
		return osoite;
	}
	public void setOsoite(String osoite) {
		this.osoite = osoite;
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
		return "Tontin nimi: "+ nimi + "\nTontin osoite: " + osoite +
				"\nTontin pinta-ala: " + pinta_ala + "\n" + rakennus;
	}
	public void printTontin_tiedot() {
		System.out.println("Tontin nimi: " + nimi);
		System.out.println("Tontin osoite: " + osoite);
		System.out.println("Tontin pinta-ala: " + pinta_ala);
		System.out.println(rakennus);
	}
}
