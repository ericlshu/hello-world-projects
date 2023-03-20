package com.eric.mp;


import com.eric.mp.mapper.UserMapper;
import com.eric.mp.pojo.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-03-15 20:46
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MybatisPlusSpringBootTest {

    public static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test()
    {
        List<User> userList = userMapper.selectList(null);
        for (User user : userList)
        {
            LOGGER.info(user);
        }
    }
}
