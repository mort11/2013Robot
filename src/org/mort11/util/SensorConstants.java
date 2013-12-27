package org.mort11.util;

/**
 *
 * @author Ryan
 */
public class SensorConstants {

    public static final String CAMERA_IP = "10.0.11.33"; //to be changed
    public static final int CAMERA_PORT = 1123; //to be changed
    
    public static final int SHOOTER_POT_PORT = 1;
    public static final int SPATULA_POT_PORT = 2;
    
    public static final double MAX_GYRO_CHANGE_DEGREES = 10;
    
    public static final double SHOOTER_OFFSET_DEGREES = -196.5;//comp = -78 prac = -160
    public static final double SHOOTER_MIN_DEGREES = 0;
    public static final double SHOOTER_MAX_DEGREES = 37;// comp 40 prac 30
    
    public static final double SPATULA_OFFSET_DEGREES = -172;//prac -96 comp - 172.5
    public static final double SPATULA_MIN_DEGREES = 0;
    public static final double SPATULA_MAX_DEGREES = 93;
    
    public static final int SHOOTER_ENCODER_CHANNEL_A = 5;
    public static final int SHOOTER_ENCODER_CHANNEL_B = 6;
    public static final int LEFT_ENCODER_CHANNEL_A = 1;
    public static final int LEFT_ENCODER_CHANNEL_B = 2;
    public static final int RIGHT_ENCODER_CHANNEL_A = 3;
    public static final int RIGHT_ENCODER_CHANNEL_B = 4;
    
    public static final int JUMPER_PORT = 14;
}
