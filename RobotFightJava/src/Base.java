
public class Base {
	private Position p;
	private String dir;
	
	public Base() {
		p = new Position();
		dir = new String();
	}
	
	public Base(Position p,String s) {
		setP(p);
		setDir(s);
	}

	public Position getP() {
		return p;
	}

	public void setP(Position p) {
		this.p = p;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}
}
