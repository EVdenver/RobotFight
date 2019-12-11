import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

public class Agent  {
	// Parametres de debugage et de lancement reel
	private final static boolean DEBUG=false;

	//Constantes representant les etats de l automate
	private final static int CHERCHE_EN_ROND=0;
	private final static int OBSTACLE_EN_VU=1;
	private final static int DETECTION_PALET=2;
	private final static int AUCUN_PALET_EN_VU=3;
	private final static int FACE_AU_PALET=4;
	private final static int PALET_ATTRAPE=5;
	private final static int RECALIBRAGE_A_FAIRE=6;
	private final static int STOP=7;
	private final static int FIRTS_PALET=8;
	private final static int SECOND_PALET=9;

	//Constantes de l'algorithme
	private final static double seuilDetectionPalet = 0.38;
	private final static double margeDistance = 0.05;
	private final static double seuilArretMur = 0.25;
	private final static double angleRotationPalet=360;
	private final static double angleDemiTour=180;
	private final static double angleRecalage=15;
	private final static double margeRotation = 1;
	private final static double vitesseRotation=200;
	private final static double vitesseAvancer=500;
	private final static double tempsAttenteEntreDeuxMesureDistance = 100;
	private final static double distanceDeReculPostBut=0.8;
	private final static double distanceDeReculPostObstacle=0.5;

	// parametres d initialisation de position
	private static char positionBase='A';
	private static int regardRobot;
	private static int baseRobot;

	//Parametres d'instance de l agent
	private Actionneur a  ;
	private EchoSensor es ;
	private TouchSensor ts ;
	private ColorimetrieSensor cs;
	private Boussole b ;
	private Carte c ;
	private double distanceMaintenant = 0;
	private double distanceAvant = 0; 
	private double distanceAParcourir = 0; 
	private int etat;
	private int etatPrecedent;
	private String couleur;
	private Chrono chrono;
	private int nbrPaletAttrape;

