package com.epam.rd.boundedbuffer;

import java.util.concurrent.Semaphore;

public class BoundedBufferSemaphoreImpl<T> implements BoundedBuffer<T> {

    private T[] buffer;
    private int in;
    private int out;
    private int counter;
    private Semaphore mutex;
    private Semaphore full;
    private Semaphore empty;

    public BoundedBufferSemaphoreImpl(Class<T> clazz, int capacity) {
	this.buffer = BoundedBuffer.createBuffer(clazz, capacity);
	this.in = this.out = this.counter = 0;
	this.mutex = new Semaphore(1);
	this.full  = new Semaphore(capacity);
	this.empty = new Semaphore(0);
    }

    @Override
    public void put(T elem) {
	try {
	    full.acquire();
	    try {
		mutex.acquire();
		// Start Critical Section
		buffer[in] = elem;
		in = (in + 1) % buffer.length;
		counter++;
		// End Critical Section
	    }
	    finally {
		mutex.release();
	    }
	    empty.release();
	}
	catch (InterruptedException ie) {
	    ie.printStackTrace();
	}
    }

    @Override
    public T get() {
	T ret = null;
	try {
	    empty.acquire();
	    try {
		mutex.acquire();
		// Begin Critical Section
		ret = buffer[out];
		out = (out + 1) % buffer.length;
		counter--;
		// End Critical Section
	    }
	    finally {
		mutex.release();
	    }
	    full.release();
	}
	catch (InterruptedException ie) {
	    ie.printStackTrace();
	}
	return ret;
    }
}
