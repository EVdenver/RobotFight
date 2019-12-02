import lejos.hardware.Button;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.utility.Delay;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import lejos.hardware.port.Port;

public class ColorimetrieSensor {
	private static Port port;
	private static EV3ColorSensor colorSensor;
	
	public ColorimetrieSensor(Port port ) {
		this.port=port;
		this.colorSensor=new EV3ColorSensor(port);
		
	}



	public void calibration() {
		try {
			Properties sauveur = new Properties();
			OutputStream out=new FileOutputStream("couleur"); 
			boolean again= true;



			SampleProvider average = new MeanFilter(colorSensor.getRGBMode(), 1);
			colorSensor.setFloodlight(Color.WHITE);

			System.out.println("Press enter to calibrate blue...");
			Button.ENTER.waitForPressAndRelease();
			float[] blue = new float[average.sampleSize()];
			average.fetchSample(blue, 0);
			sauveur.setProperty("Blue", blue[0]+ ","+ blue[1]+","+ blue[2]);


			System.out.println("Press enter to calibrate red...");
			Button.ENTER.waitForPressAndRelease();
			float[] red = new float[average.sampleSize()];
			average.fetchSample(red, 0);
			sauveur.setProperty("Red", red[0]+","+ red[1]+","+ red[2]);

			System.out.println("Press enter to calibrate yellow...");
			Button.ENTER.waitForPressAndRelease();
			float[] yellow = new float[average.sampleSize()];
			average.fetchSample(yellow, 0);
			sauveur.setProperty("Yellow", yellow[0]+","+ yellow[1]+","+ yellow[2]);

			System.out.println("Press enter to calibrate green...");
			Button.ENTER.waitForPressAndRelease();
			float[] green = new float[average.sampleSize()];
			average.fetchSample(green, 0);
			sauveur.setProperty("Green", green[0]+","+ green[1]+","+ green[2]);


			System.out.println("Press enter to calibrate black...");
			Button.ENTER.waitForPressAndRelease();
			float[] black = new float[average.sampleSize()];
			average.fetchSample(black, 0);
			System.out.println("Black calibrated");
			sauveur.setProperty("Black", black[0]+","+ black[1]+","+ black[2]);

			System.out.println("Press enter to calibrate white...");
			Button.ENTER.waitForPressAndRelease();
			float[] white = new float[average.sampleSize()];
			average.fetchSample(white, 0);
			sauveur.setProperty("White", white[0]+","+ white[1]+","+ white[2]);

			System.out.println("Press enter to calibrate grey...");
			Button.ENTER.waitForPressAndRelease();
			float[] grey = new float[average.sampleSize()];
			average.fetchSample(grey, 0);
			sauveur.setProperty("Grey", grey[0]+","+ grey[1]+","+ grey[2]);

			sauveur.store(out, "comments");
		} catch (Throwable t) {
			t.printStackTrace();
			Delay.msDelay(10000);
			System.exit(0);
		}
	}
	// auteur nicolas
	public static Properties getProperties() throws IOException {
		InputStream in= new FileInputStream("couleur");
		Properties sauveur= new Properties();
		sauveur.load(in);
		return sauveur;

	}
	//auteur nicolas
	public static double scalaire(float[] v1, float[] v2) {
		return Math.sqrt (Math.pow(v1[0] - v2[0], 2.0) +
				Math.pow(v1[1] - v2[1], 2.0) +
				Math.pow(v1[2] - v2[2], 2.0));
	}

	public static float [] getEch() {
		SampleProvider med= new MeanFilter(colorSensor.getRGBMode(),1);
		float[] flat= new float[med.sampleSize()];
		med.fetchSample(flat,0);
		return flat;
	}
// auteur nicolas
	public static String LaCouleur(float [] sample, Properties sauveur) throws FileNotFoundException, IOException {



		InputStream  in= new FileInputStream("couleur");
		sauveur.load(new FileInputStream("couleur"));

		float[] blue= new float[3];
		System.out.println(sauveur.getProperty("Blue"));
		String[] tab= sauveur.getProperty("Blue").split(",");
		blue[0]=Float.parseFloat(tab[0]);
		blue[1]=Float.parseFloat(tab[1]);
		blue[2]=Float.parseFloat(tab[2]);

		float[] red= new float[3];
		tab= sauveur.getProperty("Red").split(",");
		red[0]=Float.parseFloat(tab[0]);
		red[1]=Float.parseFloat(tab[1]);
		red[2]=Float.parseFloat(tab[2]);

		float[] yellow= new float[3];
		tab= sauveur.getProperty("Yellow").split(",");
		yellow[0]=Float.parseFloat(tab[0]);
		yellow[1]=Float.parseFloat(tab[1]);
		yellow[2]=Float.parseFloat(tab[2]);

		float[] green= new float[3];
		tab= sauveur.getProperty("Green").split(",");
		green[0]=Float.parseFloat(tab[0]);
		green[1]=Float.parseFloat(tab[1]);
		green[2]=Float.parseFloat(tab[2]);

		float[] black= new float[3];
		tab= sauveur.getProperty("Black").split(",");
		black[0]=Float.parseFloat(tab[0]);
		black[1]=Float.parseFloat(tab[1]);
		black[2]=Float.parseFloat(tab[2]);

		float[] white= new float[3];
		tab= sauveur.getProperty("White").split(",");
		white[0]=Float.parseFloat(tab[0]);
		white[1]=Float.parseFloat(tab[1]);
		white[2]=Float.parseFloat(tab[2]);

		float[] grey= new float[3];
		tab= sauveur.getProperty("Grey").split(",");
		grey[0]=Float.parseFloat(tab[0]);
		grey[1]=Float.parseFloat(tab[1]);
		grey[2]=Float.parseFloat(tab[2]);




		//System.out.println(sauveur.getProperty("Red"));

		double minscal = Double.MAX_VALUE;
		String color = "";

		double scalaire = TestColor.scalaire(sample, blue);
		//Button.ENTER.waitForPressAndRelease();
		//System.out.println(scalaire);
		if (scalaire < minscal) {
			minscal = scalaire;
			color = "blue";
		}

		scalaire = TestColor.scalaire(sample, red);
		//System.out.println(scalaire);
		//Button.ENTER.waitForPressAndRelease();
		if (scalaire < minscal) {
			minscal = scalaire;
			color = "red";
		}

		scalaire = TestColor.scalaire(sample, green);
		//System.out.println(scalaire);
		//Button.ENTER.waitForPressAndRelease();
		if (scalaire < minscal) {
			minscal = scalaire;
			color = "green";
		}

		scalaire = TestColor.scalaire(sample, black);
		//System.out.println(scalaire);
		//Button.ENTER.waitForPressAndRelease();
		if (scalaire < minscal) {
			minscal = scalaire;
			color = "black";
		}
		scalaire = TestColor.scalaire(sample, white);
		//System.out.println(scalaire);
		//Button.ENTER.waitForPressAndRelease();
		if (scalaire < minscal) {
			minscal = scalaire;
			color = "white";
		}
		scalaire = TestColor.scalaire(sample, grey);
		//System.out.println(scalaire);
		//Button.ENTER.waitForPressAndRelease();
		if (scalaire < minscal) {
			minscal = scalaire;
			color = "grey";
		}


		System.out.println("The color is " + color + " \n");

		in.close();
		return color;
	}
}
