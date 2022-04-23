package cn.itcast.mq.config;

import cn.itcast.mq.utils.AppConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-23 14:58
 * @since jdk-11.0.14
 */
@Configuration
public class ErrorMessageConfig
{
    @Bean
    public DirectExchange errorExchange()
    {
        return new DirectExchange(AppConstant.ERROR_EXCHANGE);
    }

    @Bean
    public Queue errorQueue()
    {
        return new Queue(AppConstant.ERROR_QUEUE);
    }

    @Bean
    public Binding errorBinding(Queue errorQueue, DirectExchange errorExchange)
    {
        return BindingBuilder.bind(errorQueue).to(errorExchange).with(AppConstant.ERROR_ROUTING);
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
        return new RepublishMessageRecoverer(rabbitTemplate, AppConstant.ERROR_EXCHANGE, AppConstant.ERROR_ROUTING);
    }
}
