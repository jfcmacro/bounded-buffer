package com.epam.rd.boundedbuffer;

import java.util.Random;
import java.util.Properties;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class App {
    private static final String SLEEP_PRODUCER = "Sleep_Producer";
    private static final String SLEEP_CONSUMER = "Sleep_Consumer";
    
    private static Properties prop = new Properties();

    static {
	boolean readFile = false;
	File propFile = new File("ProduceConsumer.prop");
	if (propFile.exists()) {
	    try {
		FileReader fr = new FileReader(propFile);
		prop.load(fr);
		readFile = true;
	    } catch (FileNotFoundException fe) {
	    } catch (IOException ioe) {
	    }
	}

	if (!readFile) {
	    prop.setProperty(SLEEP_CONSUMER, "6000");
	    prop.setProperty(SLEEP_PRODUCER, "5000");
	}
    }

    private static int getProp(String propName) {
	return Integer.parseInt(prop.getProperty(propName));
    }
    
    public static void main(String[] args) {
	Random random = new Random(System.currentTimeMillis());

	BoundedBuffer<Double> boundedBuffer =
	    new BoundedBufferLockImpl<>(Double.class, 10);

	Runnable producer = () -> {
	    double i = 1.2;
	    while (true) {
		boundedBuffer.put(i);
		i += 1.3;
		try {
		    Thread.sleep(random.nextInt(getProp(SLEEP_PRODUCER) + 1));
		} catch(InterruptedException ie) { }
	    }
	};

	Runnable consumer = () -> {
	    while (true) {
		double value = boundedBuffer.get();
		System.out.println("Value: " + value);
		try {
		    Thread.sleep(random.nextInt(getProp(SLEEP_CONSUMER) + 1));
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
