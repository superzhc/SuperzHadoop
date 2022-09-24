package com.github.superzhc.hadoop.flink.streaming.connector.redis.container;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.Objects;

/**
 * @author superz
 * @create 2022/9/23 16:20
 **/
public class RedisSingleContainer extends RedisContainer{

    private JedisPool jedisPool;

    public RedisSingleContainer(JedisPool jedisPool){
        Objects.requireNonNull(jedisPool, "Jedis Pool can not be null");
        this.jedisPool=jedisPool;
    }

    @Override
    public void close() throws IOException {
        if(null!=this.jedisPool){
            this.jedisPool.close();
        }
    }

    @Override
    protected Jedis getInstance() {
        return this.jedisPool.getResource();
    }
}
