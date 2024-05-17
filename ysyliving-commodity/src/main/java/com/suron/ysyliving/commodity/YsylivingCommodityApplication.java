package com.suron.ysyliving.commodity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ysy
 * @version 1.0
 */
//@MapperScan指定扫描的dao包, 如果我们在Dao指定的有 @Mapper , 也可以不写
//@MapperScan("com.suron.ysyliving.commodity.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class YsylivingCommodityApplication {
    public static void main(String[] args) {
        SpringApplication.run(YsylivingCommodityApplication.class, args);
    }
}
