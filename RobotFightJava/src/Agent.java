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


public class Agent  {
	// Parametres de debugage et de lancement reel
	final static boolean DEBUG=true;
	
	//Constantes representant les etats de l automate
	final static int chercheEnRond=0;
	final static int dosAuMur=1;
	final static int detectionPalet=2;
	final static int aucunPaletEnVu=3;
	final static int faceAuPalet=4;
	final static int paletAttraper=5;
	final static int recalibrageAFaire=6;
	final static int STOP=7;
	final static int firstPalet=8;
	
	//Constantes de l'algorithme
	final static double seuilDetectionPalet = 0.38;
	final static double marge = 0.05;
	final static double seuilArretMur = 0.2;
	final static double largeurMax=2;
	final static double longeurMax=1.7;
	
	//Parametres d'instance de l agent
	Actionneur a  ;
	EchoSensor es ;
	TouchSensor ts ;
	ColorimetrieSensor cs;
	Boussole b ;
	Carte c ;
	double distanceMaintenant = 0;
	double distanceAvant = 0; 
	double distanceAParcourir = 0; 
	int etat;
	String couleur;
	
	/**
	 * initialise un agent
	 * @param Actionneur
	 * @param EchoSensor
	 * @param TouchSensor
	 * @param ColorimetrieSensor
	 * @param Boussole
	 * @param Carte
	 * @author charlotte
	 */
	public Agent(Actionneur a, EchoSensor es, TouchSensor ts, ColorimetrieSensor cs, Boussole b, Carte c) {
		super();
		this.a = a;
		this.es = es;
		this.ts = ts;
		this.cs = cs;
		this.b = b;
		this.c = c;
		distanceMaintenant = 0;
		distanceAvant = 0; 
		distanceAParcourir = 0; 
		etat=8;
		String couleur="";
	}


	public static void main(String[] args) throws IOException {
		
		//	cs.calibration();

		Agent robot=new Agent(new Actionneur(MotorPort.C, MotorPort.A, MotorPort.B),new EchoSensor (SensorPort.S3),new TouchSensor(SensorPort.S2), new ColorimetrieSensor(SensorPort.S1),new Boussole(0),new Carte(0,180));
	
	
		
			while(!Button.ESCAPE.isDown()) {
			System.out.println("Etat "+robot.etat);
			if (robot.ts.isPressed()) {
				robot.etat=paletAttraper;
				robot.a.closePince();
			}
			robot.recherchePrincipale();
			if (robot.etat==STOP) break;
		}
		
		
		
		
	} 

	/**
	 * Automate
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @author charlotte
	 */
	public void recherchePrincipale() throws FileNotFoundException, IOException {
		switch(etat) {
		case (firstPalet):
			debutAutomate();
		if (DEBUG) {
			System.out.println("boussolle "+b.getDir());
			System.out.println("getCheminParcouru "+a.getCheminParcouru());
			Button.ENTER.waitForPressAndRelease() ;
		}
		etat=chercheEnRond;
		break;
		case (chercheEnRond) : 
			distanceAParcourir=rechercheTournante();
		if (DEBUG) {
			System.out.println("boussolle "+b.getDir());
			System.out.println("getCheminParcouru "+a.getCheminParcouru());
			Button.ENTER.waitForPressAndRelease() ;
		}
		etat=detectionPalet;
		break;
		case (detectionPalet):
			if (!avanceVersPalet()) etat=dosAuMur;
			else if (distanceAvant<=seuilDetectionPalet+marge) etat=faceAuPalet;
			else etat=recalibrageAFaire;
		System.out.println("distance"+distanceAvant);
		if (DEBUG) {
			System.out.println("boussolle "+b.getDir());
			System.out.println("getCheminParcouru "+a.getCheminParcouru());
			Button.ENTER.waitForPressAndRelease() ;
		}
		break;
		case (faceAuPalet):
			if(fonceUntilPush()) etat=paletAttraper ;
			else etat=dosAuMur;
		if (DEBUG) {
			System.out.println("boussolle "+b.getDir());
			System.out.println("getCheminParcouru "+a.getCheminParcouru());
			Button.ENTER.waitForPressAndRelease() ;
		}
		break;
		case(aucunPaletEnVu) : etat=chercheEnRond; // ÃÂ  la fin de cherche en rond
		if (DEBUG) {
			System.out.println("boussolle "+b.getDir());
			System.out.println("getCheminParcouru "+a.getCheminParcouru());
			Button.ENTER.waitForPressAndRelease() ;
		}
		break;
		case(dosAuMur) : 
			a.forward(0.5);
		etat=chercheEnRond;
		if (DEBUG) {
			System.out.println("boussolle "+b.getDir());
			System.out.println("getCheminParcouru "+a.getCheminParcouru());
			Button.ENTER.waitForPressAndRelease() ;
		}
		break;
		case(paletAttraper): 
			if (mettreUnBut()) etat=chercheEnRond;
			else etat=dosAuMur;
		if (DEBUG) {
			System.out.println("boussolle "+b.getDir());
			System.out.println("getCheminParcouru "+a.getCheminParcouru());
			Button.ENTER.waitForPressAndRelease() ;
		}
		break;
		case(recalibrageAFaire) :
			System.out.println("recalibrage");
		if (rectifiePosition(1)) etat=faceAuPalet;
		else if (rectifiePosition(-1)) etat=faceAuPalet;
		else etat=aucunPaletEnVu;
		if(isMur() || isLigneBlanche()) etat=dosAuMur;
		if (DEBUG) {
			System.out.println("boussolle "+b.getDir());
			System.out.println("getCheminParcouru "+a.getCheminParcouru());
			Button.ENTER.waitForPressAndRelease() ;
		}
		break;
		}
	}

