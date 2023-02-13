package com.epam.rd.boundedbuffer;

public interface BoundedBuffer<T> {
    void put(T elem);
    T get();
}
