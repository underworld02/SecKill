package com.suron.ysyliving.goods.mapper;

import com.suron.ysyliving.goods.pojo.Goods;
import com.suron.ysyliving.goods.vo.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * @author Suron
 * @version 1.0
 */
public interface ProductRepository extends JpaRepository<Goods, Long> {
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    Page<Product> findByNameContaining(@Param("name") String name);
}