
public enum CouleurLocalisation {
	paire1("green","blue",80,280),
	paire2("blue","green",100,260),	
	paire3("red","yellow",190,350),
	paire4("yellow","red",10,170);
	
	private String couleur1,couleur2;
	private int angle1,angle2;
	
	private CouleurLocalisation(String c1,String c2,int a1, int a2) {
		this.couleur1 = c1;
		this.couleur2 = c2;
		this.angle1 = a1;
		this.angle2 = a2;
	}
	
	public String getCouleur1() {
		return this.couleur1;
	}
	
	public String getCouleur2() {
		return this.couleur2;
	}
	
	public int getAngle1() {
		return this.angle1;
	}
	
	public int getAngle2() {
		return this.angle2;
	}
}
