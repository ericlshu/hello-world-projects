package com.eric.mq.listener;

import com.eric.mq.util.AppConstant;
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
    // @RabbitListener(queues = {"simple.queue"})
    public void listenSimpleQueueMessage(String msg)
    {
        log.warn("Spring消费者接收到消息 ：[{}]", msg);
    }

    @RabbitListener(queues = {AppConstant.SIMPLE_QUEUE})
    public void listenWorkQueueMessage1(String msg) throws InterruptedException
    {
        log.info("消费者[1]在[{}]接收到消息[{}]", LocalTime.now(), msg);
        Thread.sleep(20);
    }

    @RabbitListener(queues = {AppConstant.SIMPLE_QUEUE})
    public void listenWorkQueueMessage2(String msg) throws InterruptedException
    {
        log.warn("消费者[2]在[{}]接收到消息[{}]", LocalTime.now(), msg);
        Thread.sleep(200);
    }

    @RabbitListener(queues = {AppConstant.FANOUT_QUEUE_1})
    public void listenFanoutQueue1(String msg)
    {
        log.warn("消费者[1]接收到[{}]的消息[{}]", AppConstant.FANOUT_QUEUE_1, msg);
    }

    @RabbitListener(queues = {AppConstant.FANOUT_QUEUE_2})
    public void listenFanoutQueue2(String msg)
    {
        log.warn("消费者[2]接收到[{}]的消息[{}]", AppConstant.FANOUT_QUEUE_2, msg);
    }
}
