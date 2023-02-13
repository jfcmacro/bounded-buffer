package com.epam.rd.boundedbuffer;

import java.util.Random;

public class App  {
    public static void main( String[] args ) {
	Random random = new Random(System.currentTimeMillis());

	BoundedBuffer<Character> boundedBuffer =
	    new BoundedBufferLowLevelImpl<>(Character.class, 10);

	Runnable producer = () -> {
	    int i = 'A';
	    while (true) {
		boundedBuffer.put((char) i);
		i = (((i + 1) - 'A') % ('z' - 'A' + 1)) + 'A';
		try {
		    Thread.sleep(random.nextInt(5000));
		} catch(InterruptedException ie) { }
	    }
	};

	Runnable consumer = () -> {
	    while (true) {
		char value = boundedBuffer.get();
		System.out.println("Value: " + value);
		try {
		    Thread.sleep(random.nextInt(6000));
		} catch (InterruptedException ie) { }
	    }
	};

	Thread tp = new Thread(producer);
	Thread tc = new Thread(consumer);

	tp.start();
	tc.start();

	try {
	    tp.join();
	    tc.join();
	} catch (InterruptedException ie) { }
    }
}
