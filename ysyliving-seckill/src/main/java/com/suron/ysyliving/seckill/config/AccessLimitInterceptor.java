package com.suron.ysyliving.seckill.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suron.ysyliving.seckill.pojo.User;
import com.suron.ysyliving.seckill.service.UserService;
import com.suron.ysyliving.seckill.util.CookieUtil;
import com.suron.ysyliving.seckill.vo.RespBean;
import com.suron.ysyliving.seckill.vo.RespBeanEnum;
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
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;

    //这个方法完成1. 得到user对象，并放入到ThreadLoacl 2. 去处理@Accesslimit
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            //这里我们就先获取到登录的user对象
            User user = getUser(request, response);
            //存入到ThreadLocal
            UserContext.setUser(user);

            //把handler 转成 HandlerMethod
            HandlerMethod hm = (HandlerMethod) handler;
            //获取到目标方法的注解
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {//如果目标方法没有@AccessLimit说明该接口并没有处理限流防刷
                return true; //继续
            }
            //获取注解的值
            int second = accessLimit.second();//获取到时间范围
            int maxCount = accessLimit.maxCount();//获取到最大的访问次数
            boolean needLogin = accessLimit.needLogin();//获取是否需要登录

            if (needLogin) {//说明用户必须登录才能访问目标方法/接口
                if (user == null) {//说明用户没有登录
                    //返回一个用户信息错误的提示...一会再单独处理...
                    render(response, RespBeanEnum.SESSION_ERROR);
                    return false;//返回
                }
            }

            String uri = request.getRequestURI();
            String key = uri + ":" + user.getId();
            ValueOperations valueOperations = redisTemplate.opsForValue();
            Integer count = (Integer) valueOperations.get(key);

            if (count == null) {//说明还没有key,就初始化，值为1, 过期时间为5秒
                valueOperations.set(key, 1, second, TimeUnit.SECONDS);
            } else if (count < maxCount) {//说明正常访问
                valueOperations.increment(key);
            } else {//说明用户在刷接口
                //返回一个频繁访问的的提示...一会再单独处理...
                render(response,RespBeanEnum.ACCESS_LIMIT_REACHED);
                return false;//返回
            }

        }
        return true;
    }

    //方法：构建返回对象-以流的形式返回
    private void render(HttpServletResponse response,
                        RespBeanEnum respBeanEnum) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        //构建RespBean
        RespBean error = RespBean.error(respBeanEnum);
        out.write(new ObjectMapper().writeValueAsString(error));
        out.flush();
        out.close();
    }

    //单独编写方法，得到登录的user对象-userTicket
    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String ticket = CookieUtil.getCookieValue(request, "userTicket");
        if (!StringUtils.hasText(ticket)) {
            return null;//说明该用户没有登录，直接返回null
        }
        return userService.getUserByCookie(ticket, request, response);
    }
}
