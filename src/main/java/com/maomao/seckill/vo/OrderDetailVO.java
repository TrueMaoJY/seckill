package com.maomao.seckill.vo;

import com.maomao.seckill.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author MaoJY
 * @create 2022-03-08 12:22
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVO {
    private Order order;
    private GoodsVO goodsVO;
}