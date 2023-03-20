package com.eric.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eric.mp.pojo.User;

import java.util.List;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-03-15 12:21
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * find all user
     *
     * @return user list
     */
    List<User> findAll();
}
