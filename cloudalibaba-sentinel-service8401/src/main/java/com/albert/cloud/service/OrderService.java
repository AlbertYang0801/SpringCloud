package com.albert.cloud.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;

/**
 * @author yjw
 * @date 2022/7/18 20:03
 */
@Service
public class OrderService {

    @SentinelResource("goods")
    public String queryGoods(){
        System.out.println("商品查询成功！");
        return "商品查询成功！";
    }


}
