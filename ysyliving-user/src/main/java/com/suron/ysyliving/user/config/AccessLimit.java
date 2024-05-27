package com.suron.ysyliving.user.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ysy
 * @version 1.0
 * AccessLimit: 自定义的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {
    int second();//时间范围
    int maxCount();//访问的最大次数
    boolean needLogin() default true;//是否登录
}
