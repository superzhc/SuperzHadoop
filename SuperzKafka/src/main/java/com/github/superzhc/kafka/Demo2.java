package com.github.superzhc.kafka;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 2020年07月24日 superz add
 */
public class Demo2
{
    private static final String BROKER = "ep-003.hadoop:6667";
    private static Integer timeout = 1;

    public static void main(String[] args) {
        ExecutorService consumerExecutors = Executors.newFixedThreadPool(4);

        try {
            for (int i = 0; i < 4; i++) {
                consumerExecutors.execute(new ConsumerThread(BROKER, "superz2"));

                // TimeUnit.MINUTES.sleep(timeout);
                TimeUnit.SECONDS.sleep(5);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("关闭线程池");
            consumerExecutors.shutdownNow();
        }
    }
}
