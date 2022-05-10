package com.hmdp.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-05-09 0:06
 * @since jdk-11.0.14
 */
@Slf4j
@Component
public class CacheClient
{
    private final StringRedisTemplate stringRedisTemplate;

    public CacheClient(StringRedisTemplate stringRedisTemplate)
    {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    // 线程池对象
    public static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    /**
     * 将任意Java对象序列化为json并存储在string类型的key中，并且可以设置TTL过期时间
     *
     * @param key   缓存key
     * @param value 缓存对象
     * @param ttl   过期时间
     * @param unit  时间单位
     */
    public void set(String key, Object value, Long ttl, TimeUnit unit)
    {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), ttl, unit);
    }

    /**
     * 将任意Java对象序列化为json并存储在string类型的key中，并且可以设置逻辑过期时间，用于处理缓存击穿问题
     *
     * @param key   缓存key
     * @param value 缓存对象
     * @param ttl   逻辑过期时间
     * @param unit  时间单位
     */
    public void setWithLogicalExpire(String key, Object value, Long ttl, TimeUnit unit)
    {
        RedisData redisData = new RedisData(LocalDateTime.now().plusSeconds(unit.toSeconds(ttl)), value);
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    /**
     * 通过缓存空对象解决缓存穿透问题
     *
     * @param prefix     缓存key前缀
     * @param id         缓存对象id
     * @param clazz      缓存对象类型
     * @param dbFallback 数据库查询函数
     * @param ttl        缓存对象过期时间
     * @param unit       缓存对象过期时间单位
     * @param <Result>   缓存对象泛型
     * @param <ID>       缓存对象id泛型
     * @return 缓存对象
     */
    public <Result, ID> Result queryWithCachePenetrationByEmptyObject(
            String prefix, ID id, Class<Result> clazz, Function<ID, Result> dbFallback, Long ttl, TimeUnit unit
    )
    {
        log.info("通过缓存空对象解决缓存穿透问题");
        String key = prefix + id;

        // 1 从redis查询商铺缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        log.debug("json : {}", json);

        // 2.1 命中数据，直接返回
        if (StrUtil.isNotBlank(json))
        {
            return JSONUtil.toBean(json, clazz);
        }
        // 2.2 命中空值，抛出异常
        else if (json != null) // json = ""
        {
            return null;
        }
        // 2.3 未命中数据，也未命中空对象，则查询db
        else // json == null
        {
            Result result = dbFallback.apply(id);
            log.debug("result : {}", result);

            // 3.1 不存在则返回异常
            if (result == null)
            {
                // 将空值写入redis，避免缓存穿透
                stringRedisTemplate.opsForValue().set(key, "", RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            // 3.2 存在则返回数据
            stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(result), ttl, unit);
            this.set(key, result, ttl, unit);

            return result;
        }
    }

    /**
     * 通过逻辑过期解决缓存击穿问题
     *
     * @param prefix     缓存key前缀
     * @param id         缓存对象id
     * @param clazz      缓存对象类型
     * @param dbFallback 数据库查询函数
     * @param ttl        缓存对象过期时间
     * @param unit       缓存对象过期时间单位
     * @param <Result>   缓存对象泛型
     * @param <ID>       缓存对象id泛型
     * @return 缓存对象
     */
    public <Result, ID> Result queryWithCacheBreakdownByLogicalExpire(
            String prefix, ID id, Class<Result> clazz, Function<ID, Result> dbFallback, Long ttl, TimeUnit unit
    )
    {
        log.info("通过逻辑过期解决缓存击穿问题");
        String key = prefix + id;

        // 1 从redis查询商铺缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        log.debug("json : {}", json);

        // 2 未命中数据，返回空值
        if (StrUtil.isBlank(json))
        {
            return null;
        }
        // 3 命中数据，需要先把json反序列化为对象
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        Result result = JSONUtil.toBean((JSONObject) redisData.getData(), clazz);
        LocalDateTime expireTime = redisData.getExpireTime();
        // 4 判断是否过期
        if (LocalDateTime.now().isBefore(expireTime))
        {
            // 未过期，直接返回店铺信息
            return result;
        }
        // 5 已过期，需要缓存重建
        String lockKey = RedisConstants.LOCK_SHOP_KEY + id;
        // 6 获取互斥锁
        boolean lock = obtainMutex(lockKey);
        if (lock)
        {
            // 开启独立线程，实现缓存重建
            CACHE_REBUILD_EXECUTOR.submit(
                    () ->
                    {
                        try
                        {
                            // 查询数据库
                            Result r = dbFallback.apply(id);
                            // 写入redis
                            this.setWithLogicalExpire(key, r, ttl, unit);
                        }
                        catch (Exception e)
                        {
                            throw new RuntimeException(e);
                        }
                        finally
                        {
                            releaseMutex(lockKey);
                        }
                    }
            );
        }
        // 7 返回过期的商铺信息
        return result;
    }

    /**
     * 获取互斥锁
     *
     * @param key 互斥锁key
     * @return true:成功 false:失败
     */
    private boolean obtainMutex(String key)
    {
        return BooleanUtil.isTrue(
                stringRedisTemplate.opsForValue()
                        .setIfAbsent(key, "mutex", RedisConstants.LOCK_SHOP_TTL, TimeUnit.SECONDS));
    }

    /**
     * 释放互斥锁
     *
     * @param key 互斥锁key
     */
    private void releaseMutex(String key)
    {
        stringRedisTemplate.delete(key);
    }
}
