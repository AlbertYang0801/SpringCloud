package com.albert.myrule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangjunwei
 * @date 2022/5/19 1:45 下午
 */
@Configuration
public class MySelfRule {

    @Bean
    public IRule myRule(){
        //随机
        return new RandomRule();
    }

}
