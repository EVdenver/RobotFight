/**
 * @author Vincent
 *
 * Un enum contenant les paires de couleurs traitees lors de la correction de la boussole
 */
public enum CouleurLocalisation {
	paire1("green","blue",80,280),
	paire2("blue","green",100,260),	
	paire3("red","yellow",190,350),
	paire4("yellow","red",10,170);

	private String couleur1,couleur2;
	private int angle1,angle2;

	//Constructor
	/**
	 * @author Vincent
	 * @param c1
	 * @param c2
	 * @param a1
	 * @param a2
	 * Constructeur prive de l'enum
	 */
	private CouleurLocalisation(String c1,String c2,int a1, int a2) {
		this.couleur1 = c1;
		this.couleur2 = c2;
		this.angle1 = a1;
		this.angle2 = a2;
	}

	//Getters and Setters
	/**
	 * @author Vincent
	 * @return la premiere couleur d'une paire
	 */
	public String getCouleur1() {
		return this.couleur1;
	}

	/**
	 * @author Vincent
	 * @return la deuxieme couleur d'une paire
	 */
	public String getCouleur2() {
		return this.couleur2;
	}

	/**
	 * @author Vincent
	 * @return la premiere borne d'une paire
	 */
	public int getAngle1() {
		return this.angle1;
	}

	/**
	 * @author Vincent
	 * @return la deuxieme borne d'une paire
	 */
	public int getAngle2() {
		return this.angle2;
	}

	/**
	 * @author Vincent
	 * @return toute les valeurs possible de l'enum
	 */
	public CouleurLocalisation[] getEnum() {
		return CouleurLocalisation.values();
	}
}
