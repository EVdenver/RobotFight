
public class Carte {
	private Base baseA;
	private Base baseE;
	private int nbPalet;
	private Palet[] tabP = new Palet[9];
	
	
	public Base getBaseA() {return baseA;}

	public void setBaseA(Base baseA) {this.baseA = baseA;}

	public Base getBaseE() {return baseE;}

	public void setBaseE(Base baseE) {this.baseE = baseE;}

	public int getNbPalet() {return nbPalet;}

	public void setNbPalet(int nbPalet) {this.nbPalet = nbPalet;}

	public Palet getTabP(int i) {return tabP[i];}

	public void setTabP(Palet tabP,int i) {this.tabP[i] = tabP;}

	public Carte() {
		baseA = new Base();
		baseE = new Base();
		setNbPalet(9);
		for (int i = 0; i < tabP.length; i++) {
			setTabP(new Palet(),i);
		}
	}
	
	public Carte(Position[] posP,Position posA, Position posE, String dirA,String dirE) {
		baseA = new Base(posA,dirA);
		baseE = new Base(posE,dirE);
		setNbPalet(9);
		for (int i = 0; i < posP.length; i++) {
			setTabP(new Palet(posP[1]),i);
		}
	}
}
