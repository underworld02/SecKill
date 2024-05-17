package com.suron.ysyliving.commodity.vo;

/**
 * @author 韩顺平
 * @version 1.0
 */

import com.suron.ysyliving.commodity.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * 1. 如果返回的数据， 是单独返回当前实体类/对象不能满足的需求
 * 2. 通常的解决方案可以增加VO类/对象
 * 3. 这个VO类/对象，可以根据前端的需求，进行组合, 也可以增加字段，或者删除字段
 */
@Data
public class AttrGroupWithAttrsVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
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

    private List<AttrEntity> attrs;

    //...
    //...
}
