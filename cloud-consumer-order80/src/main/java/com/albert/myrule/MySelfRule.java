package com.albert.myrule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义的配置类不能被ComponentScan注解扫描到，所以不能放到启动类的子目录下。
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
