package com.suron.ysyliving.seata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.suron.ysyliving.seata.pojo.SeckillGoods;
import com.suron.ysyliving.seata.vo.SecGoodsVo;

import java.util.List;

/**
 * @author ysy
 * @version 1.0
 */
public interface SeckillGoodsMapper
        extends BaseMapper<SeckillGoods> {

    List<SecGoodsVo> findAll();
}
