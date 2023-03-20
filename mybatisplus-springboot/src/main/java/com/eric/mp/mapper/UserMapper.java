package com.eric.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eric.mp.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-03-15 20:44
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
