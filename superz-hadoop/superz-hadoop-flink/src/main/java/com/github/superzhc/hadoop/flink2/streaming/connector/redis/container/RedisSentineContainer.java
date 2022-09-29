package com.github.superzhc.hadoop.flink2.streaming.connector.redis.container;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.io.IOException;

/**
 * @author superz
 * @create 2022/9/23 16:23
 **/
public class RedisSentineContainer extends RedisContainer {

    private JedisSentinelPool jedisSentinelPool;

    public RedisSentineContainer(JedisSentinelPool jedisSentinelPool) {
        this.jedisSentinelPool = jedisSentinelPool;
    }

    @Override
    public void close() throws IOException {
        if (null != this.jedisSentinelPool) {
            this.jedisSentinelPool.close();
        }
    }

    @Override
    protected Jedis getInstance() {
        return this.jedisSentinelPool.getResource();
    }
}
