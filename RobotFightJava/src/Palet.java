
public class Palet {
	private Position p;
	private boolean ramasse;
	private boolean estPresent;
	
	public Palet() {
		p = new Position();
		setRamasse(false);
		setEstPresent(true);
	}
	
	public Palet(Position p) {
		this();
		setP(p);
	}

	public Position getP() {
		return p;
	}

	public void setP(Position p) {
		this.p = p;
	}

	public boolean isRamasse() {
		return ramasse;
	}

	public void setRamasse(boolean ramasse) {
		this.ramasse = ramasse;
	}

	public boolean isEstPresent() {
		return estPresent;
	}

	public void setEstPresent(boolean estPresent) {
		this.estPresent = estPresent;
	}
	
	public String toString() {
		return "position : "+p.getX()+";"+p.getY()+".";
	}
}
