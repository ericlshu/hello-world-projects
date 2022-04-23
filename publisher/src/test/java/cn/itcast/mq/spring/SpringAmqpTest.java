package cn.itcast.mq.spring;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest
{
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage2SimpleQueue() throws InterruptedException
    {
        String message = "Hello, Spring AMQP!";
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        correlationData.getFuture().addCallback(
                result ->
                {
                    assert result != null;
                    if (result.isAck())
                        // 消息成功发送到exchange，返回ack
                        log.info("消息发送成功, ID:[{}]", correlationData.getId());
                    else
                        // 消息发送失败，没有到达交换机，返回nack
                        log.warn("消息发送失败, ID:[{}], 原因[{}]", correlationData.getId(), result.getReason());
                },
                // 消息发送过程中出现异常，没有收到回执
                ex -> log.error("消息发送异常, ID:[{}], 原因[{}]", correlationData.getId(), ex.getMessage())
        );
        rabbitTemplate.convertAndSend("amq.topic", "simple.test", message, correlationData);
        Thread.sleep(1000);
    }
}
