import java.io.FileNotFoundException;
import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;
import java.util.ArrayList;


public class Agent  {
	// Parametres de debugage et de lancement reel
	final static boolean DEBUG=false;
	final static boolean DEMARAGE=true;
	
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
	final static int SECOND_PALET=9;
	
	//Constantes de l'algorithme
	final static double seuilDetectionPalet = 0.38;
	final static double margeDistance = 0.05;
	final static double seuilArretMur = 0.25;
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
	private Chrono chrono;
	int nbrPaletAttrape;
	
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
		couleur="";
		chrono=new Chrono();
		nbrPaletAttrape=0;
	}


	public static void main(String[] args) throws IOException {
		

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
		
		Button.ENTER.waitForPressAndRelease();
		
		//robot.cs.calibration();
		//robot.tourner(90);
		/*robot.a.forward(2);
		System.out.println(robot.a.getCheminParcouru());*/
		
		
	
		
			while(!Button.ESCAPE.isDown()) {
			System.out.println("Etat "+robot.etat);
			if (robot.ts.isPressed()) {
				robot.etat=PALET_ATTRAPE;
				robot.a.closePince();
			}
			robot.recherchePrincipale();
			if (robot.etat==STOP) break;
		}
		
		
	
		/*
		robot.a.forward();
		while(!robot.isMur()) {
			System.out.println("avance");
		}
		robot.recalibrageMur();
		
			robot.a.forward(0.1);
		robot.rechercheTournante();
		
		
		robot.etat=PALET_ATTRAPE;
		while(!Button.ESCAPE.isDown()) {
		robot.recherchePrincipale();}*/
		
	} 

	/**
	 * Automate
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @author charlotte
	 */
	public void recherchePrincipale() throws FileNotFoundException, IOException {
		switch(etat) {
      case (FIRTS_PALET):
              if(Button.UP.isDown()) {
			           modePause();
				      }
              debutAutomate();
	          	if (DEBUG) debug();
	          	etatPrecedent=etat;
	          	etat=SECOND_PALET;
  	break;

		case (SECOND_PALET):
             if(Button.UP.isDown()) {
			           modePause();
				      }
							a.openPince();
							tourner(92);
							if (DEBUG) debug();
							etatPrecedent=etat;
							etat=FACE_AU_PALET;
		break;	
		case (CHERCHE_EN_ROND) :
             if(Button.UP.isDown()) {
			           modePause();
				      }
							distanceAParcourir=rechercheTournante();
							System.out.println("distanceAParcourir "+distanceAParcourir);
							if (DEBUG)debug();
							etatPrecedent=etat;
							etat=DETECTION_PALET;
		break;
		case (DETECTION_PALET):
              if(Button.UP.isDown()) {
			           modePause();
				      }
							chrono.start();
							if (!avanceVersPalet()) {
								etatPrecedent=etat;
								etat=OBSTACLE_EN_VU;
							}
							else if (distanceAvant<=seuilDetectionPalet+margeDistance) {
								etatPrecedent=etat;
								etat=FACE_AU_PALET;
							}
							else {
								etatPrecedent=etat;
								etat=RECALIBRAGE_A_FAIRE;
							}
							chrono.stop();
							if (a.addParcour(chrono.getDureeSec())) b.setDir(-5); 
							if (DEBUG) debug();
		break;

		case (FACE_AU_PALET):
  	if(Button.UP.isDown()) {
				modePause();
				}
							chrono.start();
							if(fonceUntilPush()) {
								etatPrecedent=etat;
								etat=PALET_ATTRAPE ;
							}
							else {
								etatPrecedent=etat;
								etat=OBSTACLE_EN_VU;
							}
							chrono.stop();
							if (a.addParcour(chrono.getDureeSec())) b.setDir(-5); 
							if (DEBUG) debug();
		break;
		case(AUCUN_PALET_EN_VU) : 
        	if(Button.UP.isDown()) {
			modePause();
			}
							etatPrecedent=etat;
							etat=CHERCHE_EN_ROND; // Ã la fin de cherche en rond
							if (DEBUG) debug();

		break;

		case(OBSTACLE_EN_VU) :
        if(Button.UP.isDown()) {
			modePause();
			}

							System.out.println("etatPrecedent "+etatPrecedent);
							if (etatPrecedent==PALET_ATTRAPE) {
								//recalibrageMurNordSud(); // se mettre vers la distance moindre du mur
								etatPrecedent=etat;
								etat=PALET_ATTRAPE;
							}
							else {
								etatPrecedent=etat;
								etat=CHERCHE_EN_ROND;
							}
							chrono.start();
							demiTour();
							chrono.stop();
							if (a.addParcour(chrono.getDureeSec())) b.setDir(-5);
							if (DEBUG) debug();
		break;

		case(PALET_ATTRAPE): 
if(Button.UP.isDown()) {
				modePause();
				}
							chrono.start();
							if (mettreUnBut()) {
								etatPrecedent=etat;
								etat=CHERCHE_EN_ROND;
							}
							else {
								etatPrecedent=etat;
								etat=OBSTACLE_EN_VU;
							}
							chrono.stop();
							if (a.addParcour(chrono.getDureeSec())) b.setDir(-5); 
							if (DEBUG) debug();
		break;

		case(RECALIBRAGE_A_FAIRE) :
        if(Button.UP.isDown()) {
				modePause();
				}

							System.out.println("recalibrage");
							if (rectifiePosition(1)) {
								etatPrecedent=etat;
								etat=FACE_AU_PALET;
							}
							else if (rectifiePosition(-1)) {
								etatPrecedent=etat;
								etat=FACE_AU_PALET;
							}
							else {
								etatPrecedent=etat;
								etat=AUCUN_PALET_EN_VU;
							}
							if(isMur() || isLigneBlanche()) {
								etatPrecedent=etat;
								etat=OBSTACLE_EN_VU;
							}
							if (DEBUG) debug();
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
		a.forward(0.5);
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
			if (nbrPaletAttrape>2) recalibrageBaseE();
			a.backward(distanceDeReculPostBut);
			a.closePince();
			tourner(angleDemiTour);
			nbrPaletAttrape++;
			return true;
		}
	
	 /** va tout droit jusqua activer le poussoir
		 * @author charlotte
		 * @return
		 * @throws FileNotFoundException
		 * @throws IOException
		 */
		 public boolean fonceUntilPush() throws FileNotFoundException, IOException {
			a.openPince();
			a.forward();
			while (!ts.isPressed() ) {
				//TODO Implementation des couleurs et du changement de case ici
				/**
				 * @author charlotte 
				 * j'ai rajouter cette ligne ; elle te renvoit la couleur en string
				 */
				lectureCouleur();
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
	 * @param angle
	 */
	private void tourner (double angle) {
		a.setSpeed((int) vitesseRotation);
		if (angle>180 && angle!=(angleRotationPalet+margeRotation)) {
			angle-=angleRotationPalet;
		}
		int dir=angle>0?1:-1;
		if (dir==-1)angle*=-1;
		if (angle<=angleRecalage) a.rotate(dir*angle);
		else {
			int i=0;
			for (;i<angle;i+=angleRecalage) {
				a.rotate(dir*angleRecalage);
			}
			a.rotate(dir*(angle-i));
		}
		b.setDir((int) (dir*angle));
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
	  *  lit la couleur et la transmet à la boussole si ce n'est ni noir ni gris
	  * @throws FileNotFoundException
	  * @throws IOException
	  * 
	  */
	 public boolean lectureCouleur() throws FileNotFoundException, IOException {
		 couleur=cs.laCouleur();
		 if (!couleur.equals("grey") && !couleur.equals("black")) {
			 //return b.corrigerBoussole(couleur);	
		 }
		 return false;
	 }
	
/**
 *  se déclenche dès qu'on croise une ligne blanche
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
		 * @author nicolas
		 * @param demarage
		 */
		public void modePause() {
			System.out.println("  MODE PAUSE ACTIVEE");
			System.out.println(" PRESS ENTER POUR LANCER");
			System.out.println("ou press escape et enter ");
			System.out.println("pour tout arreter");
			
			System.out.println("angle: "+ b.getDir());
			Delay.msDelay(3000);
			
			Button.ENTER.waitForPressAndRelease();
			if(Button.ESCAPE.isDown()) {
				etat=STOP;
				return;
			}
		}
	

	 /**
		 * trouve la plus petite valeur qui ne soit pas en dessous du seuil de détaction du palet
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
		
		
		
		/**
		 * se recalibre droit vers le mur dans lequel il fonce
		 *  indique à la boussolle si on est face au mur Nord ou Sud
		 *  @author charlotte
		 */
		/*private void recalibrageMurNordSud() {
			double trouver;
			ArrayList<Double> tabList= new ArrayList<Double>();
			
			a.backward(0.4);
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
			System.out.println(tabList.size()+" distances mesurees"); 
			trouver=distanceMinMur(tabList);
			int i=tabList.indexOf(trouver);
			System.out.println("distances min "+trouver+"a indice "+i); 
			tourner(angleDemiTour/tabList.size()*i-180); 
			System.out.println("je me suis recaler de"+angleRotationPalet/tabList.size()*i+" degrees"); 		
			System.out.println("distance "+trouver);
			if (angleRotationPalet/tabList.size()*i<90) {
				System.out.println("je suis au "+c.getDirDroiteE());
				b.setAbsoluteDir(c.getDirDroiteE());
			}
			else {
				System.out.println("je suis au "+c.getDirDroiteE());
				b.setAbsoluteDir(c.getDirGaucheE());
			}
		}*/
		
		
		/**
		 * se recalibre droit vers le mur dans lequel il fonce
		 *  indique à la boussolle si on est face au mur Nord ou Sud
		 *  @author charlotte
		 */
		private void recalibrageBaseE() {
			double trouver;
			ArrayList<Double> tabList= new ArrayList<Double>();
			a.backward(0.1);
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
			System.out.println(tabList.size()+" distances mesurees"); 
			trouver=distanceMinMur(tabList);
			int i=tabList.indexOf(trouver);
			System.out.println("distances min "+trouver+"a indice "+i); 
			tourner(angleDemiTour/tabList.size()*i-180); 
			System.out.println("je me suis recaler de"+angleRotationPalet/tabList.size()*i+" degrees"); 		
			System.out.println("distance "+trouver);
			System.out.println("je suis au "+c.getBaseE());
			b.setAbsoluteDir(c.getBaseE());
		}
		
		
		
		private void debug() {
			System.out.println("etat precedent "+etatPrecedent);
			System.out.println("etat "+etat);
			System.out.println("boussolle "+b.getDir());
			System.out.println("getCheminParcouru "+a.getCheminParcouru());
			Button.ENTER.waitForPressAndRelease() ;
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