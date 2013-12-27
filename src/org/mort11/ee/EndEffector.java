package org.mort11.ee;

import org.mort11.util.EndEffectorConstants;

/*
 * A class to control the End Effector of the robot
 * @author Ryan
 */
public class EndEffector {

    private Shooter shooter;
    private Loader loader;
    private Hanger hanger;
    
    double shootPercent = 1;
    
    public EndEffector() {
        shooter = new Shooter();
        loader = new Loader();
        hanger = new Hanger();
    }

    /*
     * Loads another frisbee into the shooter
     */
    public void load() {
        loader.setSpin();
    }
    
    public void setShootPercent(double percent)
    {
        shootPercent = percent;
    }

    public double getShootPercent(){
        return shootPercent;
    }
    public double getSpins(){
        return shooter.getSpins();
    }
    /*
     * Stops the loader from moving
     */
    public void dontLoad() {
        loader.turnOff();
    }

    public void reverseFlicker() {
        loader.setRevSpin();
    }
    /*
     * Gets the motor's raw value
     * @return the percentage and sign of the shooter's motor
     */

    public double getShooterSpeed() {
        return shooter.get();
    }

    public double getShooterSpeedRPMS() {
        return shooter.getRPM();
    }
    public double getShooterAngle(){
        return shooter.getAngle();
    }
    /*
     * Spins up the shooter
     * @param on true if spinning up, false if spinning down
     */
    public void setShoot(boolean on, double speed) {
//        System.out.println(shootPercent);
        double shootspeed = 1 * speed * shootPercent;
        if (!on) {
            shooter.stop();
        } else {
            shooter.spinUp(shootspeed);
//            shooter.encoderSpinUp(shootPercent * EndEffectorConstants.MAX_SHOOTER_RPM);
        }
    }

    /*
     * A check for whether or not the shooter is at max RPMs
     * @return true if spun up, false if not
     */
    public boolean isSpunUp() {
        if (shooter.get() > .95) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * Changes the angle of the shooter to the value of the input
     * @param angle The angle to change the shooter to
     * @return true once finished
     */
    public boolean cycleShooterElevation(double angle) {
        return shooter.cycleElevation(angle);
    }

    public void manualShooterElevation(double speed) {
        shooter.changeElevationManual(speed);
    }
    
    public void setShooterOverride(boolean override){
        shooter.setOverride(override);   
    }
    
    
    public void setHangerState(boolean state){
        hanger.setHooks(state);
    }
}
