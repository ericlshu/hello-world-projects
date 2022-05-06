package com.eric;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-05-06 22:07
 * @since jdk-11.0.14
 */
public class JedisTest
{
    private Jedis jedis;

    @BeforeEach
    void setUp()
    {
        jedis = new Jedis("120.26.107.127", 6379);
        jedis.auth("ji9)_Plko");
        jedis.select(0);
    }

    @Test
    void testString()
    {
        String result = jedis.set("name", "狗蛋");
        System.out.println("result = " + result);
        String name = jedis.get("name");
        System.out.println("name = " + name);
    }

    @Test
    void testHash()
    {
        jedis.hset("user:1", "name", "eric");
        jedis.hset("user:1", "age", "28");
        Map<String, String> user1 = jedis.hgetAll("user:1");
        System.out.println("user1 = " + user1);
    }

    @AfterEach
    void tearDown()
    {
        if (jedis != null)
        {
            jedis.close();
        }
    }
}
