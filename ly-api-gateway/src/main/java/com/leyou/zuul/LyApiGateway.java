package com.leyou.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author Srd
 * @date 2020/5/14  17:02
 */
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class LyApiGateway {
    public static void main(String[] args) {
        SpringApplication.run(LyApiGateway.class);
    }
}
