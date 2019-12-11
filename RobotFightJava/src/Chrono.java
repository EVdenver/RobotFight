/**
 * @author Vincent
 * Un chronometre pour nous permettre de calculer des distances
 */
public class Chrono {
	private long tempsDepart=0;
	private long tempsFin=0;
	private long pauseDepart=0;
	private long pauseFin=0;
	private long duree=0;

	//Getters and Setters
	/**
	 * @author Vincent
	 * @return la duree mesuree en secondes
	 */
	public long getDureeSec()
	{
		return duree/1000;
	}

	/**
	 * @author Vincent
	 * @return la duree mesuree en milli-secondes
	 */
	public long getDureeMs()
	{
		return duree;
	}        

	/**
	 * @author Vincent
	 * @return la duree mesuree en format texte heure:minute:seconde
	 */
	public String getDureeTxt()
	{
		return timeToHMS(getDureeSec());
	}

	//Method
	/**
	 * @author Vincent
	 * Methode lançant le chrono
	 */
	public void start()
	{
		tempsDepart=System.currentTimeMillis();
		tempsFin=0;
		pauseDepart=0;
		pauseFin=0;
		duree=0;
	}

	/**
	 * @author Vincent
	 * Methode mettant en pause le chrono
	 */
	public void pause()
	{
		if(tempsDepart==0) {return;}
		pauseDepart=System.currentTimeMillis();
	}

	/**
	 * @author Vincent
	 * Methode relançant le chrono apres une pause
	 */
	public void resume()
	{
		if(tempsDepart==0) {return;}
		if(pauseDepart==0) {return;}
		pauseFin=System.currentTimeMillis();
		tempsDepart=tempsDepart+pauseFin-pauseDepart;
		tempsFin=0;
		pauseDepart=0;
		pauseFin=0;
		duree=0;
	}

	/**
	 * @author Vincent
	 * Methode arretant le chrono
	 */
	public void stop()
	{
		if(tempsDepart==0) {return;}
		tempsFin=System.currentTimeMillis();
		duree=(tempsFin-tempsDepart) - (pauseFin-pauseDepart);
		tempsDepart=0;
		tempsFin=0;
		pauseDepart=0;
		pauseFin=0;
	}        


	/**
	 * @author Vincent
	 * @param tempsS la duree devant etre convertie
	 * @return la duree convertie en format heure:minute:seconde
	 */
	public static String timeToHMS(long tempsS) {

		// IN : (long) temps en secondes
		// OUT : (String) temps au format texte : "1 h 26 min 3 s"

		int h = (int) (tempsS / 3600);
		int m = (int) ((tempsS % 3600) / 60);
		int s = (int) (tempsS % 60);

		String r="";

		if(h>0) {r+=h+" h ";}
		if(m>0) {r+=m+" min ";}
		if(s>0) {r+=s+" s";}
		if(h<=0 && m<=0 && s<=0) {r="0 s";}

		return r;
	}
}
