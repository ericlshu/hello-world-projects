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
    public void testSendFanoutExchange()
    {
        rabbitTemplate.convertAndSend(AppConstant.FANOUT_EXCHANGE, null, "Hello, everyone!");
    }

    @Test
    public void testSendDirectExchange()
    {
        rabbitTemplate.convertAndSend(AppConstant.DIRECT_EXCHANGE, "blue", "Hello, everything is OK!");
        rabbitTemplate.convertAndSend(AppConstant.DIRECT_EXCHANGE, "yellow", "Warning, something goes wrong!");
        rabbitTemplate.convertAndSend(AppConstant.DIRECT_EXCHANGE, "red", "Danger, keep yourself safe!");
    }

    @Test
    public void testSendTopicExchange()
    {
        rabbitTemplate.convertAndSend(AppConstant.TOPIC_EXCHANGE, "china.news", "喜报！孙悟空大战哥斯拉，胜!");
        rabbitTemplate.convertAndSend(AppConstant.TOPIC_EXCHANGE, "japan.news", "红色警报！日本乱排核废水，导致海洋生物变异，惊现哥斯拉！");
        rabbitTemplate.convertAndSend(AppConstant.TOPIC_EXCHANGE, "china.weather", "今天天气不错，我的心情好极了。");
    }
}
