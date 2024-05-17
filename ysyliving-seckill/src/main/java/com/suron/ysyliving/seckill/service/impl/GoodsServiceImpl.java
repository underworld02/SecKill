package com.suron.ysyliving.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suron.ysyliving.seckill.mapper.GoodsMapper;
import com.suron.ysyliving.seckill.pojo.Goods;
import com.suron.ysyliving.seckill.service.GoodsService;
import com.suron.ysyliving.seckill.vo.GoodsVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ysy
 * @version 1.0
 */
@Service
public class GoodsServiceImpl
        extends ServiceImpl<GoodsMapper, Goods>
        implements GoodsService {

    //装配GoodsMapper
    @Resource
    private  GoodsMapper goodsMapper;

    @Override
    public List<GoodsVo> findGoodsVo() {
        return goodsMapper.findGoodsVo();
    }

    //根据商品id,返回对应秒杀商品的详情
    @Override
    public GoodsVo findGoodsVoByGoodsId(Long goodsId) {
        return goodsMapper.findGoodsVoByGoodsId(goodsId);
    }
}
