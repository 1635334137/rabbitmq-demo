package com.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 发布确认配置
 *
 * @author Mr.Lan
 * @create: 2022-01-29 19:44
 */
@Configuration
public class ConfirmConfig {

    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";
    public static final String CONFIRM_QUEUE_NAME = "confirm_queue";
    public static final String CONFIRM_ROUTING_KEY = "key1";

    //备份交换器
    public static final String BACKUP_EXCHANGE_NAME = "backup_exchange";
    //备份队列
    public static final String BACKUP_QUEUE_NAME = "backup_queue";
    //报警队列
    public static final String WARNING_QUEUE_NAME = "warning_queue";

    //声明交换器
//    @Bean("confirmExchange")
//    public DirectExchange confirmExchange(){
//        return new DirectExchange(CONFIRM_EXCHANGE_NAME);
//    }
    //配置拥有备份交换器的交换器
    @Bean("confirmExchange")
    public DirectExchange confirmExchange(){
        return (DirectExchange) ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).durable(true)
                .withArgument("alternate-exchange",BACKUP_EXCHANGE_NAME).build();
    }

    //声明队列
    @Bean("confirmQueue")
    public Queue confirmQueue(){
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    //绑定
    @Bean
    public Binding queueBindingExchange(@Qualifier("confirmQueue") Queue confirmQueue,
                                        @Qualifier("confirmExchange")DirectExchange confirmExchange){
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }

    //备份交换器
    @Bean("backupExchange")
    public FanoutExchange backupExchange(){
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    //备份队列
    @Bean("backupQueue")
    public Queue backupQueue(){
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    //报警队列
    @Bean("warningQueue")
    public Queue warningQueue(){
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    //绑定
    @Bean
    public Binding backupQueueBindingBackupExchange(@Qualifier("backupQueue") Queue backupQueue,
                                        @Qualifier("backupExchange")FanoutExchange backupExchange){
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }
    @Bean
    public Binding warningQueueBindingBackupExchange(@Qualifier("warningQueue") Queue warningQueue,
                                                    @Qualifier("backupExchange")FanoutExchange backupExchange){
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }

}
