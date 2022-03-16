package com.maomao.seckill.Validator;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maomao.seckill.Utils.CookieUtil;
import com.maomao.seckill.pojo.User;
import com.maomao.seckill.service.IUserService;
import com.maomao.seckill.vo.RespBean;
import com.maomao.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;


/**
 * @author MaoJY
 * @create 2022-03-10 19:56
 * @Description:
 */
/**
* Description:配置访问限制拦截器
 * true不拦截
* date: 2022/3/10 20:01
* @author: MaoJY
* @since JDK 1.8
*/
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {
    @Autowired
    private  IUserService service;
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
     User user=  getUser(request,response);
     UserContext.setUser(user);
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int second=accessLimit.second();
            int maxCount=accessLimit.maxCount();
            boolean needLogin=accessLimit.needLogin();
            String key=request.getRequestURI();
            if(needLogin){
                if (user == null) {
                    render(response, RespBeanEnum.SESSION_ERROR);
                    return false;
                }
                key+=":"+user.getId();
            }
            Integer count = (Integer) redisTemplate.opsForValue().get(key);
            if (count == null) {
                redisTemplate.opsForValue().set(key,1,second, TimeUnit.SECONDS);
            }else if(count<maxCount){
                redisTemplate.opsForValue().increment(key);
            }else {
                render(response,RespBeanEnum.ACCESS_LIMIT_REACHED);
                return false;
            }
        }
        return true;
    }
/**
* Description:构建返回对象
* date: 2022/3/10 20:22
* @author: MaoJY
* @since JDK 1.8
*/
    private void render(HttpServletResponse response, RespBeanEnum respBeanEnum) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        RespBean respBean = RespBean.error(respBeanEnum);
        writer.write(new ObjectMapper().writeValueAsString(respBean));
        writer.flush();;
        writer.close();
    }

    /**
* Description:获取当前登录用户
* date: 2022/3/10 20:07
* @author: MaoJY
* @since JDK 1.8
*/
    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String ticket = CookieUtil.getCookieValue(request, "userTicket");
        if(StringUtils.isBlank(ticket)){
            return null;
        }
        User user=service.getUserByCookies(ticket,request,response);
        return user;

    }
}