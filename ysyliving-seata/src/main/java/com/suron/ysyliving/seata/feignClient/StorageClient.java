package com.suron.ysyliving.seata.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Suron
 * @version 1.0
 */
@FeignClient(value = "ysyliving-goods")
public interface StorageClient {
    @PostMapping(value = "/goods/reduce")
    String reduce(@RequestParam("productId") Long productId, @RequestParam("nums") Integer nums);
}
