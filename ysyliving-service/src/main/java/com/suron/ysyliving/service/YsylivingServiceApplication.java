package com.suron.ysyliving.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author suron
 * @version 1.0
 */
@EnableDiscoveryClient
@SpringBootApplication
public class YsylivingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(YsylivingServiceApplication.class, args);
    }
}
