package cn.itcast.order.service;

import cn.itcast.feign.client.UserClient;
import cn.itcast.feign.pojo.User;
import cn.itcast.order.mapper.OrderMapper;
import cn.itcast.order.pojo.Order;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Slf4j
@Service
public class OrderService
{
    @Resource
    private OrderMapper orderMapper;

    @Resource
    private RestTemplate restTemplate;

    public Order queryOrderByIdOld(Long orderId)
    {
        // 1.查询订单
        Order order = orderMapper.findById(orderId);

        // String url = "http://localhost:8081/user/" + order.getUserId();
        // 用加载进eureka的服务名替换主机名和端口
        String url = "http://user-service/user/" + order.getUserId();

        User user = restTemplate.getForObject(url, User.class);
        order.setUser(user);

        // 4.返回
        return order;
    }

    @Resource
    private UserClient userClient;

    public Order queryOrderById(Long orderId)
    {
        // 1.查询订单
        Order order = orderMapper.findById(orderId);
        // 2.用Feign远程调用
        User user = userClient.findById(order.getUserId());
        // 3.封装user到Order
        order.setUser(user);
        // 4.返回
        return order;
    }

    /**
     * Sentinel默认只标记Controller中的方法为资源
     * 如果要标记其它方法，需要利用@SentinelResource注解
     */
    @SentinelResource("goods")
    public void queryGoods()
    {
        log.warn("查询商品！");
    }
}
