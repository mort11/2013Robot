package org.mort11.dt;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import org.mort11.sensors.SensorDealer;
import org.mort11.util.DriveTrainConstants;
/*
 * A method for the drive train of the robot.  
 * @author Mostly Will, a bit of Ryan
 */

public class DriveTrain {

    public static class BrakingMode {

        public final int value;
        static final int brakeAssistVal = 0;
        static final int coastVal = 1;
        static final int coastAssistVal = 2;
        public static final BrakingMode kBrakeAssist =
                new BrakingMode(brakeAssistVal);
        public static final BrakingMode kCoast =
                new BrakingMode(coastVal);
        public static final BrakingMode kCoastAssist =
                new BrakingMode(coastAssistVal);

        private BrakingMode(int val) {
            value = val;
        }
    }
    private Encoder leftEnc;
    private Encoder rightEnc;
    private Talon left;
    private Talon right;
    private Talon left2;
    private Talon right2;
    private double leftVal = 0;
    private double rightVal = 0;
    private double leftRPMS_desired = 0;
    private double rightRPMS_desired = 0;
    private double leftErrorSum = 0;
    private double rightErrorSum = 0;
    private boolean climberErrorChecked = false;
    private Timer leftTimer;
    private Timer rightTimer;
    private BrakingMode brakingMode;
    private double P_CONSTANT = DriveTrainConstants.CONTROL_LOOP_P;
    private double I_TIME = DriveTrainConstants.CONTROL_LOOP_I_TIME;

    public BrakingMode getBrakingMode() {
        return brakingMode;
    }

    public void setBrakingMode(BrakingMode brakingMode) {
        this.brakingMode = brakingMode;
    }

    public double getP_CONSTANT() {
        return P_CONSTANT;
    }

    public void setP_CONSTANT(double P_CONSTANT) {
        this.P_CONSTANT = P_CONSTANT;
    }

    public double getI_CONSTANT() {
        return I_TIME;
    }

    public void setI_CONSTANT(double I_CONSTANT) {
        this.I_TIME = I_CONSTANT;
    }

    public DriveTrain(int LPort, int RPort, int LPort2, int RPort2, Encoder leftEncoder,
            Encoder rightEncoder) {
        left = new Talon(LPort);
        right = new Talon(RPort);
        left2 = new Talon(LPort2);
        right2= new Talon(RPort2);
        leftEnc = leftEncoder;
        rightEnc = rightEncoder;
        leftTimer = new Timer();
        rightTimer = new Timer();
        leftTimer.start();
        rightTimer.start();
        //getRate now returns RPMS
        leftEnc.setDistancePerPulse((DriveTrainConstants.WHEEL_CIRCUMFRENCE_INCHES/12) / DriveTrainConstants.ENCODER_CPR);
        rightEnc.setDistancePerPulse((DriveTrainConstants.WHEEL_CIRCUMFRENCE_INCHES/12) / DriveTrainConstants.ENCODER_CPR);
        leftEnc.start();
        rightEnc.start();
        brakingMode = BrakingMode.kBrakeAssist;

    }

    public DriveTrain(Encoder leftEncoder, Encoder rightEncoder) {
        this(DriveTrainConstants.LEFT_VICTOR_PORT,
                DriveTrainConstants.RIGHT_VICTOR_PORT, DriveTrainConstants.LEFT_VICTOR_PORT2, DriveTrainConstants.RIGHT_VICTOR_PORT2,
                leftEncoder, rightEncoder);
    }

    public DriveTrain() {
        this(SensorDealer.getInstance().getLeftEncoder(),
                SensorDealer.getInstance().getRightEncoder());
    }

    public void drive(double lSpeed, double rSpeed) {
        leftVal = lSpeed;
        rightVal = rSpeed;

        if (leftVal > 1) {
            leftVal = 1;
        } else if (leftVal < -1) {
            leftVal = -1;
        }
        if (rightVal > 1) {
            rightVal = 1;
        } else if (rightVal < -1) {
            rightVal = -1;
        }
        left.set(leftVal);
        left2.set(leftVal);
        right.set(-rightVal);
        right2.set(-rightVal);
    }

    public double[] getValues() {
        double[] output = {leftVal, rightVal};
        return output;
    }

    public void driveFPS(double leftFPS, double rightFPS) {
        leftRPMS_desired = leftFPS;
        rightRPMS_desired = rightFPS;
        double deltaLeft = doControlLoopLeft();
        double deltaRight = doControlLoopRight();
//        System.out.println("LeftDelta = "+deltaLeft);
//        System.out.println("RightDelta = "+deltaRight);
        drive(deltaLeft + leftVal, deltaRight + rightVal);
    }

    //gotta figure out where the NaN is coming from!!!!!!!!!
    private double doControlLoopLeft() {
        if (leftTimer.get() < 1 / DriveTrainConstants.CONTROL_LOOP_HERTZ) {
            return 0;
        }
        double rate = leftEnc.getRate();
        if (Double.isNaN(rate)) {
            rate = leftRPMS_desired;
        }
        double error = leftRPMS_desired - rate;
//        System.out.println("ERROR = "+error);
        leftErrorSum += error * leftTimer.get();
        leftTimer.reset();
        if (Math.abs(error) < DriveTrainConstants.ACCEPTABLE_ERROR_FPS) {
//            System.out.println("RESET");
            error = 0;
            leftErrorSum = 0;
//            leftEnc.reset();
        }
        return P_CONSTANT * error;
    }

    private double doControlLoopRight() {
        if (rightTimer.get() < 1 / DriveTrainConstants.CONTROL_LOOP_HERTZ) {
            return 0;
        }
        double rate = rightEnc.getRate();
        if (Double.isNaN(rate)) {
            rate = rightRPMS_desired;
        }
        double error = rightRPMS_desired - rate;
        rightErrorSum += error * rightTimer.get();
        rightTimer.reset();
        if (Math.abs(error) < DriveTrainConstants.ACCEPTABLE_ERROR_FPS) {
            error = 0;
            rightErrorSum = 0;
//            rightEnc.reset();
        }
        return P_CONSTANT * error;
    }

    private double doCoastLeft(double deltaLeft) {
        double currentSign = leftVal / Math.abs(leftVal);
        double newSign = (leftVal + deltaLeft)
                / Math.abs((leftVal + deltaLeft));
        if (currentSign != newSign) {
            return -leftVal;
        }
        if (Math.abs(deltaLeft) > DriveTrainConstants.MAX_DELTA_DURING_COAST) {
            return deltaLeft / Math.abs(deltaLeft)
                    * DriveTrainConstants.MAX_DELTA_DURING_COAST;
        }
        return deltaLeft;
    }

    private double doCoastRight(double deltaRight) {
        double currentSign = rightVal / Math.abs(rightVal);
        double newSign = (rightVal + deltaRight)
                / Math.abs((rightVal + deltaRight));
        if (currentSign != newSign) {
            return -rightVal;
        }
        if (Math.abs(deltaRight) > DriveTrainConstants.MAX_DELTA_DURING_COAST) {
            return deltaRight / Math.abs(deltaRight)
                    * DriveTrainConstants.MAX_DELTA_DURING_COAST;
        }
        return deltaRight;
    }

}
