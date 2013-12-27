/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mort11.portrunner;

/**
 *
 * @author gridbug
 */
public class QueueFullException extends Exception {

    /**
     * Creates a new instance of
     * <code>QueueFullException</code> without detail message.
     */
    public QueueFullException() {
    }

    /**
     * Constructs an instance of
     * <code>QueueFullException</code> with the specified detail message.
     * <p/>
     * @param msg the detail message.
     */
    public QueueFullException(String msg) {
        super(msg);
    }
}
