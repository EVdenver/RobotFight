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
	final static int CHERCHE_EN_ROND=0;
	final static int OBSTACLE_EN_VU=1;
	final static int DETECTION_PALET=2;
	final static int AUCUN_PALET_EN_VU=3;
	final static int FACE_AU_PALET=4;
	final static int PALET_ATTRAPE=5;
	final static int RECALIBRAGE_A_FAIRE=6;
	final static int STOP=7;
	final static int FIRTS_PALET=8;
	
	//Constantes de l'algorithme
	final static double seuilDetectionPalet = 0.38;
	final static double margeDistance = 0.05;
	final static double seuilArretMur = 0.2;
	final static double largeurMax=2;
	final static double longeurMax=1.7;
	final static double angleRotationPalet=360;
	final static double angleDemiTour=180;
	final static double angleRecalage=15;
	final static double margeRotation = 1;
	final static double vitesseRotation=200;
	final static double vitesseAvancer=500;
	final static double tempsAttenteEntreDeuxMesureDistance = 100;
	final static double distanceDeReculPostBut=0.8;
	final static double distanceDeReculPostObstacle=0.5;
	
	// parametres d initialisation de position
	static char positionBase='A';
	static int regardRobot;
	static int baseRobot;
	
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
	int etatPrecedent;
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
		etatPrecedent=8;
		String couleur="";
	}


	public static void main(String[] args) throws IOException {
		
		//	cs.calibration();

		switch (positionBase) {
		case 'A':
			regardRobot=0;
			baseRobot=180;
			break;
		case 'B':
			regardRobot=0;
			baseRobot=180;
			break;
		case 'C':
			regardRobot=0;
			baseRobot=180;
			break;
		case 'E':
			regardRobot=180;
			baseRobot=0;
			break;
		case 'F':
			regardRobot=180;
			baseRobot=0;
			break;
		case 'G':
			regardRobot=180;
			baseRobot=0;
			break;

		}



		Agent robot=new Agent(new Actionneur(MotorPort.C, MotorPort.A, MotorPort.B),new EchoSensor (SensorPort.S3),new TouchSensor(SensorPort.S2), new ColorimetrieSensor(SensorPort.S1),new Boussole(regardRobot),new Carte(regardRobot,baseRobot));
	
	
	/*	
			while(!Button.ESCAPE.isDown()) {
			System.out.println("Etat "+robot.etat);
			if (robot.ts.isPressed()) {
				robot.etat=PALET_ATTRAPE;
				robot.a.closePince();
			}
			robot.recherchePrincipale();
			if (robot.etat==STOP) break;
		}*/
		
		
	
		
		robot.a.forward();
		while(!robot.isMur()) {
			System.out.println("avance");
		}
		robot.recalibrageMur();
		
		/*	robot.a.forward(0.1);
		robot.rechercheTournante();*/
		
	} 

	/**
	 * Automate
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @author charlotte
	 */
	public void recherchePrincipale() throws FileNotFoundException, IOException {
		etatPrecedent=etat;
		switch(etat) {
		case (FIRTS_PALET):
			debutAutomate();
		if (DEBUG) {
			System.out.println("boussolle "+b.getDir());
			System.out.println("getCheminParcouru "+a.getCheminParcouru());
			Button.ENTER.waitForPressAndRelease() ;
		}
		etat=CHERCHE_EN_ROND;
		break;
		case (CHERCHE_EN_ROND) : 
			distanceAParcourir=rechercheTournante();
		System.out.println("distanceAParcourir "+distanceAParcourir);
		if (DEBUG) {
			System.out.println("boussolle "+b.getDir());
			System.out.println("getCheminParcouru "+a.getCheminParcouru());
			Button.ENTER.waitForPressAndRelease() ;
		}
		etat=DETECTION_PALET;
		break;
		case (DETECTION_PALET):
			if (!avanceVersPalet()) etat=OBSTACLE_EN_VU;
			else if (distanceAvant<=seuilDetectionPalet+margeDistance) etat=FACE_AU_PALET;
			else etat=RECALIBRAGE_A_FAIRE;
		System.out.println("distance"+distanceAvant);
		if (DEBUG) {
			System.out.println("boussolle "+b.getDir());
			System.out.println("getCheminParcouru "+a.getCheminParcouru());
			Button.ENTER.waitForPressAndRelease() ;
		}
		break;
		case (FACE_AU_PALET):
			if(fonceUntilPush()) etat=PALET_ATTRAPE ;
			else etat=OBSTACLE_EN_VU;
		if (DEBUG) {
			System.out.println("boussolle "+b.getDir());
			System.out.println("getCheminParcouru "+a.getCheminParcouru());
			Button.ENTER.waitForPressAndRelease() ;
		}
		break;
		case(AUCUN_PALET_EN_VU) : etat=CHERCHE_EN_ROND; // ÃÂ  la fin de cherche en rond
		if (DEBUG) {
			System.out.println("boussolle "+b.getDir());
			System.out.println("getCheminParcouru "+a.getCheminParcouru());
			Button.ENTER.waitForPressAndRelease() ;
		}
		break;
		case(OBSTACLE_EN_VU) :
			if (etatPrecedent==PALET_ATTRAPE) {
			//	recalibrerMur(); se mettre vers la distance moindre du mur
				etat=PALET_ATTRAPE;
			}
			else {
				
				 etat=CHERCHE_EN_ROND;
			}
		demiTour();
		
		if (DEBUG) {
			System.out.println("boussolle "+b.getDir());
			System.out.println("getCheminParcouru "+a.getCheminParcouru());
			Button.ENTER.waitForPressAndRelease() ;
		}
		break;
		case(PALET_ATTRAPE): 
			if (mettreUnBut()) etat=CHERCHE_EN_ROND;
			else etat=OBSTACLE_EN_VU;
		if (DEBUG) {
			System.out.println("boussolle "+b.getDir());
			System.out.println("getCheminParcouru "+a.getCheminParcouru());
			Button.ENTER.waitForPressAndRelease() ;
		}
		break;
		case(RECALIBRAGE_A_FAIRE) :
			System.out.println("recalibrage");
		if (rectifiePosition(1)) etat=FACE_AU_PALET;
		else if (rectifiePosition(-1)) etat=FACE_AU_PALET;
		else etat=AUCUN_PALET_EN_VU;
		if(isMur() || isLigneBlanche()) etat=OBSTACLE_EN_VU;
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
	public void debutAutomate () throws FileNotFoundException, IOException {
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
		ArrayList<Double> tabList= new ArrayList<Double>();
		double trouver;
		System.out.println("tourne de "+(angleRotationPalet+margeRotation));
		tourner((int) (angleRotationPalet+margeRotation));
		while (a.isMoving()){
			trouver=es.getDistance();
			if (trouver==0) trouver=100;
			tabList.add(trouver);
		}
		System.out.println(tabList.size()+" distances mesurees"); // 300
		trouver=distanceMinPalet(tabList);
		int i=tabList.indexOf(trouver);
		System.out.println("distances min "+trouver+"a indice "+i); 
		tourner((int) (angleRotationPalet/tabList.size()*i)); 
		System.out.println("je me suis recaler de"+(angleRotationPalet/tabList.size()*i)+" degrees"); 		
		System.out.println("distance "+trouver);
		return trouver;
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
		Delay.msDelay((long) tempsAttenteEntreDeuxMesureDistance);
		distanceMaintenant = es.getDistance();
		while(distanceAvant > distanceMaintenant ) {
			distanceAvant=distanceMaintenant;
			Delay.msDelay((long) tempsAttenteEntreDeuxMesureDistance);
			distanceMaintenant = es.getDistance();
			/**
			 * @author charlotte 
			 * @TO DO
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
			System.out.println("Angle: " +getDiff(c.getBaseE()));
			tourner(getDiff(c.getBaseE())); 
			while (!couleur.equals("white")) {
				a.forward();
				if (lectureCouleur()) {
					return mettreUnBut();
				}
				if (isMur()) {
					return false;
				}
			}
			a.openPince();
			a.backward(distanceDeReculPostBut);
			tourner(angleDemiTour);
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
				tourner(angleRecalage*i);
				Delay.msDelay((long) tempsAttenteEntreDeuxMesureDistance);
				distanceAvant=distanceMaintenant;
				distanceMaintenant = es.getDistance();
				if (distanceMaintenant<distanceAvant) return true;
				else tourner(-angleRecalage*i);
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
	 * @param angledemitour2
	 */
	private void tourner (double angledemitour2) {
		a.setSpeed((int) vitesseRotation);
		if (angledemitour2>180 && angledemitour2!=(angleRotationPalet+margeRotation)) {
			angledemitour2-=angleRotationPalet;
		}
		int dir=angledemitour2>0?1:-1;
		if (dir==-1)angledemitour2*=-1;
		if (angledemitour2<=angleRecalage) a.rotate(dir*angledemitour2);
		else {
			int i=0;
			for (;i<angledemitour2;i+=angleRecalage) {
				a.rotate(dir*angleRecalage);
			}
			a.rotate(dir*(angledemitour2-i));
		}
		b.setDir((int) (dir*angledemitour2));
		a.setSpeed((int) vitesseAvancer);
	}



	/**
	 * @author charlotte
	 * @return
	 */
	 public boolean isMur() {
		distanceMaintenant=es.getDistance();
		System.out.println("is Mur "+distanceMaintenant);
		if (distanceMaintenant<=seuilArretMur && distanceMaintenant!=0) {
			System.out.println("mur detecte, distance "+distanceMaintenant);
			a.stop();
			return true;
		}
		return false;
	}
	 
	 /**
	  * fais un demi-tour
	  * @author charlotte
	  */
	 public void demiTour() {
		 a.backward(distanceDeReculPostObstacle);
			int i=1;
			//	if (isButOuest() && faceMurNord()) i*=-1;
			//	if (isButEst() && faceMurSud()) i*=-1;
			tourner(i*angleDemiTour);
	 }
	 
	 //TODO mettre le nom de la nouvelle methode de boussolle
	 /**
	  *  lit la couleur et la transmet � la boussole si ce n'est ni noir ni gris
	  * @throws FileNotFoundException
	  * @throws IOException
	  * 
	  */
	 public boolean lectureCouleur() throws FileNotFoundException, IOException {
		 couleur=cs.laCouleur();
		 if (!couleur.equals("grey") && !couleur.equals("black")) {
		//	 return b.(couleur);	
		 }
		 return false;
	 }
	
/**
 *  se d�clenche d�s qu'on croise une ligne blanche
 * @return
 * @throws FileNotFoundException
 * @throws IOException
 * @author charlotte
 */
	 public boolean isLigneBlanche() throws FileNotFoundException, IOException {
		lectureCouleur();
		if (couleur.equals("white")) {
			System.out.println("ligne blanche, distance "+distanceMaintenant);
			a.stop();
			return true;
		}
		return false;
	}
	
	 /**
		 * trouve la plus petite valeur qui ne soit pas en dessous du seuil de d�taction du palet
		 * @param list
		 * @return
		 * @author charlotte
		 */
		private static double distanceMinPalet (ArrayList<Double> list) {
			double res=Double.MAX_VALUE;
			for (Double d : list) {
				if (d>seuilDetectionPalet-margeDistance) res=d<res?d:res;
			}
			return res;
		}
		
		 /**
		 * trouve la plus petite valeur 
		 * @param list
		 * @return
		 * @author charlotte
		 */
		private static double distanceMinMur (ArrayList<Double> list) {
			double res=Double.MAX_VALUE;
			for (Double d : list) {
				res=d<res?d:res;
			}
			return res;
		}
		private void recalibrageMur() {
			// si je me suis d�cal� vers le moins ou vers le plus
			ArrayList<Double> tabList= new ArrayList<Double>();
			double trouver;
			tourner(-90);
			tourner(angleDemiTour);
			
			while (a.isMoving()){
				trouver=es.getDistance();
				System.out.println("trouver "+trouver);
				if (trouver==0) trouver=100;
				tabList.add(trouver);
			}
			a.stop();
			Delay.msDelay(10);
			tourner(-180);
			System.out.println(tabList.size()+" distances mesurees"); 
			trouver=distanceMinMur(tabList);
			int i=tabList.indexOf(trouver);
			System.out.println("distances min "+trouver+"a indice "+i); 
			tourner(angleDemiTour/tabList.size()*i); 
			System.out.println("je me suis recaler de"+angleRotationPalet/tabList.size()*i+" degrees"); 		
			System.out.println("distance "+trouver);
			if (angleRotationPalet/tabList.size()*i<90) {
				// recalibrage  boussole
				//je regarde le mur � droite de la base E
			}
			else {
				// je regarde le mur � gauche de la base E
			}
			
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