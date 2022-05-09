package com.albert.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author yjw
 * @date 2022/4/28 22:22
 */
@SpringBootApplication
@EnableDiscoveryClient
public class OrderMainConsul80 {

    public static void main(String[] args) {
        SpringApplication.run(OrderMainConsul80.class);
    }


}
