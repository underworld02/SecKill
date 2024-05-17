package com.suron.ysyliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.suron.common.utils.PageUtils;
import com.suron.ysyliving.commodity.entity.SpuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * spu图片集
 *
 * @author ysy
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    // 批量保存spu对应的图片集
    void saveImages(Long id, List<String> images);
}

