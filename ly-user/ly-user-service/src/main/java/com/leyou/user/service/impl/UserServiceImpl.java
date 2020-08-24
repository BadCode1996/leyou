package com.leyou.user.service.impl;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.bean.User;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.service.UserService;
import com.leyou.user.util.CodecUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Srd
 * @date 2020/8/21  1:41
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    static final String KEY_PREFIX = "user:code:phone:";
    static final Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * 数据校验
     *
     * @param data 将要校验的内容
     * @param type 校验类型 1，用户名  2，手机号
     * @return 数据库有值返回false，无值返回true
     */
    @Override
    public Boolean checkData(String data, Integer type) {
        User user = new User();
        switch (type) {
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                return null;
        }
        return this.userMapper.selectCount(user) == 0;
    }

    /**
     * 注册
     * @param user
     * @param code
     * @return
     */
    @Override
    public Boolean register(User user, String code) {
        String key = KEY_PREFIX + user.getPhone();
//        从redis中取code值
        String codeCache = this.redisTemplate.opsForValue().get(key);
        if (!code.equals(codeCache)){
//            验证码错误，返回
            return false;
        }
        user.setId(null);
        user.setCreated(new Date());
//        生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
//        对密码进行加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));
//        写入数据库
        boolean flag = this.userMapper.insertSelective(user) == 1;
//        如果注册成功，删除redis中的code
        if (flag){
            try {
                this.redisTemplate.delete(key);
            } catch (Exception e) {
                logger.error("删除缓存验证码失败，code：{}", code, e);
            }
        }
        return flag;
    }

    /**
     * 查询功能，根据参数中的用户名和密码查询指定用户
     * @param record 用户信息
     * @return user
     */
    @Override
    public User queryUser(User record) {
        User user1 = new User();
        user1.setUsername(record.getUsername());
        User user = this.userMapper.selectOne(user1);
//        校验用户
        if (user == null){
            return null;
        }
//        校验密码
        if (!user.getPassword().equals(CodecUtils.md5Hex(record.getPassword(),user.getSalt()))){
            return null;
        }
        return user;
    }

    /**
     * 发送验证码
     * @param phone
     * @return
     */
    @Override
    public Boolean sendVerifyCode(String phone) {
//        生成6位数的验证码
        String code = NumberUtils.generateCode(6);
        try {
            Map<String, String> msg = new HashMap<>(2);
            msg.put("phone", phone);
            msg.put("code", code);
//            发送消息到rabbitmq，有mq来执行发送验证码的业务
//            不发送短信验证码，直接去redis中获取
//            this.rabbitTemplate.convertAndSend("ly.sms.exchange", "sms.verify.code", msg);
//            保存验证码到redis，5分钟后到期自动删除
            this.redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            logger.error("发送短信失败。phone：{}， code：{}", phone, code);
            return false;
        }
    }
}
