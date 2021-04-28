package com.github.superzhc.demo.thread;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 队列同步器（AbstractQueuedSynchronizer，简称 AQS）
 * 主要的使用方式是<b>继承</b>，子类通过继承同步器并实现它的抽象方法来管理同步状态，
 * 在抽象方法的实现过程中免不了要对同步状态进行更改，这时就需要使用同步器提供的3个方法：（以下方法提供了访问或修改同步状态）
 * 1.getState()：获取当前同步状态
 * 2.setState(int newState)：设置当前同步状态
 * 3.compareAndSetState(int expect,int update)：使用CAS设置当前状态，该方法能够保证状态设置的原子性
 * 来进行操作，因为它们能够保证状态的改变是安全的。
 * 同步器的设计是基于<b>模板方法</b>模式的，也就是说，使用者需要继承同步器并重写指定的方法，随后将同步器组合在自定义同步组件实现中，并调用同步器提供的模板方法，而这些模板方法将会调用使用者重写的方法。
 * 2020年07月21日 superz add
 */
public class AQSDemo extends AbstractQueuedSynchronizer
{
    /* AQS提供的可重写方法如下： */

    /**
     * 当前同步器是否在独占模式下被线程占用，一般该方法表示是否被当当前线程所独占
     * @return
     */
    @Override
    protected boolean isHeldExclusively() {
        return super.isHeldExclusively();
    }

    // region===========================独占式======================================

    /**
     * 独占式获取同步状态，实现该方法需要查询当前状态并判断同步状态是否符合预期，然后再进行CAS设置同步状态
     * @param arg
     * @return
     */
    @Override
    protected boolean tryAcquire(int arg) {
        return super.tryAcquire(arg);
    }

    /**
     * 独占式释放同步状态，等待获取同步状态的线程将有机会获取同步状态
     * @param arg
     * @return
     */
    @Override
    protected boolean tryRelease(int arg) {
        return super.tryRelease(arg);
    }

    // endregion========================独占式======================================

    // region===========================共享式======================================

    /**
     * 共享式获取同步状态，返回大于等于0的值，表示获取成功，反之，获取失败
     * @param arg
     * @return
     */
    @Override
    protected int tryAcquireShared(int arg) {
        return super.tryAcquireShared(arg);
    }

    /**
     * 共享式释放同步状态
     * @param arg
     * @return
     */
    @Override
    protected boolean tryReleaseShared(int arg) {
        return super.tryReleaseShared(arg);
    }

    // endregion========================共享式======================================
}
