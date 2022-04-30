package com.eric.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description : Topics 通配符模式
 * <p>
 * 通配符规则： ：
 *  # 匹配一个或多个词
 *  * 匹配一个词，
 * 例如：
 *  item.# 能够匹配 item.insert.abc, item.insert
 *  item.* 只能匹配 item.insert
 *
 * @author Eric L SHU
 * @date 2022-04-30 21:17
 * @since jdk-11.0.14
 */
public class Producer_Topic
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
        String exchangeName = "topic_exchange";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC,
                                true, false, false, null);
        // 6.创建队列
        String queue1Name = "topic_queue1";
        String queue2Name = "topic_queue2";
        channel.queueDeclare(queue1Name, true, false, false, null);
        channel.queueDeclare(queue2Name, true, false, false, null);
        // 7.绑定队列和交换机
        // routing key : 系统的名称.日志的级别。
        // 需求： 所有error级别的日志存入数据库，所有order系统的日志存入数据库
        channel.queueBind(queue1Name, exchangeName, "#.error");
        channel.queueBind(queue1Name, exchangeName, "order.*");
        channel.queueBind(queue2Name, exchangeName, "*.*");
        // 8.发送消息
        channel.basicPublish(exchangeName, "order.info", null, "订单info信息".getBytes());
        channel.basicPublish(exchangeName, "user.error", null, "用户error信息".getBytes());
        channel.basicPublish(exchangeName, "user.info", null, "用户info信息".getBytes());
        //9. 释放资源
        channel.close();
        connection.close();
    }
}
