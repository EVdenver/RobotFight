import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author darkf
 * La boussole du Robot lui permettant de se déplacer sur la table
 */
public class Boussole {
	//Les points cardinaux de la boussole
	Map<String,Integer> card = new HashMap<>();
	//La direction dans laquelle il regarde
	int dir;
	//La position du robot
	Position pos;
	
	//Constructor
	/**
	 * @author darkf
	 * Constructeur par défaut, initialise les 4 points cardinaux et établi la base du Robot comme étant la position 0
	 * Et la position par défaut du robot
	 */
	public Boussole() {
		card.put("Nord",90);
		card.put("Sud", 270);
		card.put("Est", 0);
		card.put("Ouest",180);
		pos = new Position();
	}
	
	/**
	 * @author darkf
	 * @see Boussole.Boussole()
	 * @param angle angle de base vers lequel le robot porte son regard lors de l'initialisation de la boussole
	 * @param x position en abcisse du robot
	 * @param y position en ordonnées du robot
	 */
	public Boussole(int angle, int x, int y) {
		this();
		dir = angle;
		pos.setX(x);
		pos.setY(y);
	}
	
	//Methods
	/**
	 * @author darkf
	 * @return dir la direction dans laquelle regarde le robot
	 */
	public int getDir() {
		return this.dir;
	}
	
	/**
	 * @author darkf
	 * @param angle L'angle que le regard du robot doit effectuer
	 */
	public void setDir(int angle) {
		this.dir = (this.dir + angle)%360;
	}
	
	
	/**
	 * @author darkf
	 * @param dir La direction sur laquelle doit se fixer le robot (si elle est présente dans les cardinalité)
	 * @return l'angle de déplacement nécessaire pour que le regard se tourne dans la direction voulue
	 */
	public int setDir(String dir) {
		if(card.containsKey(dir)) {
			int tmp = this.dir;
			this.dir = card.get(dir);
			return tmp - this.dir;
		}else {
			throw new IllegalArgumentException("La cardinalité voulu n'éxiste pas");
		}
	}
	
	public void setPosition(int x, int y) {
		pos.setX(x);
		pos.setY(y);
	}
}

