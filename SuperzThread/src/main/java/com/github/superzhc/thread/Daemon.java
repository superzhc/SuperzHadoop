package com.github.superzhc.thread;

import com.github.superzhc.util.ThreadUtils;

/**
 * 2020年06月12日 superz add
 */
public class Daemon
{
    public static void main(String[] args) {
        System.out.println("主线程开始运行");
        Thread thread = new Thread(new DaemonRunner(), "DaemonRunner");
        thread.setDaemon(true);
        thread.start();
        System.out.println("主线程结束运行");
    }

    static class DaemonRunner implements Runnable
    {
        @Override
        public void run() {
            try {
                System.out.println("守护进程开始运行");
                ThreadUtils.Sleep.second(10);
                System.out.println("守护进程结束运行");
            }
            finally {
                System.out.println("DaemonThread finally run.");
            }
        }
    }
}
