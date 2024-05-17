package com.suron.ysyliving.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.suron.ysyliving.seckill.pojo.Goods;
import com.suron.ysyliving.seckill.vo.GoodsVo;

import java.util.List;

/**
 * @author ysy
 * @version 1.0
 */
public interface GoodsService extends IService<Goods> {
    //秒杀商品列表
    List<GoodsVo> findGoodsVo();
    //获取商品详情-根据goodsId
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
