package com.maomao.seckill.mapper;

import com.maomao.seckill.pojo.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maomao.seckill.vo.GoodsVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author maomao
 * @since 2022-03-06
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    List<GoodsVO> findGoodsVO();

    GoodsVO findGoodsVOByGoodsId(long goodsId);
}
