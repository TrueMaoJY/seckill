package com.maomao.seckill.Exception;

import com.maomao.seckill.vo.RespBean;
import com.maomao.seckill.vo.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author MaoJY
 * @create 2022-03-05 15:09
 * @Description:
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GlobalException.class)
    public RespBean ExceptionHandler(Exception e) {
        if (e instanceof GlobalException) {
            GlobalException gE = (GlobalException) e;
            String message = gE.getMessage();
            System.out.println(message);
            return RespBean.error(gE.getRespBeanEnum());
        } else if (e instanceof BindException) {
            BindException bE = (BindException) e;
            RespBean error = RespBean.error(RespBeanEnum.BIND_ERROR);
            error.setMessage(RespBeanEnum.BIND_ERROR.getMsg() + ":" + bE.getAllErrors().get(0).getDefaultMessage());
            return error;
        }
        return RespBean.error(RespBeanEnum.ERROR);
    }
}