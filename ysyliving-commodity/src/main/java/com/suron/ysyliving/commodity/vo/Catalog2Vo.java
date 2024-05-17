package com.suron.ysyliving.commodity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ysy
 * @version 1.0
 * Catalog2Vo 返回给前端的二级分类数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Catalog2Vo {

    /**
     * 一级父类的id
     */
    private String catalog1Id;

    /**
     * 三级分类的信息-集合
     */
    private List<Category3Vo> catalog3List;
    /**
     * 二级分类本身的信息
     */
    private String id;
    private String name;


    /**
     * 三级分类的类型-静态内部类
     */

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category3Vo {
        /**
         * 父级分类-二级分类的id
         */
        private String catalog2Id;
        /**
         * 三级分类本身的信息
         */
        private String id;
        private String name;

    }
}
