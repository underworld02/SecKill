package com.suron.ysyliving.commodity.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author ysy
 * @version 1.0
 */
@Configuration
@EnableTransactionManagement //开启事务
@MapperScan("com.suron.ysyliving.commodity.dao")
public class MyBatisConfig {

    //引入分页插件-bean

    @Bean
    public PaginationInterceptor paginationInterceptor() {

        PaginationInterceptor paginationInterceptor =
                new PaginationInterceptor();

        //做基本的设置
        //溢出总页数，设置第一页
        paginationInterceptor.setOverflow(true);
        //单页限制 100 条，小于 0 如 -1 不受限制
        paginationInterceptor.setLimit(100);
        return paginationInterceptor;
    }
}
