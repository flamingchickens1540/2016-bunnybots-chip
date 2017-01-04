package robot;

import ccre.channel.FloatOutput;
import ccre.frc.FRC;
import ccre.instinct.AutonomousModeOverException;
import ccre.instinct.InstinctModeModule;
import ccre.tuning.TuningContext;

public class AutonomousMode extends InstinctModeModule {
	
	private static final FloatOutput driveMotors = ChipBunnyBot.driveLeft.combine(ChipBunnyBot.driveRight);
	private static final FloatOutput shootMotors = ChipBunnyBot.shooterMotorRear.combine(ChipBunnyBot.shooterMotorFront);
	
	public AutonomousMode() {
		super("Move");
	}

	public void loadSettings(TuningContext context) {
	}
	
	FRC.registerAutonomous(Autonomus.mainModule {
		@Override
		protected void autonomousMain() throws InterruptedException, AutonomousModeOverException {
			try {
				runAutonomus();
			}
			finally {
				driveMotors.set(0f);
				shootMotors.set(0f);
			}
		}
	});
	
	protected void runAutonomus() throws InterruptedException, AutonomousModeOverException {
		driveMotors.set(0.5f);
		waitForTime(1500);
		driveMotors.set(0f);
	}
	
}
