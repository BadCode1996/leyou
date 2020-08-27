package com.leyou.cart.service;

import com.leyou.cart.bean.Cart;

import java.util.List;

/**
 * @author Srd
 * @date 2020/8/27  16:26
 */
public interface CartService {

    /**
     * 添加购物车
     * @param cart
     */
    void addCart(Cart cart);

    /**
     * 查询carts
     * @return
     */
    List<Cart> queryCarts();

    /**
     * 更新购物车内商品数量
     * @param skuId
     * @param num
     */
    void updateNum(Long skuId, Integer num);

    /**
     * 删除Cart
     * @param skuId
     */
    void deleteCart(Long skuId);

}
