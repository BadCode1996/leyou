package com.leyou.search.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Srd
 * @date 2020/7/22  15:58
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
