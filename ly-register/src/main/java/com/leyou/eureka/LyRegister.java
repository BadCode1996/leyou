package com.leyou.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author Srd
 * @date 2020/5/14  16:58
 */
@SpringBootApplication
@EnableEurekaServer
public class LyRegister {
    public static void main(String[] args) {
        SpringApplication.run(LyRegister.class);
    }
}