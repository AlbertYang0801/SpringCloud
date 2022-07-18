package com.albert.cloud.controller;

import com.albert.cloud.service.OrderService;
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


}
