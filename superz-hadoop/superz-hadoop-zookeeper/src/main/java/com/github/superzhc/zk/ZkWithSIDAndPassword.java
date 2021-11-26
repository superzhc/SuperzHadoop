package com.github.superzhc.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * @author superz
 * @create 2021/11/19 11:32
 */
public class ZkWithSIDAndPassword {
    private static final String CLOUD4CONTROL_CONNECTION = "namenode:2181,datanode1:2181,datanode2:2181";
    private static final String LOCALHOST_CONNECT = "localhost:2181";

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static CountDownLatch countDownLatch2 = new CountDownLatch(2);

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper(CLOUD4CONTROL_CONNECTION, 5000, new MyWatcher(countDownLatch, Watcher.Event.KeeperState.SyncConnected));
        countDownLatch.await();
        ZooKeeper zooKeeper2 = new ZooKeeper(CLOUD4CONTROL_CONNECTION, 5000, new MyWatcher(countDownLatch2), 1L, "test".getBytes());
        ZooKeeper zooKeeper3 = new ZooKeeper(CLOUD4CONTROL_CONNECTION, 5000, new MyWatcher(countDownLatch2), zooKeeper.getSessionId(), zooKeeper.getSessionPasswd());
        countDownLatch2.await();
    }

    static class MyWatcher implements Watcher {

        private CountDownLatch countDownLatch;
        private Event.KeeperState state = null;

        public MyWatcher(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        public MyWatcher(CountDownLatch countDownLatch, Event.KeeperState state) {
            this.countDownLatch = countDownLatch;
            this.state = state;
        }

        @Override
        public void process(WatchedEvent event) {
            System.out.println("Receive watched event : " + event);
            if (null != state && state == event.getState()) {
                countDownLatch.countDown();
            } else if (null == state) {
                countDownLatch.countDown();
            }
        }
    }
}
