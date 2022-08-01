package com.albert.cloud.controller;

import com.albert.cloud.entities.CommonResult;
import com.albert.cloud.myhandler.CustomerBlockHandler;
import com.albert.cloud.service.OrderService;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yjw
 * @date 2022/7/18 20:02
 */
@RestController
@RequestMapping("/order")
public class TraceController {

    @Autowired
    OrderService orderService;

    @GetMapping("/save")
    public String saveOrder() {
        return orderService.queryGoods();
    }

    @GetMapping("/query")
    public String queryOrder() {
        return orderService.queryGoods();
    }

    /**
     * 使用自定义类和自定义方法进行服务降级的触发
     * @return
     */
    @GetMapping("/testHandler")
    @SentinelResource(value = "testHandler",blockHandlerClass = CustomerBlockHandler.class,blockHandler = "blockHandlerExce")
    public CommonResult testHandler(){
        return new CommonResult(200,"OK");
    }


}
