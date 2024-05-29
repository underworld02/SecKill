package com.suron.ysyliving.seata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.suron.ysyliving.seata.pojo.SeckillGoods;
import com.suron.ysyliving.seata.vo.SecGoodsVo;

import java.util.List;

/**
 * @author ysy
 * @version 1.0
 */
public interface SeckillGoodsService extends IService<SeckillGoods> {
    List<SecGoodsVo> getRandomProducts(int count);
}
