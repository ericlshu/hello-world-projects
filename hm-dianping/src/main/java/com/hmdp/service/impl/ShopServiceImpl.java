package com.hmdp.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService
{
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryById(Long id)
    {

        // Shop shop = queryWithCachePenetration(id);
        Shop shop = queryWithCacheBreakdownByMutex(id);
        if (shop == null)
            return Result.fail("店铺不存在，请重试！");
        else
            return Result.ok(shop);
    }

    /**
     * 通过互斥锁解决缓存击穿问题
     */
    private Shop queryWithCacheBreakdownByMutex(Long id)
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
    }

    /**
     * 通过缓存空对象解决缓存穿透问题
     */
    private Shop queryWithCachePenetration(Long id)
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
    }

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

    private boolean obtainMutex(String key)
    {
        return BooleanUtil.isTrue(stringRedisTemplate.opsForValue().setIfAbsent(key, "mutex", 10, TimeUnit.SECONDS));
    }

    private void releaseMutex(String key)
    {
        stringRedisTemplate.delete(key);
    }
}
