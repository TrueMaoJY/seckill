package com.maomao.seckill.vo;

import com.maomao.seckill.Validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author MaoJY
 * @create 2022-03-04 21:44
 * @Description:
 */
@Data
public class LoginVO {
    @NotNull
    @Length
    String password;
    @NotNull
    @IsMobile
    String mobile;
}