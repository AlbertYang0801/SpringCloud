package com.albert.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author yjw
 * @date 2022/6/19 18:34
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosOrderMain83 {

    public static void main(String[] args) {
        SpringApplication.run(NacosOrderMain83.class);
    }


}
