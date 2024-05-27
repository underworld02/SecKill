package com.suron.ysyliving.goods.vo;

import lombok.Data;

import java.util.Map;

/**
 * @author Suron
 * @version 1.0
 */
@Data
public class SearchCriteria {
    private int page;
    private int size;
    private Map<String, Object> filter;
}
