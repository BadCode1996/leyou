package com.leyou.goods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Srd
 * @date 2020/8/14  14:21
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LyGoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyGoodsApplication.class);
    }
}
