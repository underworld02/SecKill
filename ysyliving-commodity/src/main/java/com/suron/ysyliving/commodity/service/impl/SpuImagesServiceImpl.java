package com.suron.ysyliving.commodity.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suron.common.utils.PageUtils;
import com.suron.common.utils.Query;

import com.suron.ysyliving.commodity.dao.SpuImagesDao;
import com.suron.ysyliving.commodity.entity.SpuImagesEntity;
import com.suron.ysyliving.commodity.service.SpuImagesService;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }

    //实现批量保存spu对应的图片集
    @Override
    public void saveImages(Long id, List<String> images) {

        if (images == null || images.size() == 0) {
            //设置默认的图片集
            SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
            spuImagesEntity.setSpuId(id);
            spuImagesEntity.setImgUrl("default1.jpg");
            spuImagesEntity.setDefaultImg(1);
            this.save(spuImagesEntity);
        } else {
            //遍历images[for/也可以直接使用流式计算], 批量保存
            List<SpuImagesEntity> spuImagesEntities = images.stream().map(img -> {
                SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
                spuImagesEntity.setSpuId(id);
                spuImagesEntity.setImgUrl(img);
                return spuImagesEntity;

            }).collect(Collectors.toList());
            //批量添加
            this.saveBatch(spuImagesEntities);
        }
    }
}