package com.hmdp.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-05-12 22:16
 * @since jdk-11.0.14
 */
@Configuration
public class RedissonConfig
{
    @Bean
    public RedissonClient redissonClient()
    {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://120.26.107.127:6379")
                .setPassword("ji9)_Plko");
        return Redisson.create(config);
    }
}
