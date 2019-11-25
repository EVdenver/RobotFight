
/**
 * 
 * @author darkf
 * 
 */
public class Carte {
	private String baseA;
	private String baseE;
	private int nbPalet;
	private Palet[] tabP = new Palet[9];
	
	
	public String getBaseA() {return baseA;}

	public String getBaseE() {return baseE;}

	public int getNbPalet() {return nbPalet;}
	
	public Palet[] getTabP() {return tabP;}

	public Palet getTabP(int i) {return tabP[i];}

	public void setTabP(Palet tabP,int i) {this.tabP[i] = tabP;}

	public Carte() {
		baseA = new String();
		baseE = new String();
		nbPalet = 9;
		for (int i = 0; i < tabP.length; i++) {
			setTabP(new Palet(),i);
		}
	}
	
	public Carte(String dirA,String dirE) {
		baseA = dirA;
		baseE = dirE;
		nbPalet = 9;
		int i = 0;
		while(i != 9) {
			for (int x = 90; x < 211;x = x+60 ) {
				for (int y = 50; y < 151; y = y+50) {
					setTabP(new Palet(new Position(x,y)),i);
					i++;
				}
			}
		}
	}
}
