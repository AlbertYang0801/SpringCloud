package com.albert.cloud.contorller;

import com.albert.cloud.entities.CommonResult;
import com.albert.cloud.entities.Payment;
import com.albert.cloud.service.PaymentService;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author yjw
 * @date 2022/4/28 23:18
 */
@RestController
@RequestMapping("payment")
public class PaymentController {
    /**
     * 服务对象
     */
    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @Resource
    private DiscoveryClient discoveryClient;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("get/{id}")
    public CommonResult<Payment> selectOne(@PathVariable("id") Long id) {
        Payment payment = this.paymentService.queryById(id);
        return new CommonResult<Payment>(200, "select success 8002!", payment);
    }

    @PostMapping("create")
    public CommonResult create(@RequestBody Payment payment) {
        Payment insert = this.paymentService.insert(payment);
        System.out.println(insert);
        System.out.println("1234567890");
        return new CommonResult(200, "insert success", insert);
    }

//    @GetMapping("discovery")
//    public Object discovery() {
//        List<String> services = discoveryClient.getServices();
//        services.forEach(service -> {
//            System.out.println("----service" + service);
//        });
//        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
//        for (ServiceInstance instance : instances) {
//            System.out.println(instance.getServiceId() + "\t" + instance.getHost() + "\t" + instance.getPort() + "\t" + instance.getUri());
//            ;
//        }
//
//        return this.discoveryClient;
//    }
//
//    @GetMapping("lb")
//    public String getPaymentLB() {
//        return serverPort;
//    }
//
    @GetMapping("feign/timeout")
    public String getFeignTimeOut() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return serverPort;
    }
//
//    @GetMapping("zipkin")
//    public String paymentZipkin() {
//        return "hi,i`am paymentzipkin server fall back.welcome to atguigu.hahahahahhahahah";
//    }



}