package com.eric;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eric.dao.UserDao;
import com.eric.domain.User;
import com.eric.domain.query.UserQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 * Description :
 *
 * @author Eric SHU
 */
@SpringBootTest
public class MybatisPlusDqlTest
{
    @Autowired
    private UserDao userDao;

    @Test
    void test0()
    {
        List<User> userList = userDao.selectList(null);
        userList.forEach(System.out::println);
    }

    /**
     * 方式一：按条件查询
     */
    @Test
    void test1()
    {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("age", 18);
        List<User> userList = userDao.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 方式二：lambda格式按条件查询
     */
    @Test
    void test2()
    {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().lt(User::getAge, 10);
        List<User> userList = userDao.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 方式三：lambda格式按条件查询
     */
    @Test
    void test3()
    {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.lt(User::getAge, 10);
        List<User> userList = userDao.selectList(lambdaQueryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 并且与或者关系
     */
    @Test
    void test4()
    {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //并且关系：10到30岁之间
        //lqw.lt(User::getAge, 30).gt(User::getAge, 10);
        //或者关系：小于10岁或者大于30岁
        lambdaQueryWrapper.lt(User::getAge, 10).or().gt(User::getAge, 30);
        List<User> userList = userDao.selectList(lambdaQueryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     *
     */
    @Test
    void test5()
    {
        // 模拟页面传递过来的查询数据
        UserQuery userQuery = new UserQuery();
        userQuery.setAge(10);
        userQuery.setAge2(30);

        // null判定

        // LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        // lqw.lt(User::getAge, userQuery.getAge2());
        // if (null != userQuery.getAge())
        // {
        //     lqw.gt(User::getAge, userQuery.getAge());
        // }
        // List<User> userList = userDao.selectList(lqw);
        // userList.forEach(System.out::println);

        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        // 判定第一个参数是否为true，如果为true连接当前条件
        // lqw.lt(null != userQuery.getAge2(), User::getAge, userQuery.getAge2());
        // lqw.gt(null != userQuery.getAge(), User::getAge, userQuery.getAge());
        lqw.lt(null != userQuery.getAge2(), User::getAge, userQuery.getAge2()).gt(null != userQuery.getAge(), User::getAge, userQuery.getAge());
        List<User> userList = userDao.selectList(lqw);
        userList.forEach(System.out::println);
    }

    /**
     * 查询投影:查询的字段控制
     */
    @Test
    void test6()
    {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(User::getId, User::getName, User::getAge);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name", "age", "tel");

        List<User> userList = userDao.selectList(queryWrapper);
        userList.forEach(System.out::println);

        queryWrapper = new QueryWrapper<>();
        queryWrapper.select("count(*) as count, tel");
        queryWrapper.groupBy("tel");
        List<Map<String, Object>> userMap = userDao.selectMaps(queryWrapper);
        System.out.println(userMap);
    }

    /**
     * 条件查询
     */
    @Test
    void test7()
    {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //等同于=
        lambdaQueryWrapper.eq(User::getName, "Jerry").eq(User::getPassword, "jerry");
        User loginUser = userDao.selectOne(lambdaQueryWrapper);
        System.out.println(loginUser);

        lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //范围查询 lt le gt ge eq between
        lambdaQueryWrapper.between(User::getAge, 10, 30);
        List<User> userList = userDao.selectList(lambdaQueryWrapper);
        userList.forEach(System.out::println);

        lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //模糊匹配 like
        lambdaQueryWrapper.likeLeft(User::getName, "J");
        userList = userDao.selectList(lambdaQueryWrapper);
        userList.forEach(System.out::println);
    }
}
