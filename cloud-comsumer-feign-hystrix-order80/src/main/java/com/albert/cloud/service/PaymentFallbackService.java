package com.albert.cloud.service;

import org.springframework.stereotype.Component;

/**
 * @author yjw
 * @date 2022/5/25 23:31
 */
@Component
public class PaymentFallbackService implements PaymentHystrixService{
    @Override
    public String getPaymentInfoOk(Integer id) {
        return "----80消费者OK-----";
    }

    @Override
    public String getPaymentInfoError(Integer id) {
        return "------80消费者------getPaymentInfoError faullBack";
    }
}
