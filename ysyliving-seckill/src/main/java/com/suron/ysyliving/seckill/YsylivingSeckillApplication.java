package com.suron.ysyliving.seckill;

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
@MapperScan("com.suron.ysyliving.seckill.mapper")
public class YsylivingSeckillApplication {
    public static void main(String[] args) {
        SpringApplication.run(YsylivingSeckillApplication.class, args);
    }
}
