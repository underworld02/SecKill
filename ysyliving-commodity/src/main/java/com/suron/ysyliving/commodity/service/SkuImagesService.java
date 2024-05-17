package com.suron.ysyliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.suron.common.utils.PageUtils;
import com.suron.ysyliving.commodity.entity.SkuImagesEntity;

import java.util.Map;

/**
 * sku图片
 *
 * @author ysy
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

