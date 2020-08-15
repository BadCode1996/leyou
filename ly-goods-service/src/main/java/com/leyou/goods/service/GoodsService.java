package com.leyou.goods.service;

import java.util.Map;

/**
 * @author Srd
 * @date 2020/8/14  15:38
 */
public interface GoodsService {

    /**
     * 封装数据Spu、Sku集合、SpecGroup、Categories、Brand、sku特有参数
     * @param id
     * @return
     */
    Map<String, Object> loadModel(Long id);
}
