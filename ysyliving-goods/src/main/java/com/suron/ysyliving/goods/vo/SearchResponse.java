package com.suron.ysyliving.goods.vo;

import com.suron.ysyliving.goods.pojo.Goods;

import java.util.List;

/**
 * @author Suron
 * @version 1.0
 */
public class SearchResponse {
    private int total;
    private int totalPage;
    private List<Goods> items;
    private List<Category> categories;
    private List<Brand> brands;

    // getters and setters
}