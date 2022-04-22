package com.heima.item.canal;

import com.github.benmanes.caffeine.cache.Cache;
import com.heima.item.config.RedisHandler;
import com.heima.item.pojo.Item;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

import javax.annotation.Resource;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-23 1:04
 * @since jdk-11.0.14
 */
@Component
@CanalTable("tb_item")
public class ItemHandler implements EntryHandler<Item>
{
    @Resource
    private RedisHandler redisHandler;

    @Resource
    private Cache<Long, Item> itemCache;

    @Override
    public void insert(Item item)
    {
        // JVM进程缓存
        itemCache.put(item.getId(), item);
        // Redis缓存
        redisHandler.saveItem(item);
    }

    @Override
    public void update(Item before, Item after)
    {
        // JVM进程缓存
        itemCache.put(after.getId(), after);
        // Redis缓存
        redisHandler.saveItem(after);
    }

    @Override
    public void delete(Item item)
    {
        // JVM进程缓存
        itemCache.invalidate(item.getId());
        // Redis缓存
        redisHandler.deleteItemById(item.getId());
    }
}
