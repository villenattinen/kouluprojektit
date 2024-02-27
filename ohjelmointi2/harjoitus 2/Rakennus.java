import java.util.List; 

public class Rakennus {

	private int ala;
	private int huoneidenLkm;
	private List<Asukas> asukkaat;
	
	public Rakennus(int ala, int huoneidenLkm, List<Asukas> asukkaat) {
		this.ala = ala;
		this.huoneidenLkm = huoneidenLkm;
		this.asukkaat = asukkaat;
	}
	public int getAla() {
		return ala;
	}
	public void setAla(int ala) {
		if (ala >= 0) {
			this.ala = ala;
		}
	}
	public int getHuoneidenLkm() {
		return huoneidenLkm;
	}
	public void setHuoneidenLkm(int huoneidenLkm) {
		if (huoneidenLkm >= 0) {
			this.huoneidenLkm = huoneidenLkm;
		}
	}
	public void setAsukkaat(List<Asukas> asukkaat) {
		this.asukkaat = asukkaat;
	}
	public List<Asukas> getAsukkaat() {
		return asukkaat;
	}
	@Override
	public String toString() {
		return String.format("\nRakennuksen pinta-ala: %o\nHuoneiden lukum‰‰r‰: %o\n"
				+ "Asukkaat: %s\n", ala, huoneidenLkm, asukkaat); 
	}
}
