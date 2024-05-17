package com.suron.ysyliving.commodity.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 家居商品属性分组
 * 
 * @author ysy
 */
@Data
@TableName("commodity_attrgroup")
public class AttrgroupEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 组名
	 */
	private String name;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 说明
	 */
	private String description;
	/**
	 * 组图标
	 */
	private String icon;
	/**
	 * 所属分类id
	 */
	private Long categoryId;

	/**
	 * 增加 cascadedCategoryId 属性,数组,数据的形式[1,21,301]
	 * @TableField(exist = false): 表示该属性不是表的字段
	 */

	@TableField(exist = false)
	private Long[] cascadedCategoryId;

}
