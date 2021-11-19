package com.github.superzhc.zk;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;

/**
 * @author superz
 * @create 2021/11/19 11:41
 */
public class ZkAdmin {
    private ZooKeeper zooKeeper;

    public ZkAdmin(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public String create(String path) {
        return create(path, null);
    }

    public String create(String path, String data) {
        return create(path, data, CreateMode.PERSISTENT);
    }

    /**
     * @param path：需要创建的数据节点的节点路径，注意事项：
     *            1. 无论是同步还是异步接口，ZooKeeper 都不支持递归创建，即无法在父节点不存在的情况下创建一个子节点
     *            2. 如果一个节点已经存在了，那么创建同名节点的时候，会抛出 NodeExistsException
     * @param data
     * @param mode：节点类型，是一个枚举类型，通常有 4 种可选的节点类型：
     *                                  1. PERSISTENT：持久
     *                                  2. PERSISTENT_SEQUENTIAL：持久顺序
     *                                  3. EPHEMERAL：临时
     *                                  4. EPHEMERAL_SEQUENTIAL：临时顺序
     * @return
     */
    public String create(String path, String data, CreateMode mode) {
        try {
            String result = zooKeeper.create(path,
                    null == data ? "".getBytes() : data.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,// 节点的 ACL 策略，OPEN_ACL_UNSAFE表明对这个节点的任何操作都不受权限控制
                    mode);
            return result;
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
            return "create failed";
        }
    }

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper(ZkMain.LOCALHOST_CONNECT, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();

        ZkAdmin admin = new ZkAdmin(zooKeeper);
        //admin.create("/superz/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), "this is a test");
        admin.create("/superz/zk");
    }
}
