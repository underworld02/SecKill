package com.suron.ysyliving.seckill.vo;

import com.suron.ysyliving.seckill.pojo.Goods;
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
public class GoodsVo extends Goods {

    private BigDecimal seckillPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;

    //如果后面有需求，也可以做修改
}
