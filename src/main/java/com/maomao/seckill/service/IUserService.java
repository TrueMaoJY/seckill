package com.maomao.seckill.service;

import com.maomao.seckill.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maomao.seckill.vo.LoginVO;
import com.maomao.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author maomao
 * @since 2022-03-04
 */
public interface IUserService extends IService<User> {



    RespBean doLogin(LoginVO loginVO, HttpServletRequest request, HttpServletResponse response);
    User getUserByCookies(String userTicket,HttpServletRequest request,HttpServletResponse response);
}
