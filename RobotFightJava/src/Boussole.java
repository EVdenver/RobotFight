/**
 * @author Vincent
 * La boussole du Robot lui permettant de se deplacer sur la table
 */
public class Boussole {
	private String couleur = new String();
	private int dir;
	//Constructor
	/**
	 * @author Vincent
	 * @param angle angle de base vers lequel le robot porte son regard lors de l'initialisation de la boussole
	 * Constructeur de la boussole
	 */
	public Boussole(int angle) {
		this.dir = angle;
	}
	//Accessor
	/**
	 * @author Vincent
	 * @return la dernière couleur gardé en mémoire
	 * Accesseur pour la couleur
	 */
	public String getCouleur() {
		return couleur;
	}
	/**
	 * @author Vincent
	 * @param couleur la nouvelle couleur croisé
	 * Mutateur pour la couleur
	 */
	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}
	/**
	 * @author Vincent
	 * @return la direction du champ visuel du robot
	 * Accesseur pour la direction
	 */
	public int getDir() {
		return this.dir;
	}		
	/**
	 * @author Vincent
	 * @param angle la valeur de l'angle qu'il a effectué
	 * Mutateur pour la direction
	 */
	public void setDir(int angle) {
		this.dir = (this.dir + (angle+360))%360;
	}

	/**
	 * @author Charlotte
	 * @param angle la nouvelle valeur de la dircetion sans incrementation
	 * Mutateur alternatif pour la direction
	 */
	public void setAbsoluteDir(int angle) {
		this.dir = angle;
	}
	//Methods

	/**
	 * @author Vincent
	 * @param couleur la nouvelle couleur vue
	 * @return true si le robot etait pas dans le bon sens tout en recalibrant la boussole, false sinon
	 * 
	 * Prend la couleur en parametre et si la combinaison de couleur existe dans l'enum existe regarde si le robot est dans le bons sens et corrige la boussole
	 */
	/*public boolean corrigerBoussole(String couleur) {
		CouleurLocalisation[] c = CouleurLocalisation.values();
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
	}*/
}