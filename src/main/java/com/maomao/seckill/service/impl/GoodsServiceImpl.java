package com.maomao.seckill.service.impl;

import com.maomao.seckill.pojo.Goods;
import com.maomao.seckill.mapper.GoodsMapper;
import com.maomao.seckill.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maomao.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author maomao
 * @since 2022-03-06
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {
    @Autowired
   private GoodsMapper goodsMapper;
    @Override
    public List<GoodsVO> findGoodsVO() {
        return goodsMapper.findGoodsVO();

    }
    @Override
    public GoodsVO findGoodsVOByGoodsId(long goodsId) {
        return goodsMapper.findGoodsVOByGoodsId(goodsId);
    }
}
