package robot;

import java.util.ArrayList;
import ccre.rconf.RConfable;

import ccre.channel.FloatInput;
import ccre.cluck.Cluck;
import ccre.frc.FRC;
import ccre.instinct.InstinctMultiModule;
import ccre.rconf.RConf;
import ccre.rconf.RConf.Entry;
import ccre.timers.ExpirationTimer;
import ccre.tuning.TuningContext;

public class Autonomous {
    public static final TuningContext autoTuning = new TuningContext("AutonomousTuning").publishSavingEvent();
    
    public static final InstinctMultiModule mainModule = new InstinctMultiModule(autoTuning);

    public static void setup() {
        mainModule.publishDefaultControls(true, true);
        mainModule.publishRConfControls();
        
        Cluck.publishRConf("Diagnostics", new RConfable() {
        	public Entry[] queryRConf() throws InterruptedException {
                ArrayList<Entry> entries = new ArrayList<>();
                entries.add(RConf.title("Diagnostics"));
                entries.add(RConf.autoRefresh(1000));
                return entries.toArray(new Entry[entries.size()]);
        	}

			@Override
			public boolean signalRConf(int field, byte[] data) throws InterruptedException {
				// TODO Auto-generated method stub
				return false;
			}
        });
        
        mainModule.addMode(new AutonomousMode());
        mainModule.loadSettings(mainModule.addNullMode("none", "I'm a sitting chicken!"));
        FRC.registerAutonomous(mainModule);
        mainModule.setShouldBeRunning(FRC.robotEnabled().and(FRC.inAutonomousMode()));
    }
}
