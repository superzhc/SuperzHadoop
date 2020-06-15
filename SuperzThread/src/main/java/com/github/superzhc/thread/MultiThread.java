package com.github.superzhc.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * 使用 JMX 来查看一个普通的 Java 程序包含哪些线程
 * 2020年06月12日 superz add
 */
public class MultiThread
{
    public static void main(String[] args) {
        /**
         * [6] Monitor Ctrl-Break
         * [5] Attach Listener
         * [4] Signal Dispatcher    // 分发处理发送给JVM信号的线程
         * [3] Finalizer            // 调用对象finalize方法的线程
         * [2] Reference Handler    // 清除Reference的线程
         * [1] main                 // main线程，用户程序入口
         */
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("[" + threadInfo.getThreadId() + "] " + threadInfo.getThreadName());
        }
    }
}
