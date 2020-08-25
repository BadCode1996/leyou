package com.leyou.user.service;

import com.leyou.user.bean.User;

/**
 * @author Srd
 * @date 2020/8/21  1:38
 */
public interface UserService {

    /**
     * 数据校验
     * @param data 将要校验的内容
     * @param type 校验类型 1，用户名  2，手机号
     * @return 数据库有值返回false，无值返回true
     */
    Boolean checkData(String data, Integer type);

    /**
     * 发送验证码
     * @param phone 发送验证码的手机号
     * @return Boolean
     */
    Boolean sendVerifyCode(String phone);

    /**
     * 注册
     * @param user 用户的注册信息
     * @param code 验证码
     * @return Boolean
     */
    Boolean register(User user, String code);

    /**
     * 查询功能，根据参数中的用户名和密码查询指定用户
     * @param username 用户名
     * @param password 密码
     * @return User
     */
    User queryUser(String username, String password);
}
