package cn.itcast.hotel.config;

import cn.itcast.hotel.util.AppConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-17 14:56
 * @since jdk-11.0.14
 */
@Configuration
public class MQConfig
{
    @Bean
    public TopicExchange topicExchange()
    {
        return new TopicExchange(AppConstants.HOTEL_EXCHANGE, true, false);
    }

    @Bean
    public Queue insertQueue()
    {
        return new Queue(AppConstants.HOTEL_INSERT_QUEUE, true);
    }

    @Bean
    public Queue deleteQueue()
    {
        return new Queue(AppConstants.HOTEL_DELETE_QUEUE, true);
    }

    @Bean
    public Binding insertQueueBinding()
    {
        return BindingBuilder
                .bind(insertQueue())
                .to(topicExchange())
                .with(AppConstants.HOTEL_INSERT_ROUTING_KEY);
    }

    @Bean
    public Binding deleteQueueBinding()
    {
        return BindingBuilder
                .bind(deleteQueue())
                .to(topicExchange())
                .with(AppConstants.HOTEL_DELETE_ROUTING_KEY);
    }
}
