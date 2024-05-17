package com.suron.ysyliving.commodity.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 商品分类表
 * 
 * @author ysy
 */
@Data
@TableName("commodity_category")
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 父分类id
	 */
	private Long parentId;
	/**
	 * 层级
	 */
	private Integer catLevel;
	/**
	 * 0不显示，1显示
	 * 1. 如果我们没有在application.yml配置逻辑删除和未删除的值, 也
	 *    可以通过 @TableLogic；来配置, 如下
	 *     @TableLogic(value = "1", delval = "0")
	 */

	@TableLogic
	private Integer isShow;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 图标
	 */
	private String icon;
	/**
	 * 统计单位
	 */
	private String proUnit;
	/**
	 * 商品数量
	 */
	private Integer proCount;


	/**
	 * 增加一个属性 childrenCategories
	 * 1. childrenCategories 表示某个分类的子分类集合
	 * 2. childrenCategories 没有对应表 commodity_category字段
	 * 3. @TableField(exist = false) 表示 childrenCategories 不对应表的字段
	 * 4. @JsonInclude(JsonInclude.Include.NON_EMPTY)
	 *     如果childrenCategories的值为空数组，就不需要返回
	 */

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@TableField(exist = false)
	private List<CategoryEntity> childrenCategories;


}
