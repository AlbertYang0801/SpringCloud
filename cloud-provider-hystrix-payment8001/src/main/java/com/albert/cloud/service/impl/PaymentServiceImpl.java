package com.albert.cloud.service.impl;

import cn.hutool.core.util.IdUtil;
import com.albert.cloud.entities.Payment;
import com.albert.cloud.service.PaymentService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author yjw
 * @date 2022/5/25 21:50
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public String getPaymentInfoOk(Integer id) {
        return Thread.currentThread().getName() + "----OK----" + id;
    }

    /**
     * 启用Hystrix
     * fallbackMethod：配置回调方法
     * commandProperties：配置回调条件
     */
    @HystrixCommand(fallbackMethod = "getPaymentInfoErrorHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
    })
    @Override
    public String getPaymentInfoError(Integer id) {
        System.out.println(Thread.currentThread().getName() + "-----------error----------" + id);
        try {
            //睡眠5秒（触发服务降级）
            TimeUnit.SECONDS.sleep(3);
            //异常也可以触发服务降级
            int age = 10 / 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Thread.currentThread().getName() + "----getPaymentInfoError---正常返回消息--------" + id;
    }

    @Override
    @HystrixCommand(
            fallbackMethod = "paymentCircuitBreakerFallback", commandProperties =
            {
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"), //是否开启断路器
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), //请求次数
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), //时间窗口期
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),//失败率达到多少后跳闸
            }
    )
    public String paymentCircuitBreaker(Integer id) {
        if (id < 0) {
            throw new RuntimeException("-----------------------id不能为负数-------------------");
        }
        return Thread.currentThread().getName() + "\t" + "成功调用，流水号是：" +IdUtil.simpleUUID();
    }

    public String getPaymentInfoErrorHandler(Integer id) {
        return Thread.currentThread().getName() + "-----------服务端服务降级----ERROR----" + id;
    }

    public String paymentCircuitBreakerFallback(Integer id) {
        return Thread.currentThread().getName() + "-----------服务端服务熔断触发----ERROR----" + id;
    }


}
