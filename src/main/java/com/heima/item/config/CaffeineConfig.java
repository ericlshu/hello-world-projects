package com.heima.item.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.heima.item.pojo.Item;
import com.heima.item.pojo.ItemStock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-21 15:56
 * @since jdk-11.0.14
 */
@Configuration
public class CaffeineConfig
{
    @Bean
    public Cache<Long, Item> itemCache()
    {
        return Caffeine.newBuilder()
                .initialCapacity(100)   // 缓存初始大小为100
                .maximumSize(10_000)    // 缓存上限为10000
                .build();
    }

    @Bean
    public Cache<Long, ItemStock> stockCache()
    {
        return Caffeine.newBuilder()
                .initialCapacity(100)   // 缓存初始大小为100
                .maximumSize(10_000)    // 缓存上限为10000
                .build();
    }
}
