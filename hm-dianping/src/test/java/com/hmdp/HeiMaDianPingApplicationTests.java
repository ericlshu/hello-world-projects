package com.hmdp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmdp.entity.Shop;
import com.hmdp.entity.ShopType;
import com.hmdp.service.IShopTypeService;
import com.hmdp.service.impl.ShopServiceImpl;
import com.hmdp.utils.RedisConstants;
import com.hmdp.utils.RedisIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    @Resource
    private StringRedisTemplate stringRedisTemplate;

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

    @Resource
    private RedissonClient redissonClient;

    private RLock lock;

    @BeforeEach
    void setUp()
    {
        lock = redissonClient.getLock("order");
    }

    @Test
    void method1() throws InterruptedException
    {
        // 尝试获取锁
        boolean isLock = lock.tryLock(1L, TimeUnit.SECONDS);
        if (!isLock)
        {
            log.error("获取锁失败 .... 1");
            return;
        }
        try
        {
            log.info("获取锁成功 .... 1");
            method2();
            log.info("开始执行业务 ... 1");
        }
        finally
        {
            log.warn("准备释放锁 .... 1");
            lock.unlock();
        }
    }

    void method2()
    {
        // 尝试获取锁
        boolean isLock = lock.tryLock();
        if (!isLock)
        {
            log.error("获取锁失败 .... 2");
            return;
        }
        try
        {
            log.info("获取锁成功 .... 2");
            log.info("开始执行业务 ... 2");
        }
        finally
        {
            log.warn("准备释放锁 .... 2");
            lock.unlock();
        }
    }

    /*
    org.redisson.RedissonBaseLock

    <T> RFuture<T> tryLockInnerAsync(long waitTime, long leaseTime, TimeUnit unit, long threadId, RedisStrictCommand<T> command) {
        return evalWriteAsync(getRawName(), LongCodec.INSTANCE, command,
                              "if (redis.call('exists', KEYS[1]) == 0) then " +
                                      "redis.call('hincrby', KEYS[1], ARGV[2], 1); " +
                                      "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                                      "return nil; " +
                                      "end; " +
                                      "if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " +
                                      "redis.call('hincrby', KEYS[1], ARGV[2], 1); " +
                                      "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                                      "return nil; " +
                                      "end; " +
                                      "return redis.call('pttl', KEYS[1]);",
                              Collections.singletonList(getRawName()), unit.toMillis(leaseTime), getLockName(threadId));
    }

    protected RFuture<Boolean> unlockInnerAsync(long threadId) {
    return evalWriteAsync(getRawName(), LongCodec.INSTANCE, RedisCommands.EVAL_BOOLEAN,
            "if (redis.call('hexists', KEYS[1], ARGV[3]) == 0) then " +
                    "return nil;" +
                    "end; " +
                    "local counter = redis.call('hincrby', KEYS[1], ARGV[3], -1); " +
                    "if (counter > 0) then " +
                    "redis.call('pexpire', KEYS[1], ARGV[2]); " +
                    "return 0; " +
                    "else " +
                    "redis.call('del', KEYS[1]); " +
                    "redis.call('publish', KEYS[2], ARGV[1]); " +
                    "return 1; " +
                    "end; " +
                    "return nil;",
            Arrays.asList(getRawName(), getChannelName()), LockPubSub.UNLOCK_MESSAGE, internalLockLeaseTime, getLockName(threadId));
    }
    */

    @Test
    void testLoadShopData()
    {
        // 1.查询店铺信息
        List<Shop> list = shopService.list();
        // 2.把店铺分组，按照typeId分组，typeId一致的放到一个集合
        Map<Long, List<Shop>> map = list.stream().collect(Collectors.groupingBy(Shop::getTypeId));
        // 3.分批完成写入Redis
        for (Map.Entry<Long, List<Shop>> entry : map.entrySet())
        {
            // 3.1.获取类型id
            Long typeId = entry.getKey();
            String key = RedisConstants.SHOP_GEO_KEY + typeId;
            // 3.2.获取同类型的店铺的集合
            List<Shop> shopList = entry.getValue();
            // 3.3.写入redis GEOADD key 经度 纬度 member
            List<RedisGeoCommands.GeoLocation<String>> locations = new ArrayList<>(shopList.size());
            for (Shop shop : shopList)
            {
                // stringRedisTemplate.opsForGeo().add(key, new Point(shop.getX(), shop.getY()), shop.getId().toString());
                locations.add(new RedisGeoCommands.GeoLocation<>(
                        shop.getId().toString(),
                        new Point(shop.getX(), shop.getY())
                ));
            }
            stringRedisTemplate.opsForGeo().add(key, locations);
        }
    }

    @Test
    void testHyperLogLog()
    {
        String[] values = new String[1_000];
        int j;
        for (int i = 0; i < 100_000; i++)
        {
            j = i % 1000;
            values[j] = "user_" + i;
            if (j == 999)
                // 每1000条数据发送到Redis一次
                stringRedisTemplate.opsForHyperLogLog().add("hll", values);
        }
        // 统计数量
        Long count = stringRedisTemplate.opsForHyperLogLog().size("hll");
        System.out.println("count = " + count);
    }
}
