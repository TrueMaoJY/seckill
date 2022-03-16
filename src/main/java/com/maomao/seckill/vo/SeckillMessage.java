package com.maomao.seckill.vo;

import com.maomao.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MaoJY
 * @create 2022-03-08 14:46
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillMessage {
   private User user;
   private long goodsId;

}