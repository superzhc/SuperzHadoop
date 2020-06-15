package com.github.superzhc.thread.lock;

/**
 * 死锁示例
 * 2020年06月11日 superz add
 */
public class DeadLockDemo
{
    private static String A = "A";
    private static String B = "B";

    public static void main(String[] args) {
        new DeadLockDemo().deadLock();
    }

    private void deadLock() {
        Thread t1 = new Thread(new Runnable()
        {
            @Override
            public void run() {
                synchronized (A) {
                    System.out.println("当前线程[" + Thread.currentThread().getName() + "]持有A锁");
                    try {
                        Thread.currentThread().sleep(1000 * 3);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    synchronized (B) {
                        System.out.println("当前线程[" + Thread.currentThread().getName() + "]持有B锁");
                        System.out.println("1");
                        System.out.println("当前线程[" + Thread.currentThread().getName() + "]释放B锁");
                    }
                    System.out.println("当前线程[" + Thread.currentThread().getName() + "]释放A锁");
                }
            }
        });

        Thread t2 = new Thread(new Runnable()
        {
            @Override
            public void run() {
                synchronized (B) {
                    System.out.println("当前线程[" + Thread.currentThread().getName() + "]持有B锁");
                    synchronized (A) {
                        System.out.println("当前线程[" + Thread.currentThread().getName() + "]持有A锁");
                        System.out.println("2");
                        System.out.println("当前线程[" + Thread.currentThread().getName() + "]释放A锁");
                    }
                    System.out.println("当前线程[" + Thread.currentThread().getName() + "]释放B锁");
                }
            }
        });

        t1.start();
        t2.start();
    }
}
