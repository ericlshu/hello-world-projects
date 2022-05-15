package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.hmdp.utils.CacheClient;
import com.hmdp.utils.RedisConstants;
import com.hmdp.utils.RedisData;
import com.hmdp.utils.SystemConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService
{
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CacheClient cacheClient;

    // private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    @Override
    public Result queryById(Long id)
    {

        // Shop shop = queryWithCachePenetration(id);
        // Shop shop = queryWithCacheBreakdownByMutex(id);
        // Shop shop = queryWithCacheBreakdownByLogicalExpire(id);

        Shop shop = cacheClient.queryWithCachePenetrationByEmptyObject(
                RedisConstants.CACHE_SHOP_KEY, id, Shop.class,
                this::getById,  // key -> getById(key),
                RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);

        // Shop shop = cacheClient.queryWithCacheBreakdownByLogicalExpire(
        //         RedisConstants.CACHE_SHOP_KEY, id, Shop.class,
        //         this::getById,  // key -> getById(key),
        //         RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);

        if (shop == null) return Result.fail("店铺不存在，请重试！");
        else return Result.ok(shop);
    }

    /*
     * 通过逻辑过期解决缓存击穿问题
     */
    /*private Shop queryWithCacheBreakdownByLogicalExpire(Long id)
    {
        String key = RedisConstants.CACHE_SHOP_KEY + id;

        // 1 从redis查询商铺缓存
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        log.debug("shopJson : {}", shopJson);

        // 2 未命中数据，返回空值
        if (StrUtil.isBlank(shopJson))
        {
            return null;
        }
        // 3 命中数据，需要先把json反序列化为对象
        RedisData redisData = JSONUtil.toBean(shopJson, RedisData.class);
        Shop shop = JSONUtil.toBean((JSONObject) redisData.getData(), Shop.class);
        LocalDateTime expireTime = redisData.getExpireTime();
        // 4 判断是否过期
        if (LocalDateTime.now().isBefore(expireTime))
        {
            // 未过期，直接返回店铺信息
            return shop;
        }
        // 5 已过期，需要缓存重建

        String lockKey = RedisConstants.LOCK_SHOP_KEY + id;
        // 6 获取互斥锁
        boolean lock = obtainMutex(lockKey);
        if (lock)
        {
            try
            {
                // 开启独立线程，实现缓存重建
                CACHE_REBUILD_EXECUTOR.submit(() -> saveShop2Redis(id, 30L));
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
            finally
            {
                // 释放锁
                releaseMutex(lockKey);
            }
        }
        // 7 返回过期的商铺信息
        return shop;
    }*/

    /*
     * 通过互斥锁解决缓存击穿问题
     */
    /*private Shop queryWithCacheBreakdownByMutex(Long id)
    {
        String key = RedisConstants.CACHE_SHOP_KEY + id;

        // 1 从redis查询商铺缓存
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        log.debug("shopJson : {}", shopJson);

        // 2.1 命中数据，直接返回
        if (StrUtil.isNotBlank(shopJson))
        {
            return JSONUtil.toBean(shopJson, Shop.class);
        }
        // 2.2 命中空值，抛出异常
        else if (shopJson != null)
        {
            // shopJson = ""
            return null;
        }
        // 2.3 未命中数据，也未命中空对象，则查询db
        else // shopJson == null
        {
            String lockKey = RedisConstants.LOCK_SHOP_KEY + id;
            try
            {
                // 3 实现缓存重建
                // 3.1 获取互斥锁
                boolean lock = obtainMutex(lockKey);
                // 3.2 获取成功则重建缓存
                if (lock)
                {
                    Shop shop = getById(id);
                    // 模拟重建延时
                    Thread.sleep(200);
                    // 4.1 不存在则返回异常
                    if (shop == null)
                    {
                        // 将空值写入redis，避免缓存穿透
                        stringRedisTemplate.opsForValue().set(key, "", RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                        return null;
                    }
                    // 4.2 存在则返回数据
                    stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop), RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);
                    return shop;
                }
                // 3.3 获取失败则休眠重试
                else
                {
                    Thread.sleep(100);
                    return queryWithCacheBreakdownByMutex(id);
                }
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
            finally
            {
                releaseMutex(lockKey);
            }
        }
    }*/


    /*
     * 通过缓存空对象解决缓存穿透问题
     */
    /*private Shop queryWithCachePenetration(Long id)
    {
        String key = RedisConstants.CACHE_SHOP_KEY + id;

        // 1 从redis查询商铺缓存
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        log.debug("shopJson : {}", shopJson);

        // 2.1 命中数据，直接返回
        if (StrUtil.isNotBlank(shopJson))
        {
            return JSONUtil.toBean(shopJson, Shop.class);
        }
        // 2.2 命中空值，抛出异常
        else if (shopJson != null)
        {
            // shopJson = ""
            return null;
        }
        // 2.3 未命中数据，也未命中空对象，则查询db
        else // shopJson == null
        {
            Shop shop = getById(id);
            log.debug("shop : {}", shop);

            // 3.1 不存在则返回异常
            if (shop == null)
            {
                // 将空值写入redis，避免缓存穿透
                stringRedisTemplate.opsForValue().set(key, "", RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            // 3.2 存在则返回数据
            stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop), RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);

            return shop;
        }
    }*/

    @Override
    @Transactional
    public Result update(Shop shop)
    {
        log.debug("shop : {}", shop);
        Long id = shop.getId();
        if (id == null)
        {
            return Result.fail("店铺id不能为空");
        }
        // 1.更新数据库
        updateById(shop);
        // 2.删除缓存
        stringRedisTemplate.delete(RedisConstants.CACHE_SHOP_KEY + id);
        return Result.ok();
    }

    @Override
    public Result queryShopByType(Integer typeId, Integer current, Double x, Double y)
    {
        // 1.判断是否需要根据坐标查询
        if (x == null || y == null)
        {
            // 不需要坐标查询，按数据库查询
            Page<Shop> page = query()
                    .eq("type_id", typeId)
                    .page(new Page<>(current, SystemConstants.DEFAULT_PAGE_SIZE));
            return Result.ok(page.getRecords());
        }
        // 2.计算分页参数
        int from = (current - 1) * SystemConstants.DEFAULT_PAGE_SIZE;
        int to = current * SystemConstants.DEFAULT_PAGE_SIZE;
        // 3.查询redis、按照距离排序、分页。结果：shopId、distance
        String key = RedisConstants.SHOP_GEO_KEY + typeId;
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = stringRedisTemplate.opsForGeo().search(
                key,
                GeoReference.fromCoordinate(x, y),
                new Distance(5000),
                RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs().includeDistance().limit(to));
        // 4.解析出id
        if (results == null)
            return Result.ok(Collections.emptyList());
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = results.getContent();
        // 没有下一页了，结束
        if (content.size() <= from)
            return Result.ok(Collections.emptyList());
        // 4.1.截取 from ~ end的部分
        List<Long> ids = new ArrayList<>(content.size());
        Map<String, Distance> distanceMap = new HashMap<>(content.size());
        content.stream().skip(from)
                .forEach(result ->
                         {
                             // 4.2.获取店铺id
                             String shopIdStr = result.getContent().getName();
                             ids.add(Long.valueOf(shopIdStr));
                             // 4.3.获取距离
                             Distance distance = result.getDistance();
                             distanceMap.put(shopIdStr, distance);
                         });
        // 5.根据id查询Shop
        String idStr = StrUtil.join(",", ids);
        List<Shop> shopList = query().in("id", ids)
                .last("ORDER BY FIELD(id," + idStr + ")")
                .list();
        shopList.forEach(shop -> shop.setDistance(distanceMap.get(shop.getId().toString()).getValue()));
        // 6.返回
        return Result.ok(shopList);
    }

    /*private boolean obtainMutex(String key)
    {
        return BooleanUtil.isTrue(stringRedisTemplate.opsForValue().setIfAbsent(key, "mutex", RedisConstants.LOCK_SHOP_TTL, TimeUnit.SECONDS));
    }

    private void releaseMutex(String key)
    {
        stringRedisTemplate.delete(key);
    }*/

    public void saveShop2Redis(Long id, Long expireSeconds)
    {
        Shop shop = getById(id);
        RedisData redisData = new RedisData(LocalDateTime.now().plusSeconds(expireSeconds), shop);
        stringRedisTemplate.opsForValue().set(RedisConstants.CACHE_SHOP_KEY + id, JSONUtil.toJsonStr(redisData));
    }
}
