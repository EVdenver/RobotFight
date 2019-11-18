import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Test {
	static double distanceMur=2;
	static Actionneur a = new Actionneur(MotorPort.C, MotorPort.A, MotorPort.B) ;
	static EchoSensor es= new EchoSensor (SensorPort.S3);
	static TouchSensor ts = new TouchSensor(SensorPort.S2);
	static double distanceMaintenant = 0;
	static double distanceAvant = 0; 
	final static double seuilDetectionPalet = 0.38;
	final static double seuilArretMur = 0.2;
	
	static int etat=0;
	final static int chercheEnRond=0;
	final static int dosAuMur=1;
	final static int detectionPalet=2;
	final static int aucunPaletEnVu=3;
	final static int faceAuPalet=4;
	final static int paletAttraper=5;
	final static int recalibrageAFaire=6;
	
	static boolean trouver=false;
	// 
	 // se met à jour en continue et avance tant que la distance diminue
	// methode qui dit s'il y a besoin d'un décalage en fct de la distance ; si trop court vas tout droit sinon recalage
	

	
	public static void main(String[] args) {
		
		
		while(!ts.isPressed()) {
			System.out.println("etat"+etat);
			rechercheSimple();
			
			Delay.msDelay(3000);
		}
		
	/*	
		while(!trouver) {
			rechercheSimple();
			distanceAvant=0;
			distanceMaintenant=0;
		}*/
		
		; //distance entre 0 et 1 
	//	System.out.println("distance objet : "+es.getDistance());
		
	//	
		
		
		
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
  
  
	public static void rechercheTournante () {
		a.closePince();
		float trouver=es.getDistance(); //distance entre 0 et 1
		double angleTotal=0;
		if (trouver==0) trouver=100;
		System.out.println("distance objet : "+trouver);
		while (trouver>distanceMur && angleTotal<360 ){
			a.rotate(15);
			angleTotal+=15;
			a.stop();
			trouver=es.getDistance(); //distance entre 0 et 1
			System.out.println("distance objet : "+trouver);
			Delay.msDelay(100);
		}
		if (trouver<distanceMur) etat=detectionPalet;
		else etat=aucunPaletEnVu;
		System.out.println("distance "+trouver);
		
	}
	
	
	public static boolean rectifiePosition (int i) {
		distanceMaintenant = es.getDistance();
		a.rotate(15*i);
		Delay.msDelay(100); // mesure du temps pour bouger de 15 degrés
		distanceAvant=distanceMaintenant;
		distanceMaintenant = es.getDistance();
		if (distanceMaintenant<distanceAvant) return true;
		else a.rotate(-15*i);
		return false;
	}
	
	static public boolean isMur() {
		if (distanceMaintenant<=seuilArretMur) {
			System.out.println("mur detecte");
			a.backward(0.2);
			a.rotate(180);
			etat=dosAuMur;
			return true;
		}
		return false;
	}
	
	static public void fonceUntilPush() {
		a.forward();
		while (!ts.isPressed() ) {
			distanceMaintenant=es.getDistance();
			if (isMur()) break;
		}
		a.stop();
		a.closePince();
		Delay.msDelay(3_000);
		etat=paletAttraper;
	}
	
	
	//a travailler
	static public void recalibrage () {
			System.out.println("recalibrage à faire");
			if (rectifiePosition(1)) etat=faceAuPalet;
			else if (rectifiePosition(-1)) etat=faceAuPalet;
			else etat=aucunPaletEnVu;
			isMur();
			return;
	}
	
	static public void avanceVersPalet() {
		a.openPince();
		distanceAvant = es.getDistance();
		a.forward();
		distanceMaintenant = es.getDistance();
		while(distanceAvant > distanceMaintenant ) {
			distanceAvant=distanceMaintenant;
			distanceMaintenant = es.getDistance();
			Delay.msDelay(100);
			if (isMur()) break;
		}
		a.stop();
		System.out.println("augmentation de distance");
		if (distanceAvant<=seuilDetectionPalet) etat=faceAuPalet;
		else etat=recalibrageAFaire;
	}
	
	static public void mettreUnBut() {
		// se diriger vers les buts à l'aide de la boussole et de la ligne blanche
	}
	
	public static void rechercheSimple() {
		
		switch(etat) {
		case (chercheEnRond) : rechercheTournante();
		break;
		case (detectionPalet): avanceVersPalet();
		break;
		case (faceAuPalet): fonceUntilPush();
		break;
		case(aucunPaletEnVu) : etat=chercheEnRond; 
		break;
		case(dosAuMur) : etat=chercheEnRond;
		break;
		case(paletAttraper): mettreUnBut();
		break;
		case(recalibrageAFaire) : recalibrage(); 
		break;
		}
	
	}
}