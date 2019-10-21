import lejos.hardware.Button;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Test {
	public static void main(String[] args) {
		Actionneur a = new Actionneur(MotorPort.C, MotorPort.A, MotorPort.B) ;
		EchoSensor es= new EchoSensor (SensorPort.S3);
		TouchSensor ts = new TouchSensor(SensorPort.S2);
		
		float trouver;
		
		
		
		; //distance entre 0 et 1 
		System.out.println("distance objet : "+es.getDistance());
		
	//	Delay.msDelay(10000);
		
		
		
		trouver=	rechercheTournante ( a, es);
		a.openPince();
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
		
		
		
		
		
	/*	for (int i=0;i<3;i++) {
				a.rotate(10);
				System.out.println("rotate");
				a.forward(0.2);
				System.out.println("avance");
				a.stop();
				System.out.println("stop");
				Delay.msDelay(10000);
			}*/
		
		
		
	}
  
  
	public static float rechercheTournante (Actionneur a,EchoSensor es) {
		float trouver=es.getDistance(); //distance entre 0 et 1
		float trouver2;
		
		if (trouver==0) trouver=100;
		System.out.println("distance objet : "+trouver);
		while (trouver>1){
			a.rotate(5);
			a.stop();
	//		Delay.msDelay(10000);
			
			trouver=es.getDistance(); //distance entre 0 et 1
			System.out.println("distance objet : "+trouver);
			Delay.msDelay(1000);
		}
		System.out.println("l' objet est vu: "+trouver);
		
		return(rectifiePositionaAntiHorraire(a,es,trouver));
	//	rectifiePositionaHorraire(a,es,trouver);
	}
	
	
	public static float rectifiePositionaAntiHorraire (Actionneur a,EchoSensor es,float distance) {
		float trouver2;
		float trouverAV;
		do {
			trouverAV=distance;
			a.rotate(5);
			a.stop();
			trouver2=es.getDistance(); //distance entre 0 et 1 
			System.out.println("distance objet : "+trouver2);
			Delay.msDelay(1000);
			distance=trouver2;
		} while(trouver2<trouverAV);
		a.rotate(-5);
		return(trouver2);
	}
	
	public static void rectifiePositionaHorraire (Actionneur a,EchoSensor es,float distance) {
		float trouver2;
		float trouverAV;
		do {
			trouverAV=distance;
			a.rotate(-5);
			a.stop();
			trouver2=es.getDistance(); //distance entre 0 et 1 
			System.out.println("distance objet : "+trouver2);
			Delay.msDelay(1000);
			distance=trouver2;
		} while(trouver2<trouverAV);
		a.rotate(5);
	}
	
}