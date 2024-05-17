/**
  * Copyright 2021 json.cn
  */
package com.suron.ysyliving.commodity.vo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Auto-generated: 2021-10-13 23:25:51
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */

@Data
public class Skus {

    private List<Attr> attr;
    private String skuName;
    private BigDecimal price;
    private String skuTitle;
    private String skuSubtitle;
    //private List<String> images; 修改一下将来会上传多张图片, 留下接口

    private List<Images> images;
    private List<String> descar;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    //private List<String> memberPrice; 修改一下将来会根据会员级别对应不同的价格, 留下接口
    private List<MemberPrice> memberPrice;

}
