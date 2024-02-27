import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Sanalista {
	
	private String tiedosto;
	private List<String> sanat = new ArrayList<>();
	private String mjono;
	private int pituus;
	
	public Sanalista(List<String> sanat) {
		this.sanat = sanat;
	}
	public Sanalista(String tiedosto) {
		try (BufferedReader sanalista = new BufferedReader ( new FileReader(tiedosto))) {
		    String rivi;
		    while ((rivi = sanalista.readLine()) != null) {
		        sanat.add(rivi.toLowerCase());
		    }
		}
		catch (FileNotFoundException e) {
			e.printStackTrace(); 
			System.out.println("Virhe: tiedostoa ei löytynyt");
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("Virhe: tiedostoa ei voitu avata");
	    }
	}
	public String getTiedosto() {
		return tiedosto;
	}
	public void setTiedosto(String tiedosto) {
		this.tiedosto = tiedosto;
	}
	public List<String> annaSanat(){
		return sanat;
	}
	public void setSanat(List<String> sanat) {
		this.sanat = sanat;
	}
	public String getMjono() {
		return mjono;
	}
	public void setMjono(String mjono) {
		this.mjono = mjono;
	}
	public int getPituus() {
		return pituus;
	}
	public void setPituus(int pituus) {
		this.pituus = pituus;
	}
	public Sanalista sanatJoidenPituusOn(int pituus) {
		Sanalista sl_pituuden_mukaan = new Sanalista(this.sanat);
		List<String> uudet_sanat = new ArrayList<>();
		for (String str : this.sanat) {
			if (str.length() == pituus) {
				uudet_sanat.add(str);
			}
		}
		sl_pituuden_mukaan.setSanat(uudet_sanat);
		return sl_pituuden_mukaan;
	}
	public Sanalista sanatJoissaMerkit(String mjono) {
		Sanalista sl_merkkien_mukaan = new Sanalista(this.sanat);
		List<String> uudet_sanat = new ArrayList<>();
		String tyhja = "_";
		for (String str : this.sanat) {
			if (str.length() == mjono.length()) {
				for (int i = 0; i < mjono.length(); i++) {
					if ((mjono.charAt(i) != tyhja.charAt(0)) && (str.charAt(i) != mjono.charAt(i))) {
						break;
					} 
					else if (i == (mjono.length()-1)) {
						uudet_sanat.add(str);
					} else continue;
				}
			}
		}
		sl_merkkien_mukaan.setSanat(uudet_sanat);
		return sl_merkkien_mukaan;
	}
}
