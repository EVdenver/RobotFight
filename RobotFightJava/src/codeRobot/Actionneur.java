package codeRobot;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;


public class Actionneur {
	private  EV3LargeRegulatedMotor mLeftMotor; //Moteur gauche
	private  EV3LargeRegulatedMotor mRightMotor; //Moteur droit
	private EV3MediumRegulatedMotor mPincesMotor; //Moteur des pinces
	private boolean open=false; //Booleen indiquant l'etat des pinces (true = ouvert, false = fermee)
	Delay d= new Delay(); 
	private final static int SPEED = 400; //La vitesse du robot en degree par secondes
	private final static double WHEEL_RADIUS = 0.05; //Rayon des roues en metres
	private final static int ROTATION_FACTOR=400; //facteur modulant la relation temps/vitesse/angle(en celcius) qui permet au robot de tourner sur son propre axe
	private boolean avance=true; //Booleen indiquant si le robot est en train d'avancer
	private double cheminParcouru=0; //Le chemin parcouru par le robot depuis de debut de l'execution du programme

	//Constructor
	/**
	 * @author Charlotte
	 * @param left_port port du moteur gauche
	 * @param right_port port du moteur droit
	 * @param pinces_port port du moteur pour les pinces
	 * Constructeur de l'actionneur. Il prend en parametres les 3 ports necessaire a l'initialisation des trois moteurs. Initialise ensuite la vitesse de base de qhaque moteur
	 * Instancie aussi la synchronisation du moteur gauche avec le moteur droit
	 */
	public Actionneur(Port left_port, Port right_port, Port pinces_port) {
		mLeftMotor = new EV3LargeRegulatedMotor(left_port);
		mRightMotor = new EV3LargeRegulatedMotor(right_port);
		mPincesMotor = new EV3MediumRegulatedMotor(pinces_port);
		mLeftMotor.setSpeed(SPEED);
		mRightMotor.setSpeed(SPEED);
		mPincesMotor.setSpeed(10000);
		mLeftMotor.synchronizeWith(new RegulatedMotor[] {mRightMotor});
	}

	//Accesseur
	/**
	 * @author Charlotte
	 * @param v la nouvelle vitesse des moteurs
	 * Indique au 2 moteurs la nouvelle vitesse pour les 2 moteurs
	 */
	public void setSpeed(int v) {
		mLeftMotor.setSpeed(v);
		mRightMotor.setSpeed(v);
	}

	/**
	 * @author Charlotte
	 * @return accesseur pour le chemin parcouru
	 */
	public double getCheminParcouru() {
		return cheminParcouru;
	}

	/**
	 * @author Charlotte
	 * @return avance
	 * Accesseur de l'attribut avance
	 */
	public boolean isAvance() {
		return avance;
	}

	//Method

	/**
	 * @author Charlotte 
	 * Ouvre les pinces de l'angles necessaires pour rammasser le palet mais suffisement peu pour ne pas gener le deroulement de la partie
	 * Methode asynchrone
	 */
	public void openPince() { 
		if(!open){ 
			mPincesMotor.rotate(800, true); // asynchrone
			open=true;
		}
	}	
	/**
	 * @author Charlotte
	 * Ferme les pinces
	 * Methode asynchrone
	 */
	public void closePince() { 
		if(open) {
			mPincesMotor.rotate(-800, true);
			open=false;
		}
	}

	/**
	 * @author Charlotte
	 * @param t Le temps mis par le robot pour parcourir une certaine distance
	 * @return true si le robot a parcouru une certaine distance et doit donc recalibrer la boussole, false sinon
	 * Methode initialisant une recalibration. Le robot devie toujours sur la droite quand il avance en ligne droite. 
	 * Au bout d'une certaine distance parcouru il faut donc indiquer a la boussole de se corriger
	 */
	public boolean addParcour(double t) {
		cheminParcouru+=t*SPEED;
		if (cheminParcouru>=4000) {
			cheminParcouru-=4000;
			return true;
		}
		return false;
	}

