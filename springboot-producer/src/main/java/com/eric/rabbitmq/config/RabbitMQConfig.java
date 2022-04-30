package com.eric.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-05-01 0:54
 * @since jdk-11.0.14
 */
@Configuration
public class RabbitMQConfig
{

    public static final String EXCHANGE_NAME = "boot_topic_exchange";
    public static final String QUEUE_NAME = "boot_queue";

    /**
     * 声明交换机
     */
    @Bean("bootExchange")
    public Exchange bootExchange()
    {
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    /**
     * 声明队列
     */
    @Bean("bootQueue")
    public Queue bootQueue()
    {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    /*
     * 绑定队列和交互机
     */
    @Bean
    public Binding bindQueueExchange(
            @Qualifier("bootQueue") Queue queue,
            @Qualifier("bootExchange") Exchange exchange)
    {
        return BindingBuilder.bind(queue).to(exchange).with("boot.#").noargs();
    }
}
