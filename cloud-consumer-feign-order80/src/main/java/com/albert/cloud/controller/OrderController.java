package com.albert.cloud.controller;

import com.albert.cloud.entities.CommonResult;
import com.albert.cloud.entities.Payment;
import com.albert.cloud.service.PaymentFeignService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author yjw
 * @date 2022/5/6 21:22
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    PaymentFeignService paymentFeignService;

    @GetMapping("/payment/get/{id}")
    public CommonResult<Payment> query(@PathVariable("id") Long id) {
        return paymentFeignService.selectOne(id);
    }

    @GetMapping("feign/timeout")
    public String getFeignTimeOut() {
       return paymentFeignService.getFeignTimeOut();
    }

    @GetMapping("/zipkin")
    public String zipkin(){
        return paymentFeignService.zipkin();
    }

}
