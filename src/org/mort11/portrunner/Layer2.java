/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mort11.portrunner;

/**
 *
 * @author gridbug
 */
public class Layer2 {
    
    private static Layer2 instance;
    private Layer1 layer1;
    private Queue queue = new Queue();
    
    private Layer2(){
        layer1 = Layer1.getInstance(queue);
    }
    
    public static Layer2 getInstance(){
        if(instance == null){
            instance = new Layer2();
        }
        return instance;
    }
    
    //(128)(64)(32)(16)(8)(4)(2)(1)
    private static final byte SETUP = intToUnsignedByte(128);
    private static final byte LENGTH = intToUnsignedByte(64+32);
    private static final byte VIRT_OUT_NUM = intToUnsignedByte(16+8+4+2+1);
    private static final byte WRITE = intToUnsignedByte(0); //1st bit 0
    private static final byte PWM_WRITE = intToUnsignedByte(64);
    private static final byte PARALLEL_WRITE = intToUnsignedByte(0); //2nd bit 0
    private static final byte PULL_UP = intToUnsignedByte(32);
    private static final byte PULL_DOWN = intToUnsignedByte(0);//3rd bit 0
    private static final byte PERSERVE_MSB = intToUnsignedByte(32);
    private static final byte PERSERVE_LSB = intToUnsignedByte(0);//3rd bit 0
    
    public static byte intToUnsignedByte(int value){
        if(value > 255 || value < 0){
            throw new IllegalArgumentException(String.valueOf(value));
        }
        return (byte) (value & 0xFF);
    }
    
    public void setupVirtualOutput(int virtualOutput, int[] pins) throws QueueFullException{
        if(pins.length < 1){
            throw new IllegalArgumentException("Need a non-empty pins array");
        }
        if(pins.length > 8){
            throw new IllegalArgumentException("No more than 8 pins can be allocated to a virtual output");
        }
        if(virtualOutput < 0 || virtualOutput > 31){
            throw new IllegalArgumentException("Expected Virtual Output between 0 and 31, got "+virtualOutput);
        }
        byte[] message = new byte[pins.length + (pins.length % 2 == 0 ? 0 : 1) + 1];
        /* len | L | len = 2(L+1) - (0 or 1)
         *  1  | 0 | (len + (0 or 1))/2 - 1 = L
         *  2  | 0
         *  3  | 1
         *  4  | 1
         *  5  | 2
         *  6  | 2
         *  7  | 3
         *  8  | 3
         */
        message[0] = (byte) (SETUP | (((pins.length + (pins.length % 2 == 0 ? 0 : 1))/2 - 1) << 5) | virtualOutput);
        for(int i = 0; i < pins.length; i++){
            message[i+1] = intToUnsignedByte(pins[i]);
        }
        if(pins.length % 2 != 0){
            message[message.length - 1] = message[message.length - 2];
        }
        queue.push(message);
    }
    
    public void writeToVirtualOutput(int virtualOutput, boolean pwm, boolean pull_up, byte unsignedData) throws QueueFullException{
        if(virtualOutput < 0 || virtualOutput > 31){
            throw new IllegalArgumentException("Expected Virtual Output between 0 and 31, got "+virtualOutput);
        }
        byte[] message = new byte[2];
        message[0] = (byte) (WRITE | (pwm ? PWM_WRITE : PARALLEL_WRITE) | (pull_up ? PULL_UP : PULL_DOWN) | virtualOutput);
        message[1] = unsignedData;
        queue.push(message);
    }
}
