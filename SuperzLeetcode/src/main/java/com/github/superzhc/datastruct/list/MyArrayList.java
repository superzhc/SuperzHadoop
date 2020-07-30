package com.github.superzhc.datastruct.list;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 2020年07月29日 superz add
 */
public class MyArrayList<T> implements Iterable<T>
{
    private static final int DEFAULT_CAPACITY = 10;

    private int theSize;
    private T[] theItems;

    public MyArrayList() {
        clear();
    }

    public void clear() {
        theSize = 0;
        ensureCapacity(DEFAULT_CAPACITY);
    }

    public void ensureCapacity(int newCapacity) {
        if (newCapacity < theSize)
            return;

        T[] old = theItems;
        theItems = (T[]) new Object[newCapacity];

        for (int i = 0; i < theSize; i++) {
            theItems[i] = old[i];
        }
    }

    public int size() {
        return theSize;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void trimToSize() {
        ensureCapacity(size());
    }

    public T get(int index) {
        if (index < 0 || index >= size())
            throw new ArrayIndexOutOfBoundsException();

        return theItems[index];
    }

    public T set(int index, T newVal) {
        if (index < 0 || index >= size())
            throw new ArrayIndexOutOfBoundsException();

        T old = theItems[index];
        theItems[index] = newVal;
        return old;
    }

    public void add(int index, T item) {
        if (theItems.length == size())
            ensureCapacity(size() * 2 + 1);

        for (int i = theSize; i > index; i--)
            theItems[i] = theItems[i - 1];
        theItems[index] = item;

        theSize++;
    }

    public boolean add(T item) {
        add(size(), item);
        return true;
    }

    public T remove(int index) {
        T item = theItems[index];
        for (int i = index; i < size() - 1; i++) {
            theItems[i] = theItems[i + 1];
        }

        theSize--;
        return item;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayListIterator();
    }

    private class ArrayListIterator implements Iterator<T>
    {
        private int current = 0;

        public boolean hasNext() {
            return current < size();
        }

        @Override
        public T next() {
            if (!hasNext())
                throw new NoSuchElementException();

            return theItems[current++];
        }

        @Override
        public void remove() {
            MyArrayList.this.remove(--current);
        }

    }
}
