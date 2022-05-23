package com.albert.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author yjw
 * @date 2022/5/23 21:25
 */
@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
public class OrderFeginMain80 {

    public static void main(String[] args) {
        SpringApplication.run(OrderFeginMain80.class);
    }


}
