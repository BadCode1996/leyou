package com.leyou.goods.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Srd
 * @date 2020/8/14  17:24
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
