package com.eric.mp.mapper;

import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.eric.mp.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-03-15 12:24
 */
public class UserMapperTest {

    public static final Logger LOGGER = LogManager.getLogger();

    InputStream resourceAsStream;

    @Before
    public void before() throws IOException
    {
        String resource = "mybatis-config.xml";
        resourceAsStream = Resources.getResourceAsStream(resource);
    }

    @Test
    public void findMybatis()
    {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = userMapper.findAll();
        for (User user : userList)
        {
            LOGGER.info(user);
        }
    }

    @Test
    public void findMybatisPlus()
    {
        SqlSessionFactory sqlSessionFactory = new MybatisSqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        // List<User> userList = userMapper.findAll();
        List<User> userList = userMapper.selectList(null);

        for (User user : userList)
        {
            LOGGER.info(user);
        }
    }
}
