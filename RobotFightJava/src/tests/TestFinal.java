package tests;
import java.io.IOException;

import codeRobot.Actionneur;
import codeRobot.Agent;
import codeRobot.Boussole;
import codeRobot.Carte;
import codeRobot.ColorimetrieSensor;
import codeRobot.EchoSensor;
import codeRobot.TouchSensor;

import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

public class TestFinal {
	

	public static void main(String[] args) throws IOException {
		Agent robot=new Agent(new Actionneur(MotorPort.C, MotorPort.A, MotorPort.B),new EchoSensor (SensorPort.S3),new TouchSensor(SensorPort.S2), new ColorimetrieSensor(SensorPort.S1),new Boussole(0),new Carte(0,180));

		//Scenario de calibration des méthodes pour avancer ou tourner avec un paramètre de distance ou d’angle
		robot.getA().forward(1);
		robot.getA().backward(1);
		robot.getA().forward(0.5);
		robot.getA().backward(0.5);
		robot.getA().rotate(90);
		robot.getA().rotate(15);
		robot.getA().rotate(180);
		robot.getA().rotate(90);
		robot.getA().rotate(-15);
		robot.getA().rotate(-180);
		robot.getA().rotate(360);

		//Scenario de recherche circulaire
		robot.getA().forward(0.1);
		robot.rechercheTournante();
		
		// Scenario de calibration face au mur
			robot.getA().forward();
			while(!robot.isMur()) {
				System.out.println("avance");
			}
	//	robot.recalibrageMurNordSud(); cette méthode est en commentaire
		
		// Scenario de mise de but
		robot.setEtat(5);
		while(!Button.ESCAPE.isDown()) {
		robot.recherchePrincipale();}
		
		//Scenario de mesure de distance parcourue
		robot.getA().forward(2);
		System.out.println(robot.getA().getCheminParcouru());
		robot.getChrono().start();
		robot.getA().forward();
		Delay.msDelay(3_000);
		robot.getChrono().stop();
		robot.getA().addParcour(robot.getChrono().getDureeSec()); 
		System.out.println(robot.getA().getCheminParcouru());
		
		//Scenario de début d'automate
		robot.debutAutomate();
		
		//Scenario de détection du palet en avançant
		robot.avanceVersPalet();
		
		//Scenario de retourn vers la base ennemie
		robot.mettreUnBut();
		
		//Scenario de détection du capteur de pression
		Agent.setVitesseavancer(500);;
		robot.fonceUntilPush();
		
		//Scenario de repositionnement
		robot.rectifiePosition(1);
		robot.rectifiePosition(-1);
		
		//Scenario de rotation
		robot.tourner(15);
		robot.tourner(-15);
		robot.tourner(180);
		robot.tourner(270);
		robot.tourner(361);
		
		//Scenario d'arrêt devant obstacle
		robot.getA().forward(1);
		robot.isMur();
		robot.isLigneBlanche();
		
	}
}
