import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Test {
	public static void main(String[] args) throws IOException {
		Properties sauveur= new Properties();
		OutputStream out= new FileOutputStream("couleur");
		float[] blue = new float[5];
		blue[0]=0;
		blue[1]=1;
		blue[2]=2;
		sauveur.setProperty("Blue", blue[0]+ ","+ blue[1]+","+ blue[2]);
		sauveur.store(out, "comments");
		sauveur.load(new FileInputStream("couleur"));
		//System.out.println(sauveur.getProperty("Blue"));
		
	}
	
	public String brunble (double sammple, double base)
	
	public static double scalaire(float[] v1, float[] v2) {
		return Math.sqrt (Math.pow(v1[0] - v2[0], 2.0) +
				Math.pow(v1[1] - v2[1], 2.0) +
				Math.pow(v1[2] - v2[2], 2.0));
	}
}
