import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.hardware.port.Port;

/**
 * @author Vincent
 * La classe qui gere la gestion de la distance du robot via le capteur ultrason
 */
public class EchoSensor extends EV3UltrasonicSensor {

	//Constructor
	/**
	 * @author Vincent
	 * @param port le port sur lequel est branche le capteur
	 * Constructeur de la classe
	 */
	public EchoSensor(Port port) {
		super(port);
	}

	/**
	 * @author Vincent
	 * @return la distance en metre entre l'obstacle present devant le robot et le robot
	 */
	public float getDistance() {
		SampleProvider distance = this.getMode("Distance");
		float[] sample = new float[distance.sampleSize()];
		distance.fetchSample(sample, 0);
		return sample[0];
	}
}
