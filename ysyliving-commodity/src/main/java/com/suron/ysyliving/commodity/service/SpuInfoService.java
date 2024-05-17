package com.suron.ysyliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.suron.common.utils.PageUtils;
import com.suron.ysyliving.commodity.entity.SpuInfoEntity;
import com.suron.ysyliving.commodity.vo.SpuSaveVO;

import java.util.Map;

/**
 * 商品spu信息
 *
 * @author ysy
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVO spuSaveVO);

    void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity);


    //通过携带的检索条件，进行分页查询
    PageUtils queryPageByCondition(Map<String, Object> params);

    //商品SPU上架
    void up(Long spuId);

    //商品SPU下架
    void down(Long spuId);
}

