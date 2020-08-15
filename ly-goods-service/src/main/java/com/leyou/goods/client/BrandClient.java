package com.leyou.goods.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Srd
 * @date 2020/8/14  17:25
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
