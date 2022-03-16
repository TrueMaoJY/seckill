package com.maomao.seckill.controller;


import com.maomao.seckill.pojo.User;
import com.maomao.seckill.service.IOrderService;
import com.maomao.seckill.vo.OrderDetailVO;
import com.maomao.seckill.vo.RespBean;
import com.maomao.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author maomao
 * @since 2022-03-06
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    IOrderService orderService;
    @ResponseBody
    @RequestMapping("/detail")
    public RespBean detail(User user, Long orderId){
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
      OrderDetailVO orderDetailVO= orderService.detail(orderId);
        return RespBean.success(orderDetailVO);
    }
}
