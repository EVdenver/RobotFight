import lejos.hardware.Button;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.port.Port;
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



public class TestColor {

public static boolean goMessage() {
		
		GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
		g.clear();
		g.drawString("Color Sensor", 5, 0, 0);
		g.setFont(Font.getSmallFont());
		 
		g.drawString("The code for this sample     ", 2, 20, 0);
		g.drawString("shows how to work with the ", 2, 30, 0);
		g.drawString("Color Sensor ", 2, 40, 0);
		g.drawString("To run the ", 2, 60, 0);
		g.drawString("code one needs an EV3 ", 2, 70, 0);
		g.drawString("brick with a EV3 color sensor", 2, 80, 0); 
		g.drawString("attached to port 1.", 2, 90, 0);
		  
		// Quit GUI button:
		g.setFont(Font.getSmallFont()); // can also get specific size using Font.getFont()
		int y_quit = 100;
		int width_quit = 45;
		int height_quit = width_quit/2;
		int arc_diam = 6;
		g.drawString("QUIT", 9, y_quit+7, 0);
		g.drawLine(0, y_quit,  45, y_quit); // top line
		g.drawLine(0, y_quit,  0, y_quit+height_quit-arc_diam/2); // left line
		g.drawLine(width_quit, y_quit,  width_quit, y_quit+height_quit/2); // right line
		g.drawLine(0+arc_diam/2, y_quit+height_quit,  width_quit-10, y_quit+height_quit); // bottom line
		g.drawLine(width_quit-10, y_quit+height_quit, width_quit, y_quit+height_quit/2); // diagonal
		g.drawArc(0, y_quit+height_quit-arc_diam, arc_diam, arc_diam, 180, 90);
		
		// Enter GUI button:
		g.fillRect(width_quit+10, y_quit, height_quit, height_quit);
		g.drawString("GO", width_quit+15, y_quit+7, 0,true);
		
		Button.waitForAnyPress();
		if(Button.ESCAPE.isDown()) {
			return false;
		}
		else {
			g.clear();
			return true;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Properties sauveur = new Properties();
			OutputStream out=new FileOutputStream("couleur"); 
			
			boolean again = true;
			
			if (!goMessage()) System.exit(0);
			
			Port port = LocalEV3.get().getPort("S1");
			EV3ColorSensor colorSensor = new EV3ColorSensor(port);
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
			
			
			sauveur.store(out, "comments");
			
			while (again) {
				float[] sample = new float[average.sampleSize()];
				System.out.println("\nPress enter to detect a color...");
				Button.ENTER.waitForPressAndRelease();
				average.fetchSample(sample, 0);
				double minscal = Double.MAX_VALUE;
				String color = "";
				System.out.println(TestColor.LaCouleur(sample));
				/*
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
				scalaire = TestColor.scalaire(sample, yellow);
				//System.out.println(scalaire);
				//Button.ENTER.waitForPressAndRelease();
				if (scalaire < minscal) {
					minscal = scalaire;
					color = "yellow";
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
				
				System.out.println("The color is " + color + " \n");
				System.out.println("Press ENTER to continue \n");
				System.out.println("ESCAPE to exit");
				Button.waitForAnyPress();*/
				if(Button.ESCAPE.isDown()) {
					colorSensor.setFloodlight(false);
					again = false;
				}
			}
			
			
		} catch (Throwable t) {
			t.printStackTrace();
			Delay.msDelay(10000);
			System.exit(0);
		}
	}
	
	public static double scalaire(float[] v1, float[] v2) {
		return Math.sqrt (Math.pow(v1[0] - v2[0], 2.0) +
				Math.pow(v1[1] - v2[1], 2.0) +
				Math.pow(v1[2] - v2[2], 2.0));
	}
	
	public static String LaCouleur(float [] sample) throws FileNotFoundException, IOException {
	
		boolean again = true;
		while (again) {
			Properties sauveur= new Properties();
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
			
				
			
			
			
			System.out.println(sauveur.getProperty("Red"));
			
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
			
			System.out.println("The color is " + color + " \n");
			System.out.println("Press ENTER to continue \n");
			System.out.println("ESCAPE to exit");
			in.close();
			
			
		
		
		return color;
		
		}
		return "inconnu ou gris";
		
	}
	

}
