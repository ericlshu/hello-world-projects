package com.eric.mq.spring;

import com.eric.mq.util.AppConstant;
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
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage()
    {
        String message = "Hello, Spring AMQP!";
        rabbitTemplate.convertAndSend(AppConstant.SIMPLE_QUEUE, message);
    }

    @Test
    public void testSendWorkQueue() throws InterruptedException
    {
        String message = "hello, message_";
        for (int i = 0; i < 50; i++)
        {
            rabbitTemplate.convertAndSend(AppConstant.SIMPLE_QUEUE, message + i);
            Thread.sleep(20);
        }
    }

    @Test
    public void testSengFanoutExchange()
    {
        rabbitTemplate.convertAndSend(AppConstant.FANOUT_EXCHANGE, null, "Hello, everyone!");
    }
}
