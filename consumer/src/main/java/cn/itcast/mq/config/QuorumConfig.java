package cn.itcast.mq.config;

import cn.itcast.mq.utils.AppConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-23 18:18
 * @since jdk-11.0.14
 */
@Configuration
public class QuorumConfig
{
    @Bean
    public Queue quorumQueue()
    {
        return QueueBuilder
                .durable(AppConstant.QUORUM_QUEUE)
                .quorum()
                .build();
    }
}
