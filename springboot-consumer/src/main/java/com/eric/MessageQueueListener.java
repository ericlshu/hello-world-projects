package com.eric;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-05-01 1:10
 * @since jdk-11.0.14
 */
@Component
public class MessageQueueListener
{
    @RabbitListener(queues = "boot_queue")
    public void ListenerQueue(Message message)
    {
        System.out.println(new String(message.getBody()));
    }
}
