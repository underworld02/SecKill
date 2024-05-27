package com.suron.ysyliving.goods.mapper;


import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.suron.ysyliving.goods.vo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;


import java.util.List;

/**
 * @auther: 闫昊
 * @date: 2019/5/9
 */
@Repository
public interface BrandMapper extends Mapper<Brand>, SelectByIdListMapper<Brand, Long> {
    @Insert("insert into tb_category_brand (category_id, brand_id) values (#{cid}, #{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    @Select("SELECT b.* FROM tb_brand b INNER JOIN tb_category_brand cb ON b.id = cb.brand_id WHERE cb.category_id = #{cid}")
    List<Brand> queryByCategoryId(@Param("cid") Long cid);
}
