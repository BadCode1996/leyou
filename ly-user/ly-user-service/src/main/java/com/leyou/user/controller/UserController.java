package com.leyou.user.controller;

import com.leyou.user.bean.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Srd
 * @date 2020/8/21  1:36
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 数据校验
     * @param data 将要校验的内容
     * @param type 校验类型 1，用户名  2，手机号
     * @return 数据库有值返回false，无值返回true
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> check(@PathVariable("data")String data, @PathVariable("type")Integer type){
        Boolean bool = this.userService.checkData(data,type);
        if (bool == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(bool);
    }

    /**
     * 注册
     * @param user
     * @param code
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code") String code){
        Boolean flag = this.userService.register(user,code);
        if (flag == null || !flag){
//            500
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
//        200
        return ResponseEntity.ok().build();
    }

    /**
     * 发送验证码
     * @param phone
     * @return
     */
    @PostMapping("code")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone") String phone){
        Boolean flag = this.userService.sendVerifyCode(phone);
        if (flag == null || !flag){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 查询功能，根据参数中的用户名和密码查询指定用户
     * @param record 用户信息
     * @return user
     */
    @PostMapping("query")
    public ResponseEntity<User> queryUser(User record){
        User userRecord = this.userService.queryUser(record);
        if (userRecord == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(userRecord);
    }
}
