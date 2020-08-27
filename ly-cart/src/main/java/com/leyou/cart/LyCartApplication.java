package com.leyou.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Srd
 * @date 2020/8/26  16:41
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LyCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyCartApplication.class);
    }
}
