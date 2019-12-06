import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;
//blabla
public class Actionneur {
	private  EV3LargeRegulatedMotor mLeftMotor;
	private  EV3LargeRegulatedMotor mRightMotor;
	private EV3MediumRegulatedMotor mPincesMotor;
	private boolean open=false;
	Delay d= new Delay();
	private final static int SPEED = 300; //degrees/sec
	private final static double WHEEL_RADIUS = 0.05; // en mètre 
	private final static int ROTATION_FACTOR=200; //facteur modulant la relation temps/vitesse/angle(en celcius) qui permet au robot de tourner sur son propre axe
	private boolean avance=true; //180
	
	public Actionneur(Port left_port, Port right_port, Port pinces_port) {
		mLeftMotor = new EV3LargeRegulatedMotor(left_port);
		mRightMotor = new EV3LargeRegulatedMotor(right_port);
		mPincesMotor = new EV3MediumRegulatedMotor(pinces_port);
		mLeftMotor.setSpeed(SPEED);
		mRightMotor.setSpeed(SPEED);
		mPincesMotor.setSpeed(10000);
		mLeftMotor.synchronizeWith(new RegulatedMotor[] {mRightMotor});
	}

	public void setSpeed(int v) {
		mLeftMotor.setSpeed(v);
		mRightMotor.setSpeed(v);
	}
	
	public void openPince() { //synchrone
		if(!open){
//		mPincesMotor.rotate(700); 
		mPincesMotor.rotate(800, true); // asynchrone
		open=true;
		}
		
	}
	
	public void closePince() { 
		if(open) {
			//mPincesMotor.rotate(-700);
			mPincesMotor.rotate(-800, true);
			open=false;
		}
		
	}

	public double backward(double distance) { //asynchrone
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
	public void forward(double distance) { // avancer de tel mètres asynchrone
		double radianByS = SPEED*0.017453292519943; //vitesse en radian/seconde
		double distanceByS=radianByS*WHEEL_RADIUS; // distance en mètre/seconde
		double time=distance/distanceByS;
		long timeStampBefore = System.currentTimeMillis();
		forward();
		Delay.msDelay((long) (time*1000)); // en ms		
		stop();
		long timeStampAfter = System.currentTimeMillis();
	//	return (timeStampBefore-timeStampAfter)*distanceByS*1000; // retourne la distance vraiment parcourue
	}
 
	public boolean backwardUntilStop() {
		while (avance) {
			backward(0.01);
		}
		return false;
	}
	
	public boolean forwardUntilStop() {
		while (avance) {
			forward(0.01);
		}
		return false;
	}
	
	public void stop() {
			mLeftMotor.startSynchronization();
	        mLeftMotor.stop();
	        mRightMotor.stop();
			mLeftMotor.endSynchronization();

	    }
	
	public boolean isMoving () {
		return (mLeftMotor.isMoving() && mRightMotor.isMoving());
	}
	

	public boolean rotate (double angle) { // 90 faire test, combien de temps = combien de degré
	double time=Math.abs(angle/SPEED*ROTATION_FACTOR);
	if (angle>0) rotateCounterClockwise();
	else rotateClockwise();
	Delay.msDelay((long) (time*10)); // en ms	problème calcul temp	
	stop();
	return true;	
	}


	void forward() {
		mLeftMotor.startSynchronization();
		mLeftMotor.forward();
		mRightMotor.forward();
		mLeftMotor.endSynchronization();
	}
	
	private void backward() {
		mLeftMotor.startSynchronization();
		mLeftMotor.backward();
		mRightMotor.backward();
		mLeftMotor.endSynchronization();
	}
	

	private void rotateClockwise() {
		mLeftMotor.startSynchronization();
	        mLeftMotor.forward();
	        mRightMotor.backward();
			mLeftMotor.endSynchronization();

	    }


	private void rotateCounterClockwise() {
		mLeftMotor.startSynchronization();
	        mLeftMotor.backward();
	        mRightMotor.forward();
			mLeftMotor.endSynchronization();
	    }

	public boolean isAvance() {
		return avance;
	}

	public void setAvance(boolean avance) {
		this.avance = avance;
	}
	
	

}
