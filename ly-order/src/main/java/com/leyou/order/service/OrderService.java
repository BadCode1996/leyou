package com.leyou.order.service;

import com.leyou.common.bean.PageResult;
import com.leyou.order.bean.Order;

/**
 * @author Srd
 * @date 2020/8/30  1:18
 */
public interface OrderService {
    Long createOrder(Order order);

    Order queryById(Long id);

    PageResult<Order> queryUserOrderList(Integer page, Integer rows, Integer status);

    Boolean updateStatus(Long id, Integer status);

}