	/**
	 * Action a effectue dans l etat de depart de l automate
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @author charlotte
	 */
	public  void debutAutomate () throws FileNotFoundException, IOException {
		System.out.println("debutAutomate");
		a.openPince();
		fonceUntilPush();
		tourner(30);
		a.forward(1);
		mettreUnBut();
	}

	/** tourne sur soi meme et se place vers l objet le plus proche
	 * @author charlotte
	 * @return
	 */
	public double rechercheTournante () {
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
		tourner(360/tabList.size()*i); 
		//	a ce niveau recalibrer
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
	 * avance jusqu a ce que la distance augmente sans se prendre de mur
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @author charlotte
	 */
	 public boolean avanceVersPalet() throws FileNotFoundException, IOException {
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
			lectureCouleur();

			if (isMur() || isLigneBlanche()) return false;
		}
		a.stop();
		return true;
	}
	 
	 /**
		 * @author charlotte
		 * @throws IOException 
		 * @throws FileNotFoundException 
		 */
		 public boolean mettreUnBut() throws FileNotFoundException, IOException {
			// la base ennemie est en carte, soit 0 soit 180
			// VINCENT
			System.out.println("Angle:" +getDiff(c.getBaseE()));
			tourner(getDiff(c.getBaseE())); 
			while (!couleur.equals("white")) {
				a.forward();
				lectureCouleur();
				if (isMur()) return false;
			}

			a.forward(0.1);
			a.openPince();
			a.backward(0.8);

			tourner(180);
			return true;
		}
	
	 /** va tout droit jusqua activer le poussoir
		 * @author charlotte
		 * @return
		 * @throws FileNotFoundException
		 * @throws IOException
		 */
		 public boolean fonceUntilPush() throws FileNotFoundException, IOException {
			a.forward();
			while (!ts.isPressed() ) {
				//TODO Implementation des couleurs et du changement de case ici
				/**
				 * @author charlotte 
				 * j'ai rajouter cette ligne ; elle te renvoit la couleur en string
				 */
				lectureCouleur();
				Case[] caseAdj = c.getCasesAdj(b.getPos());
				distanceMaintenant=es.getDistance();
				if (isMur() || isLigneBlanche()) return false;
			}
			a.stop();
			a.closePince();
			return true;
		}
	
		 /** se decalge de 15 degree dans une direction donne (droite ou gauche
			 * @author charlotte
			 * @param i
			 * @return
			 */
			public boolean rectifiePosition (int i) {
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
	 * @author darkf
	 * @param angle l'angle sur lequel le robot doit se fixer
	 * @return l'angle de deplacement necessaire pour que le regard se tourne dans la direction voulue
	 */
	public int getDiff(int angle) {
		return angle - b.getDir();
	}
	/**
	 * @author charlotte
	 * @param angle
	 */
	private void tourner (int angle) {
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
	 * @return
	 */
	 public boolean isMur() {
		distanceMaintenant=es.getDistance();
		if (distanceMaintenant<=seuilArretMur && distanceMaintenant!=0) {
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
	 
	 public void lectureCouleur() throws FileNotFoundException, IOException {
		 couleur=cs.laCouleur();
		 if (!couleur.equals("grey")) {
		//	 b.nouvelleMethode(couleur);
		 }
	 }
	

	 public boolean isLigneBlanche() throws FileNotFoundException, IOException {
		 lectureCouleur();
		if (couleur.equals("white")) {
			System.out.println("ligne blanche, distance "+distanceMaintenant);
			a.stop();
			a.backward(0.2);
			int i=1;
			//	if (isButOuest() && faceMurNord()) i*=-1;
			//	if (isButEst() && faceMurSud()) i*=-1;
			tourner(i*180);
			return true;
		}
		return false;
	}
	
	

	/*static public void changerPos(String couleur,Case[] caseAdj) {
		if (b.getDir() <= 90 && b.getDir() >= 0) {
			b.setPos(caseAdj[2].);
		}
	}*/
	

	

	 
	 
	 
		
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
		}/**
		 * @author charlotte
		 * @param distance1
		 * @param distance2
		 * @param alpha
		 * @return
		 
		private static double calculHypothenus(double distance1, double distance2, double alpha) {
			double dist=(distance1)/Math.cos(alpha);
			double sortieDeMur=dist/Math.sin(alpha);
			if (sortieDeMur>distance2) {
				sortieDeMur-=distance2;
				dist-=sortieDeMur/Math.sin(alpha);
			}
			return dist;
		}
		
		 *
		 *
		 *
		 */
	

}