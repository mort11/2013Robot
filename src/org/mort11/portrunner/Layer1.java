/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mort11.portrunner;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Timer;
import org.mort11.util.PortRunnerConstants;

/**
 *
 * @author gridbug
 */
public class Layer1 {

    public static class Layer1State {

        private static final int GOTDATA = 0;
        private static final int MSNWRITTEN = 1;
        private static final int WAITING = 2;
        public static final Layer1State gotData = new Layer1State(GOTDATA);
        public static final Layer1State mostSignificantNibbleWritten = new Layer1State(MSNWRITTEN);
        public static final Layer1State waitingForData = new Layer1State(WAITING);
        public final int value;

        private Layer1State(int val) {
            value = val;
        }
    }
    private static Layer1 instance;
    private Layer1State state;
    private DigitalOutput interrupt;
    private DigitalOutput reset;
    private DigitalOutput[] nibble = new DigitalOutput[4];
    private final Queue buffer;
    private final Timer interruptTimer;
    private boolean timerStarted = false;
    private byte currentByte;
    private static final long INTERRUPT_TIME_MS = 5;
    private static final boolean HIGH = true;
    private static final boolean LOW = false;
    private static final int BIT8 = 0;
    private static final int BIT4 = 1;
    private static final int BIT2 = 2;
    private static final int BIT1 = 3;
    private static final double SECONDS_TO_MS = 1000;

    private Layer1(int resetPort, int interrruptPort, int bit8port, int bit4port, int bit2port, int bit1port, Queue queue) {
        reset = new DigitalOutput(resetPort);
        interrupt = new DigitalOutput(interrruptPort);
        interruptTimer = new Timer();
        nibble[BIT8] = new DigitalOutput(bit8port);
        nibble[BIT4] = new DigitalOutput(bit4port);
        nibble[BIT2] = new DigitalOutput(bit2port);
        nibble[BIT1] = new DigitalOutput(bit1port);
        buffer = queue;
        state = null;
    }

    public static Layer1 getInstance(Queue queue) {
        if (instance == null) {
            instance = new Layer1(PortRunnerConstants.RESETPORT,
                                  PortRunnerConstants.INTERRUPTPORT,
                                  PortRunnerConstants.NIBBLE8PORT,
                                  PortRunnerConstants.NIBBLE4PORT,
                                  PortRunnerConstants.NIBBLE2PORT,
                                  PortRunnerConstants.NIBBLE1PORT, queue);
        }
        return instance;
    }

    private void writeNibble(byte unsignedNibble) {
//        System.out.println(toBinString(unsignedNibble));
        nibble[BIT8].set((unsignedNibble & 8) > 0);
        nibble[BIT4].set((unsignedNibble & 4) > 0);
        nibble[BIT2].set((unsignedNibble & 2) > 0);
        nibble[BIT1].set((unsignedNibble & 1) > 0);
    }

    private void writeByte(byte unsignedByte) {
//        System.out.println("Printing Nibbles " + ((unsignedByte>>4) & 15) + " " +(unsignedByte & 15));
        
        
//        System.out.println();
    }

    public void service() {
        double ms_elapsed = interruptTimer.get() * SECONDS_TO_MS;
//        System.out.println("MS ELAPSED " + ms_elapsed);
        if (state == null) {
            /*
             * Initialization stuff. Start the timer, then set the state and 
             * ms_elapsed variables so that we start grabbing bytes.
             */
            if(!timerStarted){
                interruptTimer.start();
                reset.set(HIGH);
                timerStarted = true;
                ms_elapsed  = 0;
            }
            System.out.println("MS " + ms_elapsed);
            if(ms_elapsed > PortRunnerConstants.ARDUINO_BOOT_TIME_MS){
                interruptTimer.reset();
                state = Layer1State.waitingForData;
            }
//            System.out.println("INIT");
            return;
        }
        if(state == Layer1State.waitingForData){
//            System.out.println("WAITINGFORDATA");
            if(buffer.isEmpty()){
                return;
            }else{
                try {
                    currentByte = buffer.pop();
                } catch (QueueEmptyException ex) {
                    ex.printStackTrace();
                    return;
                }
                state = Layer1State.gotData;
                ms_elapsed = INTERRUPT_TIME_MS + 1;
            }
        }
        if(ms_elapsed > INTERRUPT_TIME_MS){
            /*
             * Write the most significant nibble first
             */
            interrupt.set(LOW);
            interruptTimer.reset();
            if (state == Layer1State.gotData) {
//                System.out.println("GOTDATA "+ toBinString(currentByte));
                writeNibble((byte) ((currentByte >> 4) & 15));
                interrupt.set(HIGH);
                state = Layer1State.mostSignificantNibbleWritten;
            } else if (state == Layer1State.mostSignificantNibbleWritten) {
//                System.out.println("WRITINGSECONDNIBBLE");
                writeNibble(currentByte);
                interrupt.set(HIGH);
                state = Layer1State.waitingForData;
            }
        }
    }
    
    public String toBinString(byte b){
        String tmp ="";
        for(int i = 0;i<8;i++){
            tmp += (((b << i) & 128) > 0 ? "1":"0");
        }
        return tmp;
    }
}
