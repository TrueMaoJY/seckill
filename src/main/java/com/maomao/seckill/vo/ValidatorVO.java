package com.maomao.seckill.vo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author MaoJY
 * @create 2022-03-04 22:17
 * @Description:
 */
public class ValidatorVO {
    private static  final String regex="^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(16[5,6])|(17[0-8])|(18[0-9])|(19[1、5、8、9]))\\d{8}$";
    public static boolean isMobile(String mobile) {
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }
}