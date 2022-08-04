package com.albert.cloud.cofnig;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yjw
 * @date 2022/8/3 23:29
 */
@Configuration
@MapperScan({"com.albert.cloud.dao"})
public class MybatisConfig {

}