package com.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于插件的延迟配置  优化2代码
 *
 * @author Mr.Lan
 * @create: 2022-01-29 05:22
 */
@Configuration
public class DelayedQueueConfig {

    //队列
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    //交换器
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    //路由键
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    //声明队列
    @Bean
    public Queue delayedQueue(){
        return new Queue(DELAYED_QUEUE_NAME);
    }

    //声明交换器
    @Bean
    public CustomExchange delayedExchange(){
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type","direct");

        /**
         * 1.名字
         * 2.类型
         * 3.持久化
         * 4.自动删除
         * 5.其他参数
         */
        return new CustomExchange(DELAYED_EXCHANGE_NAME,"x-delayed-message",
                true,false,arguments);
    }

    //绑定
    @Bean
    public Binding delayedQueueBindingDelayedExchange(
            @Qualifier("delayedQueue") Queue delayedQueue,
            @Qualifier("delayedExchange") CustomExchange delayedExchange){

        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
