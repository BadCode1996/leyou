package com.leyou.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Srd
 * @date 2020/7/18  1:12
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LyEsApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyEsApplication.class);
    }
}
