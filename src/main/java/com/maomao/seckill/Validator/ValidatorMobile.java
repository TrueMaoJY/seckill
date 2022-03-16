package com.maomao.seckill.Validator;

import com.maomao.seckill.vo.ValidatorVO;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author MaoJY
 * @create 2022-03-05 14:50
 * @Description:
 */
public class ValidatorMobile implements ConstraintValidator<IsMobile,String> {
    private  boolean required =false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required= constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
       if (!required){
           if(StringUtils.isEmpty(s)){
               return false;
           }
       }
       return ValidatorVO.isMobile(s);
    }
}