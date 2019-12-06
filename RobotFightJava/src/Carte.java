/**
 * 
 * @author darkf
 *
 */
public class Carte {
	private int baseE,baseA;
	
	public Carte(int e, int a) {
		this.baseA = a;
		this.baseE = e;
	}
	
	/**
	 * @author darkf
	 * @return la position de la base ennemie
	 */
	public int getBaseE() {return baseE;}
	/**
	 * @author darkf
	 * @param baseE angle de la base ennemie
	 */
	public void setBaseE(int baseE) {this.baseE = baseE;}
	/**
	 * @author darkf
	 * @return la position de la base aliee
	 */
	public int getBaseA() {return baseA;}
	/**
	 * @author darkf
	 * @param baseA angle de la base alliee
	 */
	public void setBaseA(int baseA) {this.baseA = baseA;}
	
	
}
