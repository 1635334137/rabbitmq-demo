package com.rabbitmq.consumer;

import com.rabbitmq.config.ConfirmConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 报警队列消费者
 *
 * @author Mr.Lan
 * @create: 2022-01-29 20:45
 */
@Component
public class WarningConsumer {

    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWarningMsg(Message message){
        System.out.println(new String(message.getBody()));
    }
}
