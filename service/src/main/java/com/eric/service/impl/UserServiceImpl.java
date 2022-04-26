package com.eric.service.impl;

import com.eric.pojo.User;
import com.eric.service.UserService;
import org.apache.dubbo.config.annotation.Service;

/**
 * Description :
 * DubboService注解将本类提供的服务对外发布，将访问的地址，ip以及路径注册到注册中心
 *
 * @author Eric L SHU
 * @date 2022-04-24 14:18
 * @since jdk-11.0.14
 */
// @Service()
@Service
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
