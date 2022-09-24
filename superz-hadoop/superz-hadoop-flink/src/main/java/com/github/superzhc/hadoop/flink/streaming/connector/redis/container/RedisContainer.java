/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.superzhc.hadoop.flink.streaming.connector.redis.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;

/**
 * Redis command container if we want to connect to a single Redis server or to Redis sentinels
 * If want to connect to a single Redis server, please use the first constructor {@link #RedisContainer(JedisPool)}.
 * If want to connect to a Redis sentinels, please use the second constructor {@link #RedisContainer(JedisSentinelPool)}
 */
public abstract class RedisContainer implements RedisCommandsContainer, Closeable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(RedisContainer.class);

    @Override
    public void open() throws Exception {

        // echo() tries to open a connection and echos back the
        // message passed as argument. Here we use it to monitor
        // if we can communicate with the cluster.

        getInstance().echo("Test");
    }

    /**
     * Returns Jedis instance from the pool.
     *
     * @return the Jedis instance
     */
    protected abstract Jedis getInstance();

    @Override
    public void hset(final String key, final String hashField, final String value, final Integer ttl) {
        try (Jedis jedis = getInstance()) {
            jedis.hset(key, hashField, value);
            if (ttl != null) {
                jedis.expire(key, ttl);
            }
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command HSET to key {} and hashField {} error message {}",
                        key, hashField, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void hincrBy(final String key, final String hashField, final Long value, final Integer ttl) {
        try (Jedis jedis = getInstance()) {
            jedis.hincrBy(key, hashField, value);
            if (ttl != null) {
                jedis.expire(key, ttl);
            }
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command HINCRBY to key {} and hashField {} error message {}",
                        key, hashField, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void rpush(final String listName, final String value) {
        try (Jedis jedis = getInstance()) {
            jedis.rpush(listName, value);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command RPUSH to list {} error message {}",
                        listName, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void lpush(String listName, String value) {
        try (Jedis jedis = getInstance()) {
            jedis.lpush(listName, value);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command LUSH to list {} error message {}",
                        listName, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void sadd(final String setName, final String value) {
        try (Jedis jedis = getInstance()) {
            jedis.sadd(setName, value);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command RPUSH to set {} error message {}",
                        setName, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void publish(final String channelName, final String message) {
        try (Jedis jedis = getInstance()) {
            jedis.publish(channelName, message);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command PUBLISH to channel {} error message {}",
                        channelName, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void set(final String key, final String value) {
        try (Jedis jedis = getInstance()) {
            jedis.set(key, value);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command SET to key {} error message {}",
                        key, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void setex(final String key, final String value, final Integer ttl) {
        try (Jedis jedis = getInstance()) {
            jedis.setex(key, ttl, value);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command SETEX to key {} error message {}",
                        key, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void pfadd(final String key, final String element) {
        try (Jedis jedis = getInstance()) {
            jedis.pfadd(key, element);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command PFADD to key {} error message {}",
                        key, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void zadd(final String key, final String score, final String element) {
        try (Jedis jedis = getInstance()) {
            jedis.zadd(key, Double.valueOf(score), element);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command ZADD to set {} error message {}",
                        key, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void zincrBy(final String key, final String score, final String element) {
        try (Jedis jedis = getInstance()) {
            jedis.zincrby(key, Double.valueOf(score), element);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command ZINCRBY to set {} error message {}",
                        key, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void zrem(final String key, final String element) {
        try (Jedis jedis = getInstance()) {
            jedis.zrem(key, element);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command ZREM to set {} error message {}",
                        key, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void incrByEx(String key, Long value, Integer ttl) {
        try (Jedis jedis = getInstance()) {
            jedis.incrBy(key, value);
            if (ttl != null) {
                jedis.expire(key, ttl);
            }
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis with incrby command to key {} with increment {}  with ttl {} error message {}",
                        key, value, ttl, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void decrByEx(String key, Long value, Integer ttl) {
        try (Jedis jedis = getInstance()) {
            jedis.decrBy(key, value);
            if (ttl != null) {
                jedis.expire(key, ttl);
            }
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis with decrBy command to key {} with decrement {}  with ttl {} error message {}",
                        key, value, ttl, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void incrBy(String key, Long value) {
        try (Jedis jedis = getInstance()) {
            jedis.incrBy(key, value);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis with incrby command to key {} with increment {}  error message {}",
                        key, value, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void decrBy(String key, Long value) {
        try (Jedis jedis = getInstance()) {
            jedis.decrBy(key, value);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis with decrBy command to key {} with increment {}  error message {}",
                        key, value, e.getMessage());
            }
            throw e;
        }
    }
}