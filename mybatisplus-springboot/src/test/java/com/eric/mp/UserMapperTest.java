package com.eric.mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eric.mp.mapper.UserMapper;
import com.eric.mp.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-03-15 21:09
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserMapperTest.class);

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsert()
    {
        User user = new User();

        user.setUserName("tom");
        user.setPassword("jerry");
        user.setAge(18);
        user.setName("汤姆");
        user.setAddress("Xi'an");
        user.setMail("tom@gmail.com");

        int result = userMapper.insert(user);
        LOGGER.info("result : " + result);
        log.debug(user.toString());
    }

    @Test
    public void updateById()
    {
        User user = new User();
        user.setId(1L);
        user.setAge(18);
        user.setPassword("666");

        int result = userMapper.updateById(user);
        log.info("result : " + result);
        log.info(user.toString());
    }

    @Test
    public void testUpdate()
    {
        User user = new User();
        user.setAge(28);
        user.setPassword("888");
        user.setAddress("home");

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", "eric");
        int update = userMapper.update(user, queryWrapper);
        log.info("update : " + update);

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("age", 21).set("password", "99999").eq("user_name", "tom");
        update = userMapper.update(user, updateWrapper);
        log.info("update : " + update);
    }

    @Test
    public void testDeleteById()
    {
        int result = userMapper.deleteById(11L);
        log.info("result : " + result);
    }

    @Test
    public void testDeleteByMap()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("user_name", "tom");
        map.put("password", "99999");
        int result = userMapper.deleteByMap(map);
        log.info("result : " + result);
    }

    @Test
    public void testDelete()
    {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("age", "31");
        int result = userMapper.delete(queryWrapper);

        User user = new User();
        user.setPassword("ilI7C9PaLNq5eIU9kr55");
        user.setMail("69n0cbo3@yahoo.com");
        queryWrapper = new QueryWrapper<>(user);
        userMapper.delete(queryWrapper);
    }

    @Test
    public void deleteBatchIds()
    {
        userMapper.deleteBatchIds(Arrays.asList(5, 7, 9));
    }

    @Test
    public void testSelect()
    {
        User user = userMapper.selectById(2);
        log.info(user.toString());

        List<User> users = userMapper.selectBatchIds(Arrays.asList(2, 4, 6));
        log.info(users.toString());

        user = new User();
        user.setPassword("NE7GzGO3mRYwD3Xk72Gb");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
        userMapper.selectOne(queryWrapper);
        log.info(user.toString());

        queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("age", 20);
        Long count = userMapper.selectCount(queryWrapper);
        log.info("count : " + count);

        queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_name", "n");
        users = userMapper.selectList(queryWrapper);
        for (User u : users)
        {
            log.info(u.toString());
        }
    }

    @Test
    public void testSelectPage()
    {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_name", "n");
        queryWrapper.orderByDesc("age");
        Page<User> page = new Page<>(2, 2);

        IPage<User> iPage = userMapper.selectPage(page, queryWrapper);

        log.info("total   : " + iPage.getTotal());
        log.info("pages   : " + iPage.getPages());
        log.info("current : " + iPage.getCurrent());
        log.info("size    : " + iPage.getSize());
        log.info("class   : " + iPage.getClass());
        log.info("records : " + iPage.getRecords());
    }

}
