package com.maomao.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.maomao.seckill.Utils.JsonUtil;
import com.maomao.seckill.Validator.AccessLimit;
import com.maomao.seckill.pojo.Order;
import com.maomao.seckill.pojo.SeckillOrder;
import com.maomao.seckill.pojo.User;
import com.maomao.seckill.rabbitMQ.MQSender;
import com.maomao.seckill.service.IGoodsService;
import com.maomao.seckill.service.IOrderService;
import com.maomao.seckill.service.ISeckillOrderService;
import com.maomao.seckill.vo.GoodsVO;
import com.maomao.seckill.vo.RespBean;
import com.maomao.seckill.vo.RespBeanEnum;
import com.maomao.seckill.vo.SeckillMessage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author MaoJY
 * @create 2022-03-07 15:02
 * @Description:
 */
//TODO 每一个页面都要验证用户是否注册？
@Controller
@RequestMapping("/seckill")
public class SecKillController  implements InitializingBean {
    @Autowired
    IGoodsService goodsService;
    @Autowired
    ISeckillOrderService seckillOrderService;
    @Autowired
    IOrderService orderService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    MQSender sender;
//    给库存做标记，没有库存就不要继续访问redis了
    private HashMap<Long,Boolean> emptyStockMap=new HashMap<>();

    @RequestMapping("/doSeckill1")
    public String doSeckill1(User user, long goodsId, Model model){
        if(user==null)return "login";
        model.addAttribute("user",user);
        GoodsVO goods = goodsService.findGoodsVOByGoodsId(goodsId);
        int stockCount = goods.getStockCount();
        if(stockCount<1){
               model.addAttribute("errmsg", RespBean.error(RespBeanEnum.NULL_STOCK_ERROR).getMessage());
               return "secKillFail";
        }
        //查询该用户是否购买超过一件商品
        SeckillOrder seckillOrder = seckillOrderService.getOne
                (new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if (seckillOrder!=null){
            model.addAttribute("errmsg",RespBean.error(RespBeanEnum.REPEAT_ERROR).getMessage());
            return "secKillFail";
        }
        Order order = orderService.seckill(user, goods);
        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        return "orderDetail.html";
    }

    @RequestMapping("/{path}/doSeckill")
    @ResponseBody
    public RespBean doSeckill(@PathVariable("path") String path, User user, long goodsId){
        if(user==null)return RespBean.error(RespBeanEnum.SESSION_ERROR);
//        GoodsVO goods = goodsService.findGoodsVOByGoodsId(goodsId);
//        int stockCount = goods.getStockCount();
//        if(stockCount<1){
//            return RespBean.error(RespBeanEnum.NULL_STOCK_ERROR);
//        }
//        //查询该用户是否购买超过一件商品
//        SeckillOrder seckillOrder = (SeckillOrder)redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goods.getId());
//        if (seckillOrder != null) {
//            return  RespBean.error(RespBeanEnum.REPEAT_ERROR);
//        }
//        Order order = orderService.seckill(user, goods);
//        return RespBean.success(order);
//        判断是否重复抢购
        ValueOperations valueOperations = redisTemplate.opsForValue();
       boolean check= orderService.checkPath(user,goodsId,path);
        if (!check) {
            return RespBean.error(RespBeanEnum.REQUEST_ILLAGE);
        }
         String orderStr=(String) valueOperations.get("order:" + user.getId() + ":" + goodsId);
        if (orderStr!=null){
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        //减去redis中的库存量
        Long stock = valueOperations.decrement("secKillGoods:" + goodsId);
        if(emptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.NULL_STOCK_ERROR);
        }
        if(stock==0){
            emptyStockMap.put(goodsId,true);
            return RespBean.error(RespBeanEnum.NULL_STOCK_ERROR);
        }
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        sender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));
    //把用户，商品id存入队列，
        return RespBean.success(0);
    }
    //0---排队中 -1 失败  >0成功
    @ResponseBody
    @RequestMapping("/result")
    public  RespBean getResult(User user,Long goodsId){
        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId=seckillOrderService.getResult(user,goodsId);
        return RespBean.success(orderId);
    }
/**
* Description:秒杀地址接口
* date: 2022/3/9 22:52
* @author: MaoJY
* @since JDK 1.8
*/
@AccessLimit(second=5,maxCount=5,needLogin=true)
@RequestMapping("/path")
@ResponseBody
public RespBean getPath(User user ,Long goodsId,String captcha){
    if (user == null) {
        return RespBean.error(RespBeanEnum.SESSION_ERROR);
    }
    if (StringUtils.isBlank(captcha)) {
        return RespBean.error(RespBeanEnum.NULL_CAPTCHA);
    }
   boolean checkCaptcha= orderService.checkCaptcha(user,goodsId,captcha);
    if (!checkCaptcha) {
        return  RespBean.error(RespBeanEnum.REQUEST_ILLAGE);
    }
   String path =orderService.createPath(user,goodsId);
    return RespBean.success(path);
}
//项目启动时将该商品的库存保存到redis中
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVO> list = goodsService.findGoodsVO();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(goodsVO ->{
            emptyStockMap.put(goodsVO.getId(),false);
            redisTemplate.opsForValue().set("secKillGoods:"+goodsVO.getId(),goodsVO.getStockCount());
        });
    }
}