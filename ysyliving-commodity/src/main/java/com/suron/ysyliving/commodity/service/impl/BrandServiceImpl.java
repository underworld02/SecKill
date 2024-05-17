package com.suron.ysyliving.commodity.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suron.common.utils.PageUtils;
import com.suron.common.utils.Query;

import com.suron.ysyliving.commodity.dao.BrandDao;
import com.suron.ysyliving.commodity.entity.BrandEntity;
import com.suron.ysyliving.commodity.service.BrandService;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        // 1.通过分析后端的代码,我们指定如果需要进行带条件查询, 就给QueryWrapper 设置查询条件和参数
        // 2.这里老韩就简单实现一把
        // 3.这里我们演示一下, 后面我们有更好的方案

        //QueryWrapper<BrandEntity> brandEntityQueryWrapper = new QueryWrapper<>();
        //brandEntityQueryWrapper.like("name", params.get("name"));
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                new QueryWrapper<BrandEntity>()
                //brandEntityQueryWrapper
        );
        return new PageUtils(page);
    }

}