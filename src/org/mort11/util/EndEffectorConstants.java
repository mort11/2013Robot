package org.mort11.util;

/**
 *
 * @author Ryan
 */
public class EndEffectorConstants {

    public static final int SHOOTER_MOTOR_PORT = 5;
    public static final int LEAD_SCREW_PORT = 6;
    public static final int VEX_PORT = 7;
    public static final int[] LOCK_PORTS = {9,10};
    public static final double LOCK_DISENGAGED = 90;
    public static final double  LOCK_ENGAGED = 0;
    public static final double ACCEPTABLE_RPM_ERROR = 1;
    public static final double VEX_FORWARD = 1; 
    public static final double VEX_SPEED = 1;
    public static final double VEX_STOPPED = .5;
    public static final double LEAD_SCREW_UP = -1;
    public static final double SHOOTER_FORWARD = 1; //comp -1
    public static final double CONTROL_LOOP_P = 0.05;  //future change
    public static final double CONTROL_LOOP_I_TIME = 0.005; //future change
    public static final double CONTROL_LOOP_LEAD_SCREW_P = .5;
    public static final double ACCEPTABLE_ERROR_LEAD_SCREW_DEGREES = 0.2;
    public static final double MAX_SHOOTER_RPM = 6200;
    public static final double SHOOTER_ENCODER_CPR = 120;
    public static final double MAX_DELTA_SHOOTER_RPMS = 200;
    
    public static final double SPATULA_UP = -1; // prac 1 comp -1
    public static final double SPATULA_LOOP_P = 3e-1 * .5;
    public static final double SPATULA_SIN_ADD_CONSTANT = 5e-1;
    public static final double SPATULA_LOOP_ACCEPTABLE_ERROR_DEGREES = 1.5;
//    public static final double SPATULA_UP_ADD_CONSTANT
    
    public static final double ELEVATION_PRESET_LOADING = .5;//feed
    public static final double ELEVATION_PRESET_HANGING = 19.6; //0second fired
    public static final double ELEVATION_PRESET_BACK_LEFT_CORNER = 17.9;//back left
    public static final double ELEVATION_PRESET_BACK = 19.4;//back
    public static final double ELEVATION_PRESET_BACK_RIGHT_CORNER = 17.9;//back corner
    public static final double ELEVATION_PRESET_FRONT = 26.3;//front
    
    public static final double SPATULA_PRESET_GROUND = 0;
    public static final double SPATULA_PRESET_IDLE = 45;
    public static final double SPATULA_PRESET_HIGH = 90;
    public static final double SPATULA_PRESET_LOADING = 68;
    
    public static final int HANGER_SOLENOID_PORT = 1;
    public static final boolean HANGER_UP = true;
    public static final boolean HANGER_DOWN = !HANGER_UP;
}
