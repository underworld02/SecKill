package com.suron.ysyliving.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.suron.ysyliving.goods.vo.Brand;
import com.suron.ysyliving.goods.vo.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

/**
 * @author Suron
 * @version 1.0
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    List<Product> search(@Param("keyword") String keyword, @Param("offset") int offset, @Param("size") int size);

    int countSearch(@Param("keyword") String keyword);

}