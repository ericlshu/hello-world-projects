package com.hmdp.controller;


import com.hmdp.dto.Result;
import com.hmdp.service.IShopTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/shop-type")
public class ShopTypeController
{
    @Resource
    private IShopTypeService typeService;

    @GetMapping("list")
    public Result queryTypeList()
    {
        log.info("查询店铺类型列表");
        // List<ShopType> typeList = typeService.query().orderByAsc("sort").list();
        // log.debug("typeList : {}", typeList);
        // return Result.ok(typeList);
        return typeService.listShopType();
    }

}
