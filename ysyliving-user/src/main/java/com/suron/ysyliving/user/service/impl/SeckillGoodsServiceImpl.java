package com.suron.ysyliving.user.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suron.ysyliving.user.mapper.SeckillGoodsMapper;
import com.suron.ysyliving.user.pojo.SeckillGoods;
import com.suron.ysyliving.user.service.SeckillGoodsService;
import com.suron.ysyliving.user.vo.SecGoodsVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author ysy
 * @version 1.0
 */
@Service
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsMapper, SeckillGoods> implements SeckillGoodsService {
    @Resource
    SeckillGoodsMapper seckillGoodsMapper;

    @Override
    public List<SecGoodsVo> getRandomProducts(int count) {
        List<SecGoodsVo> allProducts = seckillGoodsMapper.findAll();
        if (allProducts.size() <= count) {
            return allProducts;
        } else {
            Random random = new Random();
            return random.ints(0, allProducts.size())
                    .distinct()
                    .limit(count)
                    .mapToObj(allProducts::get)
                    .collect(Collectors.toList());
        }
    }
}
