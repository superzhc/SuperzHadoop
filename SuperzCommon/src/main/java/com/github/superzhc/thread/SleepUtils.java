package com.github.superzhc.thread;

import java.util.concurrent.TimeUnit;

/**
 * 2020年07月20日 superz add
 */
public class SleepUtils
{
    public static final void second(long seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
