
public class Asukas {
	
	private String nimi;
	private String syntyma_aika;
	
	public Asukas(String nimi, String syntyma_aika) {
		this.nimi = nimi;
		this.syntyma_aika = syntyma_aika;
	}
	public String getNimi() {
		return nimi;
	}
	public void setNimi(String nimi) {
		this.nimi = nimi;
	}
	public String getSyntyma_aika() {
		return syntyma_aika;
	}
	public void setSyntyma_aika(String syntyma_aika) {
		this.syntyma_aika = syntyma_aika;
	}
	@Override
	public String toString() {
		return String.format("\nNimi: %s, syntymäaika: %s\n", nimi, syntyma_aika);
	}
}
