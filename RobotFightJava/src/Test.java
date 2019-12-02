import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Properties;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import java.util.ArrayList;


public class Test {

	static double distanceMur=1;
	static Actionneur a = new Actionneur(MotorPort.C, MotorPort.A, MotorPort.B) ;
	static EchoSensor es= new EchoSensor (SensorPort.S3);
	static TouchSensor ts = new TouchSensor(SensorPort.S2);
	static Boussole b = new Boussole(180,0,0);
	static Carte c = new Carte();
	static double distanceMaintenant = 0;
	static double distanceAvant = 0; 
	static double distanceAParcourir = 0; 
	final static double seuilDetectionPalet = 0.38;
	final static double seuilArretMur = 0.2;
	final static double largeurMax=2;
	final static double longeurMax=1.7;

	static int etat=0;
	final static int chercheEnRond=0;
	final static int dosAuMur=1;
	final static int detectionPalet=2;
	final static int aucunPaletEnVu=3;
	final static int faceAuPalet=4;
	final static int paletAttraper=5;
	final static int recalibrageAFaire=6;
	final static int STOP=7;
	private static final String ArrayList = null;
	static boolean trouver=false;
  
	public static void main(String[] args) throws IOException {
	  InputStream in= new FileInputStream("couleur");
		Properties sauveur= new Properties();
		sauveur.load(in);
		boolean again= true;
		while(again) {
		float[] tab= TestColor.getEch();
		System.out.println(TestColor.LaCouleur(tab, sauveur));
		Delay.msDelay(2000);
		if (Button.ENTER.isDown()){
			again=false;
		}
		}
		/*float[] blue = new float[5];
		float[] red= new float[5];
		blue[0]=0;
		blue[1]=1;
		blue[2]=2;
		red[0]=3;
		red[1]=4;
		red[2]=5;
		sauveur.setProperty("Blue", blue[0]+ ","+ blue[1]+","+ blue[2]);
		sauveur.setProperty("Red", red[0]+ ","+ red[1]+","+ red[2]);
		sauveur.store(out, "comments");
		sauveur.load(new FileInputStream("couleur"));
		System.out.println(sauveur.getProperty("Red"));
		float r0=Float.parseFloat((sauveur.getProperty("Red")).substring(0,3));
		float r1=Float.parseFloat((sauveur.getProperty("Red")).substring(4,7));
		float r2=Float.parseFloat((sauveur.getProperty("Red")).substring(8,11));
		System.out.println("r0= "+r0);
		System.out.println("r1= "+r1);
		System.out.println("r2= "+r2);
		//TestColor col= new TestColor();*/
    while(!ts.isPressed()) {
			System.out.println("etat"+etat);
			System.out.println(es.getDistance());
			Delay.msDelay(3000);
			rechercheSimple();

			Delay.msDelay(3000);
			if (etat==STOP) break;
		}

		/*

		while(!trouver) {
			rechercheSimple();
			distanceAvant=0;
			distanceMaintenant=0;
		}*/
		//distance entre 0 et 1
		//	System.out.println("distance objet : "+es.getDistance());
		/*	0.321
		while(!ts.isPressed()){
			a.forward(0.4);
			a.stop();
			if (trouver>0.3) trouver=es.getDistance(); //distance entre 0 et 1
			System.out.println("distance objet : "+trouver);
			Delay.msDelay(1000);
			if (trouver>0.3 && !(trouver>1)) {
				rectifiePositionaHorraire(a,es,trouver);
				rectifiePositionaAntiHorraire(a, es, trouver);

			}
			else a.closePince();
			}
		a.rotate(-40);
		a.forward(2);
		boolean tourner=true;
		for (int i=0;i<3;i++) {
				a.rotate(10);
				System.out.println("rotate");
				a.forward(0.2);
				System.out.println("avance");
				a.stop();
				System.out.println("stop");
				Delay.msDelay(10000);
			}*/
  }
	
