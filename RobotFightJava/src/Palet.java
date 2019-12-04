/**
 * 
 * @author darkf
 *
 */
public class Palet {
	private int id;
	private boolean ramasse;
	private boolean estPresent;
	
	/**
	 * @author darkf
	 * @param id Le numéro du palet
	 */
	public Palet(int id) {
		this.ramasse = false;
		this.estPresent = true;
		this.id = id;
	}

	/**
	 * @author darkf
	 * @return true si le palet est ramasser false sinon
	 */
	public boolean isRamasse() {
		return ramasse;
	}

	/**
	 * @author darkf
	 * @param ramasse indique le nouvel etat du palet
	 */
	public void setRamasse(boolean ramasse) {
		this.ramasse = ramasse;
	}

	/**
	 * @author darkf
	 * @return true si le palet est à l'endroit attendu false sinon
	 */
	public boolean isEstPresent() {
		return estPresent;
	}

	/**
	 * @author darkf
	 * @param estPresent etabli la nouvelle position du palet
	 */
	public void setEstPresent(boolean estPresent) {
		this.estPresent = estPresent;
	}
	
	public String toString() {
		return "position : "+p.getX()+";"+p.getY()+".";
	}
}
