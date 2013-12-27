/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mort11.ee;

import edu.wpi.first.wpilibj.Servo;
import org.mort11.util.EndEffectorConstants;

/**
 *
 * @author Ryan
 */
public class Loader {

    Servo vex;

    public Loader(int vexMotor) {
        vex = new Servo(vexMotor);
    }

    public Loader() {
        vex = new Servo(EndEffectorConstants.VEX_PORT);
    }

    /*
     * makes the loader spin forward
     */
    public void setSpin() {
        vex.set(EndEffectorConstants.VEX_FORWARD * EndEffectorConstants.VEX_SPEED);
    }

    public void setRevSpin() {
        vex.set(EndEffectorConstants.VEX_FORWARD *EndEffectorConstants.VEX_SPEED* -1);
    }

    /*
     * makes the loader stop moving
     */
    public void turnOff() {
        vex.set(EndEffectorConstants.VEX_STOPPED);
    }
}
