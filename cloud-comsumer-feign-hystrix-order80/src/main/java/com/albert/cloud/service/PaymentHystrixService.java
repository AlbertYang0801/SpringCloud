package com.albert.cloud.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author yjw
 * @date 2022/5/23 21:34
 */
@Component
@FeignClient(value = "CLOUD-PROVIDER-PAYMENT",fallback = PaymentFallbackService.class)
public interface PaymentHystrixService {

    @GetMapping("payment/info/ok/{id}")
    String getPaymentInfoOk(@PathVariable("id") Integer id);

    @GetMapping("payment/info/error/{id}")
    String getPaymentInfoError(@PathVariable("id") Integer id);


}
