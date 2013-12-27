package org.mort11.sensors;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Timer;
import org.mort11.portrunner.Layer2;
import org.mort11.portrunner.QueueFullException;
import org.mort11.util.LEDConstants;
import org.mort11.util.SensorConstants;

/**
 *
 * @author Ryan
 */
public class LEDs {

    public static class Color {

        public final int value;
        public final String name;
        public final int [] colors;
        
        static final int off_val = 7;
        static final int red_val = 1;
        static final int green_val = 2;
        static final int blue_val = 3;
        static final int yellow_val = 4;
        static final int orange_val = 5;
        static final int white_val = 6;
        static final int lightshow_val = 0;
        public static final Color Off = new Color(off_val, "Off", new int[]{0, 0, 0});
        public static final Color Red = new Color(red_val, "Red", new int[]{255, 0, 0});
        public static final Color Green = new Color(green_val, "Green", new int[]{0, 255, 0});
        public static final Color Blue = new Color(blue_val, "Blue", new int[]{0, 0, 255});
        public static final Color Yellow = new Color(yellow_val, "Yellow", new int[]{200, 120, 0});
        public static final Color Orange = new Color(orange_val, "Orange",new int[]{255, 40, 0});
        public static final Color White = new Color(white_val, "White", new int[]{255,255, 255});

        private Color(int val, String name,int[] colorValues) {
            this.value = val;
            this.colors = colorValues;
            this.name = name;
        }
        
        public String toString(){
            return name;
        }
    }
    public Color currentColor = Color.Off;
    private PWM redPort;
    private PWM greenPort;
    private PWM bluePort;

    private boolean initDone = false;
    private Timer writeTimer;

    public LEDs(int redOutput, 
                int greenOutput, 
                int blueOutput) {
        redPort = new PWM(redOutput);
        greenPort = new PWM(greenOutput);
        bluePort = new PWM(blueOutput);
    }
    
    public LEDs(){
        this(LEDConstants.REDVOUT, 
             LEDConstants.GREENVOUT, 
             LEDConstants.BLUEVOUT);
    }
    
    
    public void setLight(Color ledColor) {
        currentColor = ledColor;
        System.out.println("LED = " + ledColor);
        redPort.setRaw(ledColor.colors[0]);
        greenPort.setRaw(ledColor.colors[1]);
        bluePort.setRaw(ledColor.colors[2]);
    }
}
