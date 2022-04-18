package cn.itcast.order;

import cn.itcast.feign.client.UserClient;
import cn.itcast.feign.config.DefaultFeignConfig;
import cn.itcast.feign.config.PatternProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

/**
 * 当定义的FeignClient不在SpringBootApplication的扫描包范围时，这些FeignClient无法使用。
 * 有两种方式解决：
 * 方式一：指定FeignClient所在包 -->> @EnableFeignClients(basePackages = "cn.itcast.feign.clients")
 * 方式二：指定FeignClient字节码 -->> @EnableFeignClients(clients = {UserClient.class})
 * <p>
 * PatternProperties类移动至feign-api中后，不再位于当前启动类所在包及其子包下，需手动引入；
 */
@SpringBootApplication
@EnableFeignClients(clients = {UserClient.class}, defaultConfiguration = DefaultFeignConfig.class)
@Import({PatternProperties.class})
public class OrderApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(OrderApplication.class, args);
    }

    /**
     * 创建RestTemplate并注入Spring容器
     * 添加@LoadBalanced注解，实现负载均衡
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }

    /*
     * 配置当前模块中所有服务采用随机模式负载均衡规则
     */
    /*@Bean
    public IRule randomRule()
    {
        return new RandomRule();
    }*/
}