	/**
	 * @author Charlotte
	 * @param distance La distance a parcourir
	 * @return la distance reellement parcourue par le robot
	 * Methode visant a faire reculer le robot d'une certaine distance. L'imprecision du robot necessite de calculer la distance reelle parcourue par le robot
	 * Methode asynchrone
	 */
	public double backward(double distance) { 
		double radianByS = SPEED*0.017453292519943;
		double distanceByS=radianByS*WHEEL_RADIUS;
		double time=distance/distanceByS;
		long timeStampBefore = System.currentTimeMillis();
		backward();
		Delay.msDelay((long) (time*1000));	
		stop();
		long timeStampAfter = System.currentTimeMillis();
		return (timeStampBefore-timeStampAfter)*distanceByS*1000;
	}

	/**
	 * @author Charlotte 
	 * @param distance la distance a parcourir
	 * @see backward(double distance) identique mais pour avancer
	 * Methode asynchrone
	 */
	public void forward(double distance) { 
		double radianByS = SPEED*0.017453292519943; 
		double distanceByS=radianByS*WHEEL_RADIUS; 
		double time=distance/distanceByS;
		forward();
		Delay.msDelay((long) (time*1000));		
		stop();
		cheminParcouru+=distance*SPEED*2;
		//return (timeStampBefore-timeStampAfter)*distanceByS*1000;
	}

	/**
	 * @author Charlotte
	 * @return false, le nouvel etat de la variable avance
	 * Fait reculer le robot jusqu'a ce que le robot n'est plus considere en marche
	 */
	public boolean backwardUntilStop() {
		while (avance) {
			backward(0.01);
		}
		return false;
	}

	/**
	 * @see backwardUntilStop() idem mais pour avancer
	 * @author Charlotte
	 * @return false, le nouvel etat de la variable avance
	 */
	public boolean forwardUntilStop() {
		while (avance) {
			forward(0.01);
		}
		return false;
	}

	/**
	 * @author Charlotte
	 * Methode servant a stopper le robot sur place
	 */
	public void stop() {
		mLeftMotor.startSynchronization();
		mLeftMotor.stop();
		mRightMotor.stop();
		mLeftMotor.endSynchronization();
	}

	/**
	 * @author Charlotte
	 * @return true si les deux moteurs sont en mouvement false sinon
	 */
	public boolean isMoving () {
		return (mLeftMotor.isMoving() && mRightMotor.isMoving());
	}

	/**
	 * @author Charlotte
	 * @param angle angle de rotation que le robot doit effectuer
	 * Fait tourner le robot dans le sens horaire de l'angle voulu
	 */
	public void rotate (double angle) { // 90 faire test, combien de temps = combien de degré
		double time=Math.abs(angle/SPEED*ROTATION_FACTOR);
		if (angle>0) rotateCounterClockwise();
		else rotateClockwise();
		Delay.msDelay((long) (time*10)); // en ms	problème calcul temp	
		stop();	
	}

	/**
	 * @author Charlotte
	 * Methode pour faire avancer le robot sans condition d'arret
	 */
	public void forward() {
		mLeftMotor.startSynchronization();
		mLeftMotor.forward();
		mRightMotor.forward();
		mLeftMotor.endSynchronization();
	}

	/**
	 * @see forward()
	 * @author Charlotte 
	 * Methode pour faire reculer le robot sans condition d'arret
	 */
	private void backward() {
		mLeftMotor.startSynchronization();
		mLeftMotor.backward();
		mRightMotor.backward();
		mLeftMotor.endSynchronization();
	}

	/**
	 * @author Charlotte
	 * Initialise la rotation du robot dans le sens horaire
	 */
	private void rotateClockwise() {
		mLeftMotor.startSynchronization();
		mLeftMotor.forward();
		mRightMotor.backward();
		mLeftMotor.endSynchronization();

	}

	/**
	 * @see rotateClockwise()
	 * @author Charlotte
	 * Idem que rotateClockwise() mais dans le sens anti-horaire
	 */
	private void rotateCounterClockwise() {
		mLeftMotor.startSynchronization();
		mLeftMotor.backward();
		mRightMotor.forward();
		mLeftMotor.endSynchronization();
	}
}
