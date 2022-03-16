package com.maomao.seckill.service;

import com.maomao.seckill.pojo.SeckillOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maomao.seckill.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author maomao
 * @since 2022-03-06
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {
    Long getResult(User user, Long goodsId);

//    SeckillOrder findOne(User user, long id);


}
