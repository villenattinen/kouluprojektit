import java.util.ArrayList; 
import java.util.List; 
import java.util.Random;

public class Hirsipuu {
	
	private Sanalista sanalista;
	private List<String> sanat;
	private String sana;
	private String nakyva_sana;
	private int arvausten_lkm;
	private Character merkki;
	private List<Character> arvaukset = new ArrayList<Character>();
	
	public Hirsipuu(Sanalista sanalista, int arvausten_lkm) {
		this.sanalista = sanalista;
		this.arvausten_lkm = arvausten_lkm;
		Random rdm = new Random();
		sanat = this.sanalista.annaSanat();
		int rdm_int = rdm.nextInt(sanat.size());
		sana = sanat.get(rdm_int);
		nakyva_sana = "";
		for (int i = 0; i < sana.length(); i++) {
			nakyva_sana+="*";
		}
	}
	public Sanalista getSanalista() {
		return sanalista;
	}
	public void setSanalista(Sanalista sanalista) {
		this.sanalista = sanalista;
	}
	public Character getMerkki() {
		return merkki;
	}
	public void setMerkki(Character merkki) {
		this.merkki = merkki;
	}
	public List<Character> arvaukset() {
		return arvaukset;
	}
	public void setArvaukset(List<Character> arvaukset) {
		this.arvaukset = arvaukset;
	}
	public int arvauksiaOnJaljella() {
		return arvausten_lkm;
	}
	public void setArvausten_lkm(int arvausten_lkm) {
		if (arvausten_lkm >= 0) {
			this.arvausten_lkm = arvausten_lkm;
		}
	}
	public String nakyva_sana() {
		return nakyva_sana;
	}
	public void setNakyva_sana(String nakyva_sana) {
		this.nakyva_sana = nakyva_sana;
	}
	public String sana() {
		return sana;
	}
	public void setSana(String sana) {
		this.sana = sana;
	}
	public boolean arvaa(Character merkki) {
		boolean bool = false;
		if (!arvaukset.contains(merkki)) {
			arvaukset.add(merkki);
			for (int i = 0; i < sana.length(); i++ ) {
				if (sana.charAt(i) == merkki) {
					StringBuilder uusi_nakyva_sana = new StringBuilder(nakyva_sana);
					uusi_nakyva_sana.setCharAt(i, merkki);
					nakyva_sana = String.valueOf(uusi_nakyva_sana);
					bool = true;
				} 
			}
			if (!bool) {
				arvausten_lkm-=1;
			}
		} 
		else {
			System.out.println("\nKirjain on jo käytetty!");
		}
		return bool;
	}
	public boolean onLoppu() {
		if (nakyva_sana.equals(sana) || arvausten_lkm <= 0) {
			return true;
		} else return false;
	}
}
