package com.leyou.item.service;

import com.leyou.common.bean.PageResult;
import com.leyou.item.bean.Sku;
import com.leyou.item.bean.SpuDetail;
import com.leyou.item.bo.SpuBo;

import java.util.List;

/**
 * @author Srd
 * @date 2020/5/25  16:18
 */
public interface GoodsService {

    /**
     * 分页查询商品列表
     * @param key 搜索条件
     * @param saleable 是否上下架
     * @param page 当前页
     * @param rows 页容量
     * @return
     */
    PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows);

    /**
     * 新增商品
     * @param spu
     */
    void save(SpuBo spu);

    /**
     * 修改商品，根据id回显商品内容
     * @param id
     * @return
     */
    SpuDetail querySpuDetailBySpuId(Long id);

    /**
     * 修改商品，根据id获取sku，回显数据
     * @param id
     * @return
     */
    List<Sku> querySkuBySpuId(Long id);

    /**
     * 修改商品
     * @param spu
     */
    void edit(SpuBo spu);

    /**
     * 对商品进行上下架操作
     * @param id
     */
    void changeSaleable(Long id);
}
