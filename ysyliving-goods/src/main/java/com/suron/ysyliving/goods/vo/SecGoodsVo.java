package com.suron.ysyliving.goods.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ysy
 * @version 1.0
 * GoodsVo: 对应就是显示再秒杀商品列表的信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecGoodsVo {

    private Long id;

    private String goodsName;

    private String goodsTitle;

    private String goodsImg;

    private String goodsDetail;

    private BigDecimal goodsPrice;

    private BigDecimal seckillPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;
}
