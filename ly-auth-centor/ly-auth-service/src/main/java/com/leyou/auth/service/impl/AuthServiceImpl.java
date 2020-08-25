package com.leyou.auth.service.impl;

import com.leyou.auth.bean.UserInfo;
import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.user.bean.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;

/**
 * @author Srd
 * @date 2020/8/25  22:46
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserClient userClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    /**
     * 授权检验
     * @param username
     * @param password
     * @param privateKey
     * @param expire
     * @return
     * @throws Exception
     */
    @Override
    public String authentication(String username, String password, PrivateKey privateKey, int expire) throws Exception {
        try {
//            查询用户
            User user = this.userClient.queryUser(username, password);
            if (user == null) {
                LOGGER.info("用户信息不存在，{}", username);
                return null;
            }
//            生成token
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()), privateKey, expire);
            return token;
        } catch (Exception e) {
            LOGGER.info("服务器异常，{}",e);
            return null;
        }
    }
}
