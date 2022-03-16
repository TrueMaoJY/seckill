package com.maomao.seckill.controller;

import com.maomao.seckill.service.IUserService;
import com.maomao.seckill.vo.RespBean;
import com.maomao.seckill.vo.LoginVO;
import com.sun.deploy.net.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author MaoJY
 * @create 2022-03-04 21:38
 * @Description:
 */
@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    IUserService iUserService;
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @ResponseBody
    @RequestMapping("/doLogin")
    public RespBean doLogin(@Validated LoginVO loginVO, HttpServletRequest request, HttpServletResponse response){
       return iUserService.doLogin(loginVO,request,response);
    }
}