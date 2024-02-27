import java.util.ArrayList;

public class Omakotitalo extends Rakennus {
	
	public Omakotitalo(ArrayList<Asukas> asukkaat) {
		super(asukkaat);
	}
	public Omakotitalo(int asuntojen_lkm, int asunnon_ala, int huoneiden_lkm, ArrayList<Asukas> asukkaat) {
		super(asuntojen_lkm, asunnon_ala, huoneiden_lkm, asukkaat);
	}
	public String getRakennuksen_tyyppi() {
		return this.getClass().getName();
	}
	@Override
	public String toString() {
		return super.toString();
	}
	public void printOmakotitalon_tiedot() {
		super.printRakennuksen_tiedot();
	}
}
