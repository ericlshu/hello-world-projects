package com.eric.mq.config;

import com.eric.mq.util.AppConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-14 20:16
 * @since jdk-11.0.14
 */
@Configuration
public class AMQPConfig
{
    /**
     * 声明FanoutExchange交换机
     */
    @Bean
    public FanoutExchange fanoutExchange()
    {
        return new FanoutExchange(AppConstant.FANOUT_EXCHANGE);
    }

    @Bean
    public Queue fanoutQueue1()
    {
        return new Queue(AppConstant.FANOUT_QUEUE_1);
    }

    @Bean
    public Queue fanoutQueue2()
    {
        return new Queue(AppConstant.FANOUT_QUEUE_2);
    }

    @Bean
    public Binding fanoutBinding1(Queue fanoutQueue1, FanoutExchange fanoutExchange)
    {
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }

    @Bean
    public Binding fanoutBinding2(Queue fanoutQueue2, FanoutExchange fanoutExchange)
    {
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }

    @Bean
    public Queue objectQueue()
    {
        return new Queue(AppConstant.OBJECT_QUEUE);
    }

    @Bean
    public MessageConverter jsonMessageConverter()
    {
        return new Jackson2JsonMessageConverter();
    }
}
