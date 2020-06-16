package com.github.superzhc.thread.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 在同一时刻，只允许至多两个线程同时访问，超过两个线程的访问将被阻塞
 * 2020年06月16日 superz add
 */
public class TwinsLock implements Lock
{
    private static class Sync extends AbstractQueuedSynchronizer
    {
        public Sync(int count) {
            if (count < 0) {
                throw new IllegalArgumentException("count must large than 0.");
            }
            setState(2);
        }

        @Override
        protected int tryAcquireShared(int reduceCount) {
            for (;;) {
                int current = getState();
                int newCount = current - reduceCount;
                if (newCount < 0 || compareAndSetState(current, newCount)) {
                    return newCount;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int returnCount) {
            for (;;) {
                int current = getState();
                int newCount = current + returnCount;
                if (compareAndSetState(current, newCount))
                    return true;
            }
        }
    }

    private Sync sync = new Sync(2);

    @Override
    public void lock() {
        sync.acquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        int state = sync.tryAcquireShared(1);
        if (state > -1 & state < 3)
            return true;
        else
            return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        // TODO 直接使用这个逻辑有点问题
        // sync.tryAcquireSharedNanos(1,unit.toNanos(time));
        return false;
    }

    @Override
    public void unlock() {
        sync.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        // TODO
        return null;
    }
}
