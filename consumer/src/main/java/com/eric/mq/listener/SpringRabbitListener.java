package com.eric.mq.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-14 14:48
 * @since jdk-11.0.14
 */
@Slf4j
@Component
public class SpringRabbitListener
{
    @RabbitListener(queues = {"simple.queue"})
    public void listenSimpleQueueMessage(String msg)
    {
        log.warn("Spring消费者接收到消息 ：[{}]", msg);
    }
}
