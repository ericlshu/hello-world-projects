package com.eric.rabbitmq.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-05-01 0:33
 * @since jdk-11.0.14
 */
public class MessageQueueListener implements MessageListener
{
    @Override
    public void onMessage(Message message)
    {
        System.out.println("message = " + new String(message.getBody()));
    }
}
