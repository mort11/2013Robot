package org.mort11.sensors;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import org.mort11.util.SensorConstants;

/**
 *
 * @author Ryan
 */
public class SensorDealer {

    private Encoder shootEncoder;
    private Encoder leftDT;
    private Encoder rightDT;
    private AnalogChannel shooterPot;
    private AnalogChannel spatulaPot;
    private LEDs lights;
    private Jumper autoDriveJumper;
    private Jumper rightSideJumper;
    private static SensorDealer instance = null;

    public static SensorDealer getInstance() {
        if (instance == null) {
            instance = new SensorDealer();
        }
        return instance;
    }

    private SensorDealer() {
        shootEncoder = new Encoder(SensorConstants.SHOOTER_ENCODER_CHANNEL_A,
                SensorConstants.SHOOTER_ENCODER_CHANNEL_B, true, Encoder.EncodingType.k4X);
        leftDT = new Encoder(SensorConstants.LEFT_ENCODER_CHANNEL_A,
                SensorConstants.LEFT_ENCODER_CHANNEL_B, false, Encoder.EncodingType.k4X);
        rightDT = new Encoder(SensorConstants.RIGHT_ENCODER_CHANNEL_A,
                SensorConstants.RIGHT_ENCODER_CHANNEL_B, true, Encoder.EncodingType.k4X);
        shooterPot = new AnalogChannel(SensorConstants.SHOOTER_POT_PORT);
        spatulaPot = new AnalogChannel(SensorConstants.SPATULA_POT_PORT);
        lights = new LEDs();
//        autoDriveJumper = new Jumper(14);
//        rightSideJumper = new Jumper(13);
    }

//    public Jumper getAutoDriveJumper(){
//        return autoDriveJumper;
//    }
//    public Jumper getRightSideJumper(){
//        return rightSideJumper;
//    }


    public Encoder getShooterEncoder() {
        return shootEncoder;
    }

    public Encoder getLeftEncoder() {
        return leftDT;
    }

    public Encoder getRightEncoder() {
        return rightDT;
    }

    public AnalogChannel getShooterPot() {
        return shooterPot;
    }

    public AnalogChannel getSpatulaPot() {
        return spatulaPot;
    }

    public LEDs getLEDs() {
        return lights;
    }
}
