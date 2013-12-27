/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mort11.sensors;

import edu.wpi.first.wpilibj.DigitalInput;
import org.mort11.util.SensorConstants;

/**
 *
 * @author gridbug
 */
public class Jumper {
    private DigitalInput jump;
    public Jumper(int port){
        jump = new DigitalInput(port);
    }
    public Jumper(){
        this(SensorConstants.JUMPER_PORT);
    }
    
    public boolean isSet(){
        return !jump.get();
    }
}
