package com.suron.ysyliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.suron.common.utils.PageUtils;
import com.suron.ysyliving.commodity.entity.SkuInfoEntity;
import com.suron.ysyliving.commodity.vo.SearchResult;

import java.util.Map;

/**
 * sku信息
 *
 * @author ysy
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //保存SKU的基本信息
    void saveSkuInfo(SkuInfoEntity skuInfoEntity);

    //进行分页查询-携带相应的检索条件
    PageUtils queryPageByCondition(Map<String, Object> params);

    //返回购买用户检索的结果 PageUtils -> SearchResult
    SearchResult querySearchPageByCondition(Map<String, Object> params);
}

