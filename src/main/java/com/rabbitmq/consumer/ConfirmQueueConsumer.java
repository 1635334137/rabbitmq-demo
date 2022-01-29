package com.rabbitmq.consumer;

import com.rabbitmq.config.ConfirmConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 发布确认消费者
 *
 * @author Mr.Lan
 * @create: 2022-01-29 19:55
 */
@Component
public class ConfirmQueueConsumer {

    //监听消息
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveConfirmMessage(Message message){
        String str = new String(message.getBody());
        System.out.println(str);
    }
}
