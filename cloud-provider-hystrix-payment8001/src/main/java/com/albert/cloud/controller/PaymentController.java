package com.albert.cloud.controller;

import com.albert.cloud.service.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yjw
 * @date 2022/5/25 22:03
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @GetMapping("info/ok/{id}")
    public String getPaymentInfo_OK(@PathVariable("id") Integer id){
      return paymentService.getPaymentInfoOk(id);
    }

    @GetMapping("info/error/{id}")
    public String getPaymentInfo_Error(@PathVariable("id") Integer id){
        return paymentService.getPaymentInfoError(id);
    }

    @GetMapping("info/circuit/{id}")
    public String paymentCircuitBreaker(@PathVariable("id") Integer id){
        return paymentService.paymentCircuitBreaker(id);
    }

}
