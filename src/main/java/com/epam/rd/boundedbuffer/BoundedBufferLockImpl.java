package com.epam.rd.boundedbuffer;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class BoundedBufferLockImpl<T> implements BoundedBuffer<T> {

    private T[] buffer;
    private int in;
    private int out;
    private int counter;
    private Lock lock;
    private Condition full;
    private Condition empty;

    public BoundedBufferLockImpl(Class<T> clazz, int capacity) {
	this.buffer = BoundedBuffer.createBuffer(clazz, capacity);
	this.in = this.out = this.counter = 0;
	this.lock = new ReentrantLock();
	this.full = lock.newCondition();
	this.empty = lock.newCondition();
    }

    @Override
    public void put(T elem) {
	
	lock.lock();
	try {
	    while (counter == buffer.length) {
		full.await();
	    }
	    // Start Critical Section
	    buffer[in] = elem;
	    in = (in + 1) % buffer.length;
	    counter++;
	    empty.signal();
	    // End Critical Section
	} catch (InterruptedException ie) {
	}
	finally {
	    lock.unlock();
	}
    }

    @Override
    public T get() {
	T ret = null;
	lock.lock();
	try {
	    while (counter == 0) {
		empty.await();
	    }
	    // Begin Critical Section
	    ret = buffer[out];
	    out = (out + 1) % buffer.length;
	    counter--;
	    // End Critical Section
	    full.signal();
	} catch (InterruptedException ie) {
	}
	finally {
	    lock.unlock();
	}
	return ret;
    }
}
