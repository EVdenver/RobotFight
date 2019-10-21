import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.utility.Delay;

public class Actionneur {

	private EV3LargeRegulatedMotor mLeftMotor;
	private EV3LargeRegulatedMotor mRightMotor;
	private EV3MediumRegulatedMotor mPincesMotor;
	private boolean open=false;
	private float basePosition=(float) 0.0;

	Delay d= new Delay();
	private final static int SPEED = 50; //degrees/sec
	private final static double WHEEL_RADIUS = 0.05; // en mètre 

	public Actionneur(Port left_port, Port right_port, Port pinces_port) {
		mLeftMotor = new EV3LargeRegulatedMotor(left_port);
		mRightMotor = new EV3LargeRegulatedMotor(right_port);
		mPincesMotor = new EV3MediumRegulatedMotor(pinces_port);
		mLeftMotor.setSpeed(SPEED);
		mRightMotor.setSpeed(SPEED);
		mPincesMotor.setSpeed(SPEED);
	}

	public void openPince() { // ou foreward calculer le nomre de tour jusqu'à ouverte
		if(!open){
		mPincesMotor.rotate(700); // positif ouvert
		open=true;
		}
		
	}
	
	public void closePince() { // ou forward
		if(open) {
			mPincesMotor.rotate(-700);
			open=false;
		}
		
	}
	
	
	
	public void forward() {
		mLeftMotor.forward();
		mRightMotor.forward();
	}
	
	public void backward() {
		mLeftMotor.backward();
		mRightMotor.backward();
	}
	
	public double backward(double distance) {
		double radianByS = SPEED*0.017453292519943; //vitesse en radian/seconde
		double distanceByS=radianByS*WHEEL_RADIUS; // distance en mètre/seconde
		double time=distance/distanceByS;
		long timeStampBefore = System.currentTimeMillis();
		backward();
		Delay.msDelay((long) (time*1000)); // en ms		
		stop();
		long timeStampAfter = System.currentTimeMillis();
		return (timeStampBefore-timeStampAfter)*distanceByS*1000; // retourne la distance vraiment parcourue
	}
	
	public double forward(double distance) { // avancer de tel mètres
		double radianByS = SPEED*0.017453292519943; //vitesse en radian/seconde
		double distanceByS=radianByS*WHEEL_RADIUS; // distance en mètre/seconde
		double time=distance/distanceByS;
		long timeStampBefore = System.currentTimeMillis();
		forward();
		Delay.msDelay((long) (time*1000)); // en ms		
		stop();
		long timeStampAfter = System.currentTimeMillis();
		return (timeStampBefore-timeStampAfter)*distanceByS*1000; // retourne la distance vraiment parcourue
	}

	public void stop() {
	        mLeftMotor.stop();
	        mRightMotor.stop();
	    }

	public int rotate (double angle) { // 90 faire test, combien de temps = combien de degré
	double time=angle/SPEED*222;
	long timeStampBefore = System.currentTimeMillis();
	if (angle>0) rotateCounterClockwise();
	else rotateClockwise();
	Delay.msDelay((long) (time*1000)); // en ms		
	stop();
	long timeStampAfter = System.currentTimeMillis();
	return 0;	
	}

	    public void rotateClockwise() {
	        mLeftMotor.forward();
	        mRightMotor.backward();
	    }


	    public void rotateCounterClockwise() {
	        mLeftMotor.backward();
	        mRightMotor.forward();
	    }

}
