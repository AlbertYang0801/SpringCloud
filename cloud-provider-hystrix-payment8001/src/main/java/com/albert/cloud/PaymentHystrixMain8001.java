package com.albert.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author yjw
 * @date 2022/5/25 21:06
 */
@SpringBootApplication
@EnableDiscoveryClient
//激活Hystrix
@EnableCircuitBreaker
public class PaymentHystrixMain8001 {

    public static void main(String[] args) {
        SpringApplication.run(PaymentHystrixMain8001.class);
    }

}
