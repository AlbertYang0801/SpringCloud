package com.albert.cloud.controller;

import com.albert.cloud.entities.CommonResult;
import com.albert.cloud.entities.Payment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author yjw
 * @date 2022/5/6 21:22
 */
@RestController
@RequestMapping("order")
public class OrderController {

    //访问consul中的服务名（负载均衡）
    public static final String PAYMENT_URL = "http://consul-payment-service";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/payment/consul")
    public String craete() {
        return restTemplate.getForObject(PAYMENT_URL + "/payment/consul", String.class);
    }


}
