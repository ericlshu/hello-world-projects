package cn.itcast.redisdemo;

import io.lettuce.core.ReadFrom;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RedisDemoApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(RedisDemoApplication.class, args);
    }

    /**
     * 3.配置主从读写分离
     * MASTER             : 从主节点读取
     * MASTER_PREFERRED   : 优先从master节点读取，master不可用才读取replica
     * REPLICA            : 从slave（replica）节点读取
     * REPLICA _PREFERRED : 优先从slave（replica）节点读取，所有的slave都不可用才读取master
     */
    @Bean
    public LettuceClientConfigurationBuilderCustomizer clientConfigurationBuilderCustomizer()
    {
        return clientConfigurationBuilder -> clientConfigurationBuilder.readFrom(ReadFrom.REPLICA_PREFERRED);
    }
}