	/**
	 * @author Charlotte
	 * @param Actionneur
	 * @param EchoSensor
	 * @param TouchSensor
	 * @param ColorimetrieSensor
	 * @param Boussole
	 * @param Carte
	 * Constructeur de l'agent 
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
		//Determine le cote de depart
		switch (positionBase) {
		case 'O':
			regardRobot=0;
			baseRobot=180;
			break;
		case 'E':
			regardRobot=180;
			baseRobot=0;
			break;
		}
		//Initialisation de l'agent
		Agent robot=new Agent(new Actionneur(MotorPort.C, MotorPort.A, MotorPort.B),new EchoSensor (SensorPort.S3),new TouchSensor(SensorPort.S2), new ColorimetrieSensor(SensorPort.S1),new Boussole(regardRobot),new Carte(regardRobot,baseRobot));
		//Permet de lancer le code manuellement 
		Button.ENTER.waitForPressAndRelease();

		//robot.cs.calibration(); //Calibration des couleurs	
		while(!Button.ESCAPE.isDown()) {
			System.out.println("Etat "+robot.etat);
			if (robot.ts.isPressed()) {
				robot.etat=PALET_ATTRAPE;
				robot.a.closePince();
			}
			robot.recherchePrincipale();
			if (robot.etat==STOP) break;
		}		
	} 

	/**
	 * @author Charlotte
	 * @throws FileNotFoundException
	 * @throws IOException
	 * Automate
	 */
	public void recherchePrincipale() throws FileNotFoundException, IOException {
		switch(etat) {
		case (FIRTS_PALET):
			if(Button.UP.isDown()) {
				modePause();
			}
		debutAutomate();
		if (DEBUG) {
			debug();
		}
		etatPrecedent=etat;
		etat=SECOND_PALET;
		break;

		case (SECOND_PALET):
			if(Button.UP.isDown()) {
				modePause();
			}
		a.openPince();
		tourner(92);
		if (DEBUG) {
			debug();
		}
		etatPrecedent=etat;
		etat=FACE_AU_PALET;
		break;	
		case (CHERCHE_EN_ROND) :
			if(Button.UP.isDown()) {
				modePause();
			}
		distanceAParcourir=rechercheTournante();
		System.out.println("distanceAParcourir "+distanceAParcourir);
		if (DEBUG) {
			debug();
		}
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
		if (a.addParcour(chrono.getDureeSec())) {
			b.setDir(-5); 
		}
		if (DEBUG) {
			debug();
		}
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
		if (a.addParcour(chrono.getDureeSec())) {
			b.setDir(-5); 
		}
		if (DEBUG) {
			debug();
		}
		break;
		case(AUCUN_PALET_EN_VU) : 
			if(Button.UP.isDown()) {
				modePause();
			}
		etatPrecedent=etat;
		etat=CHERCHE_EN_ROND; // Ãƒ la fin de cherche en rond
		if (DEBUG) {
			debug();
		}
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
		if (a.addParcour(chrono.getDureeSec())) {
			b.setDir(-5);
		}
		if (DEBUG) {
			debug();
		}
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
		if (a.addParcour(chrono.getDureeSec())) {
			b.setDir(-5); 
		}
		if (DEBUG) {
			debug();
		}
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
		if (DEBUG) {
			debug();
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
		a.forward(0.5);
		mettreUnBut();
	}

	/** 
	 * @author Charlotte
	 * @return la distance de l'objet trouver pas le capteur
	 * tourne sur soi-meme et se place vers l'objet le plus proche
	 */
	public double rechercheTournante () {
		a.closePince();
		ArrayList<Double> tabList= new ArrayList<Double>();
		double trouver;
		System.out.println("tourne de "+(angleRotationPalet+margeRotation));
		tourner((int) (angleRotationPalet+margeRotation));
		while (a.isMoving()){
			trouver=es.getDistance();
			if (trouver==0) {
				trouver=100;
			}
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
	 * @author Charlotte
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @return true si l'objet percu est bien un palet, false sinon
	 * Avance vers le palet jusqu'a ce qu'il soit ramasser ou perdu
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
			if (isMur() || isLigneBlanche()) {
				return false;
			}
		}
		a.stop();
		return true;
	}

	/**
	 * @author Charlotte
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @return true si le but est mis, false sinon
	 * Redirige le robot en direction de la base ennemie et le fait avancer juqu'a ce que le but soit mis
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
		if (nbrPaletAttrape>2) {
			recalibrageBaseE();
		}
		a.backward(distanceDeReculPostBut);
		a.closePince();
		tourner(angleDemiTour);
		nbrPaletAttrape++;
		return true;
	}

	/** 
	 * @author Charlotte
	 * @return true si il rammase un palet, false sinon
	 * @throws FileNotFoundException
	 * @throws IOException
	 * Va tout droit jusqu'a activer le capteur de pression
	 */
	public boolean fonceUntilPush() throws FileNotFoundException, IOException {
		a.openPince();
		a.forward();
		while (!ts.isPressed() ) {
			distanceMaintenant=es.getDistance();
			if (isMur() || isLigneBlanche()) {
				return false;
			}
		}
		a.stop();
		a.closePince();
		return true;
	}

	/** 
	 * @author Charlotte
	 * @param direction la direction voulue
	 * @return true si la nouvelle distance est inferieure a celle d'avant, false sinon
	 * Se decale de 15 degree dans une direction donnee (droite ou gauche)
	 */
	public boolean rectifiePosition (int direction) {
		distanceMaintenant = es.getDistance();
		tourner(angleRecalage*direction);
		Delay.msDelay((long) tempsAttenteEntreDeuxMesureDistance);
		distanceAvant=distanceMaintenant;
		distanceMaintenant = es.getDistance();
		if (distanceMaintenant<distanceAvant) {
			return true;
		}else {
			tourner(-angleRecalage*direction);
		}
		return false;
	}

	/**
	 * @author Vincent
	 * @param angle l'angle sur lequel le robot doit se fixer
	 * @return l'angle de deplacement necessaire pour que le regard se tourne dans la direction voulue
	 */
	public int getDiff(int angle) {
		return angle - b.getDir();
	}

	/**
	 * @author Charlotte
	 * @param angle l'angle de rotation voulu
	 */
	private void tourner (double angle) {
		a.setSpeed((int) vitesseRotation);
		if (angle>180 && angle!=(angleRotationPalet+margeRotation)) {
			angle-=angleRotationPalet;
		}
		int dir=angle>0?1:-1;
		if (dir==-1) {
			angle*=-1;
		}
		if (angle<=angleRecalage) {
			a.rotate(dir*angle);
		}
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
	 * @author Charlotte
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
	 * @author charlotte
	 * Fait faire un demi tour au robot
	 */
	public void demiTour() {
		a.backward(distanceDeReculPostObstacle);
		int i=1;
		tourner(i*angleDemiTour);
	}

	/**
	 * @author Vincent
	 * @throws FileNotFoundException
	 * @throws IOException
	 * Lit la couleur
	 * WorkInProgress : Corrige la boussole si selon la couleur lut, la boussole se rend compte que sa valeur est biaisé
	 */
	public boolean lectureCouleur() throws FileNotFoundException, IOException {
		couleur=cs.laCouleur();
		if (!couleur.equals("grey") && !couleur.equals("black")) {
			//return b.corrigerBoussole(couleur);	
		}
		return false;
	}

	/**
	 * @author charlotte 
	 * @return true si la ligne est blanche, false sinon
	 * @throws FileNotFoundException
	 * @throws IOException
	 * Se declenche des qu'on croise une ligne blanche et enclenche la procedure de retour arriere
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
	 * @author Nicolas
	 * Permet de mettre le robot en pause lors d'une pression sur le bouton au milieu de la console du robot
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
	 * @author charlotte
	 * @param list la liste des distances trouvees lors de la recherche tournante
	 * @return la plus petite valeur parmi elle mais superieur au seuil de detection
	 * Trouve la plus petite valeur qui ne soit pas en dessous du seuil de detection d'un palet
	 */
	private static double distanceMinPalet (ArrayList<Double> list) {
		double res=Double.MAX_VALUE;
		for (Double d : list) {
			if (d>seuilDetectionPalet-margeDistance) res=d<res?d:res;
		}
		return res;
	}

	/**
	 * @author charlotte
	 * @param list la liste des distances trouvees lors de la recherche tournante
	 * @return la plus petite valeur de la liste
	 * Trouve la plus petite valeur parmi celle propose
	 */
	private static double distanceMinMur (ArrayList<Double> list) {
		double res=Double.MAX_VALUE;
		for (Double d : list) {
			res=d<res?d:res;
		}
		return res;
	}



	/**
	 * @author charlotte
	 * Se recalibre droit vers le mur dans lequel il fonce
	 * Indique a  la boussolle si on est face au mur Nord ou Sud (work in progress)
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
	 * @author Charlotte
	 * Se recalibre droit vers la base ennemie
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


	/**
	 * @author Charlotte
	 * Le mode permettant d'excecuter l'automate etape par etape
	 */
	private void debug() {
		System.out.println("etat precedent "+etatPrecedent);
		System.out.println("etat "+etat);
		System.out.println("boussolle "+b.getDir());
		System.out.println("getCheminParcouru "+a.getCheminParcouru());
		Button.ENTER.waitForPressAndRelease() ;
	}

	/**
	 * @author Charlotte
	 * @return la distance entre le robot et le mur le plus proche
	 */
	/*public static double calculDistanceMur() {
		double x = b.getPos().getX();
		double y = b.getPos().getY();
		double alpha = b.getDir();
		double dist=longeurMax;
		if (alpha>0 && alpha<=90) {
			dist=calculHypothenus(longeurMax-x, largeurMax, alpha);
		}
		if (alpha>90 && alpha<=180) {
			dist=calculHypothenus(largeurMax-y, longeurMax, alpha-90);
		}
		if (alpha>180 && alpha<=270) {
			dist=calculHypothenus(x, largeurMax, alpha-180);
		}
		if (alpha>270) {
			dist=calculHypothenus(y, longeurMax, alpha-270);
		}
		return dist;
	}*/

	/**
	 * @author charlotte
	 * @param distance1 la premiere distance mesurer
	 * @param distance2 la deuxieme distance mesurer
	 * @param alpha l'angle que le robot a effectuer pour mesurer les 2 distances
	 * @return une nouvelle distance en fonction de l'angle et des deux autres qui représente l'hypothenuse du triangle ainsi formé
	 */
	/*
	private static double calculHypothenus(double distance1, double distance2, double alpha) {
		double dist=(distance1)/Math.cos(alpha);
		double sortieDeMur=dist/Math.sin(alpha);
		if (sortieDeMur>distance2) {
			sortieDeMur-=distance2;
			dist-=sortieDeMur/Math.sin(alpha);
		}
		return dist;
	}*/
}