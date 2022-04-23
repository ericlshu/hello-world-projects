package cn.itcast.mq.listener;

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
    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueue(String msg)
    {
        log.debug("消费者接收到simple.queue的消息[{}]。", msg);
        // int i = 1 / 0;
        log.info("消费者处理消息[{}]成功！", msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "dl.queue", durable = "true"),
            exchange = @Exchange(name = "dl.direct"),
            key = "dl"
    ))
    public void listenDlQueue(String msg)
    {
        log.debug("dl.queue的消延迟消息[{}]。", msg);
    }
}
