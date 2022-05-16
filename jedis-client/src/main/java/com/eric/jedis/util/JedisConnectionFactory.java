package com.eric.jedis.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-05-06 23:12
 * @since jdk-11.0.14
 */
public class JedisConnectionFactory
{
    private static final JedisPool JEDIS_POOL;

    static
    {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMinIdle(0);
        JEDIS_POOL = new JedisPool(jedisPoolConfig, "192.168.3.33", 6379, 1000);
    }

    public static Jedis getJedis()
    {
        return JEDIS_POOL.getResource();
    }
}
