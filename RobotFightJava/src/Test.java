import java.util.Properties;

import lejos.hardware.Button;
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
		
		
		// nicolas asynchronisation
		while (Button.ENTER.isUp()) {
			Properties p= recalibrationColor.getProperties();
			System.out.println((recalibrationColor.LaCouleur(recalibrationColor.getEch(), p));
		}
	}
	
}
