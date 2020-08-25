package com.leyou.auth.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Srd
 * @date 2020/8/25  22:51
 */
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
