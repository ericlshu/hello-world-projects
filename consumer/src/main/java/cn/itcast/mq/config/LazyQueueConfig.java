package cn.itcast.mq.config;

import cn.itcast.mq.utils.AppConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-23 17:18
 * @since jdk-11.0.14
 */
@Configuration
public class LazyQueueConfig
{
    @Bean
    public Queue lazyQueue()
    {
        return QueueBuilder
                .durable(AppConstant.LAZY_QUEUE)
                .lazy()
                .build();
    }

    @Bean
    public Queue normalQueue()
    {
        return QueueBuilder
                .durable(AppConstant.NORMAL_QUEUE)
                .build();
    }
}
