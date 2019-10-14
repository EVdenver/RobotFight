import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.hardware.port.Port;

public class EchoSensor extends EV3UltrasonicSensor {
	
	public EchoSensor(Port port) {
		super(port);
	}
	
	public float getDistance() {
		SampleProvider distance = this.getMode("Distance");
		float[] sample = new float[distance.sampleSize()];
		distance.fetchSample(sample, 0);
		return sample[0];
	}
	
}
