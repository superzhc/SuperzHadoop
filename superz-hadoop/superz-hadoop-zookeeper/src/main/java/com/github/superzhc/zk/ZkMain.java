package com.github.superzhc.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * @author superz
 * @create 2021/11/19 11:11
 */
public class ZkMain {
    public static final String CLOUD4CONTROL_CONNECTION = "namenode:2181,datanode1:2181,datanode2:2181";
    public static final String LOCALHOST_CONNECT = "localhost:2181";

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper(CLOUD4CONTROL_CONNECTION, 5000, new MyWatcher());
        System.out.println(zooKeeper.getState());
        countDownLatch.await();
        System.out.println("ZooKeeper session established.");
    }

    static class MyWatcher implements Watcher {
        @Override
        public void process(WatchedEvent event) {
            System.out.println("Receive watched event : " + event);
            if (Event.KeeperState.SyncConnected == event.getState()) {
                countDownLatch.countDown();
            }
        }
    }
}
