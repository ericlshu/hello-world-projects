package cn.itcast.order.service;

import cn.itcast.order.mapper.OrderMapper;
import cn.itcast.order.pojo.Order;
import cn.itcast.order.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class OrderService
{
    @Resource
    private OrderMapper orderMapper;

    @Resource
    private RestTemplate restTemplate;

    public Order queryOrderById(Long orderId)
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
}
