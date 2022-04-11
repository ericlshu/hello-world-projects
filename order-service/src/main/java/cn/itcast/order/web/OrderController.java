package cn.itcast.order.web;

import cn.itcast.feign.config.PatternProperties;
import cn.itcast.order.pojo.Order;
import cn.itcast.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("order")
public class OrderController
{
    @Resource
    private OrderService orderService;

    @GetMapping("{orderId}")
    public Order queryOrderByUserId(@PathVariable("orderId") Long orderId)
    {
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
}
