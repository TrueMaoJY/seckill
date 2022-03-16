package com.maomao.seckill.vo;

import com.maomao.seckill.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author MaoJY
 * @create 2022-03-06 16:02
 * @Description:
 */

//封装商品列表页展示字段
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVO extends Goods {
    private BigDecimal seckillPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;
}