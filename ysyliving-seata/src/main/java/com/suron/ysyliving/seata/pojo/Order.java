package com.suron.ysyliving.seata.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ysy
 * @version 1.0
 * Order: 对应 t_order表
 */
@Data
@TableName("t_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long goodsId;

    private Long deliveryAddrId;

    private String goodsName;

    private Integer goodsCount;

    private BigDecimal goodsPrice;

    /**
     * 订单渠道1pc，2Android，3ios
     */
    private Integer orderChannel;

    /**
     * 订单状态：0新建未支付 1已支付 2已发货 3已收货 4已退款 5已完成
     */
    private Integer status;

    private Date createDate;

    private Date payDate;

}
