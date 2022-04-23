package cn.itcast.mq.config;

import cn.itcast.mq.utils.AppConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-23 14:59
 * @since jdk-11.0.14
 */
@Configuration
public class TTLMessageConfig
{
    @Bean
    public DirectExchange ttlDirectExchange()
    {
        return new DirectExchange(AppConstant.TTL_EXCHANGE);
    }

    @Bean
    public Queue ttlQueue()
    {
        return QueueBuilder
                .durable(AppConstant.TTL_QUEUE)                 // 指定队列名称，并持久化
                .ttl(10_000)                                    // 设置队列的超时时间，10秒
                .deadLetterExchange(AppConstant.DL_EXCHANGE)    // 指定死信交换机
                .deadLetterRoutingKey(AppConstant.DL_ROUTING)   // 指定死信RoutingKey
                .build();
    }

    @Bean
    public Binding ttlBinding()
    {
        return BindingBuilder
                .bind(ttlQueue())
                .to(ttlDirectExchange())
                .with(AppConstant.TTL_ROUTING);
    }
}
