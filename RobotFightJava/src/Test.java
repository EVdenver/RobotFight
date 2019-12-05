import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Properties;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import java.util.ArrayList;


public class Test  {

	static double distanceMur=1;
	static Actionneur a = new Actionneur(MotorPort.C, MotorPort.A, MotorPort.B) ;
	static EchoSensor es= new EchoSensor (SensorPort.S3);
	static TouchSensor ts = new TouchSensor(SensorPort.S2);

	
	static ColorimetrieSensor cs;
	
	static Boussole b = new Boussole(0);

	static Carte c = new Carte(0,180);

	static double distanceMaintenant = 0;
	static double distanceAvant = 0; 
	static double distanceAParcourir = 0; 
	final static double seuilDetectionPalet = 0.38;
	final static double marge = 0.05;
	final static double seuilArretMur = 0.2;
	final static double largeurMax=2;
	final static double longeurMax=1.7;

	static int etat=8;
	final static int chercheEnRond=0;
	final static int dosAuMur=1;
	final static int detectionPalet=2;
	final static int aucunPaletEnVu=3;
	final static int faceAuPalet=4;
	final static int paletAttraper=5;
	final static int recalibrageAFaire=6;
	final static int STOP=7;
	final static int firstPalet=8;
	private static final String ArrayList = null;
	static boolean trouver=false;
	
	static String couleur;
	static Properties sauveur;
	
	public static void main(String[] args) throws IOException {
		//	cs = new ColorimetrieSensor(LocalEV3.get().getPort("S1")); 
		//	cs.calibration();

		cs = new ColorimetrieSensor(SensorPort.S1);
		//couleur=cs.laCouleur();

		while(!Button.ESCAPE.isDown()) {
			System.out.println("Etat "+etat);			
			recherchePrincipale();
			if (etat==STOP) break;
		}
		/*a.setSpeed(500);
		a.forward(2);*/
	}
	

	/**
	 * si renvoit un nombre positif, alors la distance entre le robot est l'obstacle s'est rÃ©duite
	 * @author charlotte
	 * @return
	 */
	public static double differentielDistance () { // est-ce non
		distanceMaintenant=es.getDistance();
		distanceAvant=distanceMaintenant;
		distanceMaintenant=es.getDistance();
		return distanceAvant-distanceMaintenant;
	}

	/**
	 * @author charlotte
	 * @return
	 */

/*	public static double calculDistanceMur() {
		double x = b.getPos().getX();
		double y = b.getPos().getY();
		double alpha = b.getDir();
		double dist=longeurMax;
		if (alpha>0 && alpha<=90) dist=calculHypothenus(longeurMax-x, largeurMax, alpha);
		if (alpha>90 && alpha<=180) dist=calculHypothenus(largeurMax-y, longeurMax, alpha-90);
		if (alpha>180 && alpha<=270) dist=calculHypothenus(x, largeurMax, alpha-180);
		if (alpha>270) dist=calculHypothenus(y, longeurMax, alpha-270);
		return dist;
	} */

	/**
	 * @author charlotte
	 * @param distance1
	 * @param distance2
	 * @param alpha
	 * @return
	 */
	private static double calculHypothenus(double distance1, double distance2, double alpha) {
		double dist=(distance1)/Math.cos(alpha);
		double sortieDeMur=dist/Math.sin(alpha);
		if (sortieDeMur>distance2) {
			sortieDeMur-=distance2;
			dist-=sortieDeMur/Math.sin(alpha);
		}
		return dist;
	}

	/**
	 * @author charlotte
	 * @return
	 */
	public static double rechercheTournante () {
		a.closePince();
		int angleMax=360;
		ArrayList<Double> tabList= new ArrayList<Double>();
		double trouver;
		tourner(angleMax);
		while (a.isMoving()){
			trouver=es.getDistance();
			if (trouver==0) trouver=100;
			tabList.add(trouver);
		}
		System.out.println(tabList.size()+" distances mesurees"); // 300
		trouver=distanceMin(tabList);
		int i=tabList.indexOf(trouver);
		System.out.println("distances min "+trouver+"a indice "+i); 
		tourner(360/tabList.size()*i+10);
		System.out.println("je me suis recaler de"+360/tabList.size()*i+" degrees"); 		
		System.out.println("distance "+trouver);
		return trouver;
	}
	
	private static double distanceMin (ArrayList<Double> list) {
		double res=Double.MAX_VALUE;
		for (Double d : list) {
			if (d>seuilDetectionPalet-marge) res=d<res?d:res;
		}
		return res;
	}
	
	/**
	 * @author charlotte
	 * @param angle
	 */
	private static void tourner (int angle) {
		a.setSpeed(300);
		int dir=angle>0?1:-1;
		if (dir==-1)angle*=-1;
	//	System.out.println("direction "+dir);
	//	System.out.println("angle "+angle);
		if (angle<=15) a.rotate(dir*angle);
		else {
			int i=0;
			for (;i<angle;i+=15) {
				a.rotate(dir*15);
			}
			a.rotate(dir*(angle-i));
		}
		b.setDir(dir*angle);
		a.setSpeed(500);
	//	Delay.msDelay(1_000);

	}
	

