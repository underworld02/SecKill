package com.suron.ysyliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.suron.common.utils.PageUtils;
import com.suron.ysyliving.commodity.entity.AttrgroupEntity;
import com.suron.ysyliving.commodity.vo.AttrGroupWithAttrsVo;

import java.util.List;
import java.util.Map;

/**
 * 家居商品属性分组
 *
 * @author hsp
 * @email hsp@gmail.com
 * @date 2022-11-21 10:11:30
 */
public interface AttrgroupService extends IService<AttrgroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    // 增加根据分类(第3级分类)+查询条件[key和categoryId]+分页的API接口
    // 根据自己的业务逻辑，进行定制
    PageUtils queryPage(Map<String, Object> params, Long categoryId);

    /**
     * 根据分类categoryId返回该分类关联的属性组和这些属性组关联的基本属性
     */
    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCategoryId(Long categoryId);
}

