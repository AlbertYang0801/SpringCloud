package com.albert.cloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author yjw
 * @date 2022/6/19 18:35
 */
@Configuration
public class ApplicationContextConfig {

    /**
     * LoadBalanced 负载均衡，Nacos集成了Ribbon
     */
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }


}