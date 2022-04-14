package com.eric.mq.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

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
    public static final String QUEUE_NAME = "simple.queue";

    // @RabbitListener(queues = {"simple.queue"})
    public void listenSimpleQueueMessage(String msg)
    {
        log.warn("Spring消费者接收到消息 ：[{}]", msg);
    }

    @RabbitListener(queues = {QUEUE_NAME})
    public void listenWorkQueueMessage1(String msg) throws InterruptedException
    {
        log.info("消费者[1]在[{}]接收到消息[{}]", LocalTime.now(), msg);
        Thread.sleep(20);
    }

    @RabbitListener(queues = {QUEUE_NAME})
    public void listenWorkQueueMessage2(String msg) throws InterruptedException
    {
        log.warn("消费者[2]在[{}]接收到消息[{}]", LocalTime.now(), msg);
        Thread.sleep(200);
    }
}
