package com.github.superzhc.demo.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 独占锁，在同一个时刻只能有一个线程获取到锁，而其他获取锁的线程只能处于同步队列中等待，只有获取锁的线程释放了锁，后继的线程才能够获取锁
 * 2020年07月20日 superz add
 */
public class Mutex implements Lock
{
    /*
     * 静态内部类，该内部类继承了同步器并实现了独占式获取和释放同步状态
     */
    private static class Sync extends AbstractQueuedSynchronizer
    {
        /* 是否处于占用状态 */
        @Override
        protected boolean isHeldExclusively() {
            return 1 == getState();
        }

        /* 当状态为 0 的时候获取锁 */
        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        /* 释放锁，将状态设置为 0 */
        @Override
        protected boolean tryRelease(int arg) {
            if (0 == getState())
                throw new IllegalMonitorStateException();

            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        /* 返回一个Condition，每个condition都包含了一个condition队列 */
        Condition newCondition() {
            return new ConditionObject();
        }
    }

    // 仅需要将操作代理到Sync上
    private final Sync sync = new Sync();

    /**
     * 加锁
     */
    @Override
    public void lock() {
        sync.acquire(1);
    }

    public boolean isLocked() {
        return sync.isHeldExclusively();
    }

    public boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    /**
     * 解锁
     */
    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
