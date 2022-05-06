package com.eric;

import com.eric.redis.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-05-06 23:36
 * @since jdk-11.0.14
 */
@SpringBootTest
@Slf4j
public class SpringDataRedisTest
{
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testString()
    {
        redisTemplate.opsForValue().set("name", "狗剩");
        Object name = redisTemplate.opsForValue().get("name");
        log.warn("name = {}", name);
    }

    @Test
    void testObject()
    {
        redisTemplate.opsForValue().set("user:02", new User("大佬",28));
        User user = (User) redisTemplate.opsForValue().get("user:02");
        log.warn("user = {}", user);
    }
}
