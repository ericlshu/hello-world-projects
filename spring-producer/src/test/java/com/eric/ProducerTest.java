package com.eric;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.CorrelationData;
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

    /**
     * 确认模式
     * 步骤：
     * // 1. 确认模式开启：ConnectionFactory中开启publisher-confirms="true"
     * 1. 确认模式开启：ConnectionFactory中开启confirm-type="CORRELATED"
     * 2. 在rabbitTemplate定义ConfirmCallBack回调函数
     */
    @Test
    public void testConfirm()
    {
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback()
        {
            /**
             * @param correlationData correlation data for the callback.
             * @param ack true for ack, false for nack
             * @param cause An optional cause, for nack, when available, otherwise null.
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause)
            {
                System.out.println("org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback.confirm ......");
                if (ack)
                    System.out.println("消息接收成功：" + cause);
                else
                    System.out.println("消息接收失败：" + cause);
            }
        });
        rabbitTemplate.convertAndSend("confirm_exchange", "confirm", "Helle, confirm queue!");
    }

    /**
     * 回退模式： 当消息发送给Exchange后，Exchange路由到Queue失败是 才会执行 ReturnCallBack
     * 1. 开启回退模式:publisher-returns="true"
     * 2. 设置ReturnCallBack
     * 3. 设置Exchange处理消息的模式：
     * 1). 如果消息没有路由到Queue，则丢弃消息（默认）
     * 2). 如果消息没有路由到Queue，返回给消息发送方ReturnCallBack
     */
    @Test
    public void testReturn() throws InterruptedException
    {
        //1. 设置交换机处理失败消息的模式
        rabbitTemplate.setMandatory(true);

        //2. 设置ReturnsCallBack
        rabbitTemplate.setReturnsCallback(
                returned ->
                {
                    System.out.println("org.springframework.amqp.rabbit.core.RabbitTemplate.setReturnsCallback");
                    System.out.println("message    = " + returned.getMessage());    // 消息对象
                    System.out.println("replyCode  = " + returned.getReplyCode());  // 错误码
                    System.out.println("replyText  = " + returned.getReplyText());  // 错误信息
                    System.out.println("exchange   = " + returned.getExchange());   // 交换机
                    System.out.println("routingKey = " + returned.getRoutingKey()); // 路由键
                });
        //3. 发送消息
        rabbitTemplate.convertAndSend("confirm_exchange", "confirm", "Helle, confirm queue!");
        Thread.sleep(1000);
    }

    @Test
    public void testSend() throws InterruptedException
    {
        for (int i = 0; i < 10; i++)
        {
            rabbitTemplate.convertAndSend("confirm_exchange", "confirm", "Helle, confirm queue [" + (i + 1) + "]!");
        }
        Thread.sleep(1000);
    }
}
