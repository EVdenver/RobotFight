/**
 * 
 * @author darkf
 * La boussole du Robot lui permettant de se deplacer sur la table
 */
public class Boussole {
	private String couleur = new String();
	private int dir;
	//Constructor
	/**
	 * @author darkf
	 * @param angle angle de base vers lequel le robot porte son regard lors de l'initialisation de la boussole
	 */
	public Boussole(int angle) {
		this.dir = angle;
	}
	//Accessor
	/**
	 * @author darkf
	 * @return la dernière couleur gardé en mémoire
	 */
	public String getCouleur() {
		return couleur;
	}
	/**
	 * @author darkf
	 * @param couleur la nouvelle couleur croisé
	 */
	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}
	/**
	 * @author darkf
	 * @return la direction du champ visuel du robot
	 */
	public int getDir() {
		return this.dir;
	}		
	/**
	 * @author darkf
	 * @param angle la valeur de l'angle qu'il a effectué
	 */
	public void setDir(int angle) {
		this.dir = (this.dir + (angle+360))%360;
	}
	//Methods
	
	/**
	 * @author darkf
	 * @param couleur la nouvelle couleur vue
	 * @return true si le robot etait pas dans le bon sens tout en recalibrant la boussole, false sinon
	 * 
	 * Prend la couleur en parametre et si la combinaison de couleur existe dans l'enum existe regarde si le robot est dans le bons sens et corrige la boussole
	 */
	public boolean corrigerBoussole(String couleur) {
		CouleurLocalisation c = initCouleur(couleur);
		if(!getCouleur().equals(couleur)) {
			switch (c) {
			case paire1:
				if (this.dir > c.getAngle1() && this.dir < c.getAngle2()) {
					if (c.getAngle1()+this.dir <180) {
						setDir(c.getAngle1()+10);
					}else {
						setDir(c.getAngle2()-10);
					}
					return true;
				}
			case paire2 :
				if (this.dir < c.getAngle1() && this.dir > c.getAngle2()) {
					if (c.getAngle1()-this.dir > 0) {
						setDir(c.getAngle1()-10);
					}else {
						setDir(c.getAngle2()+10);
					}
					return true;
				}
			case paire3 :
				if (this.dir < c.getAngle1() && this.dir > c.getAngle2()) {
					if (c.getAngle1()-this.dir > 90) {
						setDir(c.getAngle1()-10);
					}else {
						setDir(c.getAngle2()+10);
					}
					return true;
				}
			case paire4 :
				if (this.dir < c.getAngle1() && this.dir > c.getAngle2()) {
					if ((c.getAngle1()+(this.dir+360))%360 > 270 || (c.getAngle1()+(this.dir+360))%360 < 10) {//Ce if est plus complique car risque de nombre negatif
						setDir(c.getAngle1()-10);
					}else {
						setDir(c.getAngle2()+10);
					}
					return true;
				}
			default:
				break;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param c1 la nouvelle couleur vue
	 * @return un element de l enum correspondant a la combinaisonn de couleur
	 */
	private CouleurLocalisation initCouleur(String c1) {
		if (c1.equals("green") && this.couleur.equals("blue")) {
			System.out.println("1");
			return CouleurLocalisation.paire1;
		}else if (c1.equals("blue") && this.couleur.equals("green")) {
			System.out.println("2");
			return CouleurLocalisation.paire2;
		}else if (c1.equals("yellow") && this.couleur.equals("red")) {
			System.out.println("4");
			return CouleurLocalisation.paire4;
		}else if (c1.equals("red") && this.couleur.equals("yellow")) {
			System.out.println("3");
			return CouleurLocalisation.paire3;
		}else {
			System.out.println("D");
			throw new IllegalArgumentException("Combinaison n'existe pas");
		}
	}
	
	public void setAbsoluteDir(int angle) {
		this.dir = angle;
	}
}

