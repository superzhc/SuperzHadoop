package com.github.superzhc.demo.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 2020年07月24日 superz add
 */
public class InterruptedDemo
{
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Runnable runnable = new Runnable()
        {
            @Override
            public void run() {
                System.out.println("进入线程：" + Thread.currentThread().getName());

                int i = 0;
                while (!Thread.currentThread().isInterrupted()) {
                    if (++i % 50000000 == 0)
                        System.out.println(i + ". doing...");
                }

                System.out.println("执行完成");
            }
        };

//        Thread t=new Thread(runnable);
//        t.start();

        executor.execute(runnable);

        try {
            TimeUnit.SECONDS.sleep(5);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("操作完成");

        // 中断线程
//        t.interrupt();

//        // executor.shutdown();//中断所有没有正在执行任务的线程

        executor.shutdownNow();//尝试停止所有正在执行或暂停任务的线程，并返回等待执行任务的列表

        System.out.println("关闭");
    }
}
