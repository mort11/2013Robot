/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mort11.portrunner;

/**
 *
 * @author gridbug
 */
public class Queue {

    public static final int DEFAULT_BUFLEN = 1024;
    private byte[] buffer;
    private int nextFreeIndex = 0;

    public Queue(){
        this(DEFAULT_BUFLEN);
    }
    
    public Queue(int len){
        buffer = new byte[len];
    }
    
    public synchronized void push(byte[] b) throws QueueFullException {
        if(nextFreeIndex == 0){
            this.notifyAll();
        }
        if (nextFreeIndex + b.length > buffer.length) {
            throw new QueueFullException();
        }
        for (int i = 0; i < b.length; i++) {
            buffer[nextFreeIndex++] = b[i];
        }
    }

    public synchronized byte pop() throws QueueEmptyException {
        if (isEmpty()) {
            throw new QueueEmptyException();
        }
        byte popped = buffer[0];
        for (int i = 0; i < nextFreeIndex-1; i++) {
            buffer[i] = buffer[i + 1];
        }
        nextFreeIndex--;
        return popped;
    }
    
    public boolean isEmpty(){
        return nextFreeIndex == 0;
    }
    
    public boolean isFull(){
        return nextFreeIndex >= buffer.length;
    }
}
