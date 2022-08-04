package com.albert.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author yjw
 * @date 2022/8/3 23:30
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消数据源的自动创建
@EnableFeignClients
@EnableDiscoveryClient
public class SeataAccountMainApp2003 {

    public static void main(String[] args) {
        SpringApplication.run(SeataAccountMainApp2003.class);
    }


}
