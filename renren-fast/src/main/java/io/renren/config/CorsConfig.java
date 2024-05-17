/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // 解决跨域
    // 注意如果注销了这段代码，还是报错
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //registry.addMapping("/**")
        //    .allowedOrigins("*")
        //    .allowCredentials(true)
        //    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        //    .maxAge(3600);
    }
}