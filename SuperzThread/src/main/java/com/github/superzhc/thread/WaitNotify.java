package com.github.superzhc.thread;

import com.github.superzhc.util.ThreadUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 2020年06月15日 superz add
 */
public class WaitNotify
{
    static boolean flag = true;
    static Object lock = new Object();

    public static void main(String[] args) throws Exception {
        Thread waitThread = new Thread(new Wait(), "WaitThread");
        waitThread.start();
        TimeUnit.SECONDS.sleep(1);
        Thread notifyThread = new Thread(new Notify(), "NotifyThread");
        notifyThread.start();
    }

    static class Wait implements Runnable
    {
        @Override
        public void run() {
            synchronized (lock) {
                while (flag) {
                    try {
                        System.out.println(Thread.currentThread() + " flag is true. wait @ "
                                + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                        lock.wait();
                    }
                    catch (Exception e) {
                    }

                    System.out.println(Thread.currentThread() + " flag is false. running @ "
                            + new SimpleDateFormat("HH:mm:ss").format(new Date()));

                }
            }
        }
    }

    static class Notify implements Runnable
    {
        @Override
        public void run() {
            synchronized (lock) {
                System.out.println(Thread.currentThread() + " hold lock. notify @ "
                        + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                lock.notifyAll();
                flag = false;
                ThreadUtils.Sleep.second(5);
            }

            // ThreadUtils.Sleep.second(3);

            synchronized (lock) {
                System.out.println(Thread.currentThread() + " hold lock again. notify @ "
                        + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                ThreadUtils.Sleep.second(5);
            }
        }
    }
}
