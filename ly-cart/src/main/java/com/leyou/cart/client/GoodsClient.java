package com.leyou.cart.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Srd
 * @date 2020/8/27  16:41
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
