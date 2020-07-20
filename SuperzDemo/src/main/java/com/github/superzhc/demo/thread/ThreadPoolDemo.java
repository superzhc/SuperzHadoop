package com.github.superzhc.demo.thread;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池的处理流程如下：
 * 1.线程池判断<b>核心线程池</b>里的线程是否都在执行任务。如果不是，则创建一个新的工作线程来执行任务；如果核心线程池里的线程都在执行任务，则进入下个流程
 * 2.线程池判断<b>工作队列</b>是否已经满了。如果工作队列没有满，则将新提交的任务存储在这个工作队列里；如果工作队列满了，则进入下个流程
 * 3.线程池判断线程池的线程是否都处于工作状态。如果没有，则创建一个新的工作线程来执行；如果已经满了，则交给饱和策略来处理这个任务
 * 2020年07月20日 superz add
 */
public class ThreadPoolDemo
{
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 5, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>());

        for(int i=0;i<5;i++){
            // execute用于提交不需要返回值的任务
            executor.execute(new MyPrintThread());

            // submit用于提交需要返回值的任务。线程池会返回一个future类型的对象，通过这个future对象可以判断任务是否执行成功，并且可以通过future的get方法获取返回值，get方法会阻塞当前线程直到任务完成。
        }

        System.out.printf("线程池需要执行的任务数量：%d\n",executor.getTaskCount());

        // 关闭线程池
        executor.shutdown();
    }
}
