package cn.itcast.mq.config;

import cn.itcast.mq.utils.AppConstant;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig
{
    @Bean
    public DirectExchange simpleDirectExchange()
    {
        return new DirectExchange(AppConstant.DIRECT_EXCHANGE, true, false);
    }

    @Bean
    public Queue simpleQueue()
    {
        return QueueBuilder.durable(AppConstant.DIRECT_QUEUE).build();
    }
}
