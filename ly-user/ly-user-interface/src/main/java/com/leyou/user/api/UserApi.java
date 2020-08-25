package com.leyou.user.api;

import com.leyou.user.bean.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Srd
 * @date 2020/8/25  22:48
 */
public interface UserApi {

    /**
     * 查询功能，根据参数中的用户名和密码查询指定用户
     * @param username 用户名
     * @param password 密码
     * @return user
     */
    @PostMapping("query")
    User queryUser(@RequestParam("username") String username, @RequestParam("password") String password);
}
