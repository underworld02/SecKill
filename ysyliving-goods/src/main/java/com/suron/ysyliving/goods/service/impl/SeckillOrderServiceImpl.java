package com.suron.ysyliving.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suron.ysyliving.goods.mapper.SeckillOrderMapper;
import com.suron.ysyliving.goods.service.SeckillOrderService;
import com.suron.ysyliving.goods.pojo.SeckillOrder;
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
