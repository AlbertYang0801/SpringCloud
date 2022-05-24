package com.albert.cloud.service;

import com.albert.cloud.entities.CommonResult;
import com.albert.cloud.entities.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author yjw
 * @date 2022/5/23 21:34
 */
@Component
@FeignClient(value = "CLOUD-PAYMENT-SERVICE")
public interface PaymentFeignService {

    @GetMapping("payment/get/{id}")
    CommonResult<Payment> selectOne(@PathVariable("id") Long id);

    @GetMapping("payment/feign/timeout")
    String getFeignTimeOut();


}
