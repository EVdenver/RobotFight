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
		float[] red= new float[5];
		blue[0]=0;
		blue[1]=1;
		blue[2]=2;
		red[0]=3;
		red[1]=4;
		red[2]=5;
		sauveur.setProperty("Blue", blue[0]+ ","+ blue[1]+","+ blue[2]);
		sauveur.setProperty("Red", red[0]+ ","+ red[1]+","+ red[2]);
		sauveur.store(out, "comments");
		sauveur.load(new FileInputStream("couleur"));
		System.out.println(sauveur.getProperty("Red"));
		float r0=Float.parseFloat((sauveur.getProperty("Red")).substring(0,3));
		float r1=Float.parseFloat((sauveur.getProperty("Red")).substring(4,7));
		float r2=Float.parseFloat((sauveur.getProperty("Red")).substring(8,11));
		System.out.println("r0= "+r0);
		System.out.println("r1= "+r1);
		System.out.println("r2= "+r2);
		TestColor col= new TestColor();
	
	}
	
	//public String brunble (double sammple, double base) {
		
	//}
	
	public static double scalaire(float[] v1, float[] v2) {
		return Math.sqrt (Math.pow(v1[0] - v2[0], 2.0) +
				Math.pow(v1[1] - v2[1], 2.0) +
				Math.pow(v1[2] - v2[2], 2.0));
	}
}
