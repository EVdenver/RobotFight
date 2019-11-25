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
	static boolean trouver=false;
	// 
	 // se met à jour en continue et avance tant que la distance diminue
	// methode qui dit s'il y a besoin d'un décalage en fct de la distance ; si trop court vas tout droit sinon recalage
	

	
	public static void main(String[] args) {
		
		
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
	
	public static double calculDistanceMur() {
		double x = 0;
		double y = 0;
		double alpha = 0;
		double dist;
		double sortieDeMur=0;
		if (alpha>0 && alpha<=90) {
			dist=(longeurMax-x)/Math.cos(alpha);
			sortieDeMur=dist/Math.sin(alpha);
			if (sortieDeMur>largeurMax) {
				sortieDeMur-=largeurMax;
				dist-=sortieDeMur/Math.sin(alpha);
			}
				
					// enlever le second 
		}
		if (alpha>90 && alpha<=180) {
			dist=(largeurMax-y)/Math.cos(alpha-90);
			
			sortieDeMur=dist/Math.sin(alpha-90);
			if (sortieDeMur>longeurMax) {
				sortieDeMur-=longeurMax;
				dist-=sortieDeMur/Math.sin(alpha-90);
			}
			
			
		}
		if (alpha>180 && alpha<=270) {
			dist=(x)/Math.cos(alpha-180);
			
			sortieDeMur=dist/Math.sin(alpha-180);
			if (sortieDeMur>largeurMax) {
				sortieDeMur-=largeurMax;
				dist-=sortieDeMur/Math.sin(alpha-180);
			}
			
		}
		if (alpha>270) {
			dist=(y)/Math.cos(alpha-270);
			
			sortieDeMur=dist/Math.sin(alpha-270);
			if (sortieDeMur>longeurMax) {
				sortieDeMur-=longeurMax;
				dist-=sortieDeMur/Math.sin(alpha-270);
			}
			
		}
		
		return distanceAvant;
		
	}
  
  
	public static double rechercheTournante () {
		a.closePince();
		double trouver=es.getDistance(); //distance entre 0 et 1
		double angleTotal=0;
		if (trouver==0) trouver=100;
		System.out.println("distance objet : "+trouver);
		while (trouver>distanceMur  && angleTotal<360 ){
			a.rotate(15);
			angleTotal+=15;
			a.stop();
			trouver=es.getDistance(); //distance entre 0 et 1
			System.out.println("distance objet : "+trouver);
			Delay.msDelay(100);
		}
		System.out.println("distance "+trouver);
		return trouver;
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
			a.stop();
			a.backward(0.2);
			int i=1;
		//	if (isButOuest() && faceMurNord()) i*=-1;
		//	if (isButEst() && faceMurSud()) i*=-1;
			a.rotate(i*180);
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
		distanceMaintenant = es.getDistance();
		while(distanceAvant > distanceMaintenant ) {
			distanceAvant=distanceMaintenant;
			distanceMaintenant = es.getDistance();
			Delay.msDelay(100);
			if (isMur()) return false;
		}
		a.stop();
		return true;
	}
	
	static public void mettreUnBut() {
		// se diriger vers les buts à l'aide de la boussole et de la ligne blanche
		etat=STOP;
		
	}
	
	public static void rechercheSimple() {
		
		switch(etat) {
		case (chercheEnRond) : 
			if (rechercheTournante()<distanceMur) etat=detectionPalet;
			else etat=aucunPaletEnVu;
		break;
		case (detectionPalet): 
			if (!avanceVersPalet()) etat=dosAuMur;
			else if (distanceAvant<=seuilDetectionPalet) etat=faceAuPalet;
			else etat=recalibrageAFaire;
		break;
		case (faceAuPalet): 
			if(fonceUntilPush()) etat=paletAttraper ;
			else etat=dosAuMur;
		break;
		case(aucunPaletEnVu) : etat=chercheEnRond; // à la fin de cherche en rond
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