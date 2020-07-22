package com.leyou.search.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Srd
 * @date 2020/7/22  15:52
 */
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
