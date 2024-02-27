import java.util.ArrayList; 

public class InsInfoContainer {
	
	private ArrayList<InsuranceInfo> vakuutuskokoelma = new ArrayList<>();

	public void addToContainer(InsuranceInfo vakuutustieto) {
		vakuutuskokoelma.add(vakuutustieto);
	}
	public void printContainer() {
		for (int i = 0; i < vakuutuskokoelma.size(); i++) {
			System.out.println(vakuutuskokoelma.get(i));
		}
	}
	public void printHigherInsValue(double raja_arvo) {
		for (int i = 0; i < vakuutuskokoelma.size(); i++) {
			if (vakuutuskokoelma.get(i).getVakuutusarvo() > raja_arvo) {
				System.out.println(vakuutuskokoelma.get(i));
			}
		}
	}
	public void printLowerInsValue(double raja_arvo) {
		for (int i = 0; i < vakuutuskokoelma.size(); i++) {
			if (vakuutuskokoelma.get(i).getVakuutusarvo() < raja_arvo) {
				System.out.println(vakuutuskokoelma.get(i));			
			}
		}
	}
}
