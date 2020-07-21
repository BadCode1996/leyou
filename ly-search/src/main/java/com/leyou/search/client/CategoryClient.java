package com.leyou.search.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Srd
 * @date 2020/7/22  0:53
 */
@FeignClient(value = "item-service")
public interface CategoryClient extends CategoryApi {
}
