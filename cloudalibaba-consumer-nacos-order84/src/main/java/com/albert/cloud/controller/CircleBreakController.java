package com.albert.cloud.controller;

import com.albert.cloud.entities.CommonResult;
import com.albert.cloud.entities.Payment;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author yjw
 * @date 2022/8/1 23:07
 */
@RestController
@Slf4j
public class CircleBreakController {
    public static final String SERVICE_URL = "http://nacos-payment-provider";

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/consumer/fallback/{id}")
//    @SentinelResource(value = "fallback")//没有配置
//    @SentinelResource(value = "fallback",fallback = "handlerFallback") //handlerFallback
//    @SentinelResource(value = "fallback",blockHandler = "blockHandler") //handlerFallback
    @SentinelResource(value = "fallback",fallback = "handlerFallback",blockHandler = "blockHandler") //handlerFallback
    public String fallback(@PathVariable Long id)
    {
        //测试ribbon，使用ribbon的负载均衡
        String result = restTemplate.getForObject(SERVICE_URL + "/payment/getPayment/"+id, String.class,id);

        if (id == 4) {
            throw new IllegalArgumentException ("IllegalArgumentException,非法参数异常....");
        }else if (StringUtils.isEmpty(result)) {
            throw new NullPointerException ("NullPointerException,该ID没有对应记录,空指针异常");
        }

        return result;
    }

    public CommonResult handlerFallback(@PathVariable Long id, Throwable e) {
        Payment payment = new Payment(id, "null");
        return new CommonResult(444,"兜底异常handlerFallback, exception内容: " +e.getMessage(), payment);
    }

    public CommonResult blockHandler(@PathVariable Long id, BlockException blockException) {
        Payment payment = new Payment(id, null);
        return new CommonResult(445, "blockHandler-sentinel 限流, 无此流水: blockException: " + blockException.getMessage(), payment);
    }


}
