package com.eric.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * Description : 工作队列模式
 *
 * @author Eric L SHU
 * @date 2022-04-30 21:17
 * @since jdk-11.0.14
 */
public class Producer_WorkQueues
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
        // 5.创建队列
        channel.queueDeclare("work_queue", true, false, false, null);
        // 6.发送消息
        String message = "Hello, RabbitMQ!";
        for (int i = 0; i < 10; i++)
        {
            String body = message + "工作队列模式消息[" + i + "];";
            System.out.println("body = " + body);
            channel.basicPublish("", "work_queue", null, body.getBytes(StandardCharsets.UTF_8));
        }
        // 7.释放资源
        channel.close();
        connection.close();
    }
}
