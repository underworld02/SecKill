package com.suron.ysyliving.seata.config;

import com.suron.ysyliving.seata.exception.FeignRequestInterceptor;
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