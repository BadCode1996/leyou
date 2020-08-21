package com.leyou.sms.listener;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.leyou.sms.property.SmsProperties;
import com.leyou.sms.util.SmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Srd
 * @date 2020/8/21  16:40
 */
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {

    @Autowired
    private SmsUtils smsUtils;

    @Autowired
    private SmsProperties smsProperties;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ly.sms.queue", durable = "true"),
            exchange = @Exchange(
                    value = "ly.sms.exchange",
                    ignoreDeclarationExceptions = "true"),
            key = {"sms.verify.code"}
    ))
    public void smsListen(Map<String, String> msg) throws ClientException, InterruptedException {
        Thread.sleep(500);
        if (msg == null || msg.size() == 0) {
//            放弃处理
            return;
        }
        String phone = msg.get("phone");
        String code = msg.get("code");
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(code)) {
//            放弃处理
            return;
        }
//        fas消息
        SendSmsResponse response = this.smsUtils.sendSms(
                phone,
                code,
                smsProperties.getSignName(),
                smsProperties.getVerifyCodeTemplate());
        if (!"OK".equals(response.getMessage())) {
//            发送失败
            throw new RuntimeException();
        }
    }
}
