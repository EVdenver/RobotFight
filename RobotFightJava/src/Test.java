import lejos.hardware.*;
public class Test {
	
	public static void main(String[] args) {
		Actionneur a = new Actionneur(MotorPort.C, MotorPort.A, MotorPort.B) ;

		EchoSensor es= new EchoSensor (SensorPort.S3);
	//	test.forwardUntilStop();
		
	//	test.rotate(-90);
	//	test.openPince();
	//	test.forward(0.2);
		rechercheTournante (a,es);
		a.forward(1);
		float f=es.getDistance();
	//	System.out.println("distance objet : "+f);
	//	Delay.msDelay(10_000);
	
	}
	
//0.086 devant pinces
	

	public static void rechercheTournante (Actionneur a,EchoSensor es) {
		float trouver=es.getDistance(); //distance entre 0 et 1
		System.out.println("distance objet : "+trouver);
		Delay.msDelay(10000);
		while (trouver<0.23){
			a.rotate(10);
			trouver=es.getDistance(); //distance entre 0 et 1
			System.out.println("distance objet : "+trouver);
			Delay.msDelay(10000);
		}
	}


}
