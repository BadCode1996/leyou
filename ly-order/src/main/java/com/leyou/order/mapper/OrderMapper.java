package com.leyou.order.mapper;

import com.leyou.order.bean.Order;
import tk.mybatis.mapper.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Srd
 * @date 2020/8/30  1:37
 */
public interface OrderMapper extends Mapper<Order> {

    List<Order> queryOrderList(
            @Param("userId") Long userId,
            @Param("status") Integer status);
}
