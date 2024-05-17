package com.suron.ysyliving.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author ysy
 * @version 1.0
 * MQReceiver: 消息的接收者/消费者
 */
@Service
@Slf4j
public class MQReceiver {


    //方法:接收消息
    @RabbitListener(queues = "queue")
    public void receive(Object msg) {
        log.info("接收到消息-->" + msg);
    }

    /**
     * 监听队列 queue_fanout01
     *
     * @param msg
     */
    @RabbitListener(queues = "queue_fanout01")
    public void receive1(Object msg) {
        log.info("从queue_fanout01 接收到消息-->" + msg);
    }

    /**
     * 监听队列 queue_fanout02
     *
     * @param msg
     */
    @RabbitListener(queues = "queue_fanout02")
    public void receive2(Object msg) {
        log.info("从queue_fanout02 接收到消息-->" + msg);
    }

    /**
     * 监听队列 queue_direct01
     *
     * @param msg
     */
    @RabbitListener(queues = "queue_direct01")
    public void queue_direct1(Object msg) {
        log.info("从queue_direct01 接收到消息-->" + msg);
    }

    /**
     * 监听队列 queue_direct02
     *
     * @param msg
     */
    @RabbitListener(queues = "queue_direct02")
    public void queue_direct2(Object msg) {
        log.info("从queue_direct02 接收到消息-->" + msg);
    }

    /**
     * 监听队列 queue_topic01
     *
     * @param msg
     */
    @RabbitListener(queues = "queue_topic01")
    public void queue_topic01(Object msg) {
        log.info("从queue_topic01 接收到消息-->" + msg);
    }

    /**
     * 监听队列 queue_topic01
     *
     * @param msg
     */
    @RabbitListener(queues = "queue_topic02")
    public void queue_topic02(Object msg) {
        log.info("从queue_topic02 接收到消息-->" + msg);
    }


    /**
     * 监听 queue_header01
     */
    @RabbitListener(queues = "queue_header01")
    public void queue_header01(Message message) {
        log.info("queue_header01接收到消息对象-->" + message);
        //java基础->byte[]->string
        log.info("queue_header01接收到消息内容-->" + new String(message.getBody()));
    }

    /**
     * 监听 queue_header02
     */
    @RabbitListener(queues = "queue_header02")
    public void queue_header02(Message message) {
        log.info("queue_header02接收到消息对象-->" + message);
        //java基础->byte[]->string
        log.info("queue_header02接收到消息内容-->" + new String(message.getBody()));
    }
}
