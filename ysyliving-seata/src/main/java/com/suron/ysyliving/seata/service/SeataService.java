package com.suron.ysyliving.seata.service;

import com.suron.ysyliving.seata.feignClient.GoodsClient;
import com.suron.ysyliving.seata.feignClient.OrderClient;
import com.suron.ysyliving.seata.vo.GoodsVo;

import javax.annotation.Resource;

/**
 * @author Suron
 * @version 1.0
 */
public interface SeataService {
    public void save(GoodsVo order);
}
