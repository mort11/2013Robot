package org.mort11.ee;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import org.mort11.util.EndEffectorConstants;

/*
 * An object representing the locks that will be deployed once the robot is
 * at a respectable height on the pyramid
 * @author Ryan
 */
public class ClimbLocks {

    Servo vex1;
    Servo vex2;
    boolean locked = false;

    public ClimbLocks() {
        int[] ports = EndEffectorConstants.LOCK_PORTS;
        vex1 = new Servo(ports[0]);
        vex2 = new Servo(ports[1]);
        lock();
    }

    /*
     * Spins the vex motors to deploy the locks
     * @param speed the speed of the vex motors, with .5 being still and 1
     * being full speed forward
     */
//    public void spinVexes(double pos) {
//        
//       
//            vex1.setAngle(180 - pos);
//            vex2.setAngle(pos);
//    }

    /*
     * locks the vexes and stops them from moving anymore
     */
    public void lock() {
//        spinVexes(EndEffectorConstants.LOCK_DISENGAGED); //set constants
        vex1.setAngle(180);
        vex2.setAngle(0);
//        Timer.delay(EndEffectorConstants.LOCK_TIME);
//        spinVexes(.5);
//        locked = true;
    }
    
    public void unlock(){
//        spinVexes(EndEffectorConstants.LOCK_ENGAGED);
        vex1.setAngle(90);
        vex2.setAngle(90);
    }
}
