package com.suron.ysyliving.goods.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @author ysy
 * @version 1.0
 * AccessLimitInterceptor: 自定义的拦截器
 */
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {

    //装配需要的组件/对象

    @Resource
    private RedisTemplate redisTemplate;

    //这个方法完成1. 得到user对象，并放入到ThreadLoacl 2. 去处理@Accesslimit
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(request.getRequestURI());
        if (handler instanceof HandlerMethod) {
            //这里我们就先获取到登录的user对象
            System.out.println(request.getRequestURI());
        }
        return true;
    }
}
