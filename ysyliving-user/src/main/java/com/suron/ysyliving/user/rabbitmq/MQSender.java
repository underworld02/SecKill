package com.suron.ysyliving.user.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ysy
 * @version 1.0
 * MQSender: 消息的发送者/生产者
 */
@Service
@Slf4j
public class MQSender {

    //装配RabbitTemplate->操作RabbitMQ
    @Resource
    private RabbitTemplate rabbitTemplate;

    //方法：发送消息
    public void send(Object msg) {
        log.info("发送消息-->" + msg);
        rabbitTemplate.convertAndSend("queue", msg);
    }

    //方法：发送消息到交换机
    public void sendFanout(Object msg) {
        log.info("发送消息-->" + msg);
        rabbitTemplate.convertAndSend("fanoutExchange", "", msg);
    }

    //方法:发送消息到direct交换机，同时指定路由queue.red
    public void sendDirect1(Object msg) {
        log.info("发送消息-->" + msg);
        rabbitTemplate.convertAndSend("directExchange", "queue.red", msg);
    }

    //方法:发送消息到direct交换机，同时指定路由queue.green
    public void sendDirect2(Object msg) {
        log.info("发送消息-->" + msg);
        rabbitTemplate.convertAndSend("directExchange", "queue.green", msg);
    }

    //方法:发送消息到topic交换机，同时指定路由 queue.red.message
    public void sendTopic3(Object msg) {
        log.info("发送消息-->" + msg);
        rabbitTemplate.convertAndSend("topicExchange", "queue.red.message", msg);
    }

    //方法:发送消息到topic交换机，同时指定路由 green.queue.green.message
    public void sendTopic4(Object msg) {
        log.info("发送消息-->" + msg);
        rabbitTemplate.convertAndSend("topicExchange", "green.queue.green.message", msg);
    }

    //方法:发送消息到headers交换机,同时携带/指定 匹配的k-v
    public void sendHeader01(String msg) {
        log.info("发送消息--->" + msg);
        //创建消息属性MesssageProperties
        MessageProperties properties = new MessageProperties();
        properties.setHeader("color","red");
        properties.setHeader("speed","fast");
        //创建Message对象【包含了发送的消息本身和属性】
        Message message = new Message(msg.getBytes(), properties);
        rabbitTemplate.convertAndSend("headersExchange","",message);
    }

    //方法:发送消息到headers交换机,同时携带/指定 匹配的k-v
    public void sendHeader02(String msg) {
        log.info("发送消息--->" + msg);
        //创建消息属性MesssageProperties
        MessageProperties properties = new MessageProperties();
        properties.setHeader("color","red");
        properties.setHeader("speed","normal");
        //创建Message对象【包含了发送的消息本身和属性】
        Message message = new Message(msg.getBytes(), properties);
        rabbitTemplate.convertAndSend("headersExchange","",message);

    }
}
