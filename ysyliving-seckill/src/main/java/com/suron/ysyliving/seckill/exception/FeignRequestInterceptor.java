package com.suron.ysyliving.seckill.exception;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author Suron
 * @version 1.0
 */

public class FeignRequestInterceptor implements RequestInterceptor {

    private final String authToken;


    public FeignRequestInterceptor(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public void apply(RequestTemplate template) {
        // Add an Authorization header to the request
        template.header("Authorization", "Bearer " + authToken);

        // You can also add logging here or manipulate other aspects of the request
    }
}
