import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author darkf
 * La boussole du Robot lui permettant de se d�placer sur la table
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
	 * Constructeur par d�faut, initialise les 4 points cardinaux et �tabli la base du Robot comme �tant la position 0
	 * Et la direction � l'Ouest (Direction de la base ennemie)
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
	 * @param y position en ordonn�es du robot
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
	
	
	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	/**
	 * @author darkf
	 * @param dir La direction sur laquelle doit se fixer le robot (si elle est pr�sente dans les cardinalit�)
	 * @return l'angle de d�placement n�cessaire pour que le regard se tourne dans la direction voulue
	 */
	public int setDir(String dir) {
		if(card.containsKey(dir)) {
			int tmp = this.dir;
			this.dir = card.get(dir);
			return tmp - this.dir;
		}else {
			throw new IllegalArgumentException("La cardinalit� voulu n'�xiste pas");
		}
	}
	
	public void setPosition(int x, int y) {
		pos.setX(x);
		pos.setY(y);
	}
}

