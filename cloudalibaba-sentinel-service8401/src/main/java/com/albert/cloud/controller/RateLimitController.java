package com.albert.cloud.controller;

import com.albert.cloud.entities.CommonResult;
import com.albert.cloud.entities.Payment;
import com.albert.cloud.myhandler.CustomerBlockHandler;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yjw
 * @date 2022/8/1 22:47
 */
@RestController
public class RateLimitController {

    @GetMapping("/rateLimit/byUrl")
    @SentinelResource(value = "byUrl")
    public CommonResult byUrl() {
        return new CommonResult(200, "按url限流测试ok", new Payment(2020L, "serial002"));
    }

    @GetMapping("/byResource")
    @SentinelResource(value = "byResource", blockHandler = "handleException")
    public CommonResult byResource() {
        return new CommonResult(200, "按资源名称限流测试ok", new Payment(2020L, "serial001"));
    }

    public CommonResult handleException(BlockException exception) {
        return new CommonResult(444, exception.getClass().getCanonicalName() + "\t 服务不可用");
    }

    /**
     * 使用自定义限流处理类 CustomerBlockHandler
     */
    @GetMapping("/rateLimit/CustomerBlockHandler")
    @SentinelResource(value = "CustomerBlockHandler", blockHandlerClass = CustomerBlockHandler.class, blockHandler = "blockHandlerExce")
    public CommonResult CustomerBlockHandler() {
        return new CommonResult(200, "按自定义限流处理类限流测试ok", new Payment(2020L, "serial003"));
    }


}