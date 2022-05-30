package com.eric;

import com.eric.dao.UserDao;
import com.eric.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MybatisPlus03DmlApplicationTests
{
    @Autowired
    private UserDao userDao;

    @Test
    void testSave()
    {
        User user = new User();
        user.setName("测试");
        user.setPassword("test");
        user.setAge(12);
        user.setTel("1888888888");
        userDao.insert(user);
    }

    @Test
    void testDelete()
    {
        userDao.deleteById(2L);
        // List<Long> list = new ArrayList<>();
        // list.add(1402551342481838081L);
        // list.add(1402553134049501186L);
        // list.add(1402553619611430913L);
        // userDao.deleteBatchIds(list);
    }

    @Test
    void testUpdate()
    {
        // User user = new User();
        // user.setId(3L);
        // user.setName("Jock666");
        // user.setVersion(1);
        // userDao.updateById(user);

        // 先通过要修改的数据id将当前数据查询出来
        User user1 = userDao.selectById(3L);
        User user2 = userDao.selectById(3L);

        user2.setName("Jock aaa");
        userDao.updateById(user2);

        user1.setName("Jock bbb");
        userDao.updateById(user1);
    }
}
