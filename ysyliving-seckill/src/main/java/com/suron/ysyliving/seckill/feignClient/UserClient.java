package com.suron.ysyliving.seckill.feignClient;

import com.suron.ysyliving.seckill.pojo.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author Suron
 * @version 1.0
 */
@FeignClient(name = "ysyliving-goods",contextId = "userClient")
public interface UserClient {

    @GetMapping("/goods")
    List<Goods> getGoods();

}
