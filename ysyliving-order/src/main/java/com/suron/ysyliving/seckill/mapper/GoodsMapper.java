package com.suron.ysyliving.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.suron.ysyliving.seckill.pojo.Goods;
import com.suron.ysyliving.seckill.vo.GoodsVo;

import java.util.List;

/**
 * @author ysy
 * @version 1.0
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    //获取商品列表-秒杀
    List<GoodsVo> findGoodsVo();

    //获取指定商品详情-根据id
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
