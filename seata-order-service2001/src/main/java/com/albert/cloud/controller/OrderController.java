package com.albert.cloud.controller;

import com.albert.cloud.domain.Order;
import com.albert.cloud.entities.CommonResult;
import com.albert.cloud.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yjw
 * @date 2022/8/3 23:27
 */
@RestController
public class OrderController {
    @Resource
    private OrderService orderService;

    @GetMapping("/order/create")
    public CommonResult create(Order order) {
        orderService.create(order);
        return new CommonResult(200,"订单创建成功");
    }

}

