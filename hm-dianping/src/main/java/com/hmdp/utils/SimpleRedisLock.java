package com.hmdp.utils;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-05-10 23:19
 * @since jdk-11.0.14
 */
public class SimpleRedisLock implements Lock
{
    private static final String KEY_PREFIX = "lock:";

    private final String name;
    private final StringRedisTemplate stringRedisTemplate;

    public SimpleRedisLock(String name, StringRedisTemplate stringRedisTemplate)
    {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean tryLock(long timeoutSec)
    {
        long threadId = Thread.currentThread().getId();
        System.out.println("threadId = " + threadId);
        Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(
                KEY_PREFIX + name, threadId + "", timeoutSec, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(result);
    }

    @Override
    public void unlock()
    {
        stringRedisTemplate.delete(KEY_PREFIX + name);
    }
}
