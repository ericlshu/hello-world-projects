package com.eric.mq.spring;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest
{
    public static final String QUEUE_NAME = "simple.queue";

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage()
    {
        String message = "Hello, Spring AMQP!";
        rabbitTemplate.convertAndSend(QUEUE_NAME, message);
    }

    @Test
    public void testWorkQueue() throws InterruptedException
    {
        String message = "hello, message_";
        for (int i = 0; i < 50; i++)
        {
            rabbitTemplate.convertAndSend(QUEUE_NAME, message + i);
            Thread.sleep(20);
        }
    }
}
