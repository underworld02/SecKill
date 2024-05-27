package com.suron.ysyliving.goods.service;


import com.suron.ysyliving.goods.pojo.Goods;
import com.suron.ysyliving.goods.vo.*;
import org.springframework.data.domain.Page;

import javax.naming.directory.SearchResult;

/**
 * @author Suron
 * @version 1.0
 */
public interface SearchService {
    PageResult<Product> searchProducts(SearchRequest request);//SearchRequest
}
