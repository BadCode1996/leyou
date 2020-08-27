package com.leyou.cart.interceptor;

import com.leyou.auth.bean.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.cart.config.JwtProperties;
import com.leyou.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Srd
 * @date 2020/8/27  15:49
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private JwtProperties properties;

    /**
     * 定义一个线程域，存放登录用户信息
     */
    private static final ThreadLocal<UserInfo> tl = new ThreadLocal<>();

    public LoginInterceptor(JwtProperties properties) {
        this.properties = properties;
    }

    /**
     * This implementation always returns {@code true}.
     *
     * @param request
     * @param response
     * @param handler
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
//            获取token
            String token = CookieUtils.getCookieValue(request, properties.getCookieName());
            if (StringUtils.isBlank(token)) {
//                token为空，说明未登录，返回401
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
//            有token，获取userInfo
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, properties.getPublicKey());
//            放入线程域
            tl.set(userInfo);
            return true;
        } catch (Exception e) {
//            抛出异常，说明未登录，返回401
            e.printStackTrace();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }

    /**
     * This implementation is empty.
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        tl.remove();
    }

    public static UserInfo getLoginUser(){
        return tl.get();
    }
}
