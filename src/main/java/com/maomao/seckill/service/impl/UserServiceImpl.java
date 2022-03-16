package com.maomao.seckill.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.maomao.seckill.Exception.GlobalException;
import com.maomao.seckill.Utils.CookieUtil;
import com.maomao.seckill.Utils.MD5Util;
import com.maomao.seckill.Utils.UUIDUtil;
import com.maomao.seckill.pojo.User;
import com.maomao.seckill.mapper.UserMapper;
import com.maomao.seckill.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maomao.seckill.vo.LoginVO;
import com.maomao.seckill.vo.RespBean;
import com.maomao.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author maomao
 * @since 2022-03-04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
@Autowired
UserMapper mapper;
@Autowired
    RedisTemplate redisTemplate;
    @Override
    public RespBean doLogin(LoginVO loginVO, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();
//    if(StringUtils.isEmpty(mobile)){
//        return  RespBean.error(RespBeanEnum.LOGIN_ERROR);
//    }
////手机号格式不匹配
//    if(!ValidatorVO.isMobile(mobile)){
//        return RespBean.error(RespBeanEnum.MOBILE_PATTERN_ERROR);
//    }
        User user = mapper.selectById(mobile);
        if(user==null){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        String truePassword= MD5Util.DBPass(password,MD5Util.salt);
       if(user.getPasword().equals(truePassword)){
           String ticket=UUIDUtil.uuid();
//           request.getSession().setAttribute("ticket",user);
           redisTemplate.opsForValue().set("user:"+ticket,user);
           CookieUtil.setCookie(request,response,"userTicket",ticket);
           return RespBean.success();
       }else {
           throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
       }



    }

    @Override
    public User getUserByCookies(String userTicket,HttpServletRequest request,HttpServletResponse response) {
        if (StringUtils.isNotBlank(userTicket)) {
            User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
            return user;
        }

        return null;
    }
}
