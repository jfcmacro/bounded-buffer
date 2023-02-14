package com.epam.rd.boundedbuffer;

import java.lang.reflect.Array;

public interface BoundedBuffer<T> {
    void put(T elem);
    T get();
    public static <E> E[] createBuffer(Class<E> clazz, int capacity) {
	return (E[]) Array.newInstance(clazz, capacity);
    }
}
