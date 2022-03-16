package com.maomao.seckill.service;

import com.maomao.seckill.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maomao.seckill.pojo.User;
import com.maomao.seckill.vo.GoodsVO;
import com.maomao.seckill.vo.OrderDetailVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author maomao
 * @since 2022-03-06
 */
public interface IOrderService extends IService<Order> {

    Order seckill(User user, GoodsVO goodsVO);

    OrderDetailVO detail(Long orderId);

    String createPath(User user, Long goodsId);

    boolean checkPath(User user, long goodsId, String path);

    boolean checkCaptcha(User user, Long goodsId, String captcha);

}
