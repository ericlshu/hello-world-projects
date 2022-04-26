package com.eric.controller;

import com.eric.pojo.User;
import com.eric.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description :
 * 当服务的提供方和消费方都设置了超时时间时，以超时时间较小的为准；
 * 超时时间建议配置在服务的提供方；
 *
 * @author Eric L SHU
 * @date 2022-04-24 14:27
 * @since jdk-11.0.14
 */
@RestController
@RequestMapping("/user")
public class UserController
{
    /**
     * 本地注入 @Autowired
     * 远程注入 @Reference
     * 1. 从zookeeper注册中心获取userService的url
     * 2. 进行RPC
     * 3. 将结果封装为一个代理对象，给变量赋值
     */
    @Reference(timeout = 2_000)
    private UserService userService;

    @GetMapping("/sayHello")
    public String sayHello()
    {
        return userService.sayHello();
    }

    @GetMapping("/findUser")
    public User findUser(int id)
    {
        return userService.findUserById(id);
    }
}
