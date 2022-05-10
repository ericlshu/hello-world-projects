package com.hmdp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmdp.entity.ShopType;
import com.hmdp.service.IShopTypeService;
import com.hmdp.service.impl.ShopServiceImpl;
import com.hmdp.utils.RedisConstants;
import com.hmdp.utils.RedisIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootTest
class HeiMaDianPingApplicationTests
{
    @Autowired
    private RedisTemplate<String, ShopType> redisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Resource
    private IShopTypeService typeService;

    @Resource
    private ShopServiceImpl shopService;

    @Resource
    private RedisIdWorker redisIdWorker;

    private final ExecutorService service = Executors.newFixedThreadPool(500);

    @Test
    void testOpsForList()
    {
        List<ShopType> shopTypeList;
        List<ShopType> redisList = redisTemplate.opsForList().range(RedisConstants.CACHE_TYPE_KEY, 0, -1);
        shopTypeList = objectMapper.convertValue(redisList, new TypeReference<>()
        {
        });
        if (shopTypeList.isEmpty())
        {
            shopTypeList = typeService.query().orderByAsc("sort").list();
            shopTypeList.forEach(value -> log.debug("value = {}", value));
            redisTemplate.opsForList().leftPushAll(RedisConstants.CACHE_TYPE_KEY, shopTypeList);
        }
    }

    @Test
    void testSaveShop2redis()
    {
        shopService.saveShop2Redis(1L, 20L);
    }


    @Test
    void testIdWorker() throws InterruptedException
    {
        CountDownLatch latch = new CountDownLatch(100);
        Runnable task = () ->
        {
            for (int i = 0; i < 100; i++)
            {
                long id = redisIdWorker.nextId("user");
                log.info("id = {}", id);
            }
            latch.countDown();
        };
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 100; i++)
        {
            service.submit(task);
        }
        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("(end-begin) = " + (end - begin));
    }
}
