package com.suron.ysyliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.suron.common.utils.PageUtils;
import com.suron.ysyliving.commodity.entity.AttrAttrgroupRelationEntity;
import com.suron.ysyliving.commodity.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性表
 *
 * @author ysy
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 增加接口，完成保存商品属性(基本属性) 的同时，
     * 要需要保存该基本属性和属性组的关联关系
     */

    void saveAttrAndRelation(AttrEntity attrEntity);

    // 增加根据分类categoryId+查询条件[key] 进行分页检索[基本属性]的API接口
    // 根据自己的业务逻辑，进行定制
    PageUtils queryBaseAttrPage(Map<String, Object> params, Long categoryId);


    // 增加根据分类categoryId+查询条件[key] 进行分页检索[销售属性]的API接口
    // 根据自己的业务逻辑，进行定制
    PageUtils querySaleAttrPage(Map<String, Object> params, Long categoryId);

    /**
     * 根据属性组id , 返回该属性组关联的商品属性(基本属性)
     */
    List<AttrEntity> getRelationAttr(Long attrgroupId);

    /**
     * 批量删除属性组和属性的关联关系
     */
    void deleteRelation(AttrAttrgroupRelationEntity[] attrAttrgroupRelationEntities);

    /**
     * 获取某个属性组可以关联的基本属性
     * 1. 如果某个基本属性已经和某个属性组关联了, 就不能再关联
     * 2. 某个属性组可以关联的基本属性，必须是同一个分类
     */

    PageUtils getAllowRelationAttr(Map<String, Object> params, Long attrgroupId);
}

