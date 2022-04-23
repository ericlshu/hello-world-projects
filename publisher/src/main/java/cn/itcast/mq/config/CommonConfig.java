package cn.itcast.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * 每个RabbitTemplate只能配置一个ReturnCallback，因此需要在项目启动过程中配置：
 */
@Slf4j
@Configuration
public class CommonConfig implements ApplicationContextAware
{
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        // 获取RabbitTemplate
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        // 设置ReturnCallback
        /*
        rabbitTemplate.setReturnCallback(
                (message, replyCode, replyText, exchange, routingKey) ->
                {
                    log.info("消息发送失败，应答码[{}]，原因[{}]，交换机[{}]，路由键[{}]，消息[{}]；",
                             replyCode,
                             replyText,
                             exchange,
                             routingKey,
                             message);
                }
        );
        */
        // 消息成功发送到exchange，但没有路由到queue，调用ReturnCallback
        rabbitTemplate.setReturnsCallback(
                returned ->
                {
                    log.warn("消息发送失败，应答码[{}]，原因[{}]，交换机[{}]，路由键[{}]，消息[{}]；",
                             returned.getReplyCode(),
                             returned.getReplyText(),
                             returned.getExchange(),
                             returned.getRoutingKey(),
                             returned.getMessage());
                    // 若发送失败，可选择重发或告知管理员
                }
        );
    }
}