	/**
	 * @author charlotte
	 * @param i
	 * @return
	 */
	public static boolean rectifiePosition (int i) {
		distanceMaintenant = es.getDistance();
		tourner(15*i);
		Delay.msDelay(100); // mesure du temps pour bouger de 15 degres
		distanceAvant=distanceMaintenant;
		distanceMaintenant = es.getDistance();
		if (distanceMaintenant<distanceAvant) return true;
		else tourner(-15*i);
		return false;
	}

	/**
	 * @author charlotte
	 * @return
	 */
	static public boolean isMur() {
		distanceMaintenant=es.getDistance();
		if (distanceMaintenant<=seuilArretMur) {
			System.out.println("mur detecte, distance "+distanceMaintenant);
			a.stop();
			a.backward(0.2);
			int i=1;
		//	if (isButOuest() && faceMurNord()) i*=-1;
		//	if (isButEst() && faceMurSud()) i*=-1;
			tourner(i*180);
      //a.rotate(i*180);
			return true;
		}
		return false;
	}
/**
 * @author charlotte
 * @return
 * @throws FileNotFoundException
 * @throws IOException
 */
	static public boolean fonceUntilPush() throws FileNotFoundException, IOException {
		a.forward();
		while (!ts.isPressed() ) {
			//TODO Implementation des couleurs et du changement de case ici
			/**
			 * @author charlotte 
			 * j'ai rajouter cette ligne ; elle te renvoit la couleur en string
			 */
			couleur=cs.laCouleur();
			Case[] caseAdj = c.getCasesAdj(b.getPos());
			distanceMaintenant=es.getDistance();
			if (isMur()) return false;
		}
		a.stop();
		a.closePince();
		return true;
	}
	
	/*static public void changerPos(String couleur,Case[] caseAdj) {
		if (b.getDir() <= 90 && b.getDir() >= 0) {
			b.setPos(caseAdj[2].);
		}
	}*/
/**
 * @author charlotte
 * @return
 * @throws FileNotFoundException
 * @throws IOException
 */
	static public boolean avanceVersPalet() throws FileNotFoundException, IOException {
		a.openPince();
		distanceAvant = es.getDistance();
		a.forward();
		Delay.msDelay(1000);
		distanceMaintenant = es.getDistance();
		while(distanceAvant > distanceMaintenant ) {
			distanceAvant=distanceMaintenant;
			Delay.msDelay(1000);
			distanceMaintenant = es.getDistance();
			/**
			 * @author charlotte 
			 * VINCENT ICI AUSSI LES COULEURS CHANGENT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			 */
			couleur=cs.laCouleur(); 

			if (isMur()) return false;
		}
		a.stop();
		return true;
	}

	/**
	 * @author charlotte
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	static public void mettreUnBut() throws FileNotFoundException, IOException {
		// la base ennemie est en carte, soit 0 soit 180
		// VINCENT
		
		tourner(b.setDir(c.getBaseE())); // pour le moment
		System.out.println(b.setDir(c.getBaseE()));
		Delay.msDelay(2000);

		while (!couleur.equals("white")) {
			a.forward();
			couleur=cs.laCouleur();
		}

		a.forward(0.1);
		a.openPince();
		a.backward(0.8);

		tourner(180);
	}
	
	public static void debutAutomate () throws FileNotFoundException, IOException {
		a.openPince();
		fonceUntilPush();
		mettreUnBut();
	}

	public static void recherchePrincipale() throws FileNotFoundException, IOException {

		switch(etat) {
		case (firstPalet):
			debutAutomate();
			etat=chercheEnRond;
			break;
		case (chercheEnRond) : 
		distanceAParcourir=rechercheTournante();
		etat=detectionPalet;
		//	if (rechercheTournante()<distanceMur) etat=detectionPalet;
		//	else etat=aucunPaletEnVu;

		break;
		case (detectionPalet):
			if (!avanceVersPalet()) etat=dosAuMur;
			else if (distanceAvant<=seuilDetectionPalet+marge) etat=faceAuPalet;
			else etat=recalibrageAFaire;
		System.out.println("distance"+distanceAvant);
		break;
		case (faceAuPalet):
			if(fonceUntilPush()) etat=paletAttraper ;
			else etat=dosAuMur;
		break;
		case(aucunPaletEnVu) : etat=chercheEnRond; // ÃÂ  la fin de cherche en rond
		break;
		case(dosAuMur) : etat=chercheEnRond;
		break;
		case(paletAttraper): mettreUnBut(); 
			etat=chercheEnRond;
		break;
		case(recalibrageAFaire) :
			System.out.println("recalibrage");
		if (rectifiePosition(1)) etat=faceAuPalet;
		else if (rectifiePosition(-1)) etat=faceAuPalet;
		else etat=aucunPaletEnVu;
		if(isMur()) etat=dosAuMur;
		break;
		}

	}
	
	
}