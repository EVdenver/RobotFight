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
	
	public Case() {
		id = new String();
	}
	
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
	
	public Map<String, String> getMurs() {
		return murs;
	}

	public String getId() {
		return id;
	}

	/**
	 * @author darkf
	 * @param mur
	 * @return retourne la couleur du mur passé en parametre
	 */
	public String getColorMur(String mur) {
		return (String) this.murs.get(mur);
	}
	
	public String getMur(String color) {
		if (murs.get("Est").equals(color)) {
			return "Est";
		}else if(murs.get("Ouest").equals(color)) {
			return "Ouest";
		}else if(murs.get("Nord").equals(color)) {
			return "Nord";
		}else {
			return "Sud";
		}
	}
}
