package com.eric;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-05-01 0:07
 * @since jdk-11.0.14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-producer.xml")
public class ProducerTest
{
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendDirectMessage()
    {
        rabbitTemplate.convertAndSend("direct_queue", "Hello, 默认Direct类型交换机消息；");
    }

    @Test
    public void testSendFanoutMessage()
    {
        rabbitTemplate.convertAndSend("fanout_exchange", "", "Hello, Fanout类型交换机消息；");
    }

    @Test
    public void testSendTopicMessage()
    {
        rabbitTemplate.convertAndSend("topic_exchange",
                                      "eric.test.test",
                                      "Hello, Topic类型交换机消息#通配符；");

        rabbitTemplate.convertAndSend("topic_exchange",
                                      "eric.test",
                                      "Hello, Topic类型交换机消息*通配符；");
    }
}
