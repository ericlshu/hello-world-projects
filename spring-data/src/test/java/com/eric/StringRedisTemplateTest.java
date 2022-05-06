package com.eric;

import com.eric.redis.pojo.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

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
}
