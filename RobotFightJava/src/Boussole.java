import java.util.HashMap;
import java.util.Map;

public class Boussole {
	
	Map<String,Integer> card = new HashMap<>();
	int dir;
	
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
		int tmp = card.get("Base");
		card.put("BaseE",(tmp + 180)%360);
		dir = card.get("Ouest");
	}
	
	/**
	 * @author darkf
	 * @see Boussole.Boussole()
	 * @param angle angle de base vers lequel le robot porte son regard lors de l'initialisation de la boussole
	 * @param base La position de la base alli�, soit Est soit Ouest
	 */
	public Boussole(int angle, String base) {
		super();
		card.put("Base", card.get(base));
		int tmp = card.get("Base");
		card.put("BaseE",(tmp + 180)%360);
		dir = angle;
	}
	
	//Methods
	/**
	 * @author darkf
	 * @param angle L'angle que le regard du robot doit effectuer
	 */
	public void setPos(int angle) {
		this.dir = (this.dir + angle)%360;
	}
	
	
	/**
	 * @author darkf
	 * @param pos La position sur laquelle doit se fixer le robot (si elle est pr�sente dans les cardinalit�)
	 * @return l'angle de d�placement n�cessaire pour que le regard se tourne dans la direction voulue
	 */
	public int setPos(String pos) {
		if(card.containsKey(pos)) {
			int tmp = dir;
			dir = card.get(pos);
			return tmp - dir;
		}else {
			throw new IllegalArgumentException("La cardinalit� voulu n'�xiste pas");
		}
	}
}

