package com.maomao.seckill.controller;

import com.maomao.seckill.pojo.User;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author MaoJY
 * @create 2022-03-10 15:03
 * @Description:
 */
@Controller

public class CaptchaController {
    @Autowired
    RedisTemplate redisTemplate;
    @RequestMapping("/captcha")
    public void captcha(User user, Long goodsId,  HttpServletResponse response) throws Exception {
        if (user == null||goodsId<0) {
            return ;
        }
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        //不缓存自动刷新
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130,32,3);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        redisTemplate.opsForValue().set("captcha:"+user.getId()+":"+goodsId,captcha.text(),60, TimeUnit.SECONDS);
    }


}