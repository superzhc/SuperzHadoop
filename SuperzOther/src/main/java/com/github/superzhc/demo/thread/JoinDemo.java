package com.github.superzhc.demo.thread;

import java.util.concurrent.TimeUnit;

/**
 * 如果一个线程A执行了thread.join()语句，其含义是：当前线程A等待线程thread线程终止之后菜从thread.join()返回。
 * 2020年07月20日 superz add
 */
public class JoinDemo
{
    public static void main(String[] args) throws InterruptedException {
        Thread pervious=Thread.currentThread();
        for(int i=0;i<10;i++){
            Thread thread=new Thread(new Domino(pervious),String.valueOf(i));
            thread.start();
            pervious=thread;
        }

        TimeUnit.SECONDS.sleep(5);
        System.out.println(Thread.currentThread().getName()+" terminate.");
    }

    static class Domino implements Runnable
    {
        private Thread thread;

        public Domino(Thread thread) {
            this.thread = thread;
        }

        @Override
        public void run() {
            try {
                thread.join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" terminate.");
        }
    }
}
