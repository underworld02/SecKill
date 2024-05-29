package com.suron.ysyliving.seata.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ysy
 * @version 1.0
 */
@Data
@TableName("t_seckill_goods")
public class SeckillGoods {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long goodsId;

    private BigDecimal seckillPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;
}
