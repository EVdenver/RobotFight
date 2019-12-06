
public enum CouleurLocalisation {
	paire1("green","blue"),
	paire2("green","yellow"),
	paire3("green","red"),
	paire4("blue","yellow"),
	paire5("blue","green"),
	paire6("blue","red"),
	paire7("yellow","red"),
	paire8("yellow","blue"),
	paire9("yellow","green"),
	paire10("red","blue"),
	paire11("red","green"),
	paire12("red","yellow");
	
	private String couleur1,couleur2;
	
	private CouleurLocalisation(String c1,String c2) {
		this.couleur1 = c1;
		this.couleur2 = c2;
	}
	
	public String getCouleur1() {
		return this.couleur1;
	}
	
	public String getCoueleur2() {
		return this.couleur2;
	}
}
