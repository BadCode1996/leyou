package com.leyou.goods.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Srd
 * @date 2020/8/14  17:31
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
