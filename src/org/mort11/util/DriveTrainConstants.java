package org.mort11.util;

/**
 *
 * @author Ryan
 */
public class DriveTrainConstants {

    public static final int LEFT_VICTOR_PORT = 1;
    public static final int LEFT_VICTOR_PORT2 = 3;
    public static final int RIGHT_VICTOR_PORT = 2;
    public static final int RIGHT_VICTOR_PORT2 = 4;
    public static final double MAX_FPS = 11;
    public static final double WHEEL_DIAMETER_INCHES = 4;
    public static final double WHEEL_CIRCUMFRENCE_INCHES = Math.PI * WHEEL_DIAMETER_INCHES;
    public static final double ENCODER_CPR = 120;
    public static final double MAX_RPMS = (MAX_FPS * 12 / (WHEEL_DIAMETER_INCHES * Math.PI)) * 60;
    public static final double ACCEPTABLE_ERROR_RPMS = 10;
    public static final double ACCEPTABLE_ERROR_FPS = 3.0/12.0;
    public static final double CONTROL_LOOP_P = 3e-2; //5.6e-4 practive
    public static final double CONTROL_LOOP_I_TIME = 1; // Ti = 2.7 practive
    public static final double CONTROL_LOOP_HERTZ = 50;
    public static final double MAX_DELTA_DURING_COAST = 0.02;
    public static final double PYRAMID_ANGLE = 68;
}
