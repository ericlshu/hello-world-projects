package com.eric.mq.listener;

import com.eric.mq.util.AppConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
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

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = AppConstant.DIRECT_QUEUE_1),
            exchange = @Exchange(value = AppConstant.DIRECT_EXCHANGE, type = ExchangeTypes.DIRECT),
            key = {"blue", "yellow"}
    ))
    public void listenDirectQueue1(String msg)
    {
        log.info("消费者[1]接收到[{}]的消息[{}]", AppConstant.DIRECT_QUEUE_1, msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = AppConstant.DIRECT_QUEUE_2),
            exchange = @Exchange(value = AppConstant.DIRECT_EXCHANGE),
            key = {"blue", "red"}
    ))
    public void listenDirectQueue2(String msg)
    {
        log.warn("消费者[2]接收到[{}]的消息[{}]", AppConstant.DIRECT_QUEUE_2, msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(AppConstant.TOPIC_QUEUE_1),
            exchange = @Exchange(value = AppConstant.TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = "china.#"
    ))
    public void listenTopicQueue1(String msg)
    {
        log.info("消费者[1]接收到[{}]的消息[{}]", AppConstant.TOPIC_QUEUE_1, msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(AppConstant.TOPIC_QUEUE_2),
            exchange = @Exchange(value = AppConstant.TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = "#.news"
    ))
    public void listenTopicQueue2(String msg)
    {
        log.warn("消费者[2]接收到[{}]的消息[{}]", AppConstant.TOPIC_QUEUE_2, msg);
    }
}
