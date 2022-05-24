package com.albert.cloud.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yjw
 * @date 2022/5/23 23:30
 */
@Configuration
public class FeignConfig {

    /**
     * Feign接口的日志级别
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}

