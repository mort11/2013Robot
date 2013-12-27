package org.mort11.ee;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import org.mort11.sensors.SensorDealer;
import org.mort11.util.EndEffectorConstants;
import org.mort11.util.SensorConstants;

/**
 *
 * @author Ryan
 */
public class Shooter {

    Talon shooterWheel;
    Talon elevation;
    double forwardPositive = EndEffectorConstants.SHOOTER_FORWARD;
    double RPMsDesired = 0;
    double errorSum = 0;
    double totalSpins = 0;
    double currentRPMs = 0;
    double angleDesired = 0;
    int correctReadings = 0;
    private double P_CONSTANT = EndEffectorConstants.CONTROL_LOOP_P;
    private double I_TIME = EndEffectorConstants.CONTROL_LOOP_I_TIME;
    Encoder shooterEnc;
    Timer shooterTimer;
    AnalogChannel shooterPot;
    private boolean override = false;

    public Shooter(int shooterPort, int elevPort, Encoder shooterEncoder) {
        shooterWheel = new Talon(shooterPort);
        elevation = new Talon(elevPort);
        shooterEnc = shooterEncoder;
        shooterEnc.setDistancePerPulse(60 / EndEffectorConstants.SHOOTER_ENCODER_CPR);
        shooterEnc.setMinRate(10);
        shooterTimer = new Timer();
        shooterTimer.start();
        shooterEnc.start();
        shooterPot = SensorDealer.getInstance().getShooterPot();
    }

    public Shooter() {
        this(EndEffectorConstants.SHOOTER_MOTOR_PORT,EndEffectorConstants.LEAD_SCREW_PORT,
                SensorDealer.getInstance().getShooterEncoder());
    }

    /*
     * gets the raw value from the Talon
     * @return the value, between 1 and -1, that the Talon is ourputting
     */
    public double get() {
        return shooterWheel.get();
    }

    /*
     * spins up the shooter to the desired speed, using raw value
     * @param speed the speed of the wheel to be set, from 1 to -1
     */
    public void spinUp(double speed) {
        shooterWheel.set(EndEffectorConstants.SHOOTER_FORWARD * speed);
    }

    /*
     * spins up the shooter to a desired RPMs using the encoder on the wheel
     * @param rate the desired RPMs
     */
    public void encoderSpinUp(double rate) {
//        RPMsDesired = rate;
//        if (rate == 0) {
//            shooterWheel.set(0);
//            return;
//        }
//        double deltaRate = doControlLoop();
//        double newShooterSpeed = deltaRate + shooterWheel.get();
//        shooterWheel.set(newShooterSpeed);
    }

    /*
     * control loop method to reduce or increase the speed
     * @return the new shooter speed.
     */
    private double doControlLoop() {
        double rate = shooterEnc.getRate();
        if (rate == Double.NaN) {
            rate = RPMsDesired;
        }
        double error = RPMsDesired - rate;
        errorSum += error * shooterTimer.get() / 60;
        shooterTimer.reset();
        if (Math.abs(error) < EndEffectorConstants.ACCEPTABLE_RPM_ERROR) {
            error = 0;
            errorSum = 0;
            totalSpins += shooterEnc.get();
            shooterEnc.reset();
        }
        return P_CONSTANT * (error);
    }

    /*
     * stops the shooter from moving
     */
    public void stop() {
        shooterWheel.set(0);
    }

    /**
     * returns the RPMs of the shooter
     *
     * @return returns the rate of the shooter in RPMs, recieved from the
     * encoder
     */
    public double getRPM() {
        return shooterEnc.getRate();
//        double rate = shooterEnc.getRate();
//        if(Double.valueOf(rate).isNaN()){
//            System.out.println("NAN in rate");
//            rate  = currentRPMs;
////            int i =1/0;
//        }
//        double delta = RPMWrapper(rate - currentRPMs);
//        if(Double.valueOf(delta).isNaN()){
//            System.out.println("NAN in delta");
//            delta = 0;
//        }
//        currentRPMs = currentRPMs + delta;
//        return currentRPMs; //maybe change if broken
    }

    /*
     * Hashtag spin to win
     * @return spins for no good reason
     */
    public double getSpins() {
        return shooterEnc.get() + totalSpins;  //maybe change if broken
    }

    /*
     * changes the shooter's elevation to the desired angle
     * @param angle the desired angle
     * @return true if finished cycling, false is not
     */
    public boolean cycleElevation(double angle) {
        if (override) {
            System.out.println("OVERRIDE");
            return true;
        }
        if (angle != angleDesired) {
            angleDesired = angle;
            correctReadings = 0;
        }
        double error = angle - getAngle();
//        System.out.println("ERROR = " + error);
        if (Math.abs(error) < EndEffectorConstants.ACCEPTABLE_ERROR_LEAD_SCREW_DEGREES) {
            correctReadings++;
            if (correctReadings >= 10) {
                correctReadings = 0;
                changeElevationManual(0);
                return true;
            }
        }
        changeElevationManual(EndEffectorConstants.CONTROL_LOOP_LEAD_SCREW_P * error * (Math.abs(getAngle()+5)) / (SensorConstants.SHOOTER_MAX_DEGREES - SensorConstants.SHOOTER_MIN_DEGREES));
        return false;

    }

    /*
     * Changes the speed and allows the joysticks to be used
     * @param speed the speed of the angle change.
     */
    public void changeElevationManual(double speed) {
//        System.out.println("ANGLE " +getAngle());
//        System.out.println("SPEED = "+speed);
        if (!override) {
            if (speed <0 && getAngle() <= SensorConstants.SHOOTER_MIN_DEGREES) {
                speed = 0;
//                System.out.println("DOWNLIM");
            } else if (speed > 0&& getAngle() >= SensorConstants.SHOOTER_MAX_DEGREES) {
                speed = 0;
//                System.out.println("UPLIM");
            }
        }
        elevation.set(speed * EndEffectorConstants.LEAD_SCREW_UP);
    }

    public double getAngle() {
//        System.out.println("VOLTAGE" +shooterPot.getVoltage());
        return (360 -(shooterPot.getVoltage() / 5) * 360) + SensorConstants.SHOOTER_OFFSET_DEGREES;
    }

    public double RPMWrapper(double takenValue) {
        if (takenValue == 0) {
            return takenValue;
        }
        double finalValue = takenValue;
        if (Math.abs(finalValue) > EndEffectorConstants.MAX_DELTA_SHOOTER_RPMS) {
            finalValue = EndEffectorConstants.MAX_DELTA_SHOOTER_RPMS * (finalValue / Math.abs(finalValue));
        }
        return finalValue;
    }

    public void setOverride(boolean val) {
        override = val;
    }
}
