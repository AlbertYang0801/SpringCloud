package com.albert.cloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author yjw
 * @date 2022/5/6 21:18
 */
@Configuration
public class ApplicationContextConfig {

    /**
     * LoadBalanced 负载均衡策略，默认是轮询
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


}
