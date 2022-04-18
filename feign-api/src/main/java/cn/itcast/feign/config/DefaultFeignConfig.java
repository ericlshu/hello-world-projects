package cn.itcast.feign.config;

import cn.itcast.feign.client.UserClientFallbackFactory;
import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * Description : 自定义Feign的配置
 * 如果是全局配置，则把它放到@EnableFeignClients这个注解中
 * 如果是局部配置，则把它放到@FeignClient这个注解中
 *
 * @author Eric L SHU
 * @date 2022-04-11 21:51
 * @since jdk-11.0.14
 */
public class DefaultFeignConfig
{
    @Bean
    public Logger.Level logLevel()
    {
        return Logger.Level.BASIC;
    }

    /**
     * 步骤二：将UserClientFallbackFactory注册为一个Bean
     */
    @Bean
    public UserClientFallbackFactory userClientFallbackFactory()
    {
        return new UserClientFallbackFactory();
    }
}
