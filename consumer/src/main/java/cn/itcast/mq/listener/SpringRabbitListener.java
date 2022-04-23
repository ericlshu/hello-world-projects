package cn.itcast.mq.listener;

import cn.itcast.mq.utils.AppConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringRabbitListener
{
    @RabbitListener(queues = AppConstant.DIRECT_QUEUE)
    public void listenSimpleQueue(String msg)
    {
        log.debug("消费者接收到[{}]的消息[{}]。", AppConstant.DIRECT_QUEUE, msg);
        // int i = 1 / 0;
        log.info("消费者处理消息[{}]成功！", msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = AppConstant.DL_QUEUE, durable = AppConstant.TRUE),
            exchange = @Exchange(name = AppConstant.DL_EXCHANGE),
            key = AppConstant.DL_ROUTING
    ))
    public void listenDlQueue(String msg)
    {
        log.debug("消费者接收到[{}]的消息[{}]。", AppConstant.DL_QUEUE, msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = AppConstant.DELAY_QUEUE, durable = AppConstant.TRUE),
            exchange = @Exchange(name = AppConstant.DELAY_EXCHANGE, delayed = AppConstant.TRUE),
            key = AppConstant.DELAY_ROUTING
    ))
    public void listenDelayQueue(String msg)
    {
        log.debug("消费者接收到[{}]的消息[{}]。", AppConstant.DELAY_QUEUE, msg);
    }
}
