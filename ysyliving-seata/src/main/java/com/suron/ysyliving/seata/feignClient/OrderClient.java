package com.suron.ysyliving.seata.feignClient;

/**
 * @author Suron
 * @version 1.0
 */

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 老师解读:
 * 1. 这里使用 Openfeign 接口方式进行远程调用
 * 2. seata-account-micor-service 就是 被调用微服务在 Nacos Server 注册名
 */
@FeignClient(value = "ysyliving-order")
public interface OrderClient {
    @PostMapping(value = "/order/reduce")
    String reduce(@RequestParam("userId") Long userId, @RequestParam("money") Integer money);
}