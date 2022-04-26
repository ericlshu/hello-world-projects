package com.eric.service;

import com.eric.pojo.User;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-24 14:17
 * @since jdk-11.0.14
 */
public interface UserService
{
    String sayHello();

    User findUserById(int id);
}
