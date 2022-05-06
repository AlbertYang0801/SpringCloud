package com.albert.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * eureka客户端
 * @author yjw
 * @date 2022/4/28 22:22
 */
@SpringBootApplication
@EnableEurekaClient
public class PaymentMain8081 {

    public static void main(String[] args) {
        SpringApplication.run(PaymentMain8081.class);
    }


}
