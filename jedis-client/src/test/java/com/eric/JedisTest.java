package com.eric;

import com.eric.jedis.util.JedisConnectionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.resps.ScanResult;

import java.util.HashMap;
import java.util.List;
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
        // jedis = new Jedis("120.26.107.127", 6379);
        // jedis.auth("ji9)_Plko");
        jedis = JedisConnectionFactory.getJedis();
        jedis.select(0);
    }

    @AfterEach
    void tearDown()
    {
        if (jedis != null)
        {
            jedis.close();
        }
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

    final static int STR_MAX_LEN = 10 * 1024;
    final static int HASH_MAX_LEN = 500;

    @Test
    void testScan()
    {
        int maxLen = 0;
        long len = 0;

        String cursor = "0";
        do
        {
            // 扫描并获取一部分key
            ScanResult<String> result = jedis.scan(cursor);
            // 记录cursor
            cursor = result.getCursor();
            List<String> list = result.getResult();
            if (list == null || list.isEmpty())
            {
                break;
            }
            // 遍历
            for (String key : list)
            {
                // 判断key的类型
                String type = jedis.type(key);
                switch (type)
                {
                    case "string":
                        len = jedis.strlen(key);
                        maxLen = STR_MAX_LEN;
                        break;
                    case "hash":
                        len = jedis.hlen(key);
                        maxLen = HASH_MAX_LEN;
                        break;
                    case "list":
                        len = jedis.llen(key);
                        maxLen = HASH_MAX_LEN;
                        break;
                    case "set":
                        len = jedis.scard(key);
                        maxLen = HASH_MAX_LEN;
                        break;
                    case "zset":
                        len = jedis.zcard(key);
                        maxLen = HASH_MAX_LEN;
                        break;
                    default:
                        break;
                }
                if (len >= maxLen)
                {
                    System.out.printf("Found big key : %s, type: %s, length or size: %d %n", key, type, len);
                }
            }
        } while (!cursor.equals("0"));
    }

    @Test
    void testBigHash()
    {
        Map<String, String> map = new HashMap<>();
        for (int i = 1; i <= 10_000; i++)
        {
            map.put("key_" + i, "value_" + i);
        }
        jedis.hmset("test:big:hash", map);
    }

    @Test
    void testBigString()
    {
        for (int i = 1; i <= 10_000; i++)
        {
            jedis.set("test:str:key_" + i, "value_" + i);
        }
    }

    @Test
    void testSmallHash()
    {
        int hashSize = 100;
        Map<String, String> map = new HashMap<>(hashSize);
        for (int i = 1; i <= 10_000; i++)
        {
            int k = (i - 1) / hashSize;
            int v = i % hashSize;
            map.put("key_" + v, "value_" + v);
            if (v == 0)
            {
                jedis.hmset("test:small:hash_" + k, map);
            }
        }
    }

    @Test
    void testSingle()
    {
        long b = System.currentTimeMillis();
        for (int i = 1; i <= 10_000; i++)
        {
            jedis.set("test:key_" + i, "value_" + i);
        }
        long e = System.currentTimeMillis();
        System.out.println("time: " + (e - b));
    }

    @Test
    void testBatch()
    {
        String[] arr = new String[2000];
        int j;
        long b = System.currentTimeMillis();
        for (int i = 1; i <= 10_000; i++)
        {
            j = (i % 1000) << 1;
            arr[j] = "test:key_" + i;
            arr[j + 1] = "value_" + i;
            if (j == 0)
            {
                jedis.mset(arr);
            }
        }
        long e = System.currentTimeMillis();
        System.out.println("time: " + (e - b));
    }

    @Test
    void testPipeline()
    {
        // 创建管道
        Pipeline pipeline = jedis.pipelined();
        long b = System.currentTimeMillis();
        for (int i = 1; i <= 10_000; i++)
        {
            // 放入命令到管道
            pipeline.set("test:key_" + i, "value_" + i);
            if (i % 1000 == 0)
            {
                // 每放入1000条命令，批量执行
                pipeline.sync();
            }
        }
        long e = System.currentTimeMillis();
        System.out.println("time: " + (e - b));
    }
}
