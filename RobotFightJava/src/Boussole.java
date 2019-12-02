import java.util.HashMap;
import java.util.Map;

public class Boussole {
	
	private Map<String,Integer> card = new HashMap<>();
	private int dir;
	private String pos;
	//Constructor
	/**
	 * @author darkf
	 * Constructeur par défaut, initialise les 4 points cardinaux et établi la base du Robot comme étant la position 0
	 * Et la direction à l'Ouest (Direction de la base ennemie)
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
	 */
	public Boussole(int angle) {
		super();
		dir = angle;
	}
	
	//Methods
	/**
	 * @author darkf
	 * @return
	 */
	public int getDir() {
		return this.dir;
	}
	
	/**
	 * @author darkf
	 * @param newPos
	 */
	public void setPos(String newPos) {
		this.pos = newPos;
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
}

