import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author darkf
 * La boussole du Robot lui permettant de se deplacer sur la table
 */
public class Boussole {

	private Map<String,Integer> card = new HashMap<>();
	private int dir;
	private String pos;
	//Constructor
	/**
	 * @author darkf
	 * Constructeur par defaut, initialise les 4 points cardinaux et etabli la base du Robot comme etant la position 0
	 */
	public Boussole() {
		card.put("Nord",90);
		card.put("Sud", 270);
		card.put("Est", 0);
		card.put("Ouest",180);
		this.pos = "A";
	}
	
	/**
	 * @author darkf
	 * @see Boussole.Boussole()
	 * @param angle angle de base vers lequel le robot porte son regard lors de l'initialisation de la boussole
	 * @param x position en abcisse du robot
	 * @param y position en ordonnees du robot
	 */

	public Boussole(int angle) {
		this();
		dir = angle;
	}
	
	//Methods
	/**
	 * @author darkf
	 * @return la direction du champ visuel du robot
	 */
	public int getDir() {
		return this.dir;
	}
	
	/**
	 * @author darkf
	 * @param newPos la nouvelle case sur laquelle se trouve le robot
	 */
	public void setPos(String newPos) {
		this.pos = newPos;
	}
	
	/**
	 * @author darkf
	 * @param angle L'angle que le regard du robot doit effectuer
	 */
	
	
	public String getPos() {
		return pos;
	}

	/**
	 * @author darkf
	 * @param dir La direction sur laquelle doit se fixer le robot (si elle est presente dans les cardinalite)
	 * @return l'angle de deplacement necessaire pour que le regard se tourne dans la direction voulue
	 */
	public int setDir(int angle) {
		int tmp = this.dir;
		this.dir = (this.dir + angle)%360;
		return tmp - this.dir;
	}
}

