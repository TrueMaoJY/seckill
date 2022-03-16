package com.maomao.seckill.vo;

import com.maomao.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MaoJY
 * @create 2022-03-09 15:56
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailVO {
    private User user;
    private GoodsVO goodsVO;
    private int secKillStatus;
    private int remainSeconds;


}