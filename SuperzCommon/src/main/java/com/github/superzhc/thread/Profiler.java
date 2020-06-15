package com.github.superzhc.thread;

import java.util.concurrent.TimeUnit;

/**
 * 2020年06月15日 superz add
 */
public class Profiler
{
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new ThreadLocal<Long>()
    {
        @Override
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };

    public static final void begin() {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    public static final long end() {
        return System.currentTimeMillis() - TIME_THREADLOCAL.get();
    }

//    public static void main(String[] args) throws Exception {
//        begin();
//        TimeUnit.SECONDS.sleep(1);
//        System.out.println("Cost: " + end() + " mills");
//    }
}
