package com.suron.ysyliving.goods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ysy
 * @version 1.0
 */
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.suron.ysyliving.goods.mapper")
public class YsylivingGoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(YsylivingGoodsApplication.class, args);
    }
}
