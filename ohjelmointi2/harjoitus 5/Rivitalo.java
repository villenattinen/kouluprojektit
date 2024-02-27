import java.util.ArrayList;

public class Rivitalo extends Rakennus {
	
	public Rivitalo(ArrayList<Asukas> asukkaat) {
		super(asukkaat);
	}
	public Rivitalo(int asuntojen_lkm, int asunnon_ala, int huoneiden_lkm, ArrayList<Asukas> asukkaat) {
		super(asuntojen_lkm, asunnon_ala, huoneiden_lkm, asukkaat);
	}
	public String getRakennuksen_tyyppi() {
		return this.getClass().getName();
	}
	@Override
	public String toString() {
		return super.toString();
	}
	public void printRivitalon_tiedot() {
		super.printRakennuksen_tiedot();
	}
}
