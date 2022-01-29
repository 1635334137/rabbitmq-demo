package com.rabbitmq.controller;

import com.rabbitmq.config.ConfirmConfig;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发布确认Controller
 *
 * @author Mr.Lan
 * @create: 2022-01-29 19:51
 */
@RestController
@RequestMapping("/confirm")
public class ConfirmSendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //发送消息
    @GetMapping("/sendMsg/{message}")
    public void sendMessage(@PathVariable String message){

        //唯一标识
        CorrelationData correlationData = new CorrelationData("1");

        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY,message+"key1",correlationData);


        //故意发送一条不可达消息
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY+1,message+"key2",correlationData);

    }
}
