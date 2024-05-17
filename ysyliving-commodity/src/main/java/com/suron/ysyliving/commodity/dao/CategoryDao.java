package com.suron.ysyliving.commodity.dao;

import com.suron.ysyliving.commodity.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类表
 * 
 * @author ysy
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
