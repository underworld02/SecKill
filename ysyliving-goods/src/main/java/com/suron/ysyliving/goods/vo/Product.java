package com.suron.ysyliving.goods.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.ConstructorArgs;


import javax.persistence.*;

/**
 * @author Suron
 * @version 1.0
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_goods")
public class Product {
    @Id //  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "goods_name")
    private String name;
    @Column(name = "goods_detail")
    private String subtitle;
    @Column(name = "skus")
    private String skus;


}
