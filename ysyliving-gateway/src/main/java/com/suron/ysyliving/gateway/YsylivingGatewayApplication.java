package com.suron.ysyliving.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.time.Instant;

/**
 * @author ysy
 * @version 1.0
 */
@EnableDiscoveryClient
@SpringBootApplication
public class YsylivingGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(YsylivingGatewayApplication.class, args);
    }
}

