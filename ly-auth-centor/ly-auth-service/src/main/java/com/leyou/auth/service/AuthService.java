package com.leyou.auth.service;

import java.security.PrivateKey;

/**
 * @author Srd
 * @date 2020/8/25  16:09
 */
public interface AuthService {

    /**
     * 授权检验
     * @param username
     * @param password
     * @param privateKey
     * @param expire
     * @return
     * @throws Exception
     */
    String authentication(String username, String password, PrivateKey privateKey,int expire) throws Exception;
}
