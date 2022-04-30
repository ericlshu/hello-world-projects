package com.eric.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description : Routing 路由模式
 * <p>
 * 队列与交换机的绑定，不能是任意绑定了，而是要指定一个 RoutingKey
 * 消息的发送方在向 Exchange 发送消息时，也必须指定消息的 RoutingKey
 * Exchange 不再把消息交给每一个绑定的队列，而是根据消息的 Routing Key 进行判断，只有队列的 RoutingKey 与消息的 RoutingKey 完全一致，才会接收到消息
 *
 * @author Eric L SHU
 * @date 2022-04-30 21:17
 * @since jdk-11.0.14
 */
public class Producer_Routing
{
    public static void main(String[] args) throws IOException, TimeoutException
    {
        // 1.创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 2.设置连接参数
        connectionFactory.setHost("110.40.224.64");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/eric");
        connectionFactory.setUsername("eric");
        connectionFactory.setPassword("1234");
        // 3.创建connection
        Connection connection = connectionFactory.newConnection();
        // 4.创建channel
        Channel channel = connection.createChannel();
        // 5.创建交换机
        String exchangeName = "direct_exchange";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT,
                                true, false, false, null);
        // 6.创建队列
        String queue1Name = "direct_queue1";
        String queue2Name = "direct_queue2";
        channel.queueDeclare(queue1Name, true, false, false, null);
        channel.queueDeclare(queue2Name, true, false, false, null);
        // 7.绑定队列和交换机
        channel.queueBind(queue1Name, exchangeName, "error");
        channel.queueBind(queue2Name, exchangeName, "info");
        channel.queueBind(queue2Name, exchangeName, "warn");
        channel.queueBind(queue2Name, exchangeName, "error");
        // 8.发送消息
        String body = "日志信息：张三调用了findAll方法...日志级别：info...";
        channel.basicPublish(exchangeName, "info", null, body.getBytes());
        body = "日志信息：张三调用了delete方法...日志级别：warn...";
        channel.basicPublish(exchangeName, "warn", null, body.getBytes());
        body = "日志信息：张三调用了delete方法并出现错误...日志级别：error...";
        channel.basicPublish(exchangeName, "error", null, body.getBytes());
        //9. 释放资源
        channel.close();
        connection.close();
    }
}
