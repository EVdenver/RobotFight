import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Test {
	public static void main(String[] args) {
		
	}
  
  
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