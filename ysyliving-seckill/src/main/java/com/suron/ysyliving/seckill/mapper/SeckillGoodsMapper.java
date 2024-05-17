package com.suron.ysyliving.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.suron.ysyliving.seckill.pojo.Goods;
import com.suron.ysyliving.seckill.pojo.SeckillGoods;
import com.suron.ysyliving.seckill.vo.SecGoodsVo;

import java.util.List;

/**
 * @author ysy
 * @version 1.0
 */
public interface SeckillGoodsMapper
        extends BaseMapper<SeckillGoods> {

    List<SecGoodsVo> findAll();
}
