package com.suron.ysyliving.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @author ysy
 * @version 1.0
 */
//把session信息提取出来存到redis中
//主要实现序列化, 这里是以常规操作
@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
        lettuceConnectionFactory.setHostName("49.235.172.87");
        lettuceConnectionFactory.setPassword("Ysy123123");
        return lettuceConnectionFactory;
    }
    //自定义 RedisTemplate对象, 注入到容器
    //后面我们操作Redis时，就使用自定义的 RedisTemplate对象
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //设置相应key的序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //value序列化
        //redis默认是jdk的序列化是二进制,这里使用的是通用的json数据,不用传具体的序列化的对象
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //设置相应的hash序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        //注入连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        System.out.println("测试--> redisTemplate" + redisTemplate.hashCode());
        return redisTemplate;
    }

}

