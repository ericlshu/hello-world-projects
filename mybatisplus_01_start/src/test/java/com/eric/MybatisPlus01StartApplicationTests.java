package com.eric;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eric.dao.UserDao;
import com.eric.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class MybatisPlus01StartApplicationTests
{
    @Autowired
    private UserDao userDao;

    @Test
    void testGetAll()
    {
        List<User> userList = userDao.selectList(null);
        userList.forEach(System.out::println);
    }

    @Test
    void testSave()
    {
        User user = new User();
        user.setName("黑马程序员");
        user.setPassword("itheima");
        user.setAge(12);
        user.setTel("4006184000");
        userDao.insert(user);
    }

    @Test
    void testDelete()
    {
        userDao.deleteById(1530885118571962369L);
    }

    @Test
    void testUpdate()
    {
        User user = new User();
        user.setId(1L);
        user.setName("Tom888");
        user.setPassword("tom888");
        userDao.updateById(user);
    }

    @Test
    void testGetById()
    {
        User user = userDao.selectById(2L);
        System.out.println(user);
    }

    @Test
    void testGetByPage()
    {
        //IPage对象封装了分页操作相关的数据
        IPage<User> page = new Page<>(1, 3);
        userDao.selectPage(page, null);
        System.out.println("当前页码值：" + page.getCurrent());
        System.out.println("每页显示数：" + page.getSize());
        System.out.println("一共多少页：" + page.getPages());
        System.out.println("一共多少条数据：" + page.getTotal());
        List<User> users = page.getRecords();
        users.forEach(System.out::println);
    }
}
