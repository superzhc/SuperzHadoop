package com.github.superzhc.hadoop.flink2.streaming.connector.redis.container;

import com.github.superzhc.hadoop.flink2.streaming.connector.redis.config.FlinkJedisClusterConfig;
import com.github.superzhc.hadoop.flink2.streaming.connector.redis.config.FlinkJedisConfigBase;
import com.github.superzhc.hadoop.flink2.streaming.connector.redis.config.FlinkJedisPoolConfig;
import com.github.superzhc.hadoop.flink2.streaming.connector.redis.config.FlinkJedisSentinelConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Objects;

/**
 * The builder for {@link RedisCommandsContainer}.
 */
public class RedisCommandsContainerBuilder {

    /**
     * Initialize the {@link RedisCommandsContainer} based on the instance type.
     *
     * @param flinkJedisConfigBase configuration base
     * @return @throws IllegalArgumentException if jedisPoolConfig, jedisClusterConfig and jedisSentinelConfig are all null
     */
    public static RedisCommandsContainer build(FlinkJedisConfigBase flinkJedisConfigBase) {
        if (flinkJedisConfigBase instanceof FlinkJedisPoolConfig) {
            FlinkJedisPoolConfig flinkJedisPoolConfig = (FlinkJedisPoolConfig) flinkJedisConfigBase;
            return RedisCommandsContainerBuilder.build(flinkJedisPoolConfig);
        } else if (flinkJedisConfigBase instanceof FlinkJedisClusterConfig) {
            FlinkJedisClusterConfig flinkJedisClusterConfig = (FlinkJedisClusterConfig) flinkJedisConfigBase;
            return RedisCommandsContainerBuilder.build(flinkJedisClusterConfig);
        } else if (flinkJedisConfigBase instanceof FlinkJedisSentinelConfig) {
            FlinkJedisSentinelConfig flinkJedisSentinelConfig = (FlinkJedisSentinelConfig) flinkJedisConfigBase;
            return RedisCommandsContainerBuilder.build(flinkJedisSentinelConfig);
        } else {
            throw new IllegalArgumentException("Jedis configuration not found");
        }
    }

    /**
     * Builds container for single Redis environment.
     *
     * @param jedisPoolConfig configuration for JedisPool
     * @return container for single Redis environment
     * @throws NullPointerException if jedisPoolConfig is null
     */
    public static RedisCommandsContainer build(FlinkJedisPoolConfig jedisPoolConfig) {
        Objects.requireNonNull(jedisPoolConfig, "Redis pool config should not be Null");

        GenericObjectPoolConfig genericObjectPoolConfig = getGenericObjectPoolConfig(jedisPoolConfig);

        JedisPool jedisPool = new JedisPool(genericObjectPoolConfig, jedisPoolConfig.getHost(),
                jedisPoolConfig.getPort(), jedisPoolConfig.getConnectionTimeout(), jedisPoolConfig.getPassword(),
                jedisPoolConfig.getDatabase());
        return new RedisSingleContainer(jedisPool);
    }

    /**
     * Builds container for Redis Cluster environment.
     *
     * @param jedisClusterConfig configuration for JedisCluster
     * @return container for Redis Cluster environment
     * @throws NullPointerException if jedisClusterConfig is null
     */
    public static RedisCommandsContainer build(FlinkJedisClusterConfig jedisClusterConfig) {
        Objects.requireNonNull(jedisClusterConfig, "Redis cluster config should not be Null");

        GenericObjectPoolConfig genericObjectPoolConfig = getGenericObjectPoolConfig(jedisClusterConfig);

        JedisCluster jedisCluster = new JedisCluster(jedisClusterConfig.getNodes(),
                jedisClusterConfig.getConnectionTimeout(),
                jedisClusterConfig.getConnectionTimeout(),
                jedisClusterConfig.getMaxRedirections(),
                jedisClusterConfig.getPassword(),
                genericObjectPoolConfig);
        return new RedisClusterContainer(jedisCluster);
    }

    /**
     * Builds container for Redis Sentinel environment.
     *
     * @param jedisSentinelConfig configuration for JedisSentinel
     * @return container for Redis sentinel environment
     * @throws NullPointerException if jedisSentinelConfig is null
     */
    public static RedisCommandsContainer build(FlinkJedisSentinelConfig jedisSentinelConfig) {
        Objects.requireNonNull(jedisSentinelConfig, "Redis sentinel config should not be Null");

        GenericObjectPoolConfig genericObjectPoolConfig = getGenericObjectPoolConfig(jedisSentinelConfig);

        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(jedisSentinelConfig.getMasterName(),
                jedisSentinelConfig.getSentinels(), genericObjectPoolConfig,
                jedisSentinelConfig.getConnectionTimeout(), jedisSentinelConfig.getSoTimeout(),
                jedisSentinelConfig.getPassword(), jedisSentinelConfig.getDatabase());
        return new RedisSentineContainer(jedisSentinelPool);
    }

    public static GenericObjectPoolConfig getGenericObjectPoolConfig(FlinkJedisConfigBase jedisConfig) {
        GenericObjectPoolConfig genericObjectPoolConfig = jedisConfig.getTestWhileIdle() ? new JedisPoolConfig() : new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(jedisConfig.getMaxIdle());
        genericObjectPoolConfig.setMaxTotal(jedisConfig.getMaxTotal());
        genericObjectPoolConfig.setMinIdle(jedisConfig.getMinIdle());
        genericObjectPoolConfig.setTestOnBorrow(jedisConfig.getTestOnBorrow());
        genericObjectPoolConfig.setTestOnReturn(jedisConfig.getTestOnReturn());

        return genericObjectPoolConfig;
    }
}
