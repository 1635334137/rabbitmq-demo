package com.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ配置类
 *
 * 基于队列设置过期时间是不太好的，后期会大量创建各种队列，所以应该为每条消息设置过期时间
 *
 * 优化1：为每条消息设置过期时间，并定义一个通用队列，用于存放设置过期时间的消息
 * 致命问题：虽然给消息设置了不同的过期时间，但是如果消息1先进入队列并且设置是过期时间长，
 * 而消息2后进入队列，设置的过期时间短，消息2并不会先于消息1消费。因为消息1的过期时间结束才会设置
 * 消息2的过期时间。
 *
 * 优化2：使用rabbitmq插件 rabbitmq_delayed_message_exchange
 * 1.首先下载插件 （由于我的rabbitmq版本是3.7.x的，所以插件要看是否兼容）
 * https://github.com/rabbitmq/rabbitmq-delayed-message-exchange
 * 通过查看历史发布Releases找到了v3.8.x是兼容3.7.x版本的
 * 下载：rabbitmq_delayed_message_exchange-3.8.0.ez
 * 粘贴到rabbitmq目录的plugins目录下
 * 通过sbin目录下启用插件：rabbitmq-plugins.bat enable rabbitmq_delayed_message_exchange
 * 通过网页管理界面Exchanges菜单的Add new exchange->Type查看是否安装成功->x-delayed-message
 *
 * @author Mr.Lan
 * @create: 2022-01-29 03:57
 */
@Configuration
public class TtlQueueConfig {

    //普通交换器的名称
    public static final String X_EXCHANGE = "X";
    //死信交换器的名称
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    //普通队列的名称
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    //死信队列的名称
    public static final String DEAD_LETTER_QUEUE = "QD";

    //普通队列 优化1代码
    public static final String QUEUE_C = "QC";

    //声明xExchange并设置别名
    @Bean("xExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANGE);
    }

    //声明yExchange并设置别名
    @Bean("yExchange")
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    //声明普通队列 TTL为10S
    @Bean("queueA")
    public Queue queueA(){
        Map<String,Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        arguments.put("x-dead-letter-routing-key","YD");
        arguments.put("x-message-ttl",10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    //声明普通队列 TTL为40S
    @Bean("queueB")
    public Queue queueB(){
        Map<String,Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        arguments.put("x-dead-letter-routing-key","YD");
        arguments.put("x-message-ttl",40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    //死信队列
    @Bean("queueD")
    public Queue queueD(){
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    //声明普通队列 优化1代码
    @Bean("queueC")
    public Queue queueC(){
        Map<String,Object> arguments = new HashMap<>(2);
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        arguments.put("x-dead-letter-routing-key","YD");
        return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
    }

    //绑定
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    public Binding queueDBindingX(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }

    //绑定 优化1代码
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }

}
