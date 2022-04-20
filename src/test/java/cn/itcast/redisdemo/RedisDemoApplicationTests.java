package cn.itcast.redisdemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
class RedisDemoApplicationTests
{
    @Resource
    private StringRedisTemplate redisTemplate;

    @Test
    void contextLoads()
    {
        redisTemplate.opsForValue().set("num", "666");
    }
}
