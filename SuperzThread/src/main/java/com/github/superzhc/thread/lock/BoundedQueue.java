package com.github.superzhc.thread.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * 2020年06月16日 superz add
 */
public class BoundedQueue<T>
{
    private Object[] items;
    private int addIndex, removeIndex, count;
    private Lock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    public BoundedQueue(int size) {
        items = new Object[size];
    }

    public void add(T t) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length)
                notFull.await();

            // 书上的这个addIndex，removeIndex写的不明所以，直接用count
//            items[addIndex] = t;
//            if (++addIndex == items.length)
//                addIndex = 0;
//            ++count;

            items[count++]=t;// 直接使用count作为索引应该更合理点

            notEmpty.signal();
        }
        finally {
            lock.unlock();
        }
    }

    public T remove() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0)
                notEmpty.await();

            // 书上的这个addIndex，removeIndex写的不明所以，直接用count
//            Object x = items[removeIndex];
//            if (++removeIndex == items.length)
//                removeIndex = 0;
//            --count;

            Object x=items[--count];// 直接使用count作为索引应该合理点

            notFull.signal();
            return (T) x;
        }
        finally {
            lock.unlock();
        }
    }
}
