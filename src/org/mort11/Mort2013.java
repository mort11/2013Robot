package org.mort11;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;

import org.mort11.dt.DriveTrain;
import org.mort11.ee.EndEffector;
import org.mort11.sensors.LEDs;
import org.mort11.sensors.LEDs.Color;
import org.mort11.sensors.SensorDealer;
import org.mort11.util.AutonomousConstants;
import org.mort11.util.DriveTrainConstants;
import org.mort11.util.EndEffectorConstants;
import org.mort11.util.TeleopConstants;

/*
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Mort2013 extends SimpleRobot {

    SensorDealer dealer;
    Joystick dtLeftJoy, dtRightJoy, eeJoy;
    EndEffector ee;
    DriveTrain dt;
    LEDs lights;
    Timer looptimer;
    double desiredShooterAngle = 0;
    double shootSpeed = 0;
    boolean shooterCycled = true;
    boolean brakeModePressed = false;
    boolean changePPressed = false;
    boolean changeIPressed = false;
    boolean wasTriggerPressed = false;
    boolean speedControlled = false;
    boolean spinning = false;
    boolean spinButtonPressed = false;
    boolean overridePressed = false;
    boolean override = false;
    boolean doneAuto = false;
    boolean shooterElevationManual = false;
    Encoder leftEnc;
    Encoder rightEnc;
    boolean wasHangToggle = false;
    boolean hangExtended = false;

    public void robotInit() {
        dtLeftJoy = new Joystick(1);
        dtRightJoy = new Joystick(2);
        eeJoy = new Joystick(3);
        ee = new EndEffector();
        dt = new DriveTrain();

        dealer = SensorDealer.getInstance();
        lights = dealer.getLEDs();
        leftEnc = dealer.getLeftEncoder();
        rightEnc = dealer.getRightEncoder();
        looptimer = new Timer();
    }

    public void disabled() {
        doneAuto = false;
        override = false;
        System.out.println("DISABLED");
    }
    /*
     * This function is called once each time the robot enters autonomous mode.
     */

    public void autonomous() {
        if (isSystemActive() && !doneAuto) {
            dt.drive(0, 0);
            doneAuto = true;
            ee.setShootPercent(1);
            while (!ee.cycleShooterElevation(AutonomousConstants.ELEVATION_PRESET_AUTON) && isAutonomous()) {
                dt.drive(0, 0);

                System.out.println("rising");
            }
            System.out.println("done rising");
            ee.setShoot(true, 1);
            dt.drive(0, 0);
            Timer.delay(AutonomousConstants.SPIN_UP_TIME);
            for (int i = 0; i < 4 && isAutonomous(); i++) {
                ee.load();
                lights.setLight(Color.Orange);
                Timer.delay(AutonomousConstants.LOAD_TIME);
                ee.dontLoad();
                lights.setLight(Color.Blue);
                Timer.delay(AutonomousConstants.BETWEEN_TIME);
            }
            ee.setShoot(false, 0);
            leftEnc.reset();
            rightEnc.reset();
            lights.setLight(Color.Red);
            //drive back to line
            boolean backdriving = true;
            while (backdriving && isAutonomous()) {
                double leftSpeed = -AutonomousConstants.DRIVE_BACKWARDS_SPEED_FPS;
                double rightSpeed = -AutonomousConstants.DRIVE_BACKWARDS_SPEED_FPS;
                if (leftEnc.getDistance() <= -AutonomousConstants.DRIVE_BACKWARDS_DISTANCE_FEET) {
                    leftSpeed = 0;
                }
                if (rightEnc.getDistance() <= -AutonomousConstants.DRIVE_BACKWARDS_DISTANCE_FEET) {
                    rightSpeed = 0;
                }
                System.out.println(rightEnc.getDistance() + " " + leftEnc.getDistance());
                dt.driveFPS(leftSpeed, rightSpeed);
                if (leftSpeed == 0 && rightSpeed == 0) {
                    backdriving = false;
                }
            }
        }
        while (isAutonomous()) {
            dt.driveFPS(0, 0);
        }
        if (this.isDisabled()) {
            disabled();
            lights.setLight(Color.Orange);
        }
    }

    /*
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        looptimer.start();
        lights.setLight(Color.Off);
        desiredShooterAngle = ee.getShooterAngle();
        shooterCycled = false;
        while (isOperatorControl()) {
//            if(dtLeftJoy.getRawButton(8) && !ledpressed){
//                ledpressed=true;
//                ledstate+=1;
//                if(ledstate ==8 ){
//                    ledstate = 0;
//                }
//            }else if(!dtLeftJoy.getRawButton(8) && ledpressed){
//                ledpressed = false;
//            }
//            switch(ledstate){
//                case 0:
//                    lights.setLight(Color.Off);
//                    break;
//                case 1:
//                    lights.setLight(Color.Red);
//                    break;
//                case 2:
//                    lights.setLight(Color.Green);
//                    break;
//                case 3:
//                    lights.setLight(Color.Blue);
//                    break;
//                case 4:
//                    lights.setLight(Color.Yellow);
//                    break;
//                case 5:
//                    lights.setLight(Color.Orange);
//                    break;
//                case 6:
//                    lights.setLight(Color.White);
//                    break;
//                    
//            }
//            System.out.println("LIGHT = " + lights.currentColor.name);

            //determines percent of shoot speed
            ee.setShootPercent(((-eeJoy.getThrottle() + 1) / 2));

//                        if (dtLeftJoy.getRawButton(9) && !switchDrivePressed) {
//                            switchDrivePressed = true;
//                            speedControlled = !speedControlled;
//                        } else if (!dtLeftJoy.getRawButton(9) && switchDrivePressed) {
//                            switchDrivePressed = false;
//                        }

            if (eeJoy.getRawButton(7) && !overridePressed) {
                overridePressed = true;
                override = !override;
            } else if (!eeJoy.getRawButton(7) && overridePressed) {
                overridePressed = false;
            }
            ee.setShooterOverride(override);
            if (override) {
                lights.setLight(Color.Orange);
            }

            //cycling shooter
            if (eeJoy.getRawButton(TeleopConstants.PRESET_LOADING_BUTTON)) {
                shooterCycled = false;
                desiredShooterAngle = EndEffectorConstants.ELEVATION_PRESET_LOADING;
            }
            if (eeJoy.getRawButton(TeleopConstants.PRESET_BACK_LEFT_BUTTON)) {
                shooterCycled = false;
                desiredShooterAngle = EndEffectorConstants.ELEVATION_PRESET_BACK_LEFT_CORNER;
            }
            if (eeJoy.getRawButton(TeleopConstants.PRESET_BACK_BUTTON)) {
                shooterCycled = false;
                if (hangExtended) {
                    desiredShooterAngle = EndEffectorConstants.ELEVATION_PRESET_HANGING;
                } else {
                    desiredShooterAngle = EndEffectorConstants.ELEVATION_PRESET_BACK;
                }
            }
            if (eeJoy.getRawButton(TeleopConstants.PRESET_BACK_RIGHT_BUTTON)) {
                shooterCycled = false;
                desiredShooterAngle = EndEffectorConstants.ELEVATION_PRESET_BACK_RIGHT_CORNER;
            }
            if (eeJoy.getRawButton(TeleopConstants.PRESET_FRONT_BUTTON)) {
                shooterCycled = false;
                desiredShooterAngle = EndEffectorConstants.ELEVATION_PRESET_FRONT;
            }
            double speedVal = doThreshhold(-eeJoy.getY());
            double reducedSpeedVal = doThreshhold(-eeJoy.getRawAxis(6));
            if (speedVal != 0 || reducedSpeedVal != 0) {
                lights.setLight(Color.Yellow);
                shooterCycled = true;
            }

            boolean climbToggle = (dtRightJoy.getRawButton(2) || dtLeftJoy.getRawButton(2) || eeJoy.getRawButton(4));
            if (climbToggle && !wasHangToggle) {
                wasHangToggle = true;
                ee.setHangerState(!hangExtended);
                hangExtended = !hangExtended;
            } else if (!climbToggle && wasHangToggle) {
                wasHangToggle = false;
            }

            if (!shooterCycled) {
                if (ee.cycleShooterElevation(desiredShooterAngle)) {
                    lights.setLight(Color.Blue);
                    shooterCycled = true;
                } else {
                    lights.setLight(Color.Red);
                }
            } else {
                ee.manualShooterElevation(-(speedVal + reducedSpeedVal * .2));
            }


            double leftVal = doThreshhold(-dtLeftJoy.getY());
            double rightVal = doThreshhold(-dtRightJoy.getY());
            if (dtLeftJoy.getTrigger() && !wasTriggerPressed) {
                wasTriggerPressed = true;
                speedControlled = !speedControlled;
            } else if (!dtLeftJoy.getTrigger() && wasTriggerPressed) {
                wasTriggerPressed = false;
            }

            if (false && speedControlled) {
                dt.driveFPS(leftVal * DriveTrainConstants.MAX_FPS,
                            rightVal * DriveTrainConstants.MAX_FPS);
            } else {
                dt.drive(leftVal, rightVal);
            }
            if (eeJoy.getRawButton(TeleopConstants.SPIN_UP_BUTTON) && !spinButtonPressed) {
                spinning = !spinning;
                shootSpeed = 1;
                spinButtonPressed = true;
            } else if (!eeJoy.getRawButton(TeleopConstants.SPIN_UP_BUTTON) && spinButtonPressed) {
                spinButtonPressed = false;
            }
            if (spinning) {
                lights.setLight(Color.Green);
                ee.setShoot(true, shootSpeed);
                if (eeJoy.getTrigger()) {
                    ee.load();
                } else {
                    ee.dontLoad();
                }
            } else {
                if (lights.currentColor == Color.Green) {
                    lights.setLight(Color.Off);
                }
                ee.setShoot(false, 0);
                if (eeJoy.getRawButton(TeleopConstants.REVERSE_FLICKER_BUTTON)) {
                    ee.reverseFlicker();
                } else {
                    ee.dontLoad();
                }
            }

            //You know what this is
            if (dtRightJoy.getRawButton(6) && dtRightJoy.getRawButton(7)
                && dtRightJoy.getRawButton(8) && eeJoy.getTrigger()) {
                spin();
            }

            //change braking mode
//            if (dtLeftJoy.getRawButton(TeleopConstants.BRAKE_ASSIST_BUTTON) && !brakeModePressed) {
//                dt.setBrakingMode(BrakingMode.kBrakeAssist);
//                brakeModePressed = true;
//            } else if (dtLeftJoy.getRawButton(TeleopConstants.COAST_BUTTON) && !brakeModePressed) {
//                dt.setBrakingMode(BrakingMode.kCoast);
//                brakeModePressed = true;
//            } else if (dtLeftJoy.getRawButton(TeleopConstants.COAST_ASSIST_BUTTON) && !brakeModePressed) {
//                dt.setBrakingMode(BrakingMode.kCoastAssist);
//                brakeModePressed = true;
//            } else if (!dtLeftJoy.getRawButton(TeleopConstants.BRAKE_ASSIST_BUTTON) && !dtLeftJoy.getRawButton(TeleopConstants.COAST_BUTTON) && !dtLeftJoy.getRawButton(TeleopConstants.COAST_ASSIST_BUTTON) && brakeModePressed) {
//                brakeModePressed = false;
//            }

            //Alter the P and I constants for tuning
            if (dtRightJoy.getRawButton(TeleopConstants.P_UP_BUTTON) && !changePPressed) {
                changePPressed = true;
                dt.setP_CONSTANT(dt.getP_CONSTANT() + TeleopConstants.TUNING_DELTA_P);
            } else if (dtRightJoy.getRawButton(TeleopConstants.P_DOWN_BUTTON) && !changePPressed) {
                changePPressed = true;
                dt.setP_CONSTANT(dt.getP_CONSTANT() - TeleopConstants.TUNING_DELTA_P);
            } else if (!dtRightJoy.getRawButton(TeleopConstants.P_UP_BUTTON) && !dtRightJoy.getRawButton(TeleopConstants.P_DOWN_BUTTON) && changePPressed) {
                changePPressed = false;
            }
            if (dtRightJoy.getRawButton(TeleopConstants.I_UP_BUTTON) && !changeIPressed) {
                changeIPressed = true;
                dt.setI_CONSTANT(dt.getI_CONSTANT() + TeleopConstants.TUNING_DELTA_I);
            } else if (dtRightJoy.getRawButton(TeleopConstants.I_DOWN_BUTTON) && !changeIPressed) {
                changeIPressed = true;
                dt.setI_CONSTANT(dt.getI_CONSTANT() - TeleopConstants.TUNING_DELTA_I);
            } else if (!dtRightJoy.getRawButton(TeleopConstants.I_UP_BUTTON) && !dtRightJoy.getRawButton(TeleopConstants.I_DOWN_BUTTON) && changeIPressed) {
                changeIPressed = false;
            }

            System.out.println("SHOOTER = " + ee.getShooterAngle());
//            System.out.println("VALS "+leftVal + " " + rightVal);
//                        System.out.println("FPS DESIRED = " +
//                                leftVal * DriveTrainConstants.MAX_FPS + ", " +
//                                rightVal * DriveTrainConstants.MAX_FPS);
//            System.out.println("FPS ACHIEVED = " +
//                    dealer.getLeftEncoder().getRate() + ", " +
//                    dealer.getRightEncoder().getRate() );
//                        System.out.println("P = " + dt.getP_CONSTANT() + ", Ti = " + dt.getI_CONSTANT());
//                        System.out.println(looptimer.get() +" seconds elapsed");
            looptimer.reset();
        }
    }

    /**
     * Threshold the input to create a deadband
     * <p/>
     * @param input Raw Joystick Value
     * @return 0 if input is within deadband, otherwise a scaled value
     * representing distance outside the deadband
     */
    public double doThreshhold(double input) {
        if (Math.abs(input) <= TeleopConstants.DEADBAND) {
            return 0;
        }
        return input / Math.abs(input) * (Math.abs(input)
                                          - TeleopConstants.DEADBAND) / (1 - TeleopConstants.DEADBAND);
    }

    public void spin() {
        String[] words = {"You", "spin", "my", "head", "right", "round", "right",
                          "round", "like", "a", "record", "baby", "right", "round", "right", "round", ""};
        int i;
        for (i = 0; i <= words.length; i++) {
            System.out.println(words[i]);
        }
    }
}
