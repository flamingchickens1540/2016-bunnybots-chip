package robot;

import ccre.channel.BooleanCell;
import ccre.channel.BooleanInput;
import ccre.channel.EventCell;
import ccre.channel.FloatCell;
import ccre.channel.FloatInput;
import ccre.channel.FloatOutput;
import ccre.cluck.Cluck;
import ccre.ctrl.ExtendedMotorFailureException;
import ccre.frc.FRC;
import ccre.frc.FRCApplication;
import ccre.timers.PauseTimer;

/**
 * This is the core class of a CCRE project. The CCRE launching system will make
 * sure that this class is loaded, and will have set up everything else before
 * loading it. If you change the name, use Eclipse's rename functionality. If
 * you don't, you will have to change the name in Deployment.java.
 *
 * Make sure to set {@link #TEAM_NUMBER} to your team number.
 */
public class ChipBunnyBot implements FRCApplication {

    /**
     * This is where you specify your team number. It is used to find your
     * roboRIO when you download code.
     */
    public static final int TEAM_NUMBER = 1540;
    
    static float rampingLimit;
    static float shooterRamping;
    static float deadzone;
    static FloatOutput driveLeft;
    static FloatOutput driveRight;
    static FloatOutput shooterMotorRear;
    static FloatOutput shooterMotorFront;
    
    static {
	    
	    final float rampingLimit = 0.1f;
		final float shooterRamping = 0.1f;
		final float deadzone = 0.2f;
	    
		try {
		    driveLeft = FRC.talonCAN(2).simpleControl().combine(FRC.talonCAN(1).simpleControl()).negate()
		    		.addRamping(rampingLimit, FRC.constantPeriodic).outputDeadzone(deadzone);
		    driveRight = FRC.talonCAN(8).simpleControl().combine(FRC.talonCAN(3).simpleControl())
		    		.addRamping(rampingLimit, FRC.constantPeriodic).outputDeadzone(deadzone);
		    shooterMotorRear = FRC.talonCAN(5).simpleControl();
		    shooterMotorFront  = FRC.talonCAN(6).simpleControl().addRamping(shooterRamping, FRC.constantPeriodic);
		}
		catch(ExtendedMotorFailureException exeption) {
			
		}
		finally {
			
		}
		
    }
    
    @Override
    public void setupRobot() throws ExtendedMotorFailureException {
//    	float rampingLimit = 0.1f;
//    	float shooterRamping = 0.1f;
//    	float deadzone = 0.2f;
//    	
//    	FloatOutput driveMotorLeftR2D2 = FRC.talonCAN(2).simpleControl().negate().addRamping(rampingLimit, FRC.constantPeriodic).outputDeadzone(deadzone);
//    	FloatOutput driveMotorLeftR2KT = FRC.talonCAN(1).simpleControl().negate().addRamping(rampingLimit, FRC.constantPeriodic).outputDeadzone(deadzone);
//    	
//    	FloatOutput driveMotorRightBB8 = FRC.talonCAN(8).simpleControl().addRamping(rampingLimit, FRC.constantPeriodic).outputDeadzone(deadzone);
//    	FloatOutput driveMotorRightC3PO = FRC.talonCAN(3).simpleControl().addRamping(rampingLimit, FRC.constantPeriodic).outputDeadzone(deadzone);
//    	
//    	driveLeft = driveMotorLeftR2D2.combine(driveMotorLeftR2KT);
//    	driveRight = driveMotorRightBB8.combine(driveMotorRightC3PO);
//    	
//    	shooterMotorRear = FRC.talonCAN(5).simpleControl();
//    	shooterMotorFront = FRC.talonCAN(6).simpleControl().addRamping(shooterRamping, FRC.constantPeriodic);
//    	
    	///////////////////////////////////////////////////////////////////////////////////
    	
    	FloatInput axisLeft = FRC.joystick1.axis(2);
    	FloatInput axisRight = FRC.joystick1.axis(6);
    	FloatInput triggerRightDriver = FRC.joystick1.axis(4);
    	BooleanInput lbButtonDriver = FRC.joystick1.button(5);
    	BooleanInput rbButtonDriver = FRC.joystick1.button(6);
    	
    	///////////////////////////////////////////////////////////////////////////////////
    	
    	FloatInput y = axisLeft.plus(axisRight).dividedBy(2f);
    	FloatInput x = axisLeft.minus(axisRight).dividedBy(2f).multipliedBy(0.24f).deadzone(0.12f);
    	
    	FloatInput l = y.plus(x);
    	FloatInput r = y.minus(x);
    	
    	l.send(driveLeft);
    	r.send(driveRight);
    	
    	///////////////////////////////////////////////////////////////////////////////////
    	
    	float s = 0.5f;
    	
    	lbButtonDriver.toFloat(0f, s).send(driveLeft.combine(driveRight));
    	rbButtonDriver.toFloat(0f, s).send(driveLeft.negate().combine(driveRight).negate());
    	
    	PauseTimer spinupTimer = new PauseTimer(500);
    	PauseTimer shootTimer = new PauseTimer(250);
    	PauseTimer coolDownTimer = new PauseTimer(2000);
    	BooleanCell disableShooting = new BooleanCell();
    	triggerRightDriver.atLeast(0.5f).onPress().andNot(disableShooting).send(
    			spinupTimer.combine(() -> {shooterMotorFront.set(0.6f); disableShooting.set(true);}));
    	spinupTimer.triggerAtEnd(shootTimer.combine(() -> {shooterMotorFront.set(0f); shooterMotorRear.set(1f);}));
    	shootTimer.triggerAtEnd(coolDownTimer.combine(() -> shooterMotorRear.set(0f)));
    	coolDownTimer.triggerAtEnd(() -> disableShooting.set(false));
    	
    	///////////////////////////////////////////////////////////////////////////////////
    	
    	Cluck.publish("Y Axis Left Input", axisLeft);
    	Cluck.publish("Y Axis Right Input", axisRight);
    	Cluck.publish("Drive Left Output", driveLeft);
    	Cluck.publish("Drive Right Output", driveRight);
    	
    	Cluck.publish("Left Bumper Input", lbButtonDriver);
    	Cluck.publish("Right Bumper Input", rbButtonDriver);
    	
    	Cluck.publish("Shooter Rear Output", shooterMotorRear);
    	Cluck.publish("Shooter Front Output", shooterMotorFront);
    	Cluck.publish("Trigger Right Input", triggerRightDriver);
    	Cluck.publish("Disable Shooting", disableShooting);
    	
    }
    
}
