import java.util.HashMap;
import java.util.Map;

public class Boussole {
	
	Map<String,Integer> card = new HashMap<>();
	int dir;
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
		card.put("Base", 0);
		dir = card.get("Ouest");
		pos = new Position();
	}
	
	/**
	 * @author darkf
	 * @see Boussole.Boussole()
	 * @param angle angle de base vers lequel le robot porte son regard lors de l'initialisation de la boussole
	 */
	public Boussole(int angle, int x, int y) {
		this();
		dir = angle;
		pos.setX(x);
		pos.setY(y);
	}
	
	//Methods
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
}

