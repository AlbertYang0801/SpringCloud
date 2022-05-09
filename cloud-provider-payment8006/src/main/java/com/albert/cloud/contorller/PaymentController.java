package com.albert.cloud.contorller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author yjw
 * @date 2022/4/28 23:18
 */
@RestController
@RequestMapping("payment")
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/consul")
    public String consul() {
        return "SprinCloud with consul:" + serverPort + "\t" + UUID.randomUUID().toString();
    }


}