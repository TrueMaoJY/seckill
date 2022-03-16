package com.maomao.seckill.rabbitMQ;

import com.maomao.seckill.Utils.JsonUtil;
import com.maomao.seckill.pojo.Order;
import com.maomao.seckill.pojo.SeckillOrder;
import com.maomao.seckill.pojo.User;
import com.maomao.seckill.service.IGoodsService;
import com.maomao.seckill.service.IOrderService;
import com.maomao.seckill.vo.GoodsVO;
import com.maomao.seckill.vo.RespBean;
import com.maomao.seckill.vo.RespBeanEnum;
import com.maomao.seckill.vo.SeckillMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * @author MaoJY
 * @create 2022-03-08 14:41
 * @Description:
 */
@Service
public class MQReceiver {
    @Autowired
    IGoodsService goodsService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    IOrderService orderService;
    @RabbitListener(queues = "seckillQueue")
    public void receive(String message){
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(message, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
           GoodsVO goodsVOByGoodsId = goodsService.findGoodsVOByGoodsId(goodsId);
        if (goodsVOByGoodsId.getStockCount()<1) {
            return;
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();
//        判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder)valueOperations.get("order:" + user.getId()+":"+goodsId);
        if (seckillOrder != null) {
            return ;
        }
        orderService.seckill(user, goodsVOByGoodsId);
    }
}