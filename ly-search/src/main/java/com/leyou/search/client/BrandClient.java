package com.leyou.search.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Srd
 * @date 2020/7/28  15:30
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
