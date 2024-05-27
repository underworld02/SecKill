package com.suron.ysyliving.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suron.ysyliving.gateway.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * @author ysy
 * @version 1.0
 * RateLimiterFilter: 自定义的过滤器
 */
@Component
public class RateLimiterFilter implements GlobalFilter, Ordered {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RateLimiterConfig rateLimiterConfig;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String uri = exchange.getRequest().getURI().getPath();

        RateLimiterConfig.LimitConfig limitConfig = rateLimiterConfig.getDefaultConfig();  //获取默认配置 max-visits: 10        seconds: 60

        int maxVisits = limitConfig.getMaxVisits();
        long seconds = limitConfig.getSeconds();

        if (!isAllowed(uri, maxVisits, seconds)) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;  // 控制过滤器的执行顺序
    }

    public boolean isAllowed(String uri, int maxVisits, long seconds) {
        String key = "rateLimiter";  // 使用 URI 作为 part of key
        String member = uri + ":" + Instant.now().toEpochMilli();  // URI + 当前时间戳

        long now = Instant.now().toEpochMilli();
        double windowStart = now - seconds * 1000;

        // 添加当前请求
        redisTemplate.opsForZSet().add(key, member, now);

        // 清除窗口之前的数据
        redisTemplate.opsForZSet().removeRangeByScore(key, 0, windowStart);

        // 获取窗口内的请求数量
        long count = redisTemplate.opsForZSet().count(key, windowStart, now);

        // 设置过期时间
        redisTemplate.expire(key, seconds+2, java.util.concurrent.TimeUnit.SECONDS);

        // 检查是否超过最大请求次数
        return count <= maxVisits;
    }
}
