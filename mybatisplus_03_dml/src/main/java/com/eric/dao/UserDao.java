package com.eric.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eric.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * Description :
 *
 * @author Eric SHU
 */
@Mapper
public interface UserDao extends BaseMapper<User>
{
}
