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
		this.dir = (this.dir + angle)%360;
	}
	//Methods
	public void corrigerBoussole(String couleur) {
		if(!getCouleur().equals(couleur)) {
			
		}
	}
}

