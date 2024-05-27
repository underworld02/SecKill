package com.suron.ysyliving.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ysy
 * @version 1.0
 * RabbitMQSeckillConfig: 配置类，创建消息队列和交换机
 */
@Configuration
public class RabbitMQSeckillConfig {

    //定义消息队列名和交换机名
    private static final String QUEUE = "seckillQueue";
    private static final String EXCHANGE = "seckillExchange";

    //创建队列
    @Bean
    public Queue queue_seckill() {
        return new Queue(QUEUE);
    }

    //创建交换机-Topic
    @Bean
    public TopicExchange topicExchange_seckill() {
        return new TopicExchange(EXCHANGE);
    }

    //将队列绑定到交换机，并指定路由
    @Bean
    public Binding binding_seckill() {
        return BindingBuilder.bind(queue_seckill()).to(topicExchange_seckill()).with("seckill.#");
    }
}
