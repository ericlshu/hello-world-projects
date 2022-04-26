package com.eric.service.impl;

import com.eric.pojo.User;
import com.eric.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-24 14:18
 * @since jdk-11.0.14
 */
// @Service()
@Slf4j
@Service(version = "v2.0")
public class UserServiceImplNew implements UserService
{
    @Override
    public String sayHello()
    {
        return "Hello, Dubbo Demo!";
    }

    @Override
    public User findUserById(int id)
    {
        log.info("This is User Service V2.0");
        return new User(id, "tom", "jerry");
    }
}
