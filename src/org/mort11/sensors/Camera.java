package org.mort11.sensors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

import javax.microedition.io.Connection;

import org.mort11.util.SensorConstants;

public class Camera extends Thread {

    private static Camera instance;
    private InputStream is;
    private OutputStream os;
    private SocketConnection sc;
    private int port;
    private String ip;
    private boolean isAiming, isDistancing, isBalling, isRunning;
    private byte first, second;
    private boolean isConnected;
    private char ballPosition;

    private Camera(String ip, int port) {
        this.port = port;
        this.ip = ip;

        isAiming = false;
        isDistancing = false;
        isBalling = false;
        isRunning = true;
        isConnected = false;
    }

    public static Camera getInstance() {
        if (instance == null) {
            instance = new Camera(SensorConstants.CAMERA_IP,
                    SensorConstants.CAMERA_PORT);
            instance.start();
        }
        return instance;
    }

    public void run() {
        while (isRunning) {
            if (is != null) {
                if (isDistancing()) {
                    send("GD");
                    try {
                        first = (byte) is.read();
                        second = (byte) is.read();
                        System.out.println("CLIENT DISTANCE: " + first + "." + second);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (isAiming()) {
                    send("GA");
                    try {
                        first = (byte) is.read();
                        System.out.println("CLIENT AIMING: " + first);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (isBalling()) {
                    send("GB");
                    try {
                        ballPosition = (char) is.read();
                        System.out.println("CLIENT AIMING: " + first);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets up the connection to the laptop.
     */
    public void connect() {
        try {
            System.out.println("Connecting to laptop.");
            isConnected = true;
            sc = (SocketConnection) Connector.open("socket://" + ip + ":"
                    + port);
            sc.setSocketOption(SocketConnection.LINGER, 5);
            is = sc.openInputStream();
            os = sc.openOutputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Stops the connection between the laptop.
     */
    public void disconnect() {
        System.out.println("Disconnecting from laptop");
        isConnected = false;
        if (os != null) {
            try {
                is.close();
                os.close();
                sc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void send(String c) {
        c += ' ';
        if (os != null) {
            try {
                os.write(c.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public char getBallPosition() {
        return ballPosition;
    }

    /**
     * Sends a signal to the laptop to start the lowish resolution, high fps
     * camera used for horizontal aiming.
     */
    public void startAimingCamera() {
        System.out.println("Starting aiming camera.");
        send("A");
    }

    /**
     * Sends a signal to the laptop to stop the lowish resolution, high fps
     * camera used for horizontal aiming.
     */
    public void stopAimingCamera() {
    }

    /**
     * @return True if the aiming camera is on. False if not.
     */
    public boolean isAiming() {
        return isAiming;
    }

    /**
     * Sends a signal to the laptop to start the high resolution, low fps camera
     * used for horizontal tweaking, distance to the target, and angle of the
     * target relative to the turret.
     */
    public void startDistanceCamera() {
        send("D");
        isDistancing = true;
    }

    /**
     * Sends a signal to the laptop to stop the high resolution, low fps camera
     * used for horizontal tweaking, distance to the target, and angle of the
     * target relative to the turret.
     */
    public void stopDistanceCamera() {
    }

    /**
     * @return True if the distance camera is on. False if not.
     */
    public boolean isDistancing() {
        return isDistancing;
    }

    /**
     * Sends a signal to the laptop to start the PS3 camera used for finding
     * balls.
     */
    public void startBallCamera() {
        send("B");
    }

    /**
     * Sends a signal to the laptop to stop the PS3 camera used for finding
     * balls.
     */
    public void stopBallCamera() {
    }

    /**
     * @return True if the ball camera is on. False if not.
     */
    public boolean isBalling() {
        return isBalling;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
