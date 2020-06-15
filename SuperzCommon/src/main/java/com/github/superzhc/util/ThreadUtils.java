package com.github.superzhc.util;

import java.util.concurrent.TimeUnit;

/**
 * 2020年06月12日 superz add
 */
public class ThreadUtils
{
    public static class Sleep
    {
        public static final void second(long seconds) {
            try {
                TimeUnit.SECONDS.sleep(seconds);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
