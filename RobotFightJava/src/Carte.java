import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author darkf
 *
 */
public class Carte {
	private List<Case> cases = new LinkedList<Case>();
	private List<Palet> palets = new ArrayList<Palet>();
	
	/**
	 * @author darkf
	 * Constructeur par defaut. 
	 * Initialise la liste des 9 palets numerote de 1 a 9
	 * Initialise les 18 cases en fonction de leur mur Est Ouest Nord Sud ainsi que leur nom A,E ou de 1 a 16
	 */
	public Carte() {
		for (int i = 0; i < 9; i++) {
			palets.add(new Palet(i+1));
		}
		Map<String,String> tmp = new HashMap<String,String>();
		tmp.put("Nord", "");
		tmp.put("Sud", "");
		tmp.put("Est", "Blanc");
		tmp.put("Ouest", "");
		cases.add(new Case("A",tmp));
		for (int i = 0; i < 4; i++) {
			if(i == 0) {
				tmp.put("Sud", "");
				tmp.put("Nord", "Jaune");
			}else if(i == 1) {
				tmp.put("Sud", "Jaune");
				tmp.put("Nord", "Noir");
			}else if(i == 2) {
				tmp.put("Sud", "Noir");
				tmp.put("Nord", "Rouge");
			}else if(i == 3) {
				tmp.put("Sud", "Rouge");
				tmp.put("Nord", "");
			}
			for (int j = 0; j < 4; j++) {
				if(j == 0) {
					tmp.put("Est", "Vert");
					tmp.put("Ouest", "Blanc");
				}else if(j == 1) {
					tmp.put("Est", "Noir");
					tmp.put("Ouest", "Vert");
				}else if(j == 2) {
					tmp.put("Est", "Bleu");
					tmp.put("Ouest", "Noir");
				}else if(j == 3) {
					tmp.put("Est", "Blanc");
					tmp.put("Ouest", "Bleu");
				}
				cases.add(new Case(String.valueOf((4*i+j)+1),tmp));
			}
		}
		tmp.put("Sud", "");
		tmp.put("Est", "");
		tmp.put("Ouest", "Blanc");
		cases.add(new Case("E", tmp));
	}
	
	public void enleverPalet(int id) {
		
	}
	
}
