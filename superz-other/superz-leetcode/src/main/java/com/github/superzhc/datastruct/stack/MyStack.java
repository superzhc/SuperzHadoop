package com.github.superzhc.datastruct.stack;

/**
 * 2020年07月29日 superz add
 */
public class MyStack<T>
{
    private static final int DEFAULT_CAPACITY = 16;

    private T[] arr;
    private int size;

    public MyStack() {
        arr = (T[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public void push(T item) {
        // 判断是否需要扩容
        ensureCapacity(size);
        arr[size++] = item;
    }

    public T pop() {
        return arr[size--];
    }

    public T top() {
        return arr[size];
    }

    /**
     * 进行扩容
     * @param length
     */
    private void ensureCapacity(int length) {
        // TODO
    }
}
