package com.maomao.seckill.Validator;

import com.maomao.seckill.pojo.User;

/**
 * @author MaoJY
 * @create 2022-03-10 20:09
 * @Description:
 */
public class UserContext {
    //TODO 内存泄漏问题
    private static ThreadLocal<User> userHolder =new ThreadLocal<>();
    public static void setUser(User user){
        userHolder.set(user);
    }
    public static  User getUser(){
        return userHolder.get();
    }
}