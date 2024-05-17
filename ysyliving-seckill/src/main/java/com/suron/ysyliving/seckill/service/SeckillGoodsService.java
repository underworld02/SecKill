package com.suron.ysyliving.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.suron.ysyliving.seckill.pojo.Goods;
import com.suron.ysyliving.seckill.pojo.SeckillGoods;
import com.suron.ysyliving.seckill.vo.SecGoodsVo;

import java.util.List;

/**
 * @author ysy
 * @version 1.0
 */
public interface SeckillGoodsService extends IService<SeckillGoods> {
    List<SecGoodsVo> getRandomProducts(int count);
}
