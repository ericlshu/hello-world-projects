package com.eric.mq.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-14 14:39
 * @since jdk-11.0.14
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest
{
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage()
    {
        String queueName = "simple.queue";
        String message = "Hello, Spring AMQP!";
        rabbitTemplate.convertAndSend(queueName,message);
    }
}
