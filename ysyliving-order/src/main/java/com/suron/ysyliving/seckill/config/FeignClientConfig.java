package com.suron.ysyliving.seckill.config;

import com.suron.ysyliving.seckill.exception.FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Suron
 * @version 1.0
 */
@Configuration
public class FeignClientConfig {

    @Bean
    public FeignRequestInterceptor customRequestInterceptor() {
        return new FeignRequestInterceptor("your-auth-token");
    }
}