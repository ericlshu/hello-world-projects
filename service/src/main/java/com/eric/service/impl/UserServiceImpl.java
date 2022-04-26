package com.eric.service.impl;

import com.eric.pojo.User;
import com.eric.service.UserService;
import org.apache.dubbo.config.annotation.Service;

/**
 * Description :
 * DubboService注解将本类提供的服务对外发布，将访问的地址，ip以及路径注册到注册中心
 * <p>
 * 服务消费者在调用服务提供者的时候发生了阻塞、等待的情形，这个时候，服务消费者会一直等待下去。
 * 在某个峰值时刻，大量的请求都在同时请求服务消费者，会造成线程的大量堆积，势必会造成雪崩。
 * dubbo 利用超时机制来解决这个问题，设置一个超时时间，在这个时间段内，无法完成服务访问，则自动断开连接。
 * 使用timeout属性配置超时时间，默认值1000，单位毫秒。
 * <p>
 * 设置了超时时间，在这个时间段内，无法完成服务访问，则自动断开连接。
 * 如果出现网络抖动，则这一次请求就会失败。
 * Dubbo 提供重试机制来避免类似问题的发生。
 * 通过retries属性来设置重试次数。默认为 2 次。
 *
 * @author Eric L SHU
 * @date 2022-04-24 14:18
 * @since jdk-11.0.14
 */
// @Service()
@Service(timeout = 3000, retries = 3)
public class UserServiceImpl implements UserService
{
    @Override
    public String sayHello()
    {
        return "Hello, Dubbo Demo!";
    }

    @Override
    public User findUserById(int id)
    {
        return new User(id, "eric", "1234");
    }
}
