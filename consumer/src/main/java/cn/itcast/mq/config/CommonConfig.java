package cn.itcast.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig
{
    public static final String DIRECT_EXCHANGE = "simple.direct";
    public static final String DIRECT_QUEUE = "simple.queue";
    public static final String ERROR_EXCHANGE = "error.direct";
    public static final String ERROR_QUEUE = "error.queue";
    public static final String ERROR_ROUTING = "error";

    @Bean
    public DirectExchange simpleDirectExchange()
    {
        return new DirectExchange(DIRECT_EXCHANGE, true, false);
    }

    @Bean
    public Queue simpleQueue()
    {
        return QueueBuilder.durable(DIRECT_QUEUE).build();
    }

    @Bean
    public DirectExchange errorExchange()
    {
        return new DirectExchange(ERROR_EXCHANGE);
    }

    @Bean
    public Queue errorQueue()
    {
        return new Queue(ERROR_QUEUE);
    }

    @Bean
    public Binding errorBinding(Queue errorQueue, DirectExchange errorExchange)
    {
        return BindingBuilder.bind(errorQueue).to(errorExchange).with(ERROR_ROUTING);
    }

    /**
     * 在开启重试模式后，重试次数耗尽，如果消息依然失败，则需要有MessageRecoverer接口来处理，它包含三种不同的实现：
     * RejectAndDontRequeueRecoverer：重试耗尽后，直接reject，丢弃消息。默认就是这种方式
     * ImmediateRequeueMessageRecoverer：重试耗尽后，返回nack，消息重新入队
     * RepublishMessageRecoverer：重试耗尽后，将失败消息投递到指定的交换机
     */
    @Bean
    public MessageRecoverer republishMessageRecoverer(RabbitTemplate rabbitTemplate)
    {
        return new RepublishMessageRecoverer(rabbitTemplate, ERROR_EXCHANGE, ERROR_ROUTING);
    }
}
