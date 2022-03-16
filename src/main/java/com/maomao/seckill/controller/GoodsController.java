package com.maomao.seckill.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.maomao.seckill.pojo.User;
import com.maomao.seckill.service.IGoodsService;
import com.maomao.seckill.service.IUserService;
import com.maomao.seckill.vo.DetailVO;
import com.maomao.seckill.vo.GoodsVO;
import com.maomao.seckill.vo.RespBean;
import com.maomao.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author MaoJY
 * @create 2022-03-05 16:09
 * @Description:
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
@Autowired
    IUserService service;
@Autowired
    IGoodsService goodsService;
@Autowired
RedisTemplate redisTemplate;
@Autowired
    ThymeleafViewResolver thymeleafViewResolver;
/**
* Description:商品列表
* date: 2022/3/7 14:58
* @author: MaoJY
* @since JDK 1.8
*/
//TODO 后期改进使用nginx进行动静分离
    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model,User user,HttpServletRequest request,HttpServletResponse response){
//        if(StringUtils.isBlank(ticket)){
//            return "login";
//        }
//       User user=service.getUserByCookies(ticket,request,response);
        //把页面用redis加载到缓存中 ---qps  158
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html =(String) valueOperations.get("goodsList");
        if (html != null) {
            return html;
        }
        if(user==null)return "login";
        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsService.findGoodsVO());
        //如果为空手动渲染，加入redis
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html=thymeleafViewResolver.getTemplateEngine().process("goodsList",webContext);
        if (StringUtils.isNotBlank(html)) {
                valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
        }
        return html;
    }
    /**
    * Description:商品详情
    * date: 2022/3/7 14:57
    * @author: MaoJY
    * @since JDK 1.8
    */
    @ResponseBody
    @RequestMapping("/toDetail/{goodsId}")
    public RespBean toDetail(User user, @PathVariable ("goodsId")long goodsId){
        if(user==null)return RespBean.error(RespBeanEnum.SESSION_ERROR);
        GoodsVO goods = goodsService.findGoodsVOByGoodsId(goodsId);
        Date startDate = goods.getStartDate();
        Date endDate = goods.getEndDate();
        Date nowDate=new Date();
        //设置商品状态
        int secKillStatus=0;
        //秒杀开始前
        int remainSeconds=0;
//        未开始
        if(nowDate.before(startDate)){
            secKillStatus=0;
            remainSeconds = (int)((startDate.getTime()-nowDate.getTime())/1000);
            //已结束
        }else  if(nowDate.after(endDate)){
            remainSeconds=-1;
            secKillStatus=2;
            //正在进行
        }else{
            secKillStatus=1;
        }
        DetailVO detailVO = new DetailVO();
        detailVO.setGoodsVO(goods);
        detailVO.setRemainSeconds(remainSeconds);
        detailVO.setUser(user);
        detailVO.setSecKillStatus(secKillStatus);
        return RespBean.success(detailVO);
    }
}