package com.suron.ysyliving.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.suron.ysyliving.user.pojo.SeckillGoods;
import com.suron.ysyliving.user.vo.SecGoodsVo;

import java.util.List;

/**
 * @author ysy
 * @version 1.0
 */
public interface SeckillGoodsService extends IService<SeckillGoods> {
    List<SecGoodsVo> getRandomProducts(int count);
}
