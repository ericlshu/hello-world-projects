package com.heima.item.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heima.item.pojo.Item;
import com.heima.item.pojo.ItemStock;
import com.heima.item.service.IItemService;
import com.heima.item.service.IItemStockService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-22 22:47
 * @since jdk-11.0.14
 */
@Component
public class RedisHandler implements InitializingBean
{
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IItemService itemService;

    @Resource
    private IItemStockService stockService;

    public static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void afterPropertiesSet() throws Exception
    {
        List<Item> itemList = itemService.list();
        for (Item item : itemList)
        {
            stringRedisTemplate.opsForValue().set("item:id:" + item.getId(), MAPPER.writeValueAsString(item));
        }
        List<ItemStock> stockList = stockService.list();
        for (ItemStock stock : stockList)
        {
            stringRedisTemplate.opsForValue().set("item:stock:id:" + stock.getId(), MAPPER.writeValueAsString(stock));
        }
    }
}
