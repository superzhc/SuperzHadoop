package com.github.superzhc.demo.thread;

/**
 * 2020年07月20日 superz add
 */
public class MyPrintThread implements Runnable
{
    @Override
    public void run() {
        System.out.printf("[%s]执行开始\n", Thread.currentThread().getName());
        try {
            Thread.sleep(1000*3);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("[%s]执行完成\n", Thread.currentThread().getName());
    }
}
