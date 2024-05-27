package com.suron.ysyliving.goods.mapper;

import com.suron.ysyliving.goods.vo.Category;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @auther: 闫昊
 * @date: 2019/5/9
 */
@Repository
public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category, Long> {
}
