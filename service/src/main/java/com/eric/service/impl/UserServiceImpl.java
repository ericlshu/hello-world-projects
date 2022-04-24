package com.eric.service.impl;

import com.eric.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-24 14:18
 * @since jdk-11.0.14
 */
@Service()
public class UserServiceImpl implements UserService
{
    @Override
    public String sayHello()
    {
        return "Hello, Dubbo!";
    }
}
