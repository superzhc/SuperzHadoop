# Thread Dump

Thread Dump 是非常有用的诊断Java应用问题的工具。每一个Java虚拟机都有及时生成所有线程在某一点状态的thread-dump的能力，虽然各个 Java虚拟机打印的thread dump略有不同，但是大多都提供了每个线程的所有信息，例如: 线程状态、线程 Id、本机 Id、线程名称、堆栈跟踪、优先级。

## 获取 Thread Dump

一般当服务器挂起,崩溃或者性能底下时,就需要抓取服务器的线程堆栈(Thread Dump)用于后续的分析. 在实际运行中，往往一次 dump的信息，还不足以确认问题。为了反映线程状态的动态变化，需要接连多次做threaddump，每次间隔10-20s，建议至少产生三次 dump信息，如果每次 dump都指向同一个问题，我们才确定问题的典型性。

```sh
# JDK自带命令行工具获取PID，再获取ThreadDump:

1. jps 或 ps –ef|grep java (获取PID)
2. jstack [-l ]<pid> | tee -a jstack.log  (获取ThreadDump)
```

## 日志分析

```java
/*
分为两部分：
- 头部信息
- 线程Info信息块
*/

//头部信息  包含 当前时间  jvm信息
2021-01-14 17:00:51
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.171-b11 mixed mode):

//线程info信息块
"ajp-nio-8019-exec-7" #75 daemon prio=5 os_prio=0 tid=0x00007fa0cc37e800 nid=0x2af3 waiting on condition [0x00007fa02eceb000]
   java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000000f183aa30> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
        at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
        at java.lang.Thread.run(Thread.java:748)
```

线程info信息块各个参数的意义:

- 线程名称：`ajp-nio-8019-exec-7`
- 线程类型：`daemon`
- 优先级: 默认是 5
- JVM 线程 id：`tid=0x00007fa0cc37e800`，JVM 内部线程的唯一标识（通过`java.lang.Thread.getId()`获取，通常用自增方式实现。）
- 对应系统线程 id（NativeThread ID）：`nid=0x2af3`，和 top 命令查看的线程 pid 对应，不过一个是 10 进制，一个是 16 进制。（通过命令：`top -H -p pid`，可以查看该进程的所有线程信息）
- 线程状态：`java.lang.Thread.State: WAITING (parking)`
- 线程调用栈信息：用于代码的分析。堆栈信息应该从下向上解读，因为程序调用的顺序是从下向上的。

## 系统线程状态

系统线程有如下状态：

- deadlock
    死锁线程，一般指多个线程调用期间进入了相互资源占用，导致一直等待无法释放的情况。
- runnable
    一般指该线程正在执行状态中，该线程占用了资源，正在处理某个操作，如通过SQL语句查询数据库、对某个文件进行写入等。
- blocked
    线程正处于阻塞状态，指当前线程执行过程中，所需要的资源长时间等待却一直未能获取到，被容器的线程管理器标识为阻塞状态，可以理解为等待资源超时的线程。
- waiting on condition
    线程正处于等待资源或等待某个条件的发生，具体的原因需要结合下面堆栈信息进行分析。

