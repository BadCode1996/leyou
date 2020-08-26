package com.leyou.auth.controller;

import com.leyou.auth.bean.UserInfo;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Srd
 * @date 2020/8/25  16:02
 */
@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties properties;

    /**
     * 认证中心
     */
    @PostMapping("accredit")
    public ResponseEntity<Void> authentication(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        String token = this.authService.authentication(username, password, properties.getPrivateKey(), properties.getExpire());
        if (StringUtils.isBlank(token)) {
//            UNAUTHORIZED 401 未授权
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
//        将token写入cookie，并指定httpOnly为true，防止通过js获取或修改
        CookieUtils.setCookie(request, response, properties.getCookieName(), token, properties.getCookieMaxAge(), null, true);
        return ResponseEntity.ok().build();
    }

    /**
     * 校验用户接口
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verifyUser(
            @CookieValue("LY_TOKEN") String token,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
//            获取token信息
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, properties.getPublicKey());
//            刷新token
            String newToken = JwtUtils.generateToken(userInfo, properties.getPrivateKey(), properties.getExpire());
//            把新生产的token写入cookie
            CookieUtils.setCookie(request, response, properties.getCookieName(), token, properties.getCookieMaxAge(), null, true);
//            成功直接返回
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
//            抛出异常，说明token无效，返回401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
