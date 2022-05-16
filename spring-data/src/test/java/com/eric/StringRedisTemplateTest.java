package com.eric;

import com.eric.redis.pojo.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-05-06 23:36
 * @since jdk-11.0.14
 */
@SpringBootTest
@Slf4j
public class StringRedisTemplateTest
{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // JSON序列化工具
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    void testString()
    {
        stringRedisTemplate.opsForValue().set("name", "二狗");
        Object name = stringRedisTemplate.opsForValue().get("name");
        log.warn("name = {}", name);
    }

    @Test
    void testObject() throws JsonProcessingException
    {
        // 创建对象
        User user = new User("大佬", 28);
        // 手动序列化
        String json = MAPPER.writeValueAsString(user);
        // 写入数据
        stringRedisTemplate.opsForValue().set("user:03", json);
        // 获取数据
        String jsonUser = stringRedisTemplate.opsForValue().get("user:03");
        // 手动反序列化
        User result = MAPPER.readValue(jsonUser, User.class);
        log.warn("result = {}", result);
    }

    @Test
    void testHash()
    {
        stringRedisTemplate.opsForHash().put("user:04", "name", "宝宝");
        stringRedisTemplate.opsForHash().put("user:04", "age", "32");
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries("user:04");
        log.warn("entries = {}", entries);
    }

    @Test
    void testNormal()
    {
        long begin = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++)
        {
            stringRedisTemplate.opsForValue().set("key_" + i, "value_" + i);
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时 = " + (end - begin));
    }

    @Test
    void testMset()
    {
        long begin = System.currentTimeMillis();
        Map<String, String> map = new HashMap<>(1000);
        for (int i = 1; i <= 100000; i++)
        {
            map.put("key_" + i, "value_" + i);
            if (map.size() >= 1000)
            {
                stringRedisTemplate.opsForValue().multiSet(map);
                map.clear();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("mset耗时 = " + (end - begin));
    }

    @Test
    void testPipeline()
    {
        long begin = System.currentTimeMillis();
        for (int i = 0; i <= 100; i++)
        {
            int c = i * 1000;
            stringRedisTemplate.executePipelined((RedisCallback<Object>) connection ->
            {
                for (int j = 1; j <= 1000; j++)
                {
                    int d = c + j;
                    connection.set(
                            ("key_" + d).getBytes(StandardCharsets.UTF_8),
                            ("value_" + d).getBytes(StandardCharsets.UTF_8)
                    );
                }
                return null;
            });
        }
        long end = System.currentTimeMillis();
        System.out.println("Pipeline耗时 = " + (end - begin));
    }

    @Test
    void testBigHash()
    {
        Map<String, String> map = new HashMap<>(1000);
        for (int i = 1; i <= 1000000; i++)
        {
            map.put("key_" + i, "value_" + i);
            if (map.size() >= 1000)
            {
                stringRedisTemplate.opsForHash().putAll("hk", map);
                map.clear();
            }
        }
    }

    @Test
    void testSmallHash()
    {
        Map<String, String> map = new HashMap<>(1000);
        for (int i = 0; i < 1000000; i++)
        {
            map.put("key_" + (i % 500), "value_" + i);
            if (map.size() >= 500)
            {
                stringRedisTemplate.opsForHash().putAll("k" + (i / 500), map);
                map.clear();
            }
        }
    }

    @Test
    void testMSetInCluster()
    {
        Map<String, String> map = new HashMap<>(3);
        map.put("name", "Rose");
        map.put("age", "21");
        map.put("sex", "Female");
        stringRedisTemplate.opsForValue().multiSet(map);

        List<String> strings = stringRedisTemplate.opsForValue().multiGet(Arrays.asList("name", "age", "sex"));
        strings.forEach(System.out::println);
    }
}
