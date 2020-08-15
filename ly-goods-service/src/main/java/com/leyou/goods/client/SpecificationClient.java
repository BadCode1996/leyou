package com.leyou.goods.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Srd
 * @date 2020/8/14  17:31
 */
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
