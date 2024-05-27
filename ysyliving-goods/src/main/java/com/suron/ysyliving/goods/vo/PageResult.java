package com.suron.ysyliving.goods.vo;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 因为传来的页面条数信息有可能很多集合，所以弄成对象
 * @param <T>
 */

@Data
@NoArgsConstructor
public class PageResult<T> {

    private Long total; //总条数
    private Integer totalPage;//总页数
    private List<T> items;//当前页数据

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageResult(Long total, Integer totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }


}
