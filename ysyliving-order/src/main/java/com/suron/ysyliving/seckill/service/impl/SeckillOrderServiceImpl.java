package com.suron.ysyliving.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suron.ysyliving.seckill.mapper.SeckillOrderMapper;
import com.suron.ysyliving.seckill.pojo.SeckillOrder;
import com.suron.ysyliving.seckill.service.SeckillOrderService;
import org.springframework.stereotype.Service;

/**
 * @author ysy
 * @version 1.0
 */
@Service
public class SeckillOrderServiceImpl
        extends ServiceImpl<SeckillOrderMapper, SeckillOrder>
        implements SeckillOrderService {
}
