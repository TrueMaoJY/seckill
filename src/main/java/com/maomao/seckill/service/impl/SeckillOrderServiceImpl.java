package com.maomao.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maomao.seckill.pojo.SeckillOrder;
import com.maomao.seckill.mapper.SeckillOrderMapper;
import com.maomao.seckill.pojo.User;
import com.maomao.seckill.service.ISeckillOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author maomao
 * @since 2022-03-06
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {
    @Autowired
    SeckillOrderMapper seckillOrderMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public Long getResult(User user, Long goodsId) {
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if (seckillOrder!=null){
            return seckillOrder.getOrderId();
//如果库存为0
        }else  if(redisTemplate.hasKey("isStockEmpty"+goodsId)){
            return -1l;
        }
        return 0l;
    }
//    @Autowired
//    SeckillOrderMapper seckillOrderMapper;

//    @Override
//    public SeckillOrder findOne(User user, long id) {
////        SeckillOrder seckillOrder  = baseMapper.selectList(new QueryWrapper<SeckillOrder>().eq("user_id",user.getId()).eq("goods_id",id)).get(0);
//        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", id));
//        return seckillOrder;
//    }

}
