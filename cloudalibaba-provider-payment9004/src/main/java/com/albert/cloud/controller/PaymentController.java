package com.albert.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangjunwei
 * @date 2022/6/16 8:57 下午
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Value("${server.port}")
    public String serverPort;

    @RequestMapping("/getPayment/{id}")
    public String getPayment(@PathVariable("id") Integer id){
        return "Alibaba Nacos server "+ serverPort+"-----"+id;
    }


}
