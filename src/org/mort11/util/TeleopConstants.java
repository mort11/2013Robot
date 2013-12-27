/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mort11.util;

/**
 *
 * @author gridbug
 */
public class TeleopConstants {
    public static final double DEADBAND = 0.025;
    public static final int BRAKE_ASSIST_BUTTON = 4;
    public static final int COAST_BUTTON = 3;
    public static final int COAST_ASSIST_BUTTON = 5;
    public static final double TUNING_DELTA_P = 0.005;
    public static final double TUNING_DELTA_I = 0.01;
    public static final int P_UP_BUTTON = 6;
    public static final int P_DOWN_BUTTON = 7;
    public static final int I_UP_BUTTON = 11;
    public static final int I_DOWN_BUTTON = 10;
    public static final int LINEUP_BUTTON = 2;
    public static final int SPIN_UP_BUTTON = 5;
    public static final int CLIMB_CONTROL = 2;
    
    public static final double LINEUP_SPEED =0.2;
    public static final int REVERSE_FLICKER_BUTTON = 3;
    
    public static final int SPATULA_TOGGLE_BUTTON = 2;
    public static final double SPATULA_MAX_DELTA = 90;
    
    public static final int PRESET_LOADING_BUTTON = 11;//loading
    public static final int PRESET_LOWER_BUTTON = 7; //unused
    public static final int PRESET_BACK_LEFT_BUTTON = 9; //hanging
    public static final int PRESET_BACK_BUTTON = 12; //shooting
    public static final int PRESET_BACK_RIGHT_BUTTON = 10; //back corner
    public static final int PRESET_FRONT_BUTTON = 8;//front
    
    public static final double TELEOP_LENGTH_SECONDS = 120;
    public static final double AUTOHANG_TIME = TELEOP_LENGTH_SECONDS -3;
}
