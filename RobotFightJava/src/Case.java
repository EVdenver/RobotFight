import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Vincent
 *
 */
public class Case {
	private Map<String,String> murs = new HashMap<String,String>();
	private String id;
	
	/**
	 * @author darkf
	 * @param href le nom de la case
	 * @param murs les couleurs associes a chaque mur
	 * Constructeur
	 */
	public Case(String href,Map<String,String> murs) {
		// TODO Auto-generated constructor stub
		id = href;
		this.murs=murs;
	}
	
	/**
	 * @author darkf
	 * @param mur
	 * @return retourne la couleur du mur passé en parametre
	 */
	public String getColorMur(String mur) {
		return (String) this.murs.get(mur);
	}
}
