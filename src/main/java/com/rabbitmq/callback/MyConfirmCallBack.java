package com.rabbitmq.callback;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 发布确认回调接口
 *
 * @author Mr.Lan
 * @create: 2022-01-29 19:57
 */
@Component
public class MyConfirmCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 注入回调配置
     * @PostConstruct保证执行在@Component，@Autowired之后，防止还没有RabbitTemplate的时候就注入了
     */
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     *
     * @param correlationData 由消息发送端构建 用于唯一标识一条消息
     * @param ack 是否收到消息
     * @param cause 失败原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String deliveryTag = correlationData.getId() != null ? correlationData.getId():"";
        if(ack){
            System.out.println("收到消息，消息tag为："+deliveryTag);
        }else{
            System.out.println("未收到消息，消息tag为："+deliveryTag+":::失败原因："+cause);
        }
    }

    /**
     * 发布确认只得到了失败的消息是哪些，但是没有配置失败消息该怎么处理，rabbitmq默认是丢弃掉，所以这样要配置接收
     * @param message 消息
     * @param replyCode 回复代码
     * @param replyText 退回原因
     * @param exchange 交换器
     * @param routingKey 路由键
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("消息"+message.getBody()+",被交换器"+exchange+"退回，退回原因："+replyText);
    }
}
