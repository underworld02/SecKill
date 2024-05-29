package com.suron.ysyliving.seata.service.impl;

import com.suron.ysyliving.seata.feignClient.GoodsClient;
import com.suron.ysyliving.seata.feignClient.OrderClient;
import com.suron.ysyliving.seata.service.OrderService;
import com.suron.ysyliving.seata.service.SeataService;
import com.suron.ysyliving.seata.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Suron
 * @version 1.0
 */
@Service
@Slf4j
public class SeataServiceImpl implements SeataService {
    @Resource
    private OrderService orderService;
    @Resource
    private GoodsClient storageService;
    @Resource
    private OrderClient accountService;
    /**
     * 创建订单->调用库存服务扣减库存->
     * 调用账户服务扣减账户余额->修改订单状态
     */
    @Override
    public void save(GoodsVo order) {
        log.info("=========开始新建订单 start ==========");
//新建订单
//            orderService.save(order);
        System.out.println("order=" + order);
        log.info("=========减库存 start ==========");
//            storageService.reduce(order.getId(), order.getStockCount());
        log.info("=========减库存 end ==========");
        log.info("=========减账户金额 start ==========");
//            accountService.reduce(order.getId(), order.getGoodsPrice());
        log.info("=========减账户金额 end ==========");
        log.info("=========修改订单状态 start ==========");
//            orderService.update(order.getId(), 0);
        log.info("=========修改订单状态 end ==========");
        log.info("=========下订单 end==========");
    }
}

