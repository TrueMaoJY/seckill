package com.maomao.seckill.config;

import com.maomao.seckill.Validator.AccessLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

/**
 * @author MaoJY
 * @create 2022-03-06 14:59
 * @Description:
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
@Autowired
UserArgumentResolver userArgumentResolver;
@Autowired
    AccessLimitInterceptor accessLimitInterceptor;
//    @EnableWebMvc全面接管webmvc配置，需要自己放行静态资源、、配置类》配置文件》约定
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
       registry.addResourceHandler("/**").addResourceLocations("classpath:/static/","classpath:/templates/");
    }
    //欢迎页设置
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
    }
//自定义参数处理
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessLimitInterceptor);
    }
}