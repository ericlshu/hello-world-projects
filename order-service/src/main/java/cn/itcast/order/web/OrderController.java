package cn.itcast.order.web;

import cn.itcast.feign.config.PatternProperties;
import cn.itcast.order.pojo.Order;
import cn.itcast.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("order")
public class OrderController
{
    @Resource
    private OrderService orderService;

    @GetMapping("{orderId}")
    public Order queryOrderByUserId(@PathVariable("orderId") Long orderId, @RequestHeader(value = "header", required = false) String header)
    {
        log.info("header + {}", header);
        // 根据id查询订单并返回
        return orderService.queryOrderById(orderId);
    }

    @Resource
    private PatternProperties patternProperties;

    @GetMapping("prop")
    public PatternProperties prop()
    {
        log.debug("patternProperties {}", patternProperties);
        return patternProperties;
    }

    @GetMapping("/query")
    public String queryOrder()
    {
        orderService.queryGoods();
        return "查询订单成功！";
    }

    @GetMapping("/save")
    public String saveOrder()
    {
        orderService.queryGoods();
        return "新增订单成功！";
    }

    @GetMapping("/update")
    public String updateOrder()
    {
        return "更新订单成功！";
    }
}
