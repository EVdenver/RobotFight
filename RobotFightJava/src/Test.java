import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Test {
	public static void main(String[] args) {
		TouchSensor uTouch = new TouchSensor(SensorPort.S2);
		boolean state = false;
		EV3MediumRegulatedMotor grab = new EV3MediumRegulatedMotor(MotorPort.B);
		DifferentialDrive d = new DifferentialDrive(MotorPort.C, MotorPort.A);
		EchoSensor s = new EchoSensor(SensorPort.S3);
		s.enable();
		Delay.msDelay(5000);
		s.disable();
		s.enable();
		SampleProvider distance = s.getMode("Distance");
		float[] sample = new float[distance.sampleSize()];
		int i = 0;
		while (i <= 10) {
			distance.fetchSample(sample, 0);
			Delay.msDelay(5000);
			i++;
		}
		for (int j = 0; j < sample.length; j++) {
			System.out.println(sample[j]+" m;");
		}
		Delay.msDelay(5000);
		s.disable();
		/*while(! uTouch.isPressed()) {
			d.forward();
		}
		d.stop();
		state = true;
		while (state) {
			grab.backward();
			if (! uTouch.isPressed()) {
				state = false;
				grab.stop();
			}
		}
		System.out.println(grab.getPosition()+"");
		Delay.msDelay(10000);
		grab.rotate(700);
		System.out.println(grab.getPosition()+"");
		Delay.msDelay(10000);
		d.rotateClockwise();
		Delay.msDelay(4000);*/
	}
}
