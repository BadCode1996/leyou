package com.leyou.search.service;

import com.leyou.item.bean.Spu;
import com.leyou.item.bo.SpuBo;
import com.leyou.search.bean.Goods;

/**
 * @author Srd
 * @date 2020/7/22  15:53
 */
public interface IndexService {

    /**
     * 构建商品
     * @param spuBo
     * @return
     */
    Goods buildGoods(SpuBo spuBo);
}
