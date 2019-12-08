import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author darkf
 *
 */
public class Carte {
	private List<Case> cases = new ArrayList<Case>();
	private List<Palet> palets = new ArrayList<Palet>();
	private int baseE,baseA;
	
	/**
	 * @author darkf
	 * Constructeur par defaut. 
	 * Initialise la liste des 9 palets numerote de 1 a 9
	 * Initialise les 18 cases en fonction de leur mur Est Ouest Nord Sud ainsi que leur nom A,E ou de 1 a 16
	 */
	public Carte(int e, int a) {
		baseE = e;
		baseA = a;
		for (int i = 0; i < 9; i++) {
			palets.add(new Palet(i+1));
		}
		Map<String,String> tmp = new HashMap<String,String>();
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
	}
	
	public int getBaseE() {
		return baseE;
	}

	public int getBaseA() {
		return baseA;
	}

	public List<Case> getCases() {
		return cases;
	}

	public List<Palet> getPalets() {
		return palets;
	}

	public Case getCase(String n) {
		for (int i = 0; i < cases.size(); i++) {
			Case tmp = cases.get(i);
			if(tmp.getId().equals(n)) {
				return tmp;
			}
		}
		throw new IllegalArgumentException();
	}
	
	public Case[] getCasesAdj(String n) {
		Case[] res = new Case[4]; 
		for (int i = 0; i < cases.size(); i++) {
			Case tmp = cases.get(i);
			if(tmp.getId().equals(n)) {
				if (!tmp.getColorMur("Nord").equals("")) {
					res[0] = cases.get(i+4);
				}
				if (!tmp.getColorMur("Sud").equals("")) {
					res[1] = cases.get(i-4);
				}
				if(!tmp.getColorMur("Est").equals("")) {
					res[2] = cases.get(i+1);
				}
				if(!tmp.getColorMur("Ouest").equals("")) {
					res[2] = cases.get(i-1);
				}
			}
		}
		return res;
	}
	
	public int getDirE() {
		Case tmp = new Case();
		for (int i = 0; i < cases.size(); i++) {
			if(cases.get(i).getId().equals("E")){
				tmp = cases.get(i);
			}
		}
		if(tmp.getMur("Blanc").equals("Est")) {
			return 180;
		}else {
			return 0;
		}
	}
	
	
	public int getDirDroiteE() {
		if(baseE==0) return 270;
		return 90;
	}
	
	
	public int getDirGaucheE() {
		if(baseE==0) return 90;
		return 270;
	}
	
	public int getDirA() {
		Case tmp = new Case();
		for (int i = 0; i < cases.size(); i++) {
			if(cases.get(i).getId().equals("A")){
				tmp = cases.get(i);
			}
		}
		if(tmp.getMur("Blanc").equals("Est")) {
			return 180;
		}else {
			return 0;
		}
	}
	
	public void enleverPalet(int id) {
		
	}
	
}
