package com.albert.cloud.controller;

import com.albert.cloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yjw
 * @date 2022/5/6 21:22
 */
@RestController
@RequestMapping("/order")
//指定@HystrixCommand注解，但未自定义fallbackMethod的方法，默认使用该兜底方法
//@DefaultProperties(defaultFallback = "paymentGlobalFailBackMethod")
public class OrderController {

    @Resource
    PaymentHystrixService paymentHystrixService;

    @GetMapping("info/ok/{id}")
    public String paymentInfoOk(@PathVariable("id") Integer id) {
        return paymentHystrixService.getPaymentInfoOk(id);
    }

    /**
     * 客户端服务降级
     */
    @GetMapping("info/error/{id}")
    @HystrixCommand
//    @HystrixCommand(fallbackMethod = "getPaymentServiceError",commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "500")
//    })
    public String paymentInfoError(@PathVariable("id") Integer id) {
        return paymentHystrixService.getPaymentInfoError(id);
    }

    @GetMapping("info/circuit/{id}")
    @HystrixCommand
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
        return paymentHystrixService.paymentCircuitBreaker(id);
    }

    public String getPaymentServiceError(Integer id) {
        return "80消费者调用8001服务异常!";
    }

    public String paymentGlobalFailBackMethod() {
        return "全局降级方法，180消费者调用8001服务异常!";
    }


}
