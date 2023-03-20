package com.eric.mp.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eric.mp.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-03-15 18:24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class UserMapperTest {

    public static final Logger LOGGER = LogManager.getLogger();

    @Resource(name = "userMapper")
    private UserMapper userMapper;

    @Test
    public void findSelectList()
    {
        List<User> userList = userMapper.selectList(null);
        for (User user : userList)
        {
            LOGGER.info(user);
        }
    }

    @Test
    public void findSelectPage()
    {

        OrderItem orderItem = new OrderItem();
        orderItem.setColumn("id");
        orderItem.setAsc(false);

        Page<User> userPage = new Page<>();
        userPage.setCurrent(2);
        userPage.setSize(4);
        userPage.addOrder(orderItem);

        IPage<User> iPage = userMapper.selectPage(userPage,null);
        LOGGER.info("total   : " + iPage.getTotal());
        LOGGER.info("pages   : " + iPage.getPages());
        LOGGER.info("current : " + iPage.getCurrent());
        LOGGER.info("size    : " + iPage.getSize());
        LOGGER.info("class   : " + iPage.getClass());
        List<User> records = iPage.getRecords();
        for (User user : records)
        {
            LOGGER.info(user);
        }
    }
}
