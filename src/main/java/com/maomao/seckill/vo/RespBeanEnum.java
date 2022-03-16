package com.maomao.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author MaoJY
 * @create 2022-03-04 21:22
 * @Description:
 */
@ToString
@Getter
@AllArgsConstructor
public enum RespBeanEnum {
    SUCCESS (200,"success"),
    ERROR(500,"服务器端异常"),

    //登录
    LOGIN_ERROR (500200,"用户名或密码错误"),
    MOBILE_PATTERN_ERROR(500201,"手机号格式错误"),
    BIND_ERROR (500202,"数据校验异常"),
    SESSION_ERROR(500203,"用户信息已过期，请重新登录"),
    //秒杀失败

    NULL_STOCK_ERROR(500500,"商品库存不足"),
    REPEAT_ERROR(500501,"只能购买一件商品"),
    REQUEST_ILLAGE(500502,"非法请求"),
    NULL_CAPTCHA(500503,"验证码不能为空"),
    ERROR_CAPTCHA(500504,"验证码错误"),
    ACCESS_LIMIT_REACHED(500505,"请求过于频繁，请稍后再试"),
    //订单模块
    ORDER_NOT_EXIST(500300,"订单不存在"),
    ;
    private final Integer code;
    private final  String msg;
}
