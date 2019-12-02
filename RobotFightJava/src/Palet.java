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
	 * @param id
	 */
	public Palet(int id) {
		this.ramasse = false;
		this.estPresent = true;
		this.id = id;
	}

	/**
	 * @author darkf
	 * @return
	 */
	public boolean isRamasse() {
		return ramasse;
	}

	/**
	 * @author darkf
	 * @param ramasse
	 */
	public void setRamasse(boolean ramasse) {
		this.ramasse = ramasse;
	}

	/**
	 * @author darkf
	 * @return
	 */
	public boolean isEstPresent() {
		return estPresent;
	}

	/**
	 * @author darkf
	 * @param estPresent
	 */
	public void setEstPresent(boolean estPresent) {
		this.estPresent = estPresent;
	}
}
