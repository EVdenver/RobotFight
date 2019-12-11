package codeRobot;
/**
 * @author Vincent
 * La carte sur laquelle le robot se repere
 */
public class Carte {
	private int baseE,baseA;
	
	//Constructor
	/**
	 * @author Vincent
	 * @param e la position de la base ennemie
	 * @param a la position de la base alliee
	 * Constructeur de la carte
	 */
	public Carte(int e, int a) {
		this.baseA = a;
		this.baseE = e;
	}
	
	//Getters/Setters
	/**
	 * @author Vincent
	 * @return la position de la base ennemie
	 * Accesseur de la base ennemie
	 */
	public int getBaseE() {return baseE;}
	/**
	 * @author Vincent
	 * @return la position de la base aliee
	 * Accesseur de la base alliee
	 */
	public int getBaseA() {return baseA;}
	
	//Method
	/**
	 * @author Charlotte
	 * @return la direction du mur a droite de la base ennemie
	 */
	public int getDirDroiteE() {
		if(baseE==0) {
			return 270;
		}
		return 90;
	}
	
	/**
	 * @author Charlotte
	 * @return la direction du mur a gauche de la base ennemie
	 */
	public int getDirGaucheE() {
		if(baseE==0) {
			return 90;
		}
		return 270;
	}
	
}
