/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mort11.ee;

import edu.wpi.first.wpilibj.Solenoid;
import org.mort11.util.EndEffectorConstants;

/**
 *
 * @author gridbug
 */
public class Hanger {
    
    
    
    private Solenoid solenoid;
    
    public Hanger(int port){
        solenoid = new Solenoid(port);
    }
    
    public Hanger(){
        this(EndEffectorConstants.HANGER_SOLENOID_PORT);
    }
    
    public void setHooks(boolean state){
        solenoid.set(state);
    }
    
    
    public void hooksUp(){
        solenoid.set(true);
    }
    
    public void hooksDown(){
        solenoid.set(false);
    }
}
