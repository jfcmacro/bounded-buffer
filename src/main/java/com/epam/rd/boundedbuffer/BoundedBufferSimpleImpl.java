package com.epam.rd.boundedbuffer;

import java.lang.reflect.Array;

public class BoundedBufferSimpleImpl<T> implements BoundedBuffer<T> {

    private T buffer[];
    private int in;
    private int out;
    private int counter;
    
    public BoundedBufferSimpleImpl(Class<T> clazz, int capacity) {
	buffer = (T[]) Array.newInstance(clazz, capacity);
    }
    
    @Override
    public void put(T elem) {
	while (counter == buffer.length);
	buffer[in] = elem;
	in = (in + 1) % buffer.length;
	counter++;
    }

    @Override
    public T get() {
	while (counter == 0);
	T elem = buffer[out];
	out = (out + 1) % buffer.length;
	counter--;
	return elem;
    }
}
