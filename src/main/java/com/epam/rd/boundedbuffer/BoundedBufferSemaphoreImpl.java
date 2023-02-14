package com.epam.rd.boundedbuffer;

import java.lang.reflect.Array;

public class BoundedBufferLowLevelImpl<T> implements BoundedBuffer<T> {

    private T[] buffer;
    private int in;
    private int out;
    private int counter;

    public BoundedBufferLowLevelImpl(Class<T> clazz, int capacity) {
	this.buffer = (T[]) Array.newInstance(clazz, capacity);
	this.in = this.out = this.counter = 0;
    }

    @Override
    public synchronized void put(T elem) {
	while (counter == buffer.length) {
	    try {
		wait();
	    } catch (InterruptedException ie) { }
	}
	buffer[in] = elem;
	in = (in + 1) % buffer.length;
	counter++;
	notifyAll();
    }

    @Override
    public synchronized T get() {
	while (counter == 0) {
	    try {
		wait();
	    } catch (InterruptedException ie) { }
	}
	T ret = buffer[out];
	out = (out + 1) % buffer.length;
	counter--;
	notifyAll();
	return ret;
    }
}
    


    
