package com.hmdp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.hmdp.utils.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService
{
    @Resource
    private RedisTemplate<String, ShopType> redisTemplate;

    @Resource
    ObjectMapper objectMapper;

    @Override
    public Result listShopType()
    {
        List<ShopType> shopTypeList;
        // 从redis中查询商铺列表
        List<ShopType> redisList = redisTemplate.opsForList().range(RedisConstants.CACHE_TYPE_KEY, 0, -1);
        // redisList中的商铺信息为字符串格式，需要进行转换；
        shopTypeList = objectMapper.convertValue(redisList, new TypeReference<>()
        {
        });
        if (shopTypeList.isEmpty())
        {
            // redis未查到结果，则查询db，并存入redis缓存
            shopTypeList = query().orderByAsc("sort").list();
            Assert.notEmpty(shopTypeList, "商铺列表异常！");
            shopTypeList.forEach(value -> log.debug("value = {}", value));
            redisTemplate.opsForList().rightPushAll(RedisConstants.CACHE_TYPE_KEY, shopTypeList);
            redisTemplate.expire(RedisConstants.CACHE_TYPE_KEY, 1, TimeUnit.DAYS);
        }
        return Result.ok(shopTypeList);
    }
}
