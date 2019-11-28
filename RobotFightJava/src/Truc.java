
public class Truc {
	static String e = "Est";
	static String a = "Ouest";
	static Carte c = new Carte(a,e);
	public static void main(String[] args) {
		for (int i = 0; i < c.getTabP().length; i++) {
			System.out.println(c.getTabP(i));
		}
	}
}
