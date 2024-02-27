import java.util.ArrayList; 

public class Rakennus {

	private int asuntojen_lkm;
	private int asunnon_ala;
	private int huoneiden_lkm;
	private ArrayList<Asukas> asukkaat;
	private ArrayList<Integer> asuntojen_alat = new ArrayList<>();
	private ArrayList<Integer> asuntojen_huoneiden_lkm = new ArrayList<>();
	
	public Rakennus(ArrayList<Asukas> asukkaat) {
		this.asukkaat = asukkaat;
	}
	public Rakennus(int asuntojen_lkm, int asunnon_ala, int huoneiden_lkm, ArrayList<Asukas> asukkaat) {
		this.asuntojen_lkm = asuntojen_lkm;
		this.asunnon_ala = asunnon_ala;
		this.huoneiden_lkm = huoneiden_lkm;
		this.asukkaat = asukkaat;
	}
	public int getAsunnon_ala() {
		return asunnon_ala;
	}
	public void setAsuntojen_lkm(int asuntojen_lkm) {
		if (asuntojen_lkm > 0) {
			this.asuntojen_lkm = asuntojen_lkm;
		}
	}
	public int getAsuntojen_lkm() {
		return asuntojen_lkm;
	}
	public void setAsunnon_ala(int asunnon_ala) {
		if (asunnon_ala >= 0) {
			this.asunnon_ala = asunnon_ala;
		} 
		asuntojen_alat.add(this.asunnon_ala);
	}
	public int getHuoneiden_lkm() {
		return huoneiden_lkm;
	}
	public void setHuoneiden_lkm(int huoneiden_lkm) {
		if (huoneiden_lkm >= 0) {
			this.huoneiden_lkm = huoneiden_lkm;
		}
		asuntojen_huoneiden_lkm.add(this.huoneiden_lkm);
	}
	public void setAsukkaat(ArrayList<Asukas> asukkaat) {
		this.asukkaat = asukkaat;
	}
	public ArrayList<Asukas> getAsukkaat() {
		return asukkaat;
	}
	public ArrayList<Integer> getAsuntojen_alat() {
		return asuntojen_alat;
	}
	public void setAsuntojen_alat(ArrayList<Integer> asuntojen_alat) {
		this.asuntojen_alat = asuntojen_alat;
	}
	public ArrayList<Integer> getAsuntojen_huoneiden_lkm() {
		return asuntojen_huoneiden_lkm;
	}
	public void setAsuntojen_huoneiden_lkm(ArrayList<Integer> asuntojen_huoneiden_lkm) {
		this.asuntojen_huoneiden_lkm = asuntojen_huoneiden_lkm;
	}
	public String getRakennuksen_tyyppi() {
		return "Ei määritetty";
	}
	@Override
	public String toString() {
		String str = "Rakennuksen tyyppi: " + getRakennuksen_tyyppi()
		+ "\nAsuntojen lukumäärä: " + asuntojen_lkm + "\nAsuntojen pinta-alat:";
		for (int i = 0, j = 1; i < asuntojen_alat.size(); i++, j++) {
			str+=("\n" + j + ". asunto: " + asuntojen_alat.get(i));			
		}
		str+="\nAsuntojen huoneiden lukumäärät:";
		for (int i = 0, j = 1; i < asuntojen_huoneiden_lkm.size(); i++, j++) {
			str+=("\n" + j + ". asunto: " + asuntojen_huoneiden_lkm.get(i));			
		}
		str+="\nAsukkaat:";
		for (int i = 0, j = 1; i < asukkaat.size(); i++, j++) {
			str+=("\n" + j + ". asukas: " +asukkaat.get(i));			
		}
		return str;
	}
	public void printRakennuksen_tiedot() {
		System.out.println("Rakennuksen tyyppi: " + getRakennuksen_tyyppi());
		System.out.println("Asuntojen lukumäärä: " + asuntojen_lkm);
		System.out.println("Asuntojen pinta-alat: ");
		for (int i = 0, j = 1; i < asuntojen_alat.size(); i++, j++) {
			System.out.println(j + ". asunto: " + asuntojen_alat.get(i));			
		}
		System.out.println("Asuntojen huoneiden lukumäärä: ");
		for (int i = 0, j = 1; i < asuntojen_huoneiden_lkm.size(); i++, j++) {
			System.out.println(j + ". asunto: " + asuntojen_huoneiden_lkm.get(i));			
		}
		System.out.println("Asukkaat: ");
		for (int i = 0, j = 1; i < asukkaat.size(); i++, j++) {
			System.out.println(j + ". asukas: " +asukkaat.get(i));			
		}
	}
}
