package com.github.superzhc.demo.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * 2020年08月24日 superz add
 */
public class SimpleZkClient
{
    private static final String CONNECT_URL = "";
    private static final int SESSION_TIME_OUT = 2000;

    ZooKeeper zkCli = null;

    public void init() throws Exception {
        zkCli = new ZooKeeper(CONNECT_URL, SESSION_TIME_OUT, new Watcher()
        {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent.getType() + "-------" + watchedEvent.getPath());
                try {
                    zkCli.getChildren("/", true);
                }
                catch (Exception e) {
                }
            }
        });
    }

    public void create() throws Exception {
        zkCli.create("/zk", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    public void isExist() throws Exception {
        Stat exists = zkCli.exists("/zk", false);
        System.out.println(null == exists ? "不存在" : "存在");
    }

    public void getData() throws Exception {
        byte[] data = zkCli.getData("/zk", false, null);
        System.out.println(new String(data));
    }

    public void getChildren() throws Exception {
        List<String> children = zkCli.getChildren("/", false);
        for (String s : children) {
            System.out.println("节点名称：" + s);
        }
    }

    public void delete() throws Exception {
        zkCli.delete("/zk", -1);
        this.isExist();
    }

    public void update() throws Exception {
        Stat stat = zkCli.setData("/zk", "newtest".getBytes(), -1);
        this.getData();
    }
}
