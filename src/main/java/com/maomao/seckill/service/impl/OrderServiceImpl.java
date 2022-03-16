package com.maomao.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.maomao.seckill.Exception.GlobalException;
import com.maomao.seckill.Utils.JsonUtil;
import com.maomao.seckill.Utils.MD5Util;
import com.maomao.seckill.Utils.UUIDUtil;
import com.maomao.seckill.pojo.Order;
import com.maomao.seckill.mapper.OrderMapper;
import com.maomao.seckill.pojo.SeckillGoods;
import com.maomao.seckill.pojo.SeckillOrder;
import com.maomao.seckill.pojo.User;
import com.maomao.seckill.service.IGoodsService;
import com.maomao.seckill.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maomao.seckill.service.ISeckillGoodsService;
import com.maomao.seckill.service.ISeckillOrderService;
import com.maomao.seckill.vo.GoodsVO;
import com.maomao.seckill.vo.OrderDetailVO;
import com.maomao.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author maomao
 * @since 2022-03-06
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    RedisTemplate redisTemplate;


    @Override
    public Order seckill(User user, GoodsVO goods) {
//        秒杀表减去库存
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId()));
        boolean result = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count=" +
                "stock_count-1").eq("goods_id", goods.getId()).gt("stock_count", 0));
        //判断是否有库存
        if (seckillGoods.getStockCount()<1) {
            redisTemplate.opsForValue().set("isStockEmpty"+goods.getId(),"0");
            return null;
        }
       //生成订单
        Order order =new Order();
        order.setGoodsId(goods.getId());
        order.setUserId(user.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setOrderChannel(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);
        redisTemplate.opsForValue().set("order:" + user.getId() + ":" +
                        goods.getId(),
                JsonUtil.object2JsonStr(seckillOrder));
        return order;
    }

    @Override
    public OrderDetailVO detail(Long orderId) {
        if (orderId == null) {
            throw  new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order=orderMapper.selectById(orderId);
        GoodsVO goodsVOByGoodsId = goodsService.findGoodsVOByGoodsId(order.getGoodsId());

        return new OrderDetailVO(order,goodsVOByGoodsId);
    }

    @Override
    public String createPath(User user, Long goodsId) {
        String str = MD5Util.getMd5(UUIDUtil.uuid() + "123456");
        redisTemplate.opsForValue().set("seckillPath:"+user.getId()+":"+goodsId,str,60, TimeUnit.SECONDS);
        return str;
    }
//比对两个path
    @Override
    public boolean checkPath(User user, long goodsId, String path) {
        if(user==null||goodsId<1|| StringUtils.isBlank(path)){
            return false;
        }
        String  str = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);

        return path.equals(str);
    }

    @Override
    public boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if(user==null||goodsId<0||StringUtils.isBlank(captcha))return  false;
        String  redisCaptcha =(String) redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);
        return redisCaptcha.equals(captcha);
    }
}
