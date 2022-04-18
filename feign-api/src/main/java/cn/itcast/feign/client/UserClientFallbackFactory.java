package cn.itcast.feign.client;

import cn.itcast.feign.pojo.User;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * Description : 步骤一：在feign-api项目中定义类，实现FallbackFactory
 *
 * @author Eric L SHU
 * @date 2022-04-18 14:34
 * @since jdk-11.0.14
 */
@Slf4j
public class UserClientFallbackFactory implements FallbackFactory<UserClient>
{
    @Override
    public UserClient create(Throwable cause)
    {
        return id ->
        {
            log.warn("查询用户异常",cause);
            return new User();
        };
    }

    /*public UserClient create(Throwable cause)
    {
        return new UserClient()
        {
            @Override
            public User findById(Long id)
            {
                log.warn("查询用户异常",cause);
                return new User();
            }
        };
    }*/
}
