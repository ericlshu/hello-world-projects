package com.eric.mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eric.mp.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-03-17 15:11
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestActiveRecord {

    @Test
    public void testSelectById()
    {
        User user = new User();
        user.setId(2L);
        user = user.selectById();
        // user = user.selectById(2L);
        log.info(user.toString());
    }

    @Test
    public void testSelectInsert()
    {
        User user = new User();
        user.setUserName("glen");
        user.setPassword("summer");
        user.setMail("glen@hsbc.com.cn");
        user.setName("李刚");
        user.setAge(30);

        boolean result = user.insert();
        log.info("result : " + result);
    }

    @Test
    public void testUpdateById()
    {
        User user = new User();
        user.setId(8L);
        user.setAge(35);
        boolean result = user.updateById();
        log.info("result : " + result);
    }

    @Test
    public void testDelete()
    {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", "11");
        User user = new User();
        boolean result = user.delete(queryWrapper);
        log.info("result : " + result);
    }

    @Test
    public void testSelectList()
    {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("age", 31);
        User user = new User();
        List<User> result = user.selectList(queryWrapper);
        for (User u : result)
        {
            log.info(u.toString());
        }
    }
}
