package com.eric;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

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
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testString()
    {
        redisTemplate.opsForValue().set("name", "二狗");
        Object name = redisTemplate.opsForValue().get("name");
        log.warn("name = {}", name);
    }
}
