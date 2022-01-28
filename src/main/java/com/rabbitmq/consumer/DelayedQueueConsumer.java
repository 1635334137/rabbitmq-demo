package com.rabbitmq.consumer;

import com.rabbitmq.config.DelayedQueueConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消费端 优化2代码
 *
 * @author Mr.Lan
 * @create: 2022-01-29 05:37
 */
@Component
public class DelayedQueueConsumer {

    //监听消息
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void receiveDelayQueue(Message message){
        String msg = new String(message.getBody());
        System.out.println(msg);
    }
}
