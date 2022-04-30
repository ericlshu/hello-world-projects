package com.eric;

import com.eric.rabbitmq.config.RabbitMQConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-05-01 1:01
 * @since jdk-11.0.14
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProducerTest
{
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSend()
    {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                "boot.test",
                "Hello, SpringBoot之Topic交换机消息；");
    }
}
