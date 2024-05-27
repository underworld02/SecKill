package com.suron.ysyliving.goods.vo;

import com.suron.ysyliving.goods.vo.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @auther: 闫昊
 * @date: 2019/7/18
 */

@Data
@NoArgsConstructor
public class SearchResult<Product> extends PageResult<Product> {


    private List<Brand> brands;
    private List<Category> categories;
    //规格参数过滤条件
    private List<Map<String, Object>> specs;
    private Long total; //总条数
    private Integer totalPage;//总页数
    private List<Product> items;//当前页数据
    public SearchResult(Long total,
                        Integer totalPage,
                        List<Product> items,
                        List<Category> categories,
                        List<Brand> brands
    ) {
//        super(total, totalPage, items);
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
        this.categories = categories;
        this.brands = brands;
    }

}