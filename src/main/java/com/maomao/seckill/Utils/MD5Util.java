package com.maomao.seckill.Utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * @author MaoJY
 * @create 2022-03-04 16:28
 * @Description:
 */
@Component
public class MD5Util {
    public static  final String salt="1a2b3c4d";
    public static String getMd5(String str){
     return    DigestUtils.md5Hex(str);
    }
    private static String inputToServer(String inputPass){
        String str=""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return getMd5(str);
    }
    public static  String DBPass(String serverPass,String salt){
       String str= "" +salt.charAt(0)+salt.charAt(2)+serverPass+salt.charAt(5)+salt.charAt(4);
       return getMd5(str);
    }
    public static String savePass(String inputPass,String salt){
        return DBPass(inputToServer(inputPass),salt);
    }

    public static void main(String[] args) {
        String s = inputToServer("123456");
        System.out.println(s);
        System.out.println(DBPass(s, "1a2b3c4d"));

    }

}