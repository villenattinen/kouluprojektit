public class Pankkitili {

	private String tiliNro;
	private String tilinOmistaja;
	private double saldo;
	
	public Pankkitili(String tiliNro, String tilinOmistaja, double saldo) {
		this.tiliNro = tiliNro;
		this.tilinOmistaja = tilinOmistaja;
		this.saldo = saldo;
	}
	public String getTiliNro() {
		return tiliNro;
	}
	public void setTiliNro(String tiliNro) {
		this.tiliNro = tiliNro;
	}
	public String getTilinOmistaja() {
		return tilinOmistaja;
	}
	public void setTilinOmistaja(String tilinOmistaja) {
		this.tilinOmistaja = tilinOmistaja;
	}
	public double getSaldo() {
		return saldo;
	}
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	public void tiliOtto(double otto) {
		if (otto <= saldo && otto >= 0) {
			saldo-=otto;
		}
	}
	public void tiliTalletus(double pano) {
		if (pano >= 0) {
			saldo+=pano;
		}
	}
	@Override
	public String toString() {
		return "\nPankkitili: \nTilinumero: " + tiliNro + " \nTilin omistaja: " + tilinOmistaja + "\nTilin saldo: " + saldo;
	}
}
