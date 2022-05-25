package com.albert.cloud.service;

/**
 * @author yjw
 * @date 2022/5/25 21:49
 */
public interface PaymentService {

    String getPaymentInfoOk(Integer id);

    String getPaymentInfoError(Integer id);

    String paymentCircuitBreaker(Integer id);

}
