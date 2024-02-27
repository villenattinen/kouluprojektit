import java.util.ArrayList;

public class Kerrostalo extends Rakennus{
	
	public Kerrostalo(ArrayList<Asukas> asukkaat) {
		super(asukkaat);
	}
	public Kerrostalo(int asuntojen_lkm, int asunnon_ala, int huoneiden_lkm, ArrayList<Asukas> asukkaat) {
		super(asuntojen_lkm, asunnon_ala, huoneiden_lkm, asukkaat);
	}
	public String getRakennuksen_tyyppi() {
		return this.getClass().getName();
	}
	@Override
	public String toString() {
		return super.toString();
	}
	public void printKerrostalon_tiedot() {
		super.printRakennuksen_tiedot();
	}
}
