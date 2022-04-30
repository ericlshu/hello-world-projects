import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-30 22:07
 * @since jdk-11.0.14
 */
public class Consumer_HelloWorld
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
        // channel.queueDeclare("test_queue", true, false, false, null);
        // 6.接收消息
        DefaultConsumer consumer = new DefaultConsumer(channel)
        {
            /**
             * 回调方法，当收到消息后会自动执行该方法
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
            {
                System.out.println("consumerTag：" + consumerTag);
                System.out.println("Exchange：" + envelope.getExchange());
                System.out.println("RoutingKey：" + envelope.getRoutingKey());
                System.out.println("properties：" + properties);
                System.out.println("body：" + new String(body));
            }
        };
        /*
         * 1. queue：队列名称
         * 2. autoAck：是否自动确认
         * 3. callback：回调对象
         */
        channel.basicConsume("test_queue", true, consumer);
    }
}
