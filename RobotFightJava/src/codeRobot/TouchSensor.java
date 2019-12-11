package codeRobot;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;

/**
 * @author Vincent
 * La classe gerant le capteur de pression du robot
 */
public class TouchSensor extends EV3TouchSensor {

	//Constructor
	/**
	 * @author Vincent
	 * @param port le port sur lequel est branche le capteur
	 * Constructeur de la classe
	 */
	public TouchSensor(Port port)
	{
		super(port);
	}

	/**
	 * @author Vincent
	 * @return true si le capteur est en position basse, false sinon
	 */
	public boolean isPressed()
	{
		float[] sample = new float[1];
		fetchSample(sample, 0);
		return sample[0] != 0;
	}
}
