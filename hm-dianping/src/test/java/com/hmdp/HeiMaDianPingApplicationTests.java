package com.hmdp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmdp.entity.ShopType;
import com.hmdp.service.IShopTypeService;
import com.hmdp.utils.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;

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

    @Test
    void testOpsForList()
    {
        List<ShopType> shopTypeList;
        List<ShopType> redisList = redisTemplate.opsForList().range(RedisConstants.CACHE_TYPE_KEY, 0, -1);
        shopTypeList = objectMapper.convertValue(redisList, new TypeReference<>(){});
        if (shopTypeList.isEmpty())
        {
            shopTypeList = typeService.query().orderByAsc("sort").list();
            shopTypeList.forEach(value ->log.debug("value = {}", value));
            redisTemplate.opsForList().leftPushAll(RedisConstants.CACHE_TYPE_KEY, shopTypeList);
        }
    }
}