	private static Reader FileInputStream(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	//public String brunble (double sammple, double base) {
		
	//}
	//System.out.println(sauveur.getProperty("Blue"));
		
	//public String brunble (double sammple, double base){}

	/**
	 * @author Nicolas
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double scalaire(float[] v1, float[] v2) {
		return Math.sqrt (Math.pow(v1[0] - v2[0], 2.0) +
				Math.pow(v1[1] - v2[1], 2.0) +
				Math.pow(v1[2] - v2[2], 2.0));
	}
	/**
	 * si renvoit un nombre positif, alors la distance entre le robot est l'obstacle s'est réduite
	 * @return
	 */
	public static double differentielDistance () { // est-ce non
		distanceMaintenant=es.getDistance();
		distanceAvant=distanceMaintenant;
		distanceMaintenant=es.getDistance();
		return distanceAvant-distanceMaintenant;
	}

	public static double calculDistanceMur() {
		double x = b.getPos().getX();
		double y = b.getPos().getY();
		double alpha = b.getDir();
		double dist=longeurMax;
		if (alpha>0 && alpha<=90) dist=calculHypothenus(longeurMax-x, largeurMax, alpha);
		if (alpha>90 && alpha<=180) dist=calculHypothenus(largeurMax-y, longeurMax, alpha-90);
		if (alpha>180 && alpha<=270) dist=calculHypothenus(x, largeurMax, alpha-180);
		if (alpha>270) dist=calculHypothenus(y, longeurMax, alpha-270);
		return dist;
	}

	private static double calculHypothenus(double distance1, double distance2, double alpha) {
		double dist=(distance1)/Math.cos(alpha);
		double sortieDeMur=dist/Math.sin(alpha);
		if (sortieDeMur>distance2) {
			sortieDeMur-=distance2;
			dist-=sortieDeMur/Math.sin(alpha);
		}
		return dist;
	}

	public static double rechercheTournante () {
		a.closePince();
		double angleMax=360;
		ArrayList<Double> tabList= new ArrayList<Double>();
		double trouver;
	//	double trouver=es.getDistance(); //distance entre 0 et 1
		double angleTotal=0;
	//	if (trouver==0) trouver=100;
	//	System.out.println("distance objet : "+trouver);
	//	tabList.add(trouver);
	//	distanceMur=calculDistanceMur(); quand la boussole sera au point
		a.rotate(360);
		while (a.isMoving()){
		//	a.stop();
			trouver=es.getDistance();
			//if (trouver==0) trouver=100;
			tabList.add(trouver);
			//a.rotate(15);
			//b.setDir(15);
			//angleTotal+=15;
	//		
		//	System.out.println("angle "+b.getDir()+"°");
		//	Delay.msDelay(1_000);
		//	System.out.println("distance objet : "+trouver);
		}
		
		System.out.println(tabList.size()+" distances mesurées"); // 25
		
	//	Delay.msDelay(10_000);
		
		trouver=distanceMin(tabList);
		System.out.println("distances min a indice "+tabList.indexOf(trouver)); 
		int i=tabList.indexOf(trouver); // nbr de retour en arrière pour arriver à la plus petite distance
		tourner(360/tabList.size()*i);
		
		
//		Delay.msDelay(10_000);
//		angleMax/tabList.size()*indexMin;
		
		System.out.println("distance "+trouver);
		return trouver;
	}
	
	private static double distanceMin (ArrayList<Double> list) {
		double res=Double.MAX_VALUE;
		for (Double d : list) {
			if (d>seuilDetectionPalet) res=d<res?d:res;
		}
		return res;
	}
	
	private static void tourner (int angle) {
		int dir=angle>0?1:-1;
		if (dir==-1)angle*=-1;
		System.out.println("direction "+dir);
		System.out.println("angle "+angle);
		if (angle<=15) a.rotate(dir*angle);
		else {
			int i=0;
			for (;i<angle;i+=15) {
				a.rotate(dir*15);
			//	Delay.msDelay(10);
	//			System.out.println("angle "+i);
			}
			System.out.println("rotation termine, reste "+(angle-i));
			a.rotate(dir*(angle-i));
		}
		b.setDir(dir*angle);
//		
	//	Delay.msDelay(1_000);

	}
	

	
	public static boolean rectifiePosition (int i) {
		distanceMaintenant = es.getDistance();
		tourner(15*i);
		Delay.msDelay(100); // mesure du temps pour bouger de 15 degrés
		distanceAvant=distanceMaintenant;
		distanceMaintenant = es.getDistance();
		if (distanceMaintenant<distanceAvant) return true;
		else tourner(-15*i);
		return false;
	}

	static public boolean isMur() {
		if (distanceMaintenant<=seuilArretMur) {
			System.out.println("mur detecte");
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

	static public boolean fonceUntilPush() {
		a.forward();
		while (!ts.isPressed() ) {
			distanceMaintenant=es.getDistance();
			if (isMur()) return false;
		}
		a.stop();
		a.closePince();
		Delay.msDelay(3_000);
		return true;

	}

	static public boolean avanceVersPalet() {
		a.openPince();
		distanceAvant = es.getDistance();
		a.forward();
		Delay.msDelay(100);
		distanceMaintenant = es.getDistance();
		while(distanceAvant > distanceMaintenant ) {
			distanceAvant=distanceMaintenant;
			Delay.msDelay(100);
			distanceMaintenant = es.getDistance();
			if (isMur()) return false;
		}
		a.stop();
		return true;
	}

	static public void mettreUnBut() {
		// la base ennemie est en carte, soit 0 soit 180
	
		tourner(-b.getDir());
		a.forward();
		Delay.msDelay(5_000);
		// se diriger vers les buts à l'aide de la boussole et de la ligne blanche

		etat=STOP;

	}

	public static void rechercheSimple() {

		switch(etat) {
		case (chercheEnRond) : 
		distanceAParcourir=rechercheTournante();
		etat=detectionPalet;
		//	if (rechercheTournante()<distanceMur) etat=detectionPalet;
		//	else etat=aucunPaletEnVu;

		break;
		case (detectionPalet):
			if (!avanceVersPalet()) etat=dosAuMur;
			else if (distanceAvant<=seuilDetectionPalet) etat=faceAuPalet;
			else etat=recalibrageAFaire;
		System.out.println("distance"+distanceAvant);
		break;
		case (faceAuPalet):
			if(fonceUntilPush()) etat=paletAttraper ;
			else etat=dosAuMur;
		break;
		case(aucunPaletEnVu) : etat=chercheEnRond; // Ã  la fin de cherche en rond
		break;
		case(dosAuMur) : etat=chercheEnRond;
		break;
		case(paletAttraper): mettreUnBut(); // a instancier
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