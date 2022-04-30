package com.eric.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description : 工作队列模式
 *
 * @author Eric L SHU
 * @date 2022-04-30 21:17
 * @since jdk-11.0.14
 */
public class Producer_PubSub
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
        /*
         * exchangeDeclare(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments)
         * 参数：
         *  1. exchange:交换机名称
         *  2. type:交换机类型
         *      DIRECT("direct")    ：定向
         *      FANOUT("fanout")    ：扇形（广播），发送消息到每一个与之绑定队列。
         *      TOPIC("topic")      : 通配符的方式
         *      HEADERS("headers")  : 参数匹配
         *  3. durable:是否持久化
         *  4. autoDelete:自动删除
         *  5. internal             : 内部使用。 一般false
         *  6. arguments            : 参数
         */
        String exchangeName = "fanout_exchange";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT,
                                true, false, false, null);
        // 6.创建队列
        String queue1Name = "fanout_queue1";
        String queue2Name = "fanout_queue2";
        channel.queueDeclare(queue1Name, true, false, false, null);
        channel.queueDeclare(queue2Name, true, false, false, null);
        // 7.绑定队列和交换机
        /*
         * queueBind(String queue, String exchange, String routingKey)
         *  1. queue        : 队列名称
         *  2. exchange     : 交换机名称
         *  3. routingKey   : 路由键，绑定规则
         *      如果交换机的类型为fanout ，routingKey设置为""
         */
        channel.queueBind(queue1Name, exchangeName, "");
        channel.queueBind(queue2Name, exchangeName, "");
        // 8.发送消息
        String body = "日志信息：张三调用了findAll方法...日志级别：info...";
        channel.basicPublish(exchangeName, "", null, body.getBytes());
        //9. 释放资源
        channel.close();
        connection.close();
    }
}
