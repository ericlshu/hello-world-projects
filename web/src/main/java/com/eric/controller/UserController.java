package com.eric.controller;

import com.eric.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-24 14:27
 * @since jdk-11.0.14
 */
@RestController
@RequestMapping("/user")
public class UserController
{
    @Autowired
    private UserService userService;

    @GetMapping("/sayHello")
    public String sayHello()
    {
        return userService.sayHello();
    }
}
